package com.gavin.business.service;

import com.gavin.common.dto.point.FreezePointsDto;
import com.gavin.common.dto.point.ProducePointsDto;

import java.math.BigDecimal;

public interface PointService {

    /**
     * 计算账户内可用的积分总数。
     *
     * @param _userId 用户ID
     * @return
     */
    BigDecimal calculateUsableAmount(String _userId);

    /**
     * 新增积分
     *
     * @param _production 新增积分信息
     * @return
     */
    void addPoints(ProducePointsDto _production);

    /**
     * 由于在订单中使用积分抵扣而预先冻结积分
     *
     * @param _freeze
     */
    void freezePoints(FreezePointsDto _freeze);

    /**
     * 由于取消订单而恢复被冻结的积分
     *
     * @param _orderId 订单ID
     */
    void unfreezePoints(String _orderId);

    /**
     * 订单完成后彻底扣除积分
     *
     * @param _orderId 订单ID
     */
    void consumePoints(String _orderId);

    /**
     * 清除过期积分
     */
    void cleanExpiredPoints();

}
