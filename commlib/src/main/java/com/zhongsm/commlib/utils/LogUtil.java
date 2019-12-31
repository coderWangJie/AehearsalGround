package com.zhongsm.commlib.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * LogUtil, which is used to show logs helping you to know your APP more.
 *
 * @author WangJ email:jie581825@yeah.net
 * @version 1.0
 * <p>
 * if you modify some, PLEASE RECORD YOUR MODIFY !!!
 * @since 2019/4/8 20:52
 */
public class LogUtil {

    /**
     * The min-level LogUtil will show on Logcat.<br>
     * you can change it by {@link LogUtil#setLogLevel(int)}
     */
    private static int LOG_LEVEL = Log.DEBUG;

    /**
     * change {@link #LOG_LEVEL} of LogUtil.
     *
     * @param newLevel The new log level you want to set.
     */
    public static void setLogLevel(int newLevel) {
        LOG_LEVEL = newLevel;
        Log.d(LogUtil.class.getSimpleName(), "The LogUtil's LOG_LEVEL has been Set: " + LOG_LEVEL);
    }

    /**
     * @param tag    log's tag
     * @param logMsg message
     */
    public static void d(String tag, String... logMsg) {
        if (Log.DEBUG >= LOG_LEVEL) {
            for (String item : logMsg) {
                Log.d(tag, item);
            }
        }
    }

    public static void d(String tag, @NonNull Object[] logArray) {
        if (Log.DEBUG >= LOG_LEVEL) {
            int length = logArray.length;
            Log.d(tag, "Array-length: " + length);
            for (int i = 0; i < length; i++) {
                Log.d(tag, "item-" + i + ": " + logArray[i].toString());
            }
        }
    }

    public static void d(String tag, @NonNull List<Object> logList) {
        if (Log.DEBUG >= LOG_LEVEL) {
            int size = logList.size();
            Log.d(tag, "List-size: " + size);
            for (int i = 0; i < size; i++) {
                Log.d(tag, "item-" + i + ": " + logList.get(i).toString());
            }
        }
    }

    public static void d(String tag, @NonNull Map<Object, Object> map) {
        if (Log.DEBUG >= LOG_LEVEL) {
            Set<Map.Entry<Object, Object>> set = map.entrySet();
            Log.d(tag, "Map contain set-size: " + set.size());

//            for (Map.Entry<Object, Object> item : set) {
//                Log.d(tag, item.getKey().toString() + ": " + item.getValue().toString());
//            }

            /*
             * 使用Iterator遍历虽然代码不如上边foreach循环简洁，但是Iterator有2个优点:
             * （1）兼容老版本java;
             * （2）在遍历过程中可以iterator.remove();
             */
            Iterator<Map.Entry<Object, Object>> iterator = set.iterator();
            while (iterator.hasNext()) {
                Map.Entry<Object, Object> item = iterator.next();
                Log.d(tag, item.getKey().toString() + ": " + item.getValue().toString());
            }

        }
    }

    public static void i(String tag, String... logMsg) {
        if (Log.INFO >= LOG_LEVEL) {
            for (String item : logMsg) {
                Log.i(tag, item);
            }
        }
    }

    public static void w(String tag, String... logMsg) {
        if (Log.WARN >= LOG_LEVEL) {
            for (String item : logMsg) {
                Log.w(tag, item);
            }
        }
    }

    public static void e(String tag, String... logMsg) {
        if (Log.ERROR >= LOG_LEVEL) {
            for (String item : logMsg) {
                Log.e(tag, item);
            }
        }
    }

    public static void excep(String tag, Exception e) {
//        Writer writer = new StringWriter();
//        PrintWriter pw = new PrintWriter(writer);
//        e.printStackTrace(pw);
//        Throwable cause = e.getCause();
//        while (cause != null) {
//            cause.printStackTrace(pw);
//            cause = cause.getCause();
//        }
//        Log.e(tag, writer.toString());
        Log.e(tag, e.getMessage(), e.getCause());
    }
}
