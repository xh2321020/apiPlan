package com.cnnp.social.base;

import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;

public class SocialSystemException extends RuntimeException {
    public SocialSystemException(int exceptionCode) {
        super("SIC-" + exceptionCode + ":" +
                SysCfg.getProperty("SOC-" + StringUtils.leftPad(""+exceptionCode,6,'0')));
    }

    public SocialSystemException(int exceptionCode, String... params) {
        super(MessageFormat.format("SOC-" + StringUtils.leftPad(""+exceptionCode,6,'0') + ":" +
                SysCfg.getProperty("SOC-" + StringUtils.leftPad(""+exceptionCode,6,'0')), params));
    }
}
