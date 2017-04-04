package com.gavin.exception;

/**
 * 数据库中找不到对应的记录。
 */
public class RecordNotFoundException extends RuntimeException {

    public RecordNotFoundException(String _name, String _value) {
        super(String.format("%s %s not exist.", _name, _value));
    }

}
