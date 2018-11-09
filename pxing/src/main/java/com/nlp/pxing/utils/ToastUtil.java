package com.nlp.pxing.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by zdd on 2018/11/9
 */
public final class ToastUtil {
    private static String oldMsg;
    private static long lastShowTime;


    private ToastUtil(){}

    public static void showOnceToast(Context context,String msg){
        if(!msg.equals(oldMsg)){
            Toast.makeText(context,msg, Toast.LENGTH_SHORT).show();
            lastShowTime=System.currentTimeMillis();
            oldMsg=msg;
            return;
        }

        if(System.currentTimeMillis()-lastShowTime>5*1000){
            Toast.makeText(context,msg, Toast.LENGTH_SHORT).show();
            lastShowTime=System.currentTimeMillis();
            oldMsg=msg;
        }

    }
}
