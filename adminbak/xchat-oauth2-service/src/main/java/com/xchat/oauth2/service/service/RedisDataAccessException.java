package com.xchat.oauth2.service.service;

import org.springframework.dao.DataAccessException;

/**
 * @author liuguofu
 *         on 3/13/15.
 */
public class RedisDataAccessException extends DataAccessException{

    public RedisDataAccessException(String message) {
        super(message);
    }

    public RedisDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
