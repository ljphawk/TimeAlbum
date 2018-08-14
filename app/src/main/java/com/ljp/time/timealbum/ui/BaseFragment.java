package com.ljp.time.timealbum.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljp.time.timealbum.utils.ToastUtil;

/**
 *ljp
 */

public abstract class BaseFragment extends Fragment implements BaseView {
    public Context mContext;
    public Resources mResource;
    public String pkgName;
    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mResource = getResources();
        pkgName = mContext.getPackageName();

        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);

            android.support.v4.app.FragmentTransaction ft = ((BaseActivity) mContext).getSupportFragmentManager().beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
//            ft.commit();
            ft.commitAllowingStateLoss();
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return resFragmentView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view, savedInstanceState);
        initData(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    protected abstract View resFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);


    protected abstract void initView(View view, Bundle savedInstanceState);

    protected abstract void initData(View view, Bundle savedInstanceState);


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }

    @Override
    public BaseActivity getActivitys() {
        return ((BaseActivity) mContext);
    }

    @Override
    public void showToast(String message) {
        ToastUtil.showToast(mContext, message);
    }

    @Override
    public void startActivity(Class targetActivity) {
        mContext.startActivity(new Intent(mContext, targetActivity));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
