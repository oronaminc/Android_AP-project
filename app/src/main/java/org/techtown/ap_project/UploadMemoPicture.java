package org.techtown.ap_project;

import android.content.Context;
import android.os.Environment;
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
import java.io.IOException;
import java.io.OutputStream;

public class UploadMemoPicture extends AppCompatActivity {

    Button btnRead, btnWrite;
    EditText edit_text;
    TextView text_view_fromUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_memo_picture);


        btnRead = (Button )findViewById( R.id.btnRead);
        btnWrite = (Button)findViewById(R.id.btnWrite);
        edit_text = (EditText)findViewById(R.id.edit_text);
        text_view_fromUser = (TextView)findViewById(R.id.text_view_fromUser);

        OutputStream outFs;

        /*
        특정 디렉토리를 MyDir
        특정 파일명은 MyImg.jpg라고 가정

        */


        // 이 메서드를 쓰게되면 별도의 저장 공간으로 가게 됨.
        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        sdPath += "/MyDir";
        File dir = new File(sdPath);
        dir.mkdirs();
        //sdPath += "/file.txt";
        // storage/emulated/0/



        /*
        file = new File(sdPath);
        try {
            file.createNewFile();
            Toast.makeText(getApplicationContext(), "이미지 디렉토리 및 파일생성 성공~",Toast.LENGTH_SHORT).show();
        } catch(IOException ie){
            Toast.makeText(getApplicationContext(), "이미지 디렉토리 및 파일생성 실패",Toast.LENGTH_SHORT).show();
        }
        */

        btnWrite.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {
                try {
                    //을 지정해주고 싶은데;........
                    FileOutputStream outFs = openFileOutput("file.txt", Context.MODE_PRIVATE);
                    String str = edit_text.getText().toString();
                    outFs.write(str.getBytes());
                    outFs.close();
                    text_view_fromUser.setText(str);
                    Toast.makeText(getApplicationContext(),"저장 완료입니다", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    return;
                }
            }
        });

        btnRead.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                try{
                    FileInputStream inFs = openFileInput("file.txt");
                    byte[] txt = new byte[1000];
                    inFs.read(txt);
                    String str = new String(txt);
                    Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                } catch(IOException e){
                    Toast.makeText(getApplicationContext(), "파일 없음", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
