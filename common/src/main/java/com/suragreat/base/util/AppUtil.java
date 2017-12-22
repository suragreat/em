package com.suragreat.base.util;

public final class AppUtil {
    private AppUtil() {
    }

    public static String getAppCode() {
        return System.getProperty("APP_CODE");
    }

    public static String getAppName() {
        return System.getProperty("CONFIG_CODE");
    }

}
