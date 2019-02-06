package org.techtown.ap_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class WorkList extends AppCompatActivity {

    //Global 변수 선언
    String glob_ip;
    String glob_port;
    String glob_user;

    //Toolbar 선언
    Toolbar toolbar;

    //어댑터 변수 선언
    workListAdapter adapter;

    EditText editText2;
    EditText editText3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_list);

        // ip, port, user 값 받아오는 부분
        Intent intent = getIntent();
        glob_ip = intent.getExtras().getString("glob_ip");
        glob_port = intent.getExtras().getString("glob_port");
        glob_user = intent.getExtras().getString("glob_user");

        // 입력 txt 받아오는 변수 설정
        editText2 = (EditText) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText3);

        //ListView 변수 선언
        ListView listView = (ListView) findViewById(R.id.workList);

        //Toolbar 설정
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        if (glob_ip != null){
            getSupportActionBar().setTitle(glob_ip+" Connected");
        }else{
            getSupportActionBar().setTitle("AP Disconnected");
        }

        // 어댑터에 데이터 집어 넣기
        adapter = new workListAdapter();
        adapter.addItem(new ListItem("한국인프라 본사 이전 측정", "경기도 성남시 분당구"));
        adapter.addItem(new ListItem("메가 MD 신축 건물 측정", "서울특별시 강남구 역삼동"));
        adapter.addItem(new ListItem("SKY 캐슬 증축 토지 측정", "경기도 용인시 기흥구 보정동"));
        adapter.addItem(new ListItem("네이버 본사 토지 매입", "경기도 성남시 분당구 불정로"));

        listView.setAdapter(adapter);
        // 이제 어뎁터를 설정해봐요

        //어떤 item을 선택했는 지 알려줌 -> 차후에 activity로 이동 예정
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListItem item = (ListItem) adapter.getItem(position);
                Toast.makeText(getApplicationContext(), "선택 : " + item.getSubject(), Toast.LENGTH_LONG).show();
            }
        });

        // 아이템 추가 부분 (없애도 됨)
        Button button = (Button) findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = editText2.getText().toString();
                String location = editText3.getText().toString();
                adapter.addItem(new ListItem(subject, location));
                adapter.notifyDataSetChanged();
            }

        });

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

    // 어댑터 설정하는 부분
    class workListAdapter extends BaseAdapter{
        ArrayList<ListItem> items = new ArrayList<ListItem>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(ListItem item){
            items.add(item);
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListItemView view = null;
            if(convertView == null){
                view = new ListItemView(getApplicationContext());
            } else{
                view = (ListItemView) convertView;
            }

            ListItem item = items.get(position);
            view.setLocation(item.getLocation());
            view.setSubjuect(item.getSubject());

            return view;

        }
    }
}
