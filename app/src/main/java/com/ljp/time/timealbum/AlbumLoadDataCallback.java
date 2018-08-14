package com.ljp.time.timealbum;

import com.ljp.time.timealbum.bean.AlbumFolderBean;

import java.util.ArrayList;


/*
 *@创建者       L_jp
 *@创建时间     2018/8/10 14:43.
 *@描述         相册信息加载回调
 *
 *@更新者         $Author$
 *@更新时间         $Date$
 *@更新描述         ${TODO}
 */


public interface AlbumLoadDataCallback {


    void onData(ArrayList<AlbumFolderBean> list);

}
