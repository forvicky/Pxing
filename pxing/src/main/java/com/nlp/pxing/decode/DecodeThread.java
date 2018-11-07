package com.nlp.pxing.decode;

import android.os.Looper;

import com.nlp.pxing.camera.CameraManager;
import com.nlp.pxing.scan.ScanActivity;

import java.util.concurrent.CountDownLatch;

/**
 * 解码工作线程
 * Created by zdd on 2018/11/7
 */
public class DecodeThread extends Thread{
    private DecodeHandler mDecodeHandler;
    private CountDownLatch mCountDownLatch;
    private ScanActivity mScanActivity;
    private CameraManager mCameraManager;

    public DecodeThread(ScanActivity scanActivity, CameraManager cameraManager){
        mScanActivity=scanActivity;
        mCameraManager=cameraManager;
        mCountDownLatch=new CountDownLatch(1);
    }

    public DecodeHandler getHandler(){
        try {
            mCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mDecodeHandler;
    }

    @Override
    public void run() {
        super.run();
        Looper.prepare();
        mDecodeHandler=new DecodeHandler(mScanActivity,mCameraManager);
        mCountDownLatch.countDown();
        Looper.loop();
    }
}
