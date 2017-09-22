package com.gavin.service.impl;

import com.gavin.entity.PointEntity;
import com.gavin.entity.PointHistoryEntity;
import com.gavin.enums.PointActionEnums;
import com.gavin.exception.InsufficientPointsException;
import com.gavin.exception.RecordNotFoundException;
import com.gavin.dto.point.FreezePointsDto;
import com.gavin.dto.point.ProducePointsDto;
import com.gavin.repository.PointHistoryRepository;
import com.gavin.repository.PointRepository;
import com.gavin.service.PointService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RefreshScope
public class PointServiceImpl implements PointService {

    @Value("${points.period}")
    private Integer period;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private PointHistoryRepository pointHistoryRepository;

    @Override
    @Transactional
    public void addPoints(ProducePointsDto _production) {
        PointEntity pointEntity = modelMapper.map(_production, PointEntity.class);
        String expireDate = new DateTime().plusDays(period).toString("yyyy-MM-dd");
        pointEntity.setExpireDate(expireDate);
        pointRepository.save(pointEntity);

        // 记录到积分明细表。
        PointHistoryEntity pointHistoryEntity = modelMapper.map(_production, PointHistoryEntity.class);
        pointHistoryEntity.setAction(PointActionEnums.REWARD);
        pointHistoryRepository.save(pointHistoryEntity);
    }

    @Override
    public BigDecimal calculateUsableAmount(String _accountId) {
        List<PointEntity> pointEntities = pointRepository.findUsableByAccountId(_accountId, new Sort("id"));

        return pointEntities.stream()
                .map(PointEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional
    public void freezePoints(FreezePointsDto _freeze) {
        BigDecimal newestAmount = this.calculateUsableAmount(_freeze.getUserId());

        // 账户内最新的可用积分数小与需要的积分数。
        if (newestAmount.compareTo(_freeze.getAmount()) < 0) {
            throw new InsufficientPointsException("insufficient points");
        }

        // 需要的积分数量。
        BigDecimal requiredAmount = _freeze.getAmount();

        List<Sort.Order> sortOrders = new ArrayList<>();
        // 先按照过期日期的倒序排列。
        sortOrders.add(new Sort.Order(Sort.Direction.DESC, "expireDate"));
        // 再按照积分数量的顺序排列。
        sortOrders.add(new Sort.Order(Sort.Direction.ASC, "amount"));

        // 获得可使用积分的列表。
        List<PointEntity> pointEntities = pointRepository.findUsableByAccountId(
                _freeze.getUserId(), new Sort(sortOrders));

        for (PointEntity pointEntity : pointEntities) {
            if (pointEntity.getAmount().compareTo(requiredAmount) <= 0) {
                // 锁定该积分记录。
                pointEntity.setLockForOrderId(_freeze.getOrderId());
                pointRepository.save(pointEntity);

                requiredAmount = requiredAmount.subtract(pointEntity.getAmount());
            } else {
                // 把一次用不完的积分记录拆分成两条记录。
                PointEntity newPointEntity = new PointEntity();
                newPointEntity.setUserId(pointEntity.getUserId());
                newPointEntity.setExpireDate(pointEntity.getExpireDate());
                newPointEntity.setAmount(pointEntity.getAmount().subtract(requiredAmount));
                pointRepository.save(newPointEntity);

                pointEntity.setAmount(requiredAmount);
                pointEntity.setLockForOrderId(_freeze.getOrderId());
                pointRepository.save(pointEntity);
                break;
            }
            if (requiredAmount.compareTo(new BigDecimal("0")) <= 0) {
                break;
            }
        }
        log.info("freeze {} points on account of user({}) for order({}) successfully. ", _freeze.getAmount(), _freeze.getUserId(), _freeze.getOrderId());
    }

    @Override
    @Transactional
    public void unfreezePoints(String _orderId) {
        List<PointEntity> pointEntities = pointRepository.findByLockForOrderId(_orderId);
        pointEntities.forEach(
                pointEntity -> {
                    // 解除积分记录的锁定标志。
                    pointEntity.setLockForOrderId(null);
                    pointRepository.save(pointEntity);
                }
        );
        log.info("unfreeze points for order({}) successfully. ", _orderId);
    }

    @Override
    @Transactional
    public void consumePoints(String _orderId) {
        List<PointEntity> pointEntities = Optional.ofNullable(pointRepository.findByLockForOrderId(_orderId))
                .orElseThrow(() -> new RecordNotFoundException("point records which using by", _orderId));

        // 计算此次消费的积分总数量。
        BigDecimal usedAmount = pointEntities.stream()
                .map(PointEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        PointHistoryEntity pointHistoryEntity = new PointHistoryEntity();
        pointHistoryEntity.setUserId(pointEntities.get(0).getUserId());
        pointHistoryEntity.setOrderId(_orderId);
        pointHistoryEntity.setAmount(usedAmount);
        pointHistoryEntity.setAction(PointActionEnums.CONSUME);
        pointHistoryRepository.save(pointHistoryEntity);

        pointRepository.delete(pointEntities);
        log.info("consume points for order({}) successfully. ", _orderId);
    }

    @Override
    @Transactional
    public void cleanExpiredPoints() {
        // 检索出今天已过期的所有积分记录。
        String today = new DateTime().toString("yyyy-MM-dd");
        List<PointEntity> expiredPointEntities = pointRepository.findByExpireDateLessThanEqual(today);

        // 计算每一个用户被清除的过期积分数。
        Map<String, BigDecimal> userPointsMap =
                expiredPointEntities.stream()
                        .collect(Collectors.groupingBy(
                                PointEntity::getUserId,
                                Collectors.reducing(BigDecimal.ZERO, PointEntity::getAmount, BigDecimal::add)));

        // 删除所有过期积分的记录。
        pointRepository.delete(expiredPointEntities);

        // 记录到积分明细表。
        userPointsMap.forEach(
                (userId, amount) -> {
                    log.info("{} points expired, user_id={}.", amount, userId);
                    PointHistoryEntity pointHistoryEntity = new PointHistoryEntity();
                    pointHistoryEntity.setUserId(userId);
                    pointHistoryEntity.setAmount(amount);
                    pointHistoryEntity.setAction(PointActionEnums.EXPIRE);
                    pointHistoryRepository.save(pointHistoryEntity);
                }
        );
    }

}
