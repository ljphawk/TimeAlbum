package com.ljp.time.timealbum.ui;



/*
 *@创建者       L_jp
 *@创建时间     2018/7/5 10:11.
 *@描述         ${TODO}
 *
 *@更新者         $Author$
 *@更新时间         $Date$
 *@更新描述         ${TODO}
 */

public interface BaseView {


    void showToast(String content);

    void startActivity(Class targetActivity);

    BaseActivity getActivitys();
}
