package org.techtown.ap_project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;

public class MainScreen extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        startLoading();
    }

    // 시작할 때 MainSreen을 2초가 불러오는 메서드
    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                finish();
            }
        },2000);
    }
}