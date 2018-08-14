package com.ljp.time.timealbum;



/*
 *@创建者       L_jp
 *@创建时间     2018/7/19 11:34.
 *@描述         ${TODO}
 *
 *@更新者         $Author$
 *@更新时间         $Date$
 *@更新描述         ${TODO}
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ljp.time.timealbum.adapter.AlbumShowRvAdapter;
import com.ljp.time.timealbum.bean.AlbumPhotoInfoBean;
import com.ljp.time.timealbum.utils.DateUtils;

import java.util.List;


public class AlbumItemHeaderDecoration extends RecyclerView.ItemDecoration {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private int mTitleHight = 40;
    private AlbumShowRvAdapter mAdapter;

    public AlbumItemHeaderDecoration(Context context, AlbumShowRvAdapter adapter) {
        super();
        this.mContext = context;
        this.mAdapter = adapter;
        mTitleHight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, context.getResources().getDisplayMetrics());
        this.mLayoutInflater = LayoutInflater.from(mContext);

    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        try {
            //获取到视图中第一个可见的item的position
            List<AlbumPhotoInfoBean> allData = mAdapter.getAllData();
            if (allData.size() == 0) {
                return;
            }

            int firstVisiblePosition = ((GridLayoutManager) parent.getLayoutManager()).findFirstVisibleItemPosition();


            int currentTimeItemCount = mAdapter.getCurrentTimeItemCount(firstVisiblePosition) - 1;
            if (currentTimeItemCount >= 0) {
                int remainder = currentTimeItemCount % ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
                if (remainder == 0) {
                    firstVisiblePosition = firstVisiblePosition + ((GridLayoutManager) parent.getLayoutManager()).getSpanCount() - 1;
                } else if (remainder != 1) {
                    firstVisiblePosition = firstVisiblePosition + ((GridLayoutManager) parent.getLayoutManager()).getSpanCount() - remainder;
                }
            }

            String time = DateUtils.getSdfTime(allData.get(firstVisiblePosition).getTime(), DateUtils.YMDHMS2);

            View child = parent.findViewHolderForLayoutPosition(firstVisiblePosition).itemView;
            if (child == null) {
                return;
            }
            boolean flag = false;
            if ((firstVisiblePosition + 1) < allData.size()) {
                String lastTime = DateUtils.getSdfTime(allData.get(firstVisiblePosition + 1).getTime(), DateUtils.YMDHMS2);
                if (!(time + "").equals(lastTime)) {
                    if (child.getHeight() + child.getTop() < mTitleHight) {
                        c.save();
                        flag = true;
                        c.translate(0, child.getHeight() + child.getTop() - mTitleHight);
                    }
                }
            }

            View topTitleView = mLayoutInflater.inflate(R.layout.layout_rv_calendar_timeaxis_item_head, null, false);
            TextView textView = topTitleView.findViewById(R.id.tv_title);
            textView.setText(DateUtils.getSdfTime(allData.get(firstVisiblePosition).getTime(), DateUtils.YMDHMS2));
            int toDrawWidthSpec;//用于测量的widthMeasureSpec
            int toDrawHeightSpec;//用于测量的heightMeasureSpec
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) topTitleView.getLayoutParams();
            if (lp == null) {
                lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);//这里是根据复杂布局layout的width height，new一个Lp
                topTitleView.setLayoutParams(lp);
            }
            topTitleView.setLayoutParams(lp);
            if (lp.width == ViewGroup.LayoutParams.MATCH_PARENT) {
                //如果是MATCH_PARENT，则用父控件能分配的最大宽度和EXACTLY构建MeasureSpec。
                toDrawWidthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth() - parent.getPaddingLeft() - parent.getPaddingRight(), View.MeasureSpec.EXACTLY);
            } else if (lp.width == ViewGroup.LayoutParams.WRAP_CONTENT) {
                //如果是WRAP_CONTENT，则用父控件能分配的最大宽度和AT_MOST构建MeasureSpec。
                toDrawWidthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth() - parent.getPaddingLeft() - parent.getPaddingRight(), View.MeasureSpec.AT_MOST);
            } else {
                //否则则是具体的宽度数值，则用这个宽度和EXACTLY构建MeasureSpec。
                toDrawWidthSpec = View.MeasureSpec.makeMeasureSpec(lp.width, View.MeasureSpec.EXACTLY);
            }
            //高度同理
            if (lp.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                toDrawHeightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight() - parent.getPaddingTop() - parent.getPaddingBottom(), View.MeasureSpec.EXACTLY);
            } else if (lp.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
                toDrawHeightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight() - parent.getPaddingTop() - parent.getPaddingBottom(), View.MeasureSpec.AT_MOST);
            } else {
                toDrawHeightSpec = View.MeasureSpec.makeMeasureSpec(mTitleHight, View.MeasureSpec.EXACTLY);
            }
            //依次调用 measure,layout,draw方法，将复杂头部显示在屏幕上。
            topTitleView.measure(toDrawWidthSpec, toDrawHeightSpec);
            topTitleView.layout(parent.getPaddingLeft(), parent.getPaddingTop(), parent.getPaddingLeft() + topTitleView.getMeasuredWidth(), parent.getPaddingTop() + topTitleView.getMeasuredHeight());
            topTitleView.draw(c);//Canvas默认在视图顶部，无需平移，直接绘制
            if (flag) {
                c.restore();//恢复画布到之前保存的状态}
            }
        } catch (Exception e) {
            Log.d("", "onDrawOver:  " + e);
        }
    }
}
