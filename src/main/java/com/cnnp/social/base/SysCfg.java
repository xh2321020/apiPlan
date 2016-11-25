/*
 *
 * Copyright 2016 IBM or CNNP.
 * 
 */
package com.cnnp.social.base;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

/**
 * apiPortal
 * Created by Damon on 2016/11/3.
 */
@Component
public class SysCfg {

    private static Properties props;

    public SysCfg() {
        try {
            Resource resource = new ClassPathResource("/message.properties");
            props = PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {

        return props == null ? null : props.getProperty(key);

    }


    public static String getProperty(String key, String defaultValue) {

        return props == null ? null : props.getProperty(key, defaultValue);

    }


    public static Properties getProperties() {
        return props;
    }
}
