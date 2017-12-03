package com.gavin.common.exception;

/**
 * 数据库中找不到对应的记录。
 */
public class RecordNotFoundException extends CustomException {

    public RecordNotFoundException(String _recordName, String _recordValue) {
        super(String.format("%s(%s) not found", _recordName, _recordValue));
    }

    @Override
    public String getErrorCode() {
        return "no_record";
    }

}
