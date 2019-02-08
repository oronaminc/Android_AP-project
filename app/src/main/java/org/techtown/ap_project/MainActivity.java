package org.techtown.ap_project;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_STORAGE = 1111;

    //Global 변수 선언
    String glob_ip;
    String glob_port;
    String glob_user;

    //Toolbar 선언
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getIntent().getExtras() == null){
            // 로딩화면 띄우기
            Intent intent2 = new Intent(this, MainScreen.class);
            startActivity(intent2);
        }else{
            Intent intent = getIntent();
            glob_ip = intent.getExtras().getString("glob_ip");
            glob_port = intent.getExtras().getString("glob_port");
            glob_user = intent.getExtras().getString("glob_user");
        }

        //android ver 7 이상부터 위험권한 인증 해줘야 함
        checkPermission();

        // 툴바 사용 설정 및 제목 지정
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        if (glob_ip != null){
            getSupportActionBar().setTitle(glob_ip+" Connected");
        }else{
            getSupportActionBar().setTitle("AP Disconnected");
        }
    }

    // ip, port, user 를 공용으로 쓰게 하는 부분
    @Override
    protected void onStart(){
        super.onStart();


    }

    //main_menu (우측 상단에 있는 버튼) 을 불러오는 메서드
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    // main_menu (우측 상단에 있는 버튼 )에 세부 사항 설정
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.menu_setting:
                // ip, port, user 설정을 하는 창
                Intent intent = new Intent(this, PopupActivity.class);
                startActivityForResult(intent, 1);
                return true;

            case R.id.menu_log:
                // ip, port, user 설정을 확인하는 창
                Toast.makeText(getApplicationContext(),  "IP : "+glob_ip+"\n"+"PORT : "+glob_port+"\n"+"USER : "+glob_user, Toast.LENGTH_LONG).show();
                return true;

            default:
                // 이 외의 input이 들어온 경우
                Toast.makeText(getApplicationContext(), "Wrong input", Toast.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item);
        }
    }

    // 메인 화면 잘 넘어가는 지 확인
    public void gotoMemoPicture(View v){
        Intent intent = new Intent(this, UploadMemoPicture.class);
        startActivity(intent);
    }

    // 카메라 찍는 곳으로 넘어가는 메서드
    public void gotoCamera(View v){
        //ip, user, port는 기본적으로 넘겨줌
        Intent intent = new Intent(this, CameraActivity.class);
        intent.putExtra("glob_ip", glob_ip);
        intent.putExtra("glob_port", glob_port);
        intent.putExtra("glob_user", glob_user);
        startActivity(intent);

    }

    // 작업 리스트로 넘어가는 메서드
    public void gotoWorkList(View v){
        Intent intent = new Intent(this, WorkList.class);
        intent.putExtra("glob_ip", glob_ip);
        intent.putExtra("glob_port", glob_port);
        intent.putExtra("glob_user", glob_user);
        startActivity(intent);
    }

    // PopupActivity에서 들어온 값을 받는 부분
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                //데이터받아서 변수에 저장하는 부분
                String ip = data.getStringExtra("ip");
                String port = data.getStringExtra("port");
                String user = data.getStringExtra("user");

                //global 변수로 데이터 넘겨주기
                glob_ip=ip;
                glob_port=port;
                glob_user=user;
                Toast.makeText(getApplicationContext(),  "IP : "+glob_ip+"\n"+"PORT : "+glob_port+"\n"+"USER : "+glob_user, Toast.LENGTH_LONG).show();

                //ip가 연결 되었을 때 툴바 제목 바꾸어주기
                toolbar = (Toolbar) findViewById(R.id.tool_bar);
                setSupportActionBar(toolbar);
                getSupportActionBar().setTitle(ip+" Connected");
            }
        }
    }

    private void checkPermission() {
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
                        Toast.makeText(getApplicationContext(), "해당 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                // 허용했다면 이 부분에서..

                break;
        }
    }
}