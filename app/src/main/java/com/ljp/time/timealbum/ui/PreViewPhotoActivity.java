package com.ljp.time.timealbum.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ljp.time.timealbum.HackyViewPager;
import com.ljp.time.timealbum.R;
import com.ljp.time.timealbum.adapter.PhotoPreViewViewPagerAdapter;
import com.ljp.time.timealbum.bean.AlbumPhotoInfoBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;




/*
 *@创建者       L_jp
 *@创建时间     2018/8/10 16:22.
 *@描述        预览图片 viewpager
 *
 *@更新者         $Author$
 *@更新时间         $Date$
 *@更新描述         ${TODO}
 */

public class PreViewPhotoActivity extends BaseActivity implements View.OnClickListener {

    private HackyViewPager mViewPager;
    private RadioButton mRbSelect;
    private RelativeLayout mRlPreViewBottom;
    private TextView mTvDoneCount;
    private TextView mTvLeftCount;
    private ImageView mIvBack;
    private RelativeLayout mRlPreViewTop;
    private static final String photoListExtra = "photoListExtra";
    private static final String selectListExtra = "selectListExtra";
    private static final String positionExtra = "positionExtra";
    private static final String maxSelectExtra = "maxSelectExtra";
    private List<AlbumPhotoInfoBean> mAllPhotoList;
    private List<AlbumPhotoInfoBean> mSelectList;
    private int mMaxSelectCount;
    private int lastViewPageItemPosition;


    public static void startActivity(Activity activity, int position, int maxSelect, List<AlbumPhotoInfoBean> list, List<AlbumPhotoInfoBean> selectList, int requestCode) {
        Intent intent = new Intent(activity, PreViewPhotoActivity.class);
        intent.putExtra(photoListExtra, (Serializable) list);
        intent.putExtra(selectListExtra, (Serializable) selectList);
        intent.putExtra(positionExtra, position);
        intent.putExtra(maxSelectExtra, maxSelect);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected int resView() {
        return R.layout.activity_photo_preview;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mViewPager = (HackyViewPager) findViewById(R.id.viewpager);
        mRlPreViewTop = (RelativeLayout) findViewById(R.id.rl_preview_top);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvLeftCount = (TextView) findViewById(R.id.tv_left_count);
        mTvDoneCount = (TextView) findViewById(R.id.tv_done_count);
        mRlPreViewBottom = (RelativeLayout) findViewById(R.id.rl_preview_bottom);
        mRbSelect = (RadioButton) findViewById(R.id.rb_selected);

        mIvBack.setOnClickListener(this);
        mRbSelect.setOnClickListener(this);
        mTvDoneCount.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mAllPhotoList = (List<AlbumPhotoInfoBean>) intent.getSerializableExtra(photoListExtra);
        mSelectList = (List<AlbumPhotoInfoBean>) intent.getSerializableExtra(selectListExtra);
        int position = intent.getIntExtra(positionExtra, 1);
        lastViewPageItemPosition = position;
        mMaxSelectCount = intent.getIntExtra(maxSelectExtra, 1);

        mTvLeftCount.setText((position + 1) + "/" + mAllPhotoList.size());
        upDataDoneText();
        mRbSelect.setChecked(isSelect(mAllPhotoList.get(position), mSelectList) >= 0);

        List<BaseFragment> fragmentList = new ArrayList<>();
        for (int i = 0; i < mAllPhotoList.size(); i++) {
            fragmentList.add(PreviewFragment.newInstance(mAllPhotoList.get(i)));
        }

        final PhotoPreViewViewPagerAdapter viewPagerAdapter = new PhotoPreViewViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.setCurrentItem(position);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                int dataType = mAllPhotoList.get(position).getDataType();
                if (dataType == 0) {
                    if (position > lastViewPageItemPosition || position == 0) {
                        mViewPager.setCurrentItem(position + 1);
                    } else if (position < lastViewPageItemPosition || position == viewPagerAdapter.getCount() - 1) {
                        mViewPager.setCurrentItem(position - 1);
                    }
                    mRbSelect.setChecked(false);
                } else {
                    mTvLeftCount.setText((position + 1) + "/" + mAllPhotoList.size());
                    mRbSelect.setChecked(isSelect(mAllPhotoList.get(position), mSelectList) >= 0);
                }
                lastViewPageItemPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                doneSelect(BACK_CODE);
                break;
            case R.id.tv_done_count:
                doneSelect(DONE_CODE);
                break;
            case R.id.rb_selected:

                AlbumPhotoInfoBean infoBean = mAllPhotoList.get(mViewPager.getCurrentItem());
                if (infoBean.getDataType() == 0) {
                    return;
                }

                int index = isSelect(infoBean, mSelectList);
                if (index >= 0) {
                    mSelectList.remove(index);
                    mRbSelect.setChecked(false);
                } else {
                    if (mSelectList.size() >= mMaxSelectCount) {
                        showToast("最多选择" + mMaxSelectCount + "张");
                        mRbSelect.setChecked(false);
                        return;
                    }
                    mRbSelect.setChecked(true);
                    mSelectList.add(infoBean);
                }
                upDataDoneText();
                break;
        }
    }

    private void upDataDoneText() {
        if (mSelectList.size() <= 0) {
            mTvDoneCount.setVisibility(View.GONE);
        } else {
            mTvDoneCount.setVisibility(View.VISIBLE);
            mTvDoneCount.setText("完成 (" + mSelectList.size() + "/" + mMaxSelectCount + ")");
        }
    }

    public void setBarStatus() {
        if (mRlPreViewTop.getVisibility() == View.VISIBLE) {
            mRlPreViewTop.setVisibility(View.GONE);
            mRlPreViewBottom.setVisibility(View.GONE);
        } else {
            mRlPreViewTop.setVisibility(View.VISIBLE);
            mRlPreViewBottom.setVisibility(View.VISIBLE);
        }
    }

    public static final int DONE_CODE = 100;
    public static final int BACK_CODE = 101;

    private void doneSelect(int resultCode) {
        Intent intent = new Intent();
        intent.putExtra(CustomAlbumActivity.SELECT_LIST_RESULT, (Serializable) mSelectList);
        intent.putExtra(CustomAlbumActivity.EVENT_TYPE_RESULT, resultCode);
        setResult(RESULT_OK, intent);
        this.finish();
    }


    @Override
    public void onBackPressed() {
        doneSelect(BACK_CODE);
        super.onBackPressed();
    }

    /**
     * @param media
     * @return 大于等于0 就是表示以选择，返回的是在selectMedias中的下标
     */
    public int isSelect(AlbumPhotoInfoBean media, List<AlbumPhotoInfoBean> list) {
        int index = -1;
        if (list.size() <= 0) {
            return index;
        }
        for (int i = 0; i < list.size(); i++) {
            AlbumPhotoInfoBean infoBean = list.get(i);
            if (infoBean.getPath().equals(media.getPath())) {
                return i;
            }
        }
        return index;
    }
}
