package com.ljp.time.timealbum.adapter;



/*
 *@创建者       L_jp
 *@创建时间     2018/8/10 14:43.
 *@描述        相册文件夹 列表
 *
 *@更新者         $Author$
 *@更新时间         $Date$
 *@更新描述         ${TODO}
 */

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ljp.time.timealbum.bean.AlbumFolderBean;
import com.ljp.time.timealbum.bean.AlbumPhotoInfoBean;
import com.ljp.time.timealbum.R;

import java.util.ArrayList;


public class AlbumFolderAdapter extends BaseAdapter {


    private Context mContext;
    private int lastSelected = 0;
    private ArrayList<AlbumFolderBean> mAlbumFolderBeen = new ArrayList<>();


    public AlbumFolderAdapter(Context context) {
        mContext = context;
    }

    public void setLastSelected(int lastSelected) {
        this.lastSelected = lastSelected;
    }

    @Override
    public int getCount() {
        return mAlbumFolderBeen.size();
    }

    @Override
    public AlbumFolderBean getItem(int position) {
        return mAlbumFolderBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setAdapterList(ArrayList<AlbumFolderBean> list) {
        this.mAlbumFolderBeen = list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_rv_folders_view_item, viewGroup, false);
            holder = new ViewHolder(view);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        AlbumFolderBean albumFolderBean = getItem(position);
        if (albumFolderBean.getAlbumFolderList().size() > 0) {
            AlbumPhotoInfoBean albumPhotoInfoBean = albumFolderBean.getAlbumFolderList().get(1);
            Glide.with(mContext)
                    .load(Uri.parse("file://" + albumPhotoInfoBean.getPath())).into(holder.cover);
        } else {
            holder.cover.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.ic_launcher));
        }

        holder.name.setText(albumFolderBean.name);

        holder.size.setText((albumFolderBean.getAlbumFolderList().size() - albumFolderBean.getHeadPositionList().size()) + "个");
        holder.indicator.setVisibility(lastSelected == position ? View.VISIBLE : View.INVISIBLE);
        return view;
    }


    public void setSelectIndex(int i) {
        if (lastSelected == i) return;
        lastSelected = i;
        notifyDataSetChanged();
    }

    public ArrayList<AlbumPhotoInfoBean> getSelectMedias() {
        return mAlbumFolderBeen.get(lastSelected).getAlbumFolderList();
    }

    class ViewHolder {
        ImageView cover, indicator;
        TextView name, path, size;

        ViewHolder(View view) {
            cover = view.findViewById(R.id.cover);
            name = view.findViewById(R.id.name);
            path = view.findViewById(R.id.path);
            size = view.findViewById(R.id.size);
            indicator = view.findViewById(R.id.indicator);
            view.setTag(this);
        }
    }
}
