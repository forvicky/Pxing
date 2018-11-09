package com.nlp.pxing.utils;

import android.util.Log;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义日志工具
 *
 * @author zdd
 *
 */
public class LogUtil {

    //zddrelease log日志开关
    public static final boolean LOG_OPEN_DEBUG=true;


    private static final String DEFAULT_TAG = "zddHttp";//默认tag


    public static String getObjectString(Object obj) {
        StringBuilder builder = new StringBuilder();
        if (obj != null) {
            Field[] declaredFields = obj.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                try {
                    builder.append("[\"" + field.getName() + "\":" + field.get(obj) + "]");
                }
                catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } else {
            builder.append("null");
        }
        return builder.toString();
    }

    private static List<String> split(String content, int peace) {
        List<String> list = new ArrayList<String>();
        if (content == null) {
            list.add("null");
        } else if (content.length() == 0) {
            list.add("[empty string,length 0]");
        } else {
//            content = content.replaceAll("\n", "");
            int cursor = 0;
            while (true) {
                int sub = (cursor + peace ) - (content.length() );
                if (sub <= 0) {
                    String temp = content.substring(cursor, cursor + peace);
                    list.add(temp);
                } else {
                    String temp = content.substring(cursor, content.length());
                    if (temp.length() > 0)
                        list.add(temp);
                    break;
                }
                cursor += peace;
            }
        }
        return list;
    }

    public static void d(String tag, String message) {

        if (tag != null && message != null) {

            if (LOG_OPEN_DEBUG) {
                dd(tag, message);
            }



        }

    }

    public static void e(String tag, String message) {

        if (tag != null && message != null) {

            if (LOG_OPEN_DEBUG) {
                ee(tag, message);
            }



        }

    }

    public static void dd(String tag, String content) {
        List<String> splits = split(content, 3000);
        if (splits.size() == 1) {
            Log.d(tag, splits.get(0));
        } else {
            for (int i = 0; i < splits.size(); i++) {
                Log.d(tag + "[" + i + "]", splits.get(i));
            }
        }
    }

    public static void ee(String tag, String content) {
        List<String> splits = split(content, 3000);
        if (splits.size() == 1) {
            Log.e(tag, splits.get(0));
        } else {
            for (int i = 0; i < splits.size(); i++) {
                Log.e(tag + "[" + i + "]", splits.get(i));
            }
        }
    }


    public static void d(String message) {
        d(DEFAULT_TAG,message);
    }

    public static void e(String message) {
        e(DEFAULT_TAG,message);
    }

}