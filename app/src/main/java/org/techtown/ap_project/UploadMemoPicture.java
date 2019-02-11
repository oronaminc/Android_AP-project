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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.ArrayList;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;



public class UploadMemoPicture extends AppCompatActivity {

    Button btnSave, btnWrite;
    EditText edit_text;
    RecyclerView recyclerView;
    //TextView text_view_fromUser;

    //txt 파일 경로 정하기
    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AP/";
    File dir = new File(path);
    String fullName = path + "USER_MEMO.txt";
    File file = new File (fullName);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_memo_picture);



        btnSave = (Button )findViewById( R.id.btnSave);
        btnWrite = (Button)findViewById(R.id.btnWrite);
        edit_text = (EditText)findViewById(R.id.edit_text);
        recyclerView = (RecyclerView) findViewById(R.id.main_rv);

        //text_view_fromUser = (TextView)findViewById(R.id.text_view_fromUser);

        //File 경로 없으면 만들어주기
        if (!dir.exists()) {
            dir.mkdirs();
        }


        final GalleryRecyclerViewAdapter adapter = new GalleryRecyclerViewAdapter(getApplicationContext(), new PhotoUtil().getAllPhotoPathList(getApplicationContext()));

        // recyclerview 그리드 형식의 3열로 셋팅
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        recyclerView.setAdapter(adapter);

        PermissionListener permissionListener = new PermissionListener() {
            // 접근권한 허용되었을 때
            @Override
            public void onPermissionGranted() {


                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                         String selectedPhoto = "";
                        for (Photo photo : adapter.getSelectedPhotos()) {
                            selectedPhoto += photo.getPath() + ", " + "\n";
                        }
                        Toast.makeText(getApplicationContext(), selectedPhoto + "메모 및 이미지를 보냈습니다.", Toast.LENGTH_LONG).show();

                        // EditText에서 값을 받아 드려옴.
                        String str = edit_text.getText().toString();
                        //text_view_fromUser.setText(str);

                        //File Write 하는 부분
                        FileWriter writer = null;
                        try {
                            writer = new FileWriter(file);
                            //writer = new FileWriter(file, true); 면 원래 파일에 추가해서 저장함.
                            writer.write(str);
                            writer.flush();
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
                            //Toast.makeText(getApplicationContext(), str2 + "를 저장했습니다.", Toast.LENGTH_SHORT).show();
                        } catch(IOException e){
                            Toast.makeText(getApplicationContext(), "파일을 읽어오는데 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }






                    }
                });
            }

            // 접근권한 허용되지 않았을 때
            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {

            }
        };
        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();


        /*
        btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


            }
        });
*/

        btnWrite.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(getApplicationContext(), "파일을 보냈습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
