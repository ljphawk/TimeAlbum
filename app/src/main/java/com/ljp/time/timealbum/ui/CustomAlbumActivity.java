package com.ljp.time.timealbum.ui;



/*
 *@创建者       L_jp
 *@创建时间     2018/8/9 19:06.
 *@描述         图片视频选择 界面
 *
 *@更新者         $Author$
 *@更新时间         $Date$
 *@更新描述         ${TODO}
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.ljp.time.timealbum.AlbumItemHeaderDecoration;
import com.ljp.time.timealbum.AlbumLoadDataCallback;
import com.ljp.time.timealbum.DividerGridItemDecoration;
import com.ljp.time.timealbum.ImageVideoLoader;
import com.ljp.time.timealbum.R;
import com.ljp.time.timealbum.utils.ScreenUtil;
import com.ljp.time.timealbum.adapter.AlbumFolderAdapter;
import com.ljp.time.timealbum.adapter.AlbumShowRvAdapter;
import com.ljp.time.timealbum.bean.AlbumFolderBean;
import com.ljp.time.timealbum.bean.AlbumPhotoInfoBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义相册
 */

public class CustomAlbumActivity extends BaseActivity implements AlbumLoadDataCallback, View.OnClickListener, AlbumShowRvAdapter.OnRecyclerViewItemClickListener {

    private RecyclerView mRecyclerView;
    private AlbumShowRvAdapter mAlbumShowRvAdapter;
    private AlbumFolderAdapter mFolderAdapter;
    private ListPopupWindow mFolderPopupWindow;
    private Button mBtPreView;
    private Button mBtAlbumFolder;
    private static final String maxSelectExtra = "maxSelectExtra";
    private int mMaxSelectCount = 1;
    private TextView mTvToolbarDone;

    public static void startActivity(Activity activity, int maxSelect, int requestCode) {
        Intent intent = new Intent(activity, CustomAlbumActivity.class);
        intent.putExtra(maxSelectExtra, maxSelect);
        activity.startActivityForResult(intent, requestCode);

    }


    @Override
    protected int resView() {
        return R.layout.activity_image_video_select;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setToolbarTitleAndBack(this, true, "素材选择");

        mBtPreView = (Button) findViewById(R.id.bt_preview);
        mBtAlbumFolder = (Button) findViewById(R.id.bt_album_folder);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mTvToolbarDone = (TextView) findViewById(R.id.tv_toolbar_done);

        mBtPreView.setOnClickListener(this);
        mBtAlbumFolder.setOnClickListener(this);
        mTvToolbarDone.setOnClickListener(this);

    }


    @Override
    protected void initData() {
        Intent intent = getIntent();
        mMaxSelectCount = intent.getIntExtra(maxSelectExtra, 1);

        mAlbumShowRvAdapter = new AlbumShowRvAdapter(mContext, mMaxSelectCount);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(mContext, mAlbumShowRvAdapter));
        mRecyclerView.addItemDecoration(new AlbumItemHeaderDecoration(mContext, mAlbumShowRvAdapter));
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mAlbumShowRvAdapter.getItemViewType(position) == AlbumShowRvAdapter.HEAD_TYPE) {
                    return gridLayoutManager.getSpanCount();
                } else {
                    return 1;
                }
            }
        });
        mRecyclerView.setAdapter(mAlbumShowRvAdapter);
        mAlbumShowRvAdapter.setOnItemClickListener(this);
        getLoaderManager().initLoader(1, null, new ImageVideoLoader(this, this));
        createFolderAdapter();
    }

    private void createFolderAdapter() {
        mFolderAdapter = new AlbumFolderAdapter(mContext);
        mFolderPopupWindow = new ListPopupWindow(this);
        mFolderPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        mFolderPopupWindow.setAdapter(mFolderAdapter);
        mFolderPopupWindow.setHeight((int) (ScreenUtil.getScreenHeight(this) * 0.6));
        mFolderPopupWindow.setAnchorView(findViewById(R.id.rl_footer));
        mFolderPopupWindow.setModal(true);
        mFolderPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mFolderAdapter.setLastSelected(position);
                mBtAlbumFolder.setText(mFolderAdapter.getItem(position).name);
                mAlbumShowRvAdapter.updateAdapterList(mFolderAdapter.getItem(position).getAlbumFolderList(), mFolderAdapter.getItem(position).getHeadPositionList());
                mFolderPopupWindow.dismiss();
            }
        });
    }


    @Override
    public void onData(ArrayList<AlbumFolderBean> list) {
        mFolderAdapter.setAdapterList(list);
        mAlbumShowRvAdapter.updateAdapterList(list.get(0).getAlbumFolderList(), list.get(0).getHeadPositionList());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_preview:
                List<AlbumPhotoInfoBean> allSelectData = mAlbumShowRvAdapter.getAllSelectData();
                if (allSelectData!=null) {
                    startPreViewActivity(0, allSelectData);
                }
                break;
            case R.id.bt_album_folder:
                mFolderPopupWindow.show();
                break;
            case R.id.tv_toolbar_done:
                allSelectData = mAlbumShowRvAdapter.getAllSelectData();
                if (allSelectData!=null) {
                    Intent intent = new Intent();
                    intent.putExtra(SELECT_LIST_RESULT, (Serializable) allSelectData);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
        }
    }

    //条目点击
    @Override
    public void onItemClick(AlbumPhotoInfoBean data, int position) {

        List<Integer> headPositionList = mAlbumShowRvAdapter.getHeadPositionList();
        int count = 0;
        for (int i = 0; i < headPositionList.size(); i++) {
            if (position > headPositionList.get(i)) {
                count++;
            } else {
                break;
            }
        }
        startPreViewActivity(position - count, mAlbumShowRvAdapter.getAllDataNoHead());
    }

    @Override
    public void onRbClick(int selectCount) {
        if (selectCount > 0) {
            mTvToolbarDone.setVisibility(View.VISIBLE);
            mTvToolbarDone.setText("完成 (" + selectCount + "/" + mMaxSelectCount + ")");
            mBtPreView.setVisibility(View.VISIBLE);
            if (selectCount > 0) {
                mBtPreView.setText("预览(" + selectCount + ")");
            } else {
                mBtPreView.setText("预览");
            }
        } else {
            mBtPreView.setVisibility(View.GONE);
            mTvToolbarDone.setVisibility(View.GONE);
        }
    }

    private void startPreViewActivity(int position, List<AlbumPhotoInfoBean> allData) {
        PreViewPhotoActivity.startActivity(this, position, mMaxSelectCount, allData, mAlbumShowRvAdapter.getAllSelectData(), 100);
    }


    public static final String SELECT_LIST_RESULT = "SELECT_LIST_RESULT";
    public static final String EVENT_TYPE_RESULT = "EVENT_TYPE_RESULT";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 100:
                    int doneType = data.getIntExtra(EVENT_TYPE_RESULT, -1);
                    List<AlbumPhotoInfoBean> selectList = (List<AlbumPhotoInfoBean>) data.getSerializableExtra(SELECT_LIST_RESULT);
                    switch (doneType) {
                        case PreViewPhotoActivity.BACK_CODE:
                            mAlbumShowRvAdapter.setSelectList(selectList);
                            onRbClick(selectList.size());
                            break;
                        case PreViewPhotoActivity.DONE_CODE:
                            setResult(RESULT_OK, data);
                            finish();
                            break;
                    }
                    break;
            }
        }
    }


    public static List<AlbumPhotoInfoBean> obtainPathResult(Intent data) {
        List<AlbumPhotoInfoBean> selectList = (List<AlbumPhotoInfoBean>) data.getSerializableExtra(SELECT_LIST_RESULT);
        if (selectList == null) {
            selectList = new ArrayList<>();
        }
        return selectList;
    }
}
