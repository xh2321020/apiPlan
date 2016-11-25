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
public class ExceptionMessage {
    private long timestamp;
    private String message;
    private int status;
    private String requesturl;
    private String payload;

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRequesturl() {
        return requesturl;
    }

    public void setRequesturl(String requesturl) {
        this.requesturl = requesturl;
    }
}
