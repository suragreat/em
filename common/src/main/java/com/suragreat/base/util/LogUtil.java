package com.suragreat.base.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import com.suragreat.base.model.ResponseContainer;
import com.suragreat.base.model.ResponseObjectContainer;
import org.apache.commons.lang3.StringUtils;

public class LogUtil {
    static class Container<T> {
        List<T> logs = new ArrayList<>();
        long startTimeStamp;

        boolean add(T data) {
            return logs.add(data);
        }
    }

    private static ThreadLocal<Container<String>> threadLocalLog = new ThreadLocal<Container<String>>() {

        protected Container<String> initialValue() {
            return new Container<String>();
        }

    };

    public static void logInThreadLocal(String msg) {
        int logLength = 2000;
        if (msg.length() > logLength) {
            threadLocalLog.get().add(msg.substring(0, logLength));
        } else {
            threadLocalLog.get().add(msg);
        }
    }

    public static void logInThreadLocal(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        threadLocalLog.get().add(sw.toString());
    }

    public static void logStartTimeStamp(long start) {
        threadLocalLog.get().startTimeStamp = start;
    }

    public static long getTimeCost() {
        return System.currentTimeMillis() - threadLocalLog.get().startTimeStamp;
    }

    public static void logInThreadLocal(ResponseContainer response) {
        logInThreadLocal(response.getStatus() == ResponseObjectContainer.SUCCESS ? "success"
                : ("error: " + response.getStatus()));
        if (StringUtils.isNotBlank(response.getMessage())) {
            logInThreadLocal(response.getMessage());
        }
    }

    public static List<String> getLogs() {
        try {
            return threadLocalLog.get().logs;
        } finally {
            threadLocalLog.remove();
        }
    }
}
