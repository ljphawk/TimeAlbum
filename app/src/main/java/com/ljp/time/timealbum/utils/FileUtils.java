package com.ljp.time.timealbum.utils;



/*
 *@创建者       L_jp
 *@创建时间     2018/7/10 11:45.
 *@描述         ${TODO}
 *
 *@更新者         $Author$
 *@更新时间         $Date$
 *@更新描述         ${TODO}
 */

import com.ljp.time.timealbum.bean.AlbumFolderBean;

import java.util.ArrayList;

public class FileUtils {




    public static String getParentFolderName(String path) {
        String sp[] = path.split("/");
        return sp[sp.length - 2];
    }

    public static int isExistAlbumDir(ArrayList<AlbumFolderBean> albumFolderBeen, String dirName) {
        for (int i = 0; i < albumFolderBeen.size(); i++) {
            AlbumFolderBean albumFolderBean = albumFolderBeen.get(i);
            if (albumFolderBean.name.equals(dirName)) {
                return i;
            }
        }
        return -1;
    }

}
