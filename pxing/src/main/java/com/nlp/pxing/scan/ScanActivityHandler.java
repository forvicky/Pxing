package com.nlp.pxing.scan;


import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.nlp.pxing.R;
import com.nlp.pxing.camera.CameraManager;
import com.nlp.pxing.decode.DecodeThread;

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
            state = State.SUCCESS;
            Toast.makeText(mScanActivity,"解码成功",Toast.LENGTH_SHORT).show();


        }else if(msg.what==R.id.decode_failed){
            state = State.PREVIEW;
            Toast.makeText(mScanActivity,"解码失败",Toast.LENGTH_SHORT).show();
            mCameraManager.requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
        }

    }


    //todo 退出
    public void quitSynchronously() {

    }

    private void restartPreviewAndDecode() {
        if (state == State.SUCCESS) {
            state = State.PREVIEW;
            mCameraManager.requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
            mScanActivity.drawViewfinder();
        }
    }
}
