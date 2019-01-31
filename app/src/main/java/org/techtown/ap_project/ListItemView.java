package org.techtown.ap_project;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListItemView extends LinearLayout {

    TextView subjuectText;
    TextView locationText;

    //constructor 정의
    public ListItemView(Context context) {
        super(context);
        init(context);
    }

    public ListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    //init 정의 및 inflate 실행
    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_list_item, this, true);

        subjuectText = (TextView) findViewById(R.id.subjectText);
        locationText = (TextView) findViewById(R.id.locationText);

    }

   // 받아 온 값 설정하는 메서드
    public void setSubjuect(String sub){
        subjuectText.setText(sub);
    }

    public void setLocation(String loc){
        locationText.setText(loc);
    }
}
