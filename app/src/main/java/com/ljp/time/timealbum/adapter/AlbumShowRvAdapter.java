package com.ljp.time.timealbum.adapter;


import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ljp.time.timealbum.bean.AlbumPhotoInfoBean;
import com.ljp.time.timealbum.utils.DateUtils;
import com.ljp.time.timealbum.R;
import com.ljp.time.timealbum.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/*
 *@创建者       L_jp
 *@创建时间     2018/8/10 14:43.
 *@描述        相册图片展示 列表
 *
 *@更新者         $Author$
 *@更新时间         $Date$
 *@更新描述         ${TODO}
 */

public class AlbumShowRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int mMaxSelectCount;
    private List<AlbumPhotoInfoBean> mShowItems = new ArrayList<>();

    private List<AlbumPhotoInfoBean> selectList = new ArrayList<>();
    private Context mContext;

    public static final int HEAD_TYPE = 0;
    public static final int BODY_TYPE = 1;
    private List<Integer> mHeadPositionList = new ArrayList<>();

    public AlbumShowRvAdapter(Context mContext, int maxSelectCount) {
        this.mContext = mContext;
        this.mMaxSelectCount = maxSelectCount;
    }


    @Override
    public int getItemViewType(int position) {
        return mShowItems.get(position).getDataType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == HEAD_TYPE) {
            return new HeadViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_rv_calendar_timeaxis_item_head, null));
        } else if (viewType == BODY_TYPE) {
            return new BodyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_rv_media_view_item, null));
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final AlbumPhotoInfoBean albumPhotoInfoBean = mShowItems.get(position);
        if (albumPhotoInfoBean == null) {
            return;
        }

        if (holder instanceof BodyViewHolder) {

            Uri mediaUri = Uri.parse("file://" + albumPhotoInfoBean.getPath());

            Glide.with(mContext).load(mediaUri).into(((BodyViewHolder) holder).mIvPhoto);

            if (albumPhotoInfoBean.getMediaType() == 3) {
                ((BodyViewHolder) holder).mRlGifInfo.setVisibility(View.INVISIBLE);
                ((BodyViewHolder) holder).mRlVideoInfo.setVisibility(View.VISIBLE);
                ((BodyViewHolder) holder).mTvVideoTime.setText(albumPhotoInfoBean.getDuration());
            } else {
                ((BodyViewHolder) holder).mRlVideoInfo.setVisibility(View.INVISIBLE);
                ((BodyViewHolder) holder).mRlGifInfo.setVisibility(".gif".equalsIgnoreCase(albumPhotoInfoBean.getExtension()) ? View.VISIBLE : View.INVISIBLE);
            }

            ((BodyViewHolder) holder).mTvTime.setText(DateUtils.getSdfTime(albumPhotoInfoBean.getTime() + "", DateUtils.YMDHMS2));

            boolean isSelect = isSelect(albumPhotoInfoBean) >= 0;
            ((BodyViewHolder) holder).mask_view.setVisibility(isSelect ? View.VISIBLE : View.GONE);
            ((BodyViewHolder) holder).mRbSelect.setChecked(isSelect);

            ((BodyViewHolder) holder).mFlRb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int index = isSelect(albumPhotoInfoBean);
                    if (index >= 0) {
                        selectList.remove(index);
                    } else {
                        if (selectList.size() >= mMaxSelectCount) {
                            ToastUtil.showToast(mContext, "最多选择" + mMaxSelectCount + "张");
                            return;
                        }
                        selectList.add(albumPhotoInfoBean);
                    }

                    ((BodyViewHolder) holder).mask_view.setVisibility(index >= 0 ? View.GONE : View.VISIBLE);
                    ((BodyViewHolder) holder).mRbSelect.setChecked(!(index >= 0));

                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onRbClick(selectList.size());
                    }
                }
            });

            ((BodyViewHolder) holder).mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(albumPhotoInfoBean, position);
                    }
                }
            });
        } else if (holder instanceof HeadViewHolder) {
            ((HeadViewHolder) holder).mTvTitle.setText(DateUtils.getSdfTime(albumPhotoInfoBean.getTime() + "", DateUtils.YMDHMS2));
        }
    }


    public void updateAdapterList(List<AlbumPhotoInfoBean> list, List<Integer> headPositionList) {
        this.mHeadPositionList = headPositionList;
        if (list != null) {
            this.mShowItems = list;
            notifyDataSetChanged();
        }
    }

    public void setSelectList(List<AlbumPhotoInfoBean> list) {
        if (list != null) {
            this.selectList = list;
            notifyDataSetChanged();
        }
    }

    public List<Integer> getHeadPositionList() {
        return mHeadPositionList;
    }

    /**
     * //获取当前相同时间的position
     * @param position firstVisiblePosition
     * @return
     */
    public int getCurrentTimeItemCount(int position) {
        int count = -1;
        for (int i = 0; i < mHeadPositionList.size(); i++) {
            if (i + 1 < mHeadPositionList.size()) {
                if (position > mHeadPositionList.get(i) && position < mHeadPositionList.get(i + 1)) {
                    return mHeadPositionList.get(i + 1) - mHeadPositionList.get(i);
                }
            }
        }

        return count;
    }

    /**
     * @param media
     * @return 大于等于0 就是表示以选择，返回的是在selectMedias中的下标
     */
    public int isSelect(AlbumPhotoInfoBean media) {
        int index = -1;
        if (selectList.size() <= 0) {
            return index;
        }
        for (int i = 0; i < selectList.size(); i++) {
            AlbumPhotoInfoBean infoBean = selectList.get(i);
            if (infoBean.getPath().equals(media.getPath())) {
                return i;
            }
        }
        return index;
    }

    public List<AlbumPhotoInfoBean> getAllDataNoHead() {
        List<AlbumPhotoInfoBean> mAllItems = new ArrayList<>();
        for (int i = 0; i < mShowItems.size(); i++) {
            if (mShowItems.get(i).getDataType() == BODY_TYPE) {
                mAllItems.add(mShowItems.get(i));
            }
        }
        return mAllItems;
    }

    public List<AlbumPhotoInfoBean> getAllData() {
        return mShowItems;
    }


    public List<AlbumPhotoInfoBean> getAllSelectData() {
//        ArrayList<AlbumPhotoInfoBean> selectList = new ArrayList<>();
//
//        for (int i = 0; i < mShowItems.size(); i++) {
//            AlbumPhotoInfoBean infoBean = mShowItems.get(i);
//            if (isSelect(infoBean) >= 0 && new File(infoBean.getPath()).exists()) {
//                selectList.add(infoBean);
//            }
//        }
        return selectList;
    }


    public class BodyViewHolder extends RecyclerView.ViewHolder {
        private ImageView mIvPhoto;
        private View mask_view;
        private TextView mTvVideoTime;
        private RelativeLayout mRlGifInfo;
        private RelativeLayout mRlVideoInfo;
        private TextView mTvTime;
        private RadioButton mRbSelect;
        private FrameLayout mFlRb;
        private View mItemView;

        public BodyViewHolder(View view) {
            super(view);
            this.mItemView = view;
            mIvPhoto = view.findViewById(R.id.iv_photo);
            mRbSelect = view.findViewById(R.id.rb_selected);
            mTvTime = view.findViewById(R.id.tv_time);
            mask_view = view.findViewById(R.id.mask_view);
            mRlVideoInfo = view.findViewById(R.id.rl_video_info);
            mRlGifInfo = view.findViewById(R.id.rl_gif_info);
            mTvVideoTime = view.findViewById(R.id.tv_video_time);
            mFlRb = view.findViewById(R.id.fl_rb);
        }
    }


    public class HeadViewHolder extends RecyclerView.ViewHolder {

        public TextView mTvTitle;

        public HeadViewHolder(View itemView) {
            super(itemView);
            mTvTitle = itemView.findViewById(R.id.tv_title);
        }
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    @Override
    public int getItemCount() {
        return mShowItems.size();
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(AlbumPhotoInfoBean data, int position);

        void onRbClick(int selectCount);
    }
}
