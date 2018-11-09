package com.nlp.demo;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nlp.pxing.scan.ScanActivity;
import com.nlp.pxing.scan.ScanResult;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private Button btnScan;
    private TextView tvResult;

    private static final int REQUEST_SCAN=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnScan=(Button)findViewById(R.id.btn_scan);
        tvResult=(TextView)findViewById(R.id.tv_result);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ScanActivity.class);
                startActivityForResult(intent,REQUEST_SCAN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_SCAN&&resultCode==RESULT_OK){
            ScanResult scanResult=(ScanResult) data.getSerializableExtra("ScanResult");
            tvResult.setText(scanResult.getLawsText()+"\n"+scanResult.getHiddenText());
        }
    }
}
