package org.techtown.ap_project;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

public class UploadMemoPicture extends AppCompatActivity {

    private static final int MY_PERMISSION_STORAGE = 1111;
    Button btnRead, btnWrite;
    EditText edit_text;
    TextView text_view_fromUser;

    //txt 파일 경로 정하기
    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AP/";
    File dir = new File(path);
    String fullName = path + "USER_MEMO.txt";
    File file = new File (fullName);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_memo_picture);

        checkPermission();

        btnRead = (Button )findViewById( R.id.btnRead);
        btnWrite = (Button)findViewById(R.id.btnWrite);
        edit_text = (EditText)findViewById(R.id.edit_text);
        text_view_fromUser = (TextView)findViewById(R.id.text_view_fromUser);

        //File 경로 없으면 만들어주기
        if (!dir.exists()) {
            dir.mkdirs();
        }


        btnWrite.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {

                // EditText에서 값을 받아 드려옴.
                String str = edit_text.getText().toString();
                text_view_fromUser.setText(str);

                //File Write 하는 부분
                FileWriter writer = null;
                try {
                    writer = new FileWriter(file);
                    //writer = new FileWriter(file, true); 면 원래 파일에 추가해서 저장함.
                    writer.write(str);
                    writer.flush();
                    Toast.makeText(getApplicationContext(), fullName + "에 저장했습니다.", Toast.LENGTH_SHORT).show();
                    writer.close();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "저장에 실패했습니다", Toast.LENGTH_SHORT).show();
                }

                //File Read 하는 부분
                FileReader reader = null;
                int data;
                char sh;
                String str2="";
                try {
                    reader = new FileReader(file);
                    while((data=reader.read()) != -1){
                        sh = (char)data;
                        str2 += sh;
                    }
                    Toast.makeText(getApplicationContext(), str2 + "를 저장했습니다.", Toast.LENGTH_SHORT).show();
                } catch(IOException e){
                    Toast.makeText(getApplicationContext(), "파일을 읽어오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnRead.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(getApplicationContext(), "파일을 보냈습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 다시 보지 않기 버튼을 만드려면 이 부분에 바로 요청을 하도록 하면 됨 (아래 else{..} 부분 제거)
            // ActivityCompat.requestPermissions((Activity)mContext, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_CAMERA);

            // 처음 호출시엔 if()안의 부분은 false로 리턴 됨 -> else{..}의 요청으로 넘어감
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(this)
                        .setTitle("알림")
                        .setMessage("저장소 권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용하셔야 합니다.")
                        .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getPackageName()));
                                startActivity(intent);
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_STORAGE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_STORAGE:
                for (int i = 0; i < grantResults.length; i++) {
                    // grantResults[] : 허용된 권한은 0, 거부한 권한은 -1
                    if (grantResults[i] < 0) {
                        Toast.makeText(UploadMemoPicture.this, "해당 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                // 허용했다면 이 부분에서..
                break;
        }
    }
}
