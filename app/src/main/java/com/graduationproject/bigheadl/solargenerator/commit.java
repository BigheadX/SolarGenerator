package com.graduationproject.bigheadl.solargenerator;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.session.MediaController;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.AppCompatImageHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.zxing.activity.CaptureActivity;

import java.io.File;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;
import static android.support.design.R.id.visible;

public class commit extends AppCompatActivity {

    private ImageView back;
    private File file;
    private boolean isRecording = false;
    private static final int CAMERA_PERMISSION = 1;
    private static final int RECORD_PERMISSION = 2;
    private static final int CAPTURE_PICTURES = 3;
    private static final int CAPTURE_VIDEOS = 4;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 29;
    private Class<?> mClss;

    //打开扫描界面请求码
    private int REQUEST_CODE = 0x01;
    //扫描成功返回码
    private int RESULT_OK = 0xA1;


    private final static String TAG = "Test";
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private static String filename = "record.3gp";
    private String path;


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

    }

    public void send(View v){
        RatingBar rating = (RatingBar) findViewById(R.id.ratingBar);
        if(rating.getNumStars()==0)
            Toast.makeText(this, "Please rate this serve.", Toast.LENGTH_SHORT).show();

            //upload to database;
        else finish();
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
            case RECORD_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this, "RECORD permission is granted.", Toast.LENGTH_SHORT).show();
                } else {
                    // Permission Denied
                    Toast.makeText(this, "Please grant RECORD permission.", Toast.LENGTH_SHORT).show();
                }
                return;
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this, "Storage permission is granted.", Toast.LENGTH_SHORT).show();
                    callcamera();
                } else {
                    // Permission Denied
                    Toast.makeText(this, "Please grant Storage permission.", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap picture;
        //扫描结果回调
        if (resultCode == RESULT_OK) { //RESULT_OK = -1
            switch(requestCode) {
                case CAPTURE_PICTURES:
                    picture = (Bitmap) data.getExtras().get("data");
                    ImageView image = (ImageView) findViewById(R.id.captured);
                    image.setImageBitmap(picture);
                    break;
                case CAPTURE_VIDEOS:
                    Uri uri = data.getData();
                    EditText text = (EditText)findViewById(R.id.commit);
                    text.setText(getRealPathFromUri(uri));
                    VideoView video = (VideoView) findViewById(R.id.videoView);
                    video.setVideoURI(uri);
                    video.start();
                    break;
                default:
                    Bundle bundle = data.getExtras();
                    String scanResult = bundle.getString("qr_scan_result");
                    //将扫描出的信息显示出来

                    EditText content = (EditText) findViewById(R.id.devicesmodel);
                    //content.setVisibility(View.VISIBLE);
                    content.setText(scanResult);
                    break;
            }
        }
    }

    private String getRealPathFromUri(Uri uri){
        String[] columns = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri,columns,null,null,null);
        String path = null;
        if(cursor.moveToFirst()) {
            path = cursor.getString(0);
            cursor.close();
        }
        return path;
    }

    public void camera(View v){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            callcamera();
        }
    }

    public void callcamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //file = new File(file,"picture.jpg");
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        if(isIntentAvialable(this, intent)){
            startActivityForResult(intent, CAPTURE_PICTURES);
        } else {
            Toast.makeText(this, "Camera application not found", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isIntentAvialable(Context context, Intent intent){
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public void gallery(View v){

    }

    public void recording(View v){
        if(!isRecording) {
            File dir = getRecordDir();
            if (dir == null) {
                Toast.makeText(this, "Dir not Found.", Toast.LENGTH_SHORT).show();
                return;
            }
            path = new File(dir, filename).getPath();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_PERMISSION);

            } else {
                if (recordAudio(path)) {
                    Toast.makeText(this, "Recording...", Toast.LENGTH_SHORT).show();
                    ImageView rec = (ImageView) findViewById(R.id.recording);
                    rec.setBackgroundColor(Color.RED);
                    isRecording = true;
                }
            }
        }
        else {
            if(mediaRecorder!=null){
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
                TextView test = (TextView) findViewById(R.id.recordreview);
                test.setText("Recorded: " + filename + "(touch to listen)");
                ImageView rec = (ImageView) findViewById(R.id.recording);
                rec.setBackgroundColor(Color.TRANSPARENT);
                isRecording = false;

            }
        }



    }

    public void video(View v){
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if(isIntentAvialable(this,intent)){
            startActivityForResult(intent, CAPTURE_VIDEOS);
        } else {
            Toast.makeText(this, "Camera application not found", Toast.LENGTH_SHORT).show();
        }
    }



    private boolean recordAudio(String path){

        if(mediaRecorder == null){
            mediaRecorder = new MediaRecorder();
        } else {
            mediaRecorder.reset();
        }
        try {
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(path);
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (Exception e) {
                Log.e(TAG, e.toString());
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                return false;
        }
        return true;

    }

    private boolean mediaMounted(){
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    private File getRecordDir(){
        if(!mediaMounted()) {
            return null;
        }
        return getExternalFilesDir(Environment.DIRECTORY_MUSIC);
    }

    public void touchtoplay(View v){
        playAudio(path);
    }

    private void playAudio(String path) {

        if (path == null) {
            Toast.makeText(this, "Nothing to play", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        } else {
            mediaPlayer.reset();
        }

        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepare();
            mediaPlayer.start();
            Toast.makeText(this, "Playing...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();

        }

    }



}
