package com.foodapp.commons;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class Log {
    /**
     * This method logs an error message
     *
     * @param message   The message to be logged
     */
    public static void e(String message) {

        StackTraceElement[] elements = new Exception().getStackTrace();
        if(elements.length >= 2) {
            String className = elements[1].getClassName();
            Logger logger = LoggerFactory.getLogger(elements[1].getClass());
            /*Logger logger = Logger.getLogger(className);*/
            logger.error(message);
        }
    }

    /**
     * This method logs & slacks an error message
     *
     * @param message The message to be logged
     * @param slackMessage The JSONObject with message to be slacked
     */
    public static void e(String message, JSONObject slackMessage) {

        StackTraceElement[] elements = new Exception().getStackTrace();
        if(elements.length >= 2) {
            String className = elements[1].getClassName();
            Logger logger = LoggerFactory.getLogger(elements[1].getClass());
            /*Logger logger = Logger.getLogger(className);*/
            logger.error(message);
        }
    }

    /**
     * This method logs an error message
     *
     * @param message   The message to logged
     * @param throwable A throwable (e.g Exception) as added info
     */
    public static void e(String message, Throwable throwable) {
        StackTraceElement[] elements = new Exception().getStackTrace();
        if(elements.length >= 2) {
            String className = elements[1].getClassName();
            Logger logger = LoggerFactory.getLogger(elements[1].getClass());
            logger.error(message, throwable);
        }
    }

    /**
     * This method logs & slacks an error message
     *
     * @param message The message to logged
     * @param throwable A throwable (e.g Exception) as added info
     * @param slackMessage The JSONObject with message to be slacked
     */
    public static void e(String message, Throwable throwable, JSONObject slackMessage) {
        StackTraceElement[] elements = new Exception().getStackTrace();
        if(elements.length >= 2) {
            String className = elements[1].getClassName();
            Logger logger = LoggerFactory.getLogger(elements[1].getClass());
            logger.error(message, throwable);
        }

    }

    /**
     * This method logs a warn message
     *
     * @param message   The message to be logged
     */
    public static void w(String message) {
        StackTraceElement[] elements = new Exception().getStackTrace();
        if(elements.length >= 2) {
            String className = elements[1].getClassName();
            Logger logger = LoggerFactory.getLogger(elements[1].getClass());
            logger.warn(message);
        }
    }

    /**
     * This method logs a warn message
     *
     * @param message   The message to be logged
     * @param throwable A throwable (e.g Exception) as added info
     */
    public static void w(String message, Throwable throwable) {
        StackTraceElement[] elements = new Exception().getStackTrace();
        if(elements.length >= 2) {
            String className = elements[1].getClassName();
            Logger logger = LoggerFactory.getLogger(elements[1].getClass());
            logger.warn(message, throwable);
        }
    }

    /**
     * This method logs an info message
     *
     * @param message   The message to be logged
     */
    public static void i(String message) {
        StackTraceElement[] elements = new Exception().getStackTrace();
        if(elements.length >= 2) {
            String className = elements[1].getClassName();
            Logger logger = LoggerFactory.getLogger(elements[1].getClass());
            logger.info(message);
        }
    }

    /**
     * This method logs an info message
     *
     * @param message   The message to be logged
     * @param throwable A throwable (e.g Exception) as added info
     */
    public static void i(String message, Throwable throwable) {
        StackTraceElement[] elements = new Exception().getStackTrace();
        if(elements.length >= 2) {
            String className = elements[1].getClassName();
            Logger logger = LoggerFactory.getLogger(elements[1].getClass());
            logger.info(message, throwable);
        }
    }

    /**
     * This method logs a debug message
     *
     * @param message   The message to be logged
     */
    public static void d(String message) {
        StackTraceElement[] elements = new Exception().getStackTrace();
        if(elements.length >= 2) {
            String className = elements[1].getClassName();
            Logger logger = LoggerFactory.getLogger(elements[1].getClass());
            //Logger logger = Logger.getLogger(className);
            logger.debug(message);
        }
    }

    /**
     * This method logs a debug message
     *
     * @param message   The message to be logged
     * @param throwable A throwable (e.g Exception) as added info
     */
    public static void d(String message, Throwable throwable) {
        StackTraceElement[] elements = new Exception().getStackTrace();
        if(elements.length >= 2) {
            String className = elements[1].getClassName();
            Logger logger = LoggerFactory.getLogger(elements[1].getClass());
            logger.debug(message, throwable);
        }
    }
}


