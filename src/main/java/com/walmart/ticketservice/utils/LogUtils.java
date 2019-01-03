package com.walmart.ticketservice.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LogUtils to perform logging
 * created by Laxmi Kalyan Kistapuram on 12/29/18
 */
public class LogUtils {

    /**
     * Getting logger object by providing classname
     * @param className
     * @return Logger Instance
     */
    public static Logger getLogger(String className) {
        return LoggerFactory.getLogger(className);
    }
}
