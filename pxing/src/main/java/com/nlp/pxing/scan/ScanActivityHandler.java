package com.nlp.pxing.scan;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.nlp.pxing.R;
import com.nlp.pxing.camera.CameraManager;
import com.nlp.pxing.decode.DecodeThread;
import com.nlp.pxing.utils.LogUtil;

/**
 * Created by zdd on 2018/11/7
 */
public class ScanActivityHandler extends Handler {
    private ScanActivity mScanActivity;
    private CameraManager mCameraManager;
    private final DecodeThread decodeThread;
    private State state;

    private enum State {
        PREVIEW,
        SUCCESS,
        DONE
    }

    public ScanActivityHandler(ScanActivity scanActivity, CameraManager cameraManager){
        mScanActivity=scanActivity;
        mCameraManager=cameraManager;

        decodeThread=new DecodeThread(scanActivity,mCameraManager);
        decodeThread.start();
        state = State.SUCCESS;

        cameraManager.startPreview();
        restartPreviewAndDecode();
    }


    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);


        if(msg.what==R.id.decode_succeeded){

            ScanResult scanResult=(ScanResult) msg.obj;
            LogUtil.d("解码成功");
            LogUtil.d("明文区="+scanResult.getLawsText());
            LogUtil.d("隐藏区="+scanResult.getHiddenText());
            if(scanResult.isNLP()){
                state = State.SUCCESS;

                Intent intent=new Intent();
                intent.putExtra("ScanResult",scanResult);
                mScanActivity.setResult(Activity.RESULT_OK,intent);
                mScanActivity.finish();
            }else{
                Toast.makeText(mScanActivity,"非NLP专属码",Toast.LENGTH_SHORT).show();
                state = State.PREVIEW;
                mCameraManager.requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
            }

        }else if(msg.what==R.id.decode_failed){
            LogUtil.d("解码失败");
            state = State.PREVIEW;
            mCameraManager.requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
        }

    }


    public void quitSynchronously() {
        state = State.DONE;
        mCameraManager.stopPreview();
        Message quit = Message.obtain(decodeThread.getHandler(), R.id.quit);
        quit.sendToTarget();
        try {
            // Wait at most half a second; should be enough time, and onPause() will timeout quickly
            decodeThread.join(500L);
        } catch (InterruptedException e) {
            // continue
        }

        // Be absolutely sure we don't send any queued up messages
        removeMessages(R.id.decode_succeeded);
        removeMessages(R.id.decode_failed);
    }

    private void restartPreviewAndDecode() {
        if (state == State.SUCCESS) {
            state = State.PREVIEW;
            mCameraManager.requestPreviewFrame(decodeThread.getHandler(), R.id.begin);
            mScanActivity.drawViewfinder();
        }
    }
}
