/*
 *
 * Copyright 2016 IBM or CNNP.
 * 
 */
package com.cnnp.social.base;

/**
 * apiPortal
 * Created by Damon on 2016/11/3.
 */

public class NoAuthenticationException extends SocialSystemException {
    public NoAuthenticationException(int exceptionCode) {
        super(exceptionCode);
    }

    public NoAuthenticationException(int exceptionCode, String... params) {
        super(exceptionCode, params);
    }
}
