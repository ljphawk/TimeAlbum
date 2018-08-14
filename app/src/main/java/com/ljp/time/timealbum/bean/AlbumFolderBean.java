package com.ljp.time.timealbum.bean;

import java.util.ArrayList;
import java.util.List;

/*
 *@创建者       L_jp
 *@创建时间     2018/8/10 14:43.
 *@描述        相册文件夹bean
 *
 *@更新者         $Author$
 *@更新时间         $Date$
 *@更新描述         ${TODO}
 */


public class AlbumFolderBean {

    public String name;
    public String headCount;
    public List<Integer> headPositionList = new ArrayList<>();


    ArrayList<AlbumPhotoInfoBean> mAlbumPhotoInfoBeen = new ArrayList<>();

    public void addMedias(AlbumPhotoInfoBean albumPhotoInfoBean) {
        mAlbumPhotoInfoBeen.add(albumPhotoInfoBean);
    }

    public AlbumFolderBean(String name) {
        this.name = name;
    }

    public ArrayList<AlbumPhotoInfoBean> getAlbumFolderList() {
        return this.mAlbumPhotoInfoBeen;
    }

    public String getHeadCount() {
        return headCount;
    }

    public void setHeadCount(String headCount) {
        this.headCount = headCount;
    }

    public List<Integer> getHeadPositionList() {
        return headPositionList;
    }

    public void setHeadPositionList(List<Integer> headPositionList) {
        this.headPositionList = headPositionList;
    }
}
