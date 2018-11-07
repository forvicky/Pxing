package com.nlp.pxing.decode;

import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.libyuv.util.YuvUtil;
import com.nlp.pxing.R;
import com.nlp.pxing.camera.CameraManager;
import com.nlp.pxing.scan.ScanActivity;
import com.nlp.pxing.scan.ScanResult;
import com.nlp.pxing.utils.ImageUtil;

/**
 * 解码处理队列
 * Created by zdd on 2018/11/7
 */
public class DecodeHandler extends Handler {
    private ScanActivity mScanActivity;
    private CameraManager mCameraManager;
    private boolean running=true;

    public DecodeHandler(ScanActivity scanActivity,CameraManager cameraManager){
        mScanActivity=scanActivity;
        mCameraManager=cameraManager;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        if(msg==null||!running)
            return;

        if (msg.what==R.id.decode){
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

        //todo 解码
        boolean decodeResult=false;

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
