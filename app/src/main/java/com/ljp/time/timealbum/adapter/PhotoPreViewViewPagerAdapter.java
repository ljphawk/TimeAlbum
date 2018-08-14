package com.ljp.time.timealbum.adapter;



/*
 *@创建者       L_jp
 *@创建时间     2018/8/10 17:04.
 *@描述         相册图片预览的adapter
 *
 *@更新者         $Author$
 *@更新时间         $Date$
 *@更新描述         ${TODO}
 */

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ljp.time.timealbum.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class PhotoPreViewViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<BaseFragment> mFragments = new ArrayList<>();

    public PhotoPreViewViewPagerAdapter(FragmentManager fm, List<BaseFragment> mFragments) {
        super(fm);
        this.mFragments = mFragments;
    }

    @Override
    public BaseFragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }



}
