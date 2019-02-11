package org.techtown.ap_project;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class GalleryRecyclerViewAdapter extends RecyclerView.Adapter<GalleryRecyclerViewAdapter.GalleryRecyclerViewHolder> {

    private Context mContext;
    // 사진들 정보가 담긴 어레이 리스트
    private ArrayList<Photo> photos = new ArrayList<>();
    // 선택한 사진들 담는 어레이 리스트
    private ArrayList<Photo> selectedPhotos = new ArrayList<>();

    public GalleryRecyclerViewAdapter(Context mContext, ArrayList<Photo> photos) {
        this.mContext = mContext;
        this.photos = photos;
    }

    @NonNull
    @Override
    public GalleryRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 뷰 인플레이트
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item, null, false);

        // 뷰(리사이클러뷰 각각의 아이템)의 높이를 Width와 같게해준다.
        // 열의 갯수에 따라 숫자를 변경시켜주면 됨.
        int height = parent.getWidth() / 3;
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        return new GalleryRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GalleryRecyclerViewHolder holder, final int position) {
        // 이미지 로딩라이브러리를 통해 이미지뷰에 불러온 사진을 적용시킨다.
        // apply(new RequestOptions().centerCrop())를 통해 크기에 맞게 사진의 중심 일부만 잘라서 보여준다.
        Glide.with(mContext).load(photos.get(position).getPath()).apply(new RequestOptions().centerCrop()).into(holder.imageView);

        // 체크박스 초기화 ( 뷰가 재생성되어서 사용되기 때문에 초기화를 해주지 않으면, 선택되지 않았던 아이템들도 선택되어서 보여진다.
        initCheckBox(holder, position);

        // 체크 박스 이벤트 설정
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCheckBox(holder, position);
            }
        });

        // 이미지뷰 이벤트 설정
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCheckBox(holder, position);
            }
        });

        // 이미지 버튼 이벤트 설정
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectGalleryDetailActivity(photos.get(position).getPath());
            }
        });

    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public ArrayList<Photo> getSelectedPhotos() {
        return selectedPhotos;
    }

    private void initCheckBox(GalleryRecyclerViewHolder holder, int position) {
        if (photos.get(position).isSelected()){
            holder.checkBox.setChecked(true);
        }else {
            holder.checkBox.setChecked(false);
        }
    }

    private void setCheckBox(GalleryRecyclerViewHolder holder, int position) {
        if (photos.get(position).isSelected()) {
            holder.checkBox.setChecked(false);
            photos.get(position).setSelected(false);
            selectedPhotos.remove(photos.get(position));
        }else {
            holder.checkBox.setChecked(true);
            photos.get(position).setSelected(true);
            selectedPhotos.add(photos.get(position));
        }
    }

    private void redirectGalleryDetailActivity(String imgPath) {
        Intent intent = new Intent(mContext, GalleryDetailActivity.class);
        intent.putExtra("imgPath", imgPath);
        mContext.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public class GalleryRecyclerViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        CheckBox checkBox;
        ImageButton imageButton;
        public GalleryRecyclerViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recyclerview_item_iv);
            checkBox = itemView.findViewById(R.id.recyclerview_item_cb);
            imageButton = itemView.findViewById(R.id.recyclerview_item_ib);
        }
    }
}