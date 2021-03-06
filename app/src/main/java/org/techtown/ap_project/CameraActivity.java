package org.techtown.ap_project;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Writer;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class CameraActivity extends AppCompatActivity{

    //json형식으로 정보 받아오는 URL
    final String url = "https://api.myjson.com/bins/d1s32";

    //activity request value 받아오는 glob value
    private static final int REQUEST_TAKE_PHOTO = 22;
    private static final int REQUEST_TAKE_ALBUM = 33;

    //카메라 찍고, 이미지 받아오는 부분
    Button Button_Camera;
    Button Button_Send;
    Button Button_Setting;
    ImageView imageView;
    String mCurrentPhotoPath;
    File storageDir;
    Integer photoName = 0;

    //Global 변수 선언
    String glob_ip;
    String glob_port;
    String glob_user;
    String glob_response="";

    //스래드 변수 선언
    String set_ip;
    String set_port;

    //Toolbar 선언
    Toolbar toolbar;

    //LinearLayout 불러오기
    LinearLayout container;

    //ListView 만들기
    ListView listView;

    //이미지 파일 관련된 변수
    File file;
    Uri imageUri;
    Uri photoURI, albumURI;



    //새로운 Point 배열 class를 만들어줌, 배열안에 x,y값을 갖도록
    public Point[] pointArr;
    //새로운 PolygonOptions 배열을 만들어줌, PolygonOptions는 원래 있던 Class임.
    public PolygonOptions[] fieldArr;
    //지도에 이용할 점, 지도 정의
    MapFragment mapFragment;
    Double pointX, pointY;
    String pointName, pointMemo;
    FieldResult field;
    GoogleMap map;
    Integer j;
    Uri uri;
    String fieldPath;

    //소켓통신 핸들러 정이
    Handler handler = new Handler();

    //데이터 받아올 때 필요한 변수
    String sdcard = Environment.getExternalStorageDirectory()+"/AP/";
    File dir = new File(sdcard);
    JSONObject obj;
    JSONParser jsonParser = new JSONParser();
    String folder="";


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
        //container = (LinearLayout) findViewById(R.id.container);
        listView = (ListView) findViewById(R.id.listView);
        imageView = (ImageView) findViewById(R.id.camera_image);
        Button_Send = (Button) findViewById(R.id.send_file);
        Button_Camera = (Button) findViewById(R.id.camera);
        Button_Setting =(Button) findViewById(R.id.setting);

        //Toolbar 설정
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        if (glob_ip != null){
            getSupportActionBar().setTitle(glob_ip+" Connected");
        }else{
            getSupportActionBar().setTitle("AP Disconnected");
        }



        /*
        //json을 GSON으로 받아드리는 부분(HTTP 통신을 통해서) volly 쓰는 곳
        RequestQueue rq = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(getApplicationContext(),response, Toast.LENGTH_SHORT).show();
                        dataMining(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };

        request.setShouldCache(false);
        rq.add(request);
        */

        //Fragement 에 지도 그리기
        FragmentManager fragmentManager = getFragmentManager();
        mapFragment = (MapFragment)fragmentManager.findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
            }
        });

        //Image File 만들기
        String sdcard = Environment.getExternalStorageDirectory()+"/AP/";
        File dir = new File(sdcard);
        if(!dir.exists()){dir.mkdirs();}
        //file = new File(dir, "photo.jpg");



        Button_Camera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        Log.e("captureCamera Error", ex.toString());
                    }
                    if (photoFile != null) {
                        // getUriForFile의 두 번째 인자는 Manifest provier의 authorites와 일치해야 함
                        Uri providerURI = FileProvider.getUriForFile(getApplicationContext(), "com.test.fileprovider", photoFile);
                        imageUri = providerURI;

                        // 인텐트에 전달할 때는 FileProvier의 Return값인 content://로만!!, providerURI의 값에 카메라 데이터를 넣어 보냄
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI);

                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "저장공간이 접근 불가능한 기기입니다", Toast.LENGTH_SHORT).show();
                }

                //ClientThread thread = new ClientThread(glob_ip, glob_port);
                //thread.start();
            }
        });

        Button_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addr = glob_ip;
                ConnectThread thread = new ConnectThread(addr);
                thread.start();
                Toast.makeText(getApplicationContext(), "파일을 보냈습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        Button_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UploadMemoPicture.class);
                intent.putExtra("fieldPath", fieldPath);
                intent.putExtra("pointName", pointName);
                intent.putExtra("pointMemo", pointMemo);
                intent.putExtra("glob_ip", glob_ip);
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();
        //ClientThread thread = new ClientThread(glob_ip, glob_port);
        //thread.start();


        try{
            ListenThread thread2 = new ListenThread(glob_ip);
            thread2.start();
        }catch(Exception e){
            Log.d("Client : ", "NO LISTENING");
            e.printStackTrace();
        }
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

    //지도에 나오는 요소들 표시하기
    public void dataMining(String response){

        //어댑터 선언
        PointAdapter adapter = new PointAdapter();



        Gson gson = new Gson();
        FieldResult field = gson.fromJson(response, FieldResult.class);

        // 도형이 몇개 있는 지
        int fieldNum = field.fieldResult.size();

        folder += String.valueOf(fieldNum);

        fieldArr = new PolygonOptions[fieldNum];

        for (int j = 0; j< fieldNum; j++){
            //도형안에 점이 몇개 있는 지
            String fieldName = field.fieldName;
            fieldPath = field.fieldResult.get(j).fieldKey;
            int pointNum = field.fieldResult.get(j).numbersOfPoint;
            folder += String.valueOf(pointNum);

            pointArr =new Point[pointNum];
            for(int i = 0; i<pointNum; i++) {
                pointName = field.fieldResult.get(j).fieldArea.get(i).pointName;
                pointMemo = field.fieldResult.get(j).fieldArea.get(i).memo_fromServer;
                pointX = Double.parseDouble(field.fieldResult.get(j).fieldArea.get(i).pointX);
                pointY = Double.parseDouble(field.fieldResult.get(j).fieldArea.get(i).pointY);

                LatLng pointValue = new LatLng(pointX,pointY);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(pointValue);
                markerOptions.title(fieldPath);
                markerOptions.snippet(pointName);

                String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/AP/"+fieldPath+"/"+pointName;
                File f = new File(path);
                //String[] list  = f.list();
                //Log.d("Tag", String.valueOf(list.length));
                if(f.exists()) {
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                }

                map.addMarker(markerOptions);

                //polygon 좌표 세팅하기
                Point point = new Point();
                point.x = pointX;
                point.y = pointY;
                pointArr[i] = point; //i는 for문에서 쓰는 int 변수
            }

            PolygonOptions rectOptions = new PolygonOptions()
                    .strokeColor(Color.RED)
                    .strokeWidth(5);
            for(int i=0; i<pointNum; i++){
                rectOptions.add(new LatLng(pointArr[i].x, pointArr[i].y));
            }
            rectOptions.add(new LatLng(pointArr[0].x, pointArr[0].y));
            map.addPolygon(rectOptions);
            fieldArr[j] = rectOptions;


        }

        Double x = pointX;
        Double y = pointY;
        adapter.addItem(new ListItem(field.fieldName, field.fieldLocation));

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                fieldPath = marker.getTitle();
                pointName = marker.getSnippet();

                String path2 = Environment.getExternalStorageDirectory().getAbsolutePath()+"/AP/"+fieldPath+"/"+pointName;
                File f = new File(path2);
                if(f.exists()) {
                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                }


                try{

                    String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/AP/"+marker.getTitle()+"/"+marker.getSnippet()+"/point1.jpg";
                    File dir = new File(path);
                    if (!dir.exists()) {
                        imageView.setImageResource(R.drawable.noimage);
                    } else {
                        imageView.setImageURI(Uri.parse(path));
                    }



                }catch(Exception e){
                    e.printStackTrace();
                }

                map.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                map.animateCamera(CameraUpdateFactory.zoomTo(19));

                return false;
            }
        });

        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(pointX,pointY)));
        map.animateCamera(CameraUpdateFactory.zoomTo(14));

        listView.setAdapter(adapter);

    }



    //점으로 사용할 클래스 정의하기
    public class Point{
        public double x;
        public double y;
    }

    //파일 알아서 생성하기
    public File createImageFile() throws IOException {
        // Create an image file name
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File imageFile = null;

        File storageDir = new File(Environment.getExternalStorageDirectory() + "/AP", fieldPath+"/"+pointName);

        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        String[] list = storageDir.list();
        photoName = list.length + 1;
        String imageFileName = "point" + photoName + ".jpg";


        imageFile = new File(storageDir, imageFileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();

        return imageFile;
    }

    private void galleryAddPic(){
        Log.i("galleryAddPic", "Call");
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        // 해당 경로에 있는 파일을 객체화(새로 파일을 만든다는 것으로 이해하면 안 됨)
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
        Toast.makeText(this, contentUri.getPath() + "에 저장되었습니다.", Toast.LENGTH_SHORT).show();
    }

    // PopupActivity에서 들어온 값을 받는 부분
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                //데이터받아서 변수에 저장하는 부분
                String ip = data.getStringExtra("glob_ip");
                String port = data.getStringExtra("glob_port");
                String user = data.getStringExtra("glob_user");
                //String response = data.getStringExtra("glob_json");

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
        } else if (requestCode == REQUEST_TAKE_PHOTO){
            if(resultCode == RESULT_OK){
                galleryAddPic();
                imageView.setImageURI(imageUri);
            } else{
                Toast.makeText(getApplicationContext(),"사진 찍기를 취소했습니다.", Toast.LENGTH_SHORT).show();
            }
        } else if(requestCode == REQUEST_TAKE_ALBUM){
            if(resultCode == RESULT_OK){
                Toast.makeText(getApplicationContext(),"저장했습니다.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(),"취소했습니다.", Toast.LENGTH_SHORT).show();
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

    // 점에 대한 어댑터 정의
    class PointAdapter extends BaseAdapter {

        ArrayList<ListItem> items = new ArrayList<ListItem>();

        //어뎁터에 추가해주는 부분
        public void addItem(ListItem item){
            items.add(item);
        }

        @Override
        public int getCount() {
            return items.size();
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
            //변수에 값을 넣어주는 View class 정의
            ListItemView view = new ListItemView(getApplicationContext());
            //변수 자체를 정의함
            ListItem item = items.get(position);

            view.setSubjuect(item.getSubject());
            view.setLocation(item.getLocation());

            return view;
        }
    }




    class ConnectThread extends Thread {
        String hostname;
        public ConnectThread(String addr) {
            hostname = addr;
        }
        public void run() {
            try {
                int port = 11001;
                Socket sock = new Socket(hostname, port);
                ObjectOutputStream outstream = new ObjectOutputStream(sock.getOutputStream());
                outstream.writeObject(folder);
                outstream.flush();
                sock.close();
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


    class ListenThread extends Thread {
        String hostname;

        public ListenThread(String addr) {
            hostname = addr;
        }

        Writer output = null;

        public void run() {
            try {
                while (true) {
                    int port = 11002;
                    Socket sock = new Socket(hostname, port);
                    ObjectInputStream instream = new ObjectInputStream(sock.getInputStream());
                    obj = (JSONObject) instream.readObject();
                    final String json = obj.toJSONString().trim();
                    //obj.put("name", "@서버에서왔쪄욤@");
                    //final String name = (String) obj.get("name");
                    Log.d("MainActivity", "서버에서 받은 메시지 : " + json);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (!dir.exists()) {
                                    dir.mkdirs();
                                }
                                dataMining(json);
                                File file = new File(dir + "/data.json");
                                output = new BufferedWriter(new FileWriter(file));
                                output.write(obj.toString());
                                output.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(getApplicationContext(), dir + "/data.json에 저장했어요", Toast.LENGTH_SHORT).show();
                            Log.d("ClientReceiver : ", json);
                        }
                    });
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }
}