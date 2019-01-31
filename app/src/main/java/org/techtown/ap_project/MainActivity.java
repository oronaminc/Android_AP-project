package org.techtown.ap_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Global 변수 선언
    String glob_ip;
    String glob_port;
    String glob_user;

    //Toolbar 선언
    Toolbar toolbar;

    /* 점검용으로 쓰임
    TextView popup_result_ip;
    TextView popup_result_port;
    TextView popup_result_user;
    */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 로딩화면 띄우기
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);

        // 툴바 사용 설정 및 제목 지정
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("AP Disconnected");

        /*점검용으로 쓰임
        popup_result_ip = (TextView)findViewById(R.id.popup_result_ip);
        popup_result_port = (TextView)findViewById(R.id.popup_result_port);
        popup_result_user = (TextView)findViewById(R.id.popup_result_user);
        */



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
    public void gotoMain(View v){
        Intent intent = new Intent(this, MainScreen.class);
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
        //startActivityForResult(intent, 1);
        //intent.putExtra("data", "Test Popup");
        //putExtra(key, value) 메소드를 통해서 데이터를 전송할 수 있음
        //응답 처리가 필요한 경우에 startActivityForResult() 메소드를 사용
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

                /* 점검용으로 쓰임
                //저장된 변수를 main의 textView에 전달하는 부분
                popup_result_ip.setText(ip);
                popup_result_port.setText(port);
                popup_result_user.setText(user);
                */

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
}