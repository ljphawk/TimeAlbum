package com.ljp.time.timealbum.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ljp.time.timealbum.R;
import com.ljp.time.timealbum.bean.AlbumPhotoInfoBean;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GridView mGridView;
    private PhotoAdapter mPhotoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGridView = findViewById(R.id.gridView);
        mPhotoAdapter = new PhotoAdapter();
        mGridView.setAdapter(mPhotoAdapter);
    }

    public void selectPhoto(View view) {
        CustomAlbumActivity.startActivity(this, 7, 100);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 100:
                    List<AlbumPhotoInfoBean> beanList = CustomAlbumActivity.obtainPathResult(data);
                    mPhotoAdapter.setShowItems(beanList);

                    break;
                default:
                    break;

            }
        }
    }

    class PhotoAdapter extends BaseAdapter {

        private List<AlbumPhotoInfoBean> mShowItems = new ArrayList<>();

        public void setShowItems(List<AlbumPhotoInfoBean> showItems) {
            if (showItems != null) {
                mShowItems = showItems;
            }
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mShowItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mShowItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rv_media_view_item, null);
            ImageView ivPhoto = convertView.findViewById(R.id.iv_photo);
            Uri mediaUri = Uri.parse("file://" + mShowItems.get(position).getPath());
            Glide.with(parent.getContext()).load(mediaUri.getPath()).into(ivPhoto);

            convertView.findViewById(R.id.fl_rb).setVisibility(View.GONE);

            return convertView;
        }
    }
}
