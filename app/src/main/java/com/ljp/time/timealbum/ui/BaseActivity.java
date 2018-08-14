package com.ljp.time.timealbum.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.ljp.time.timealbum.R;
import com.ljp.time.timealbum.utils.ToastUtil;


/**
 * Created by Host-0 on 2017/1/16.
 */

public abstract class BaseActivity extends AppCompatActivity implements BaseView {


    protected Context mContext;
    protected ImmersionBar mImmersionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.keyboardEnable(true) //解决软键盘与底部输入框冲突问题
                .statusBarDarkFont(true, 0.2f)//如果不能改变状态栏的颜色 就用后面0.2f的透明度
                .init();   //所有子类都将继承这些相同的属性


        setContentView(resView());
        initView(savedInstanceState);
        initData();
    }


    protected abstract int resView();


    protected abstract void initView(Bundle savedInstanceState);


    /**
     *  //做数据初始化
     */
    protected abstract void initData();


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null) {
            //必须调用该方法，防止内存泄漏，不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
            mImmersionBar.destroy();
        }
    }

    public void setToolbarTitleAndBack(BaseActivity activity, boolean backEnabled, String title) {
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);//关联toolbar
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);//不显示title
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(backEnabled); //启用back
        if (title!=null) {
            TextView tvTitle =  activity.findViewById(R.id.tv_toolbar_title);
            tvTitle.setText(title);
        }
    }

    @Override
    public void showToast(String content) {
        ToastUtil.showToast(mContext, content);
    }

    @Override
    public void startActivity(Class targetActivity) {
        mContext.startActivity(new Intent(mContext, targetActivity));
    }

    @Override
    public BaseActivity getActivitys() {
        return this;
    }

    //toolbar返回
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            activityFinish();
            hideInputMethod();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //关闭activity前的操作
    protected void activityFinish() {

    }


    //隐藏输入法
    protected void hideInputMethod() {
        try {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {

        }
    }
}
