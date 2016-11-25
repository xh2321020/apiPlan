/*
 *
 * Copyright 2016 IBM or CNNP.
 * 
 */
package com.cnnp.social.base;

/**
 * apiDuty
 * Created by Damon on 2016/11/23.
 */
public class NoContentException extends SocialSystemException {
    public NoContentException(int exceptionCode) {
        super(exceptionCode);
    }

    public NoContentException(int exceptionCode, String... params) {
        super(exceptionCode, params);
    }
}
