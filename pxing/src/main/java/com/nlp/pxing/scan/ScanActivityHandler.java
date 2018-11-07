package com.nlp.pxing.scan;


import android.os.Handler;
import android.os.Message;
import com.nlp.pxing.R;
import com.nlp.pxing.camera.CameraManager;
import com.nlp.pxing.decode.DecodeThread;

/**
 * Created by zdd on 2018/11/7
 */
public class ScanActivityHandler extends Handler {
    private ScanActivity mScanActivity;
    private CameraManager mCameraManager;
//    private final DecodeThread decodeThread;
    private State state;

    private enum State {
        PREVIEW,
        SUCCESS,
        DONE
    }

    public ScanActivityHandler(ScanActivity scanActivity, CameraManager cameraManager){
        mScanActivity=scanActivity;
        mCameraManager=cameraManager;


        cameraManager.startPreview();
        restartPreviewAndDecode();
    }


    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

    }


    //todo 退出
    public void quitSynchronously() {

    }

    private void restartPreviewAndDecode() {
        if (state == State.SUCCESS) {
            state = State.PREVIEW;
           // mCameraManager.requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
            mScanActivity.drawViewfinder();
        }
    }
}
