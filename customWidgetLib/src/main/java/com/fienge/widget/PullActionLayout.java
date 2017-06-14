package com.fienge.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Zvezda on 2017/6/7.
 * 拉的动作的布局
 */

public class PullActionLayout extends ViewGroup
{
    private final String TAG = "ZVEZDA";
    /**
     * 拖动的距离
     */
    private int pullOffset = 0;
    /**
     * 冻结
     */
    private final int FROZEN = 0;
    /**
     * 打开
     */
    public final int OPEN = 1;
    /**
     * 关闭
     */
    public final int CLOSE = 2;
    /**
     * 激活
     */
    public final int ACTIVE = 3;
    public int lastState = FROZEN;

    public PullActionLayout(Context context)
    {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = 0;
        int height = 0;
        for (int i = 0; i < getChildCount(); i++)
        {
            View view = getChildAt(i);
            measureChild(view, widthMeasureSpec, heightMeasureSpec);
            if (width <= view.getMeasuredWidth())
            {
                width = view.getMeasuredWidth();
            }
            height = height+view.getMeasuredHeight();
            //Log.i(TAG, "PullActionLayout------------------->onMeasure "+view.getWidth()+" "+view.getMeasuredWidth()+" "+view.getHeight()+" "+view.getMeasuredHeight());
        }
        //Log.i(TAG, "PullActionLayout------------------->onMeasure "+width+" "+height);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        for (int i = 0; i < getChildCount(); i++)
        {
            View child = getChildAt(i);
            final int childWidth = child.getMeasuredWidth();
            final int childHeight = child.getMeasuredHeight();
            final int childLeft = 0;
            final int childTop = 0;
            final int childRight = childLeft + childWidth;
            final int childBottom = childTop + childHeight;
            //Log.i(TAG, "PullActionLayout------------------------>onLayout " + childWidth + " " + childHeight);
            //Log.i(TAG, "PullActionLayout------------------------>onLayout " + childLeft + " " + childTop+" "+childRight+" "+childBottom);
            child.layout(childLeft, childTop, childRight, childBottom);
        }
    }

//    /**
//     * 设置可视区域高度
//     * @param height
//     */
//    public void setVisibileHeight(int height)
//    {
//        ViewGroup.LayoutParams layoutParams = getLayoutParams();
//        layoutParams.height = height;
//        setLayoutParams(layoutParams);
//    }

    /**
     * 冻结状态
     */
    public void onFrozen()
    {
        lastState = FROZEN;
    }

    /**
     * 打开状态
     */
    public void onOpen()
    {
        lastState = OPEN;
    }

    /**
     * 关闭状态
     */
    public void onClose()
    {
        lastState = CLOSE;
    }

    /**
     * 激活状态
     */
    public void onActive()
    {
        lastState = ACTIVE;
    }

    /**
     * 正在拖动
     * @param pullOffset
     */
    public void onPull(int pullOffset)
    {
        this.pullOffset = pullOffset;
        if (lastState != ACTIVE)
        {
            if (Math.abs(pullOffset) >= getMeasuredHeight())
            {
                if (lastState != OPEN)
                {
                    onOpen();
                    lastState = OPEN;
                }
            } else
            {
                if (lastState != CLOSE)
                {
                    onClose();
                    lastState = CLOSE;
                }
            }
        }
    }

//    /**
//     * 是否需要激活
//     * @return
//     */
//    public boolean isNeedActive()
//    {
//        if (Math.abs(pullOffset) >= getMeasuredHeight())
//        {
//            onActive();
//            return true;
//        }
//        return false;
//    }
}
