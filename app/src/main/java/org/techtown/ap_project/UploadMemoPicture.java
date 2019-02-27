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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.ArrayList;
import java.lang.String;

import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class UploadMemoPicture extends AppCompatActivity {

    Button btnSave, btnWrite;
    EditText edit_text;
    RecyclerView recyclerView;
    TextView textView;
    //TextView text_view_fromUser;

    //txt 파일 경로 정하기
    File dir;
    File file;
    String path, path2;
    String fullName;
    Intent intent;
    //String fieldPath;
    //String pointName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_memo_picture);



        btnSave = (Button )findViewById( R.id.btnSave);
        btnWrite = (Button)findViewById(R.id.btnWrite);
        edit_text = (EditText)findViewById(R.id.edit_text);
        recyclerView = (RecyclerView) findViewById(R.id.main_rv);
        textView = (TextView)findViewById(R.id.text_view_fromMain);

        //text_view_fromUser = (TextView)findViewById(R.id.text_view_fromUser);

        intent = getIntent();
        final String fieldPath = intent.getStringExtra("fieldPath");
        final String pointName = intent.getStringExtra("pointName");
        final String pointMemo = intent.getStringExtra("pointMemo");
        final String glob_ip = intent.getStringExtra("glob_ip");

        final char[] arr = pointName.toCharArray();
        //final char pointName2 = arr[arr.length-1];
        //Toast.makeText(getApplicationContext(), pointName2, Toast.LENGTH_SHORT).show();
        textView.setText(pointMemo);

        path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AP/" + fieldPath + "/" + pointName +"/";
        path2 = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/AP/" + fieldPath + "/";
        //path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AP/album";

        dir = new File(path2);
        //File 경로 없으면 만들어주기
        if (!dir.exists()) {
            dir.mkdirs();
        }
        fullName = path2 + "USER_MEMO.txt";
        file = new File (fullName);




        Toast.makeText(getApplicationContext(), fieldPath, Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), pointName, Toast.LENGTH_SHORT).show();

        //final GalleryRecyclerViewAdapter adapter = new GalleryRecyclerViewAdapter(getApplicationContext(), new PhotoUtil().getAllPhotoPathList(getApplicationContext()));
        final GalleryRecyclerViewAdapter adapter = new GalleryRecyclerViewAdapter(getApplicationContext(), new PhotoUtil(fieldPath,pointName).getAllPhotoPathList(getApplicationContext()));

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

                        Writer output = null;
                        FieldResult field = new FieldResult();
                         String selectedPhoto = "";
                        JSONParser jsonParser = new JSONParser();
                        ArrayList<String> arr = new ArrayList<String>();
                        for (Photo photo : adapter.getSelectedPhotos()) {
                            arr.add(photo.getPath());
                            //Log.d("kkkkkk",String.valueOf(arr.indexOf(1)));
                            selectedPhoto += photo.getPath() + ", " + "\n";
                        }

                        Toast.makeText(getApplicationContext(), selectedPhoto + "저장했습니다.", Toast.LENGTH_LONG).show();


                        //String path2 = Environment.getExternalStorageDirectory().getAbsolutePath()+"/AP/"+fieldPath+"/"+pointName;
                        //File f = new File(path2);
                        String str = edit_text.getText().toString();

                        String sdcard = Environment.getExternalStorageDirectory()+"/AP/";
                        try {
                            JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(sdcard + "/data.json"));
                            String response = obj.toJSONString().trim();

                           /*
                            Gson gson = new Gson();
                            field = gson.fromJson(response, FieldResult.class);
                            field.fieldResult.get(Integer.valueOf(fieldPath)-1).fieldArea.get(Integer.valueOf(String.valueOf(arr[5]))-1).memo_fromUser = str;
                            Log.d("What is th problem??", field.fieldResult.get(Integer.valueOf(fieldPath)-1).fieldArea.get(Integer.valueOf(String.valueOf(arr[5]))-1).memo_fromUser);

                            JSONParser jsonParser2 = new JSONParser();
                            JSONObject obj2 = (JSONObject) gson.toJsonTree(field);
                            File file = new File(path2 + "/FromClient_data.json");
                            output = new BufferedWriter(new FileWriter(file));
                            output.write(obj.toString());
                            output.close();
                            */

                        } catch(FileNotFoundException e){
                            e.printStackTrace();
                        } catch(IOException ee){
                            ee.printStackTrace();
                        } catch(org.json.simple.parser.ParseException eee){
                            eee.printStackTrace();
                        }




                        FileWriter writer2 = null;
                        //File Write 하는 부분
                        try {

                            //Writer output2 = null;
                            //FileOutputStream fos = new FileOutputStream(file);
                            //BufferedWriter output2 = new BufferedWriter(new OutputStreamWriter(fos,"UTF8"));
                            //output2.write(str);
                            //output2.close();
                            //fos.close();

                            writer2 = new FileWriter(file, true);
                            writer2.write(pointName + " : " +str +"\n");
                            writer2.flush();
                            writer2.close();
                            //writer = new FileWriter(file, true); 면 원래 파일에 추가해서 저장함.

                        } catch (IOException e) {
                            Toast.makeText(getApplicationContext(), "저장에 실패했습니다", Toast.LENGTH_SHORT).show();
                        }
                        /*
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
                        }*/
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

        btnWrite.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String addr = glob_ip;
                //ImageThread thread = new ImageThread(addr);
                //thread.start();
                Toast.makeText(getApplicationContext(), "파일을 보냈습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }




    class ImageThread extends Thread {
        String hostname;
        public ImageThread(String addr) {
            hostname = addr;
        }
        public void run() {
            try {
                int port = 11001;
                //Socket sock = new Socket(hostname, port);

                //InputStream in = sock.getInputStream();
                //FileOutputStream out = new FileOutputStream();


                //ObjectOutputStream outstream = new ObjectOutputStream(sock.getOutputStream());
                //outstream.writeObject(folder);
                //outstream.flush();
                //sock.close();
                /*
                JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(sdcard+"/data.json"));

                ObjectOutputStream outstream = new ObjectOutputStream(sock.getOutputStream());
                //outstream.writeObject(input01.getText().toString().trim());
                outstream.writeObject(obj);
                outstream.flush();
                Log.d("MainActivity", "서버로 보낼 메시지 : " + "서버로 잘 보냈습니다");
                sock.close();
                */

            } catch(Exception ex) {
                ex.printStackTrace();
            }

        }
    }

}
