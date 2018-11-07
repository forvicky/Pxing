package com.nlp.pxing.decode;

import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import com.libyuv.util.YuvUtil;
import com.nlp.pxing.R;
import com.nlp.pxing.camera.CameraManager;
import com.nlp.pxing.scan.ScanActivity;
import com.nlp.pxing.scan.ScanResult;
import com.nlscan.DigitalCitizenSDL.uIMG;

/**
 * 解码处理队列
 * Created by zdd on 2018/11/7
 */
public class DecodeHandler extends Handler {
    private static final String TAG=DecodeHandler.class.getName();
    private ScanActivity mScanActivity;
    private CameraManager mCameraManager;
    private boolean running=true;
    private uIMG uImg;

    private final static String[] MD5_LIST={"c7014372a2ee78fd449a78ecc74d92f3",
            "1275028fc3b5fd701ffba474534e021e","0c775c8e7a145df1f89260e36f0f67d6","cdc61ed4dba45c47da5a84a1c252771b","829e5b72ad326e8ee2ed3ca199c62794",
            "ba52d7cab02d02bb8ea353173f1896e9","b5984effe3628cb3486214a418c66250","f6a76b95809c2fb557b80b3ecbf6d342",
            "1c6f4be6d5eb5c4180303d25cd2d0b41"};

    public DecodeHandler(ScanActivity scanActivity,CameraManager cameraManager){
        mScanActivity=scanActivity;
        mCameraManager=cameraManager;

    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        if(msg==null||!running)
            return;

        if (msg.what==R.id.begin){
            uImg=new uIMG(mCameraManager.getFramingRectInPreview().width(),mCameraManager.getFramingRectInPreview().height(),mScanActivity);
 
            int i;
            boolean md5isright = false;
            byte[] vlaue = uImg.Verify1();
            Log.d(TAG, "vlaue length is "+vlaue.length);
            //byte[] vlaueDecode = decryptByPrivateKey(vlaue,PRIVATE_KEY.getBytes());
            byte[] vlaueDecode = decryptByPrivateKey(vlaue,Base64.decode(PRIVATE_KEY,Base64.DEFAULT));
            Log.d(TAG, "vlaueDecode length is "+vlaueDecode.length);
            byte[] vlaueMD5 = new byte[16];
            byte[] vlaueRandom = new byte[32];
            System.arraycopy(vlaueDecode, 0, vlaueMD5, 0, 16);
            System.arraycopy(vlaueDecode, 16, vlaueRandom, 0, 32);
            Log.d(TAG, "vlaueMD5 length is "+vlaueMD5.length);
            String MD5 = "";
            for(i=0;i<vlaueMD5.length;i++){
                MD5=MD5+String.format("%02x", vlaueMD5[i]&0xff);
            }

            Log.d(TAG, "readed MD5 is "+MD5);
            for(i=0;i<MD5_LIST.length;i++){
                Log.d(TAG, "MD5_LIST["+i+"] is "+MD5_LIST[i]);
                if(MD5.equalsIgnoreCase(MD5_LIST[i])){
                    md5isright = true;
                }
            }
            if(md5isright){
                Log.d(TAG, "testvlaue length is "+testvlaue.length);
                byte[] data = new byte[testvlaue.length+vlaueRandom.length];
                System.arraycopy(testvlaue,0,data,0,testvlaue.length);
                System.arraycopy(vlaueRandom,0,data,testvlaue.length,vlaueRandom.length);
                //byte[] vlaueencode = encryptByPrivateKey(data, PRIVATE_KEY.getBytes());
                byte[] vlaueencode = encryptByPrivateKey(data, Base64.decode(PRIVATE_KEY,Base64.DEFAULT));
                candecode = uImg.Verify2(vlaueencode); //这句话不能绕过
            }

            cropAndDecode((byte[]) msg.obj,msg.arg1,msg.arg2,mCameraManager.getFramingRectInPreview());
        }if (msg.what==R.id.decode){
            cropAndDecode((byte[]) msg.obj,msg.arg1,msg.arg2,mCameraManager.getFramingRectInPreview());
        }else if(msg.what==R.id.quit){
            running=false;
            Looper.myLooper().quit();
        }

    }

    /**
     *  裁剪并解码
     * @param imgData
     * @param width
     * @param height
     * @param finderRect
     */
    private void cropAndDecode(byte[] imgData,int width,int height,Rect finderRect){
        byte [] cropData=new byte[finderRect.width()*finderRect.height()*12/8];
        YuvUtil.cropYUV(imgData,width,height,cropData,finderRect.width(),finderRect.height(),finderRect.top,finderRect.left);

        //测试查看裁剪后的效果
        //ImageUtil.saveImageData(cropData);

        //坑爹，解码库bug，byte数组大小必须等于长*宽，所以舍弃了一些数据
        byte [] resultData=new byte[finderRect.width()*finderRect.height()];
        System.arraycopy(cropData, 0, resultData, 0, resultData.length);


        boolean decodeResult=uImg.decode(resultData);

        Handler handler=mScanActivity.getHandler();

        ScanResult scanResult=new ScanResult("","");

        if(handler!=null){
            Message msg;
            if(decodeResult){
                msg=Message.obtain(handler,R.id.decode_succeeded,scanResult);
            }else{
                msg=Message.obtain(handler,R.id.decode_failed,null);
            }
            msg.sendToTarget();
        }




    }



}
