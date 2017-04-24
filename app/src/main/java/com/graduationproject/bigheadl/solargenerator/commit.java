package com.graduationproject.bigheadl.solargenerator;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.activity.CaptureActivity;

import static android.Manifest.permission.READ_CONTACTS;

public class commit extends AppCompatActivity {

    private ImageView back;
    private ImageView send;
    private static final int CAMERA_PERMISSION = 1;
    private Class<?> mClss;

    //打开扫描界面请求码
    private int REQUEST_CODE = 0x01;
    //扫描成功返回码
    private int RESULT_OK = 0xA1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commit);

        TextView toolbar = (TextView) findViewById(R.id.toolbar_title);
        toolbar.setText("AddCommit");

        back = (ImageView) findViewById(R.id.toolbar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        send = (ImageView) findViewById(R.id.toolbar_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RatingBar rating = (RatingBar) findViewById(R.id.ratingBar);
                if(rating.getNumStars()==0)
                    Snackbar.make(v, "Please rate this serve.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                //upload to database;
                finish();
            }
        });
    }

    public void scanner(View v){
        //打开二维码扫描界面
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = CaptureActivity.class;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(commit.this, CaptureActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        }

    }

    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(mClss != null) {
                        Intent intent = new Intent(this, mClss);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, "Please grant camera permission to use the Barcode Scanner", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (resultCode == RESULT_OK) { //RESULT_OK = -1
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("qr_scan_result");
            //将扫描出的信息显示出来

            EditText content = (EditText) findViewById(R.id.devicesmodel);
            //content.setVisibility(View.VISIBLE);
            content.setText(scanResult);
        }
    }
}
