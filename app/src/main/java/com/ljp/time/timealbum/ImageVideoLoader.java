package com.ljp.time.timealbum;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.ljp.time.timealbum.bean.AlbumFolderBean;
import com.ljp.time.timealbum.bean.AlbumPhotoInfoBean;
import com.ljp.time.timealbum.utils.DateUtils;
import com.ljp.time.timealbum.utils.FileUtils;

import java.util.ArrayList;


/*
 *@创建者       L_jp
 *@创建时间     2018/8/10 14:43.
 *@描述        所有图片和视频
 *
 *@更新者         $Author$
 *@更新时间         $Date$
 *@更新描述         ${TODO}
 */


public class ImageVideoLoader implements LoaderManager.LoaderCallbacks {


    private Context mContext;
    private AlbumLoadDataCallback mLoader;

    public ImageVideoLoader(Context context, AlbumLoadDataCallback loader) {
        this.mContext = context;
        this.mLoader = loader;
    }


    String[] MEDIA_PROJECTION = {
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns._ID,
            MediaStore.Video.VideoColumns.DURATION,
            MediaStore.Files.FileColumns.PARENT};


    @Override
    public Loader onCreateLoader(int picker_type, Bundle bundle) {
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

        Uri queryUri = MediaStore.Files.getContentUri("external");
        CursorLoader cursorLoader = new CursorLoader(
                mContext,
                queryUri,
                MEDIA_PROJECTION,
                selection,
                null, // Selection args (none).
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC"
        );
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader loader, Object o) {
        String allLastDate = "0";
        String videoLastDate = "0";
        String addFolderLastDate = "0";
        String lastFolderName = "";

        ArrayList<AlbumFolderBean> albumFolderList = new ArrayList<>();//所有文件夹

        AlbumFolderBean allAlbumFolderList = new AlbumFolderBean("所有图片和视频");//当前文件夹中的图片
        albumFolderList.add(allAlbumFolderList);

        AlbumFolderBean allVideoDir = new AlbumFolderBean("所有视频");
        albumFolderList.add(allVideoDir);

        Cursor cursor = (Cursor) o;
        while (cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME));
            long dateTime = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED));
            int mediaType = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE));
            long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE));
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID));

            //获取所在的文件夹
            String dirName = FileUtils.getParentFolderName(path);
            AlbumPhotoInfoBean albumPhotoInfoBean = new AlbumPhotoInfoBean(path, name, dateTime, mediaType, size, id, dirName, 1);
            if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
                //添加 所有视频
                long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION));
                albumPhotoInfoBean.setDuration(DateUtils.stringForTime(duration));
                boolean isToday = DateUtils.isToday(videoLastDate, dateTime + "");
                if (!isToday) {
                    allVideoDir.addMedias(new AlbumPhotoInfoBean(0, dateTime));
                    videoLastDate = dateTime + "";

                    allVideoDir.getHeadPositionList().add(allVideoDir.getAlbumFolderList().size() - 1);
                }
                allVideoDir.addMedias(albumPhotoInfoBean);
            }

            boolean isToday = DateUtils.isToday(allLastDate, dateTime + "");
            if (!isToday) {
                //添加头部
                allAlbumFolderList.addMedias(new AlbumPhotoInfoBean(0, dateTime));
                allLastDate = dateTime + "";

                allAlbumFolderList.getHeadPositionList().add(allAlbumFolderList.getAlbumFolderList().size() - 1);
            }
            //添加所有视频和照片数据
            allAlbumFolderList.addMedias(albumPhotoInfoBean);

            int index = FileUtils.isExistAlbumDir(albumFolderList, dirName);
            if (index != -1) {
                //获取上一个保存的文件
                AlbumPhotoInfoBean lastBean = albumFolderList.get(index).getAlbumFolderList().get(albumFolderList.get(index).getAlbumFolderList().size() - 1);
                String lastDir = lastBean.getParentDir();
                //如果不是同一个文件夹 重新定义比较时间
                if (!lastDir.equals(lastFolderName)) {
                    addFolderLastDate = lastBean.getTime() + "";
                }
                isToday = DateUtils.isToday(addFolderLastDate, dateTime + "");
                if (!isToday) {
                    albumFolderList.get(index).addMedias(new AlbumPhotoInfoBean(0, dateTime));
                    addFolderLastDate = dateTime + "";
                    albumFolderList.get(index).getHeadPositionList().add(albumFolderList.get(index).getAlbumFolderList().size() - 1);
                }
                albumFolderList.get(index).addMedias(albumPhotoInfoBean);

            } else {
                AlbumFolderBean albumFolderBean = new AlbumFolderBean(dirName);
                albumFolderBean.addMedias(new AlbumPhotoInfoBean(0, dateTime));
                albumFolderBean.addMedias(albumPhotoInfoBean);
                albumFolderBean.getHeadPositionList().add(0);

                albumFolderList.add(albumFolderBean);
                addFolderLastDate = dateTime + "";
                lastFolderName = dirName;
            }

        }
        mLoader.onData(albumFolderList);
        cursor.close();
    }


    @Override
    public void onLoaderReset(Loader loader) {

    }
}
