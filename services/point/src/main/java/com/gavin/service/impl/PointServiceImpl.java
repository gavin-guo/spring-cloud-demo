package com.gavin.service.impl;

import com.gavin.domain.Point;
import com.gavin.domain.PointHistory;
import com.gavin.dto.point.FreezePointsDto;
import com.gavin.dto.point.ProducePointsDto;
import com.gavin.enums.PointActionEnums;
import com.gavin.exception.InsufficientPointsException;
import com.gavin.exception.RecordNotFoundException;
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
        Point point = modelMapper.map(_production, Point.class);
        String expireDate = new DateTime().plusDays(period).toString("yyyy-MM-dd");
        point.setExpireDate(expireDate);
        pointRepository.save(point);

        // 记录到积分明细表。
        PointHistory pointHistory = modelMapper.map(_production, PointHistory.class);
        pointHistory.setAction(PointActionEnums.REWARD);
        pointHistoryRepository.save(pointHistory);
    }

    @Override
    public BigDecimal calculateUsableAmount(String _accountId) {
        List<Point> pointEntities = pointRepository.findUsableByAccountId(_accountId, new Sort("id"));

        return pointEntities.stream()
                .map(Point::getAmount)
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
        List<Point> pointEntities = pointRepository.findUsableByAccountId(
                _freeze.getUserId(), new Sort(sortOrders));

        for (Point point : pointEntities) {
            if (point.getAmount().compareTo(requiredAmount) <= 0) {
                // 锁定该积分记录。
                point.setLockForOrderId(_freeze.getOrderId());
                pointRepository.save(point);

                requiredAmount = requiredAmount.subtract(point.getAmount());
            } else {
                // 把一次用不完的积分记录拆分成两条记录。
                Point newPoint = new Point();
                newPoint.setUserId(point.getUserId());
                newPoint.setExpireDate(point.getExpireDate());
                newPoint.setAmount(point.getAmount().subtract(requiredAmount));
                pointRepository.save(newPoint);

                point.setAmount(requiredAmount);
                point.setLockForOrderId(_freeze.getOrderId());
                pointRepository.save(point);
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
        List<Point> pointEntities = pointRepository.findByLockForOrderId(_orderId);
        pointEntities.forEach(
                point -> {
                    // 解除积分记录的锁定标志。
                    point.setLockForOrderId(null);
                    pointRepository.save(point);
                }
        );
        log.info("unfreeze points for order({}) successfully. ", _orderId);
    }

    @Override
    @Transactional
    public void consumePoints(String _orderId) {
        List<Point> pointEntities = Optional.ofNullable(pointRepository.findByLockForOrderId(_orderId))
                .orElseThrow(() -> new RecordNotFoundException("point records which using by", _orderId));

        // 计算此次消费的积分总数量。
        BigDecimal usedAmount = pointEntities.stream()
                .map(Point::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        PointHistory pointHistory = new PointHistory();
        pointHistory.setUserId(pointEntities.get(0).getUserId());
        pointHistory.setOrderId(_orderId);
        pointHistory.setAmount(usedAmount);
        pointHistory.setAction(PointActionEnums.CONSUME);
        pointHistoryRepository.save(pointHistory);

        pointRepository.delete(pointEntities);
        log.info("consume points for order({}) successfully. ", _orderId);
    }

    @Override
    @Transactional
    public void cleanExpiredPoints() {
        // 检索出今天已过期的所有积分记录。
        String today = new DateTime().toString("yyyy-MM-dd");
        List<Point> expiredPointEntities = pointRepository.findByExpireDateLessThanEqual(today);

        // 计算每一个用户被清除的过期积分数。
        Map<String, BigDecimal> userPointsMap =
                expiredPointEntities.stream()
                        .collect(Collectors.groupingBy(
                                Point::getUserId,
                                Collectors.reducing(BigDecimal.ZERO, Point::getAmount, BigDecimal::add)));

        // 删除所有过期积分的记录。
        pointRepository.delete(expiredPointEntities);

        // 记录到积分明细表。
        userPointsMap.forEach(
                (userId, amount) -> {
                    log.info("{} points expired, user_id={}.", amount, userId);
                    PointHistory pointHistory = new PointHistory();
                    pointHistory.setUserId(userId);
                    pointHistory.setAmount(amount);
                    pointHistory.setAction(PointActionEnums.EXPIRE);
                    pointHistoryRepository.save(pointHistory);
                }
        );
    }

}
