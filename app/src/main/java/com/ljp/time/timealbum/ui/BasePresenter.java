package com.ljp.time.timealbum.ui;



/*
 *@创建者       L_jp
 *@创建时间     2018/7/5 10:12.
 *@描述         ${TODO}
 *
 *@更新者         $Author$
 *@更新时间         $Date$
 *@更新描述         ${TODO}
 */


import android.content.Context;

import com.ljp.time.timealbum.utils.ToastUtil;

public abstract class BasePresenter<T> {
    public T mvpView;
    public Context mContext;

    public void attach(Context context, T view) {
        this.mvpView = view;
        this.mContext = context;
    }

    public void detach() {
        this.mvpView = null;
    }

    public void showToast(String content) {
        if (content != null && mContext != null) {
            ToastUtil.showToast(mContext, content);
        }
    }



}
