package com.nlp.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nlp.pxing.camera.CameraActivity;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private Button btnScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnScan=(Button)findViewById(R.id.btn_scan);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,CameraActivity.class);
                intent.putExtra("mPhotoPath",FolderManager.getPhotoFolder()+ File.separator+"avator.jpg");
                startActivity(intent);
            }
        });
    }
}
