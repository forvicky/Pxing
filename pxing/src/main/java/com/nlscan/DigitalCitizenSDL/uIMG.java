package com.nlscan.DigitalCitizenSDL;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Build;

public class uIMG {
    private final String TAG = "uIMG";
    private boolean bInit = false;
    private static int nCodeType;
    private static int nResultLen;
    private static byte[] mDecodeResult = null;
    private static byte[] mHideDecodeResult = null;
    private static boolean bDecodeSuccess = false;
    private static byte[] mBarcodeJPEGBuffer = null;
    private static byte[] mInitData = null;
    private static byte[] mEncryptKeyData = null;

    //private final List<String> mExclusiveModelList = new ArrayList<String>();
    /**
     * 构造函数
     * 
     * @param width - 解码图片的宽
     * @param height - 解码图片的高
     * @param context - ApplicationContext
     * 
     */
    public uIMG(int width, int height, Context context)
    {
        //mExclusiveModelList.add("HUAWEI MT7-TL10");
        //String model = Build.MODEL;
        //if (!mExclusiveModelList.contains(model))
        //{
            //System.loadLibrary("ssl");
            //System.loadLibrary("szgm");
        //}
        System.loadLibrary("szgm");
        System.loadLibrary("uIMG3");
        System.loadLibrary("digitalciti");
        System.loadLibrary("SDLJni");
        bInit = uIMG_Init(width, height, context);
    }
    
    /**
     * 图片解码
     * 
     * @param imgBuffer - 用于解码的图象数据。
     *                  要求为灰度图数据
     *                  数据长度要求等于初始化时传入的长*宽，否则会解码失败
     * 
     * @return boolean - true 解码成功，false 解码失败
     * 
     */
    public boolean decode(byte[] imgBuffer)
    {
        if (!bInit)
            return false;

        bDecodeSuccess = false;
        return uIMG_Decode(imgBuffer);
    }
    
    /**
     * 获取解码库版本号
     * 
     *  
     * @return String - 解码库版本号
     * 
     */
    public String getVersion()
    {
        if (!bInit)
            return null;

        return uIMG_GetVersion();
    }
    
    private static boolean DecodeResultCallBack(int codeType, byte[] decodeMsg, byte[] decodeHideMsg)
    {
        bDecodeSuccess = true;
        nCodeType = codeType;
        if (decodeMsg != null)
        {
            mDecodeResult = new byte[decodeMsg.length];
            System.arraycopy(decodeMsg, 0, mDecodeResult, 0, decodeMsg.length);
        }
        else
        {
            decodeMsg = null;
        }
        
        if (decodeHideMsg != null)
        {
            mHideDecodeResult = new byte[decodeHideMsg.length];
            System.arraycopy(decodeHideMsg, 0, mHideDecodeResult, 0, decodeHideMsg.length);
        }
        else
        {
            mHideDecodeResult = null;
        }
        return true;
    }

    private static boolean MakeBarcodeCallBack(byte[] JPEGBuffer)
    {
        mBarcodeJPEGBuffer = new byte[JPEGBuffer.length];
        System.arraycopy(JPEGBuffer, 0, mBarcodeJPEGBuffer, 0, JPEGBuffer.length);
        return true;
    }

    /**
     * 获取条码未隐藏部分结果
     * 
     *  
     * @return byte[] - 解码结果。解码成功后，可通过此接口获取解码结果
     * 
     */
    public byte[] getDecodeResult()
    {
        if (bInit && bDecodeSuccess)
            return mDecodeResult;
        
        return null;
    }

    /**
     * 获取条码隐藏部分结果
     * 
     *  
     * @return byte[] - 解码结果。解码成功后，可通过此接口获取解码结果，如果没有隐藏信息，返回null
     * 
     */
    public byte[] getHideDecodeResult()
    {
        if (bInit && bDecodeSuccess)
            return mHideDecodeResult;
        
        return null;
    }

    
    /**
     * 生成定制QR码
     * 
     * @param BarcodeMsg - 条码信息
     * 
     * @param BarcodeHideMsg - 隐藏的条码信息
     * 
     * @return byte[] - JPEG格式图象数据
     *                  生成失败时返回NULL
     * 
     */
    public byte[] MakeBarcode(byte[] BarcodeMsg, byte[] BarcodeHideMsg)
    {
        if (uIMG_MakeBarcode(BarcodeMsg, BarcodeHideMsg))
            return mBarcodeJPEGBuffer;
        
        return null;
    }
    
    public byte[] Verify1(){
    	return uIMG_Verify1();
    }
    
    public boolean Verify2(byte[] respond){
    	return uIMG_Verify2(respond);
    }
    
    native private boolean uIMG_Init(int width, int height, Object jCtxObj);
    native private boolean uIMG_Decode(byte[] imgBuffer);
    native private String uIMG_GetVersion();
    native private boolean uIMG_MakeBarcode(byte[] BarcodeMsg, byte[] BarcodeHideMsg);
    native private byte[] uIMG_Verify1();
    native private boolean uIMG_Verify2(byte[] respond);
    //native private boolean uIMG_SetKeyData(byte[] encryptKeyData);
};