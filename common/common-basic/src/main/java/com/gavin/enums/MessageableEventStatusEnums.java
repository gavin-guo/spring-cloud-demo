package com.gavin.enums;

/**
 * 可通过消息队列传递的事件的状态枚举
 */
public enum MessageableEventStatusEnums {

    NEW, //（发布端）事件已创建
    PUBLISHED, //（发布端）事件已发送到消息队列
    RECEIVED, // （订阅端）已从消息队列中取得
    PROCESSED, //（订阅端）事件已处理成功
    FAILED //（订阅端）事件处理失败

}
