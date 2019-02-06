package org.techtown.ap_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {

    //카메라 찍고, 이미지 받아오는 부분
    Button Button_Camera;
    ImageView imageView;

    //Global 변수 선언
    String glob_ip;
    String glob_port;
    String glob_user;

    //Toolbar 선언
    Toolbar toolbar;

    //LinearLayout 불러오기
    LinearLayout container;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // ip, port, user 값 받아오는 부분
        Intent intent = getIntent();
        glob_ip = intent.getExtras().getString("glob_ip");
        glob_port = intent.getExtras().getString("glob_port");
        glob_user = intent.getExtras().getString("glob_user");

        //화면에 어떤 요소를 배치할 지 받아오기
        container = (LinearLayout) findViewById(R.id.container);
        imageView = (ImageView) findViewById(R.id.view);
        Button_Camera = (Button) findViewById(R.id.camera);
        Button_Camera.setOnClickListener(this);



        //Toolbar 설정
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        if (glob_ip != null){
            getSupportActionBar().setTitle(glob_ip+" Connected");
        }else{
            getSupportActionBar().setTitle("AP Disconnected");
        }

        //어떤 자료인 지 보여주기
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_list_item, container, true);
    }

    //main_menu (우측 상단에 있는 버튼) 을 불러오는 함수
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

    //  사진을 찍고 사진을 가져와요, intent의 결과를 여기서 확인할 수 있음
    @Override
    public void onClick(View view){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i,0);
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


    // 뒤로가기 누르면 ip, port, user 이름 그대로 전달
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("glob_ip", glob_ip);
        intent.putExtra("glob_port", glob_port);
        intent.putExtra("glob_user", glob_user);
        startActivity(intent);

    }

}