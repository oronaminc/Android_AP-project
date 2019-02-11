package org.techtown.ap_project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class GalleryDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_detail);
        ImageView imageView = findViewById(R.id.gallery_detail_iv);
        String imgPath = getIntent().getStringExtra("imgPath");
        Glide.with(this).load(imgPath).into(imageView);
    }
}
