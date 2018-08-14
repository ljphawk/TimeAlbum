package com.ljp.time.timealbum;



/*
 *@创建者       L_jp
 *@创建时间     2018/8/9 10:45.
 *@描述        recycler 正方形
 *
 *@更新者         $Author$
 *@更新时间         $Date$
 *@更新描述         ${TODO}
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class RvGridLayout extends RelativeLayout {
    public RvGridLayout(Context context) {
        super(context);
    }

    public RvGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RvGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @SuppressWarnings("unused")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
