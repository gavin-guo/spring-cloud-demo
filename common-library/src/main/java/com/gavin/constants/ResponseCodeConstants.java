package com.gavin.constants;

/**
 * 执行结果常量
 */
public interface ResponseCodeConstants {

    /**
     * 执行成功
     */
    String OK = "ok";

    /**
     * 调用远程服务失败
     */
    String REMOTE_CALL_FAILED = "remote.call.failed";

    /**
     * 远程服务执行错误
     */
    String REMOTE_SERVER_EXCEPTION = "remote.server.exception";

    /**
     * 参数不正确
     */
    String BAD_REQUEST = "bad.request";

    /**
     * 内部执行错误
     */
    String INTERNAL_EXCEPTION = "internal.exception";

    /**
     * 用户名已经存在
     */
    String LOGIN_NAME_ALREADY_EXIST = "login.name.already.exist";

    /**
     * 用户不存在
     */
    String USER_NOT_FOUND = "user.not.found";

    /**
     * 无法预约商品
     */
    String RESERVE_PRODUCT_FAILED = "reserve.product.failed";

    /**
     * 商品库存不足
     */
    String STOCKS_NOT_ENOUGH = "stocks.not.enough";

    /**
     * 无法创建订单
     */
    String CREATE_ORDER_FAILED = "create.order.failed";

    /**
     * 积分不足
     */
    String POINTS_NOT_ENOUGH = "points.not.enough";

}
