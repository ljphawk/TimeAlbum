package com.ljp.time.timealbum.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.ljp.time.timealbum.R;
import com.ljp.time.timealbum.bean.AlbumPhotoInfoBean;

import java.io.File;
import java.util.List;

/**
 * 预览
 */

public class PreviewFragment extends BaseFragment implements View.OnClickListener {
    private PhotoView mPhotoView;
    ImageView mIvPlayVideo;
    private AlbumPhotoInfoBean mPhotoInfoBean;

    public static PreviewFragment newInstance(AlbumPhotoInfoBean media) {
        PreviewFragment f = new PreviewFragment();
        Bundle b = new Bundle();
        b.putSerializable("media", media);
        f.setArguments(b);
        return f;
    }


    @Override
    protected View resFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo_preview, null);
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {

        mPhotoInfoBean = (AlbumPhotoInfoBean) getArguments().getSerializable("media");
        mIvPlayVideo = view.findViewById(R.id.iv_play_video);
        mPhotoView = view.findViewById(R.id.photoview);

        mIvPlayVideo.setOnClickListener(this);


    }

    @Override
    protected void initData(View view, Bundle savedInstanceState) {
        if (mPhotoInfoBean.getMediaType() == 3) {
            mIvPlayVideo.setVisibility(View.VISIBLE);
        } else {
            mIvPlayVideo.setVisibility(View.GONE);
        }

        Glide.with(getActivity()).load(mPhotoInfoBean.getPath()).into(mPhotoView);


        mPhotoView.setMaximumScale(5);
        mPhotoView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
                PreViewPhotoActivity previewActivity = (PreViewPhotoActivity) getActivity();
                previewActivity.setBarStatus();
            }
        });
    }


    private Uri getUri(String path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(mContext,  "com.ljp.time.timealbum.fileprovider", new File(path));
        } else {
            return Uri.fromFile(new File(path));
        }
    }

    /**
     * 检查是否有可以处理的程序
     *
     * @param context
     * @param intent
     * @return
     */
    private boolean isIntentAvailable(Context context, Intent intent) {
        List resolves = context.getPackageManager().queryIntentActivities(intent, 0);
        return resolves.size() > 0;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_play_video:
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(getUri(mPhotoInfoBean.getPath()), "video/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    if (isIntentAvailable(getContext(), intent)) {
                        startActivity(intent);
                    } else {
                        showToast("没有可以播放的程序");
                    }
                } catch (Exception e) {
                    showToast("播放出错");
                }
                break;
        }


    }
}