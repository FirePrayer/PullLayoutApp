package com.fienge.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ScrollView;

/**
 * Created by Zvezda on 2017/6/7.
 * 上拉下拉的布局
 */
public class PullLayout extends ViewGroup
{
    /**
     * 内容布局
     */
    private View contentView;
    /**
     * 用来判断是否拖动的变量
     */
    private int touchSlop;
    /**
     * 拖动的偏移量
     */
    private int pullOffset = 0;
    /**
     * 滑动
     */
    private final int PULL_NORMAL = 100;
    /**
     * 往后拉
     */
    private final int PULL_TO_FOOTER = 101;
    /**
     * 往前拉
     */
    private final int PULL_TO_HEADER = 102;
    private int pullState = PULL_NORMAL;
    /**
     * 用来判断方向的Y
     */
    private float lastDirectionY = 0.0f;
    /**
     * 用来出来拉的距离的Y
     */
    private float lastPullY = 0.0f;
    /**
     * 头布局
     */
    private PullActionLayout headerLayout;
    /**
     * 脚布局
     */
    private PullActionLayout footerLayout;
    private int headerLayoutWidth;
    private int footerLayoutWidth;
    private int headerLayoutHeight;
    private int footerLayoutHeight;
    private boolean isFooterLayoutActive;
    private boolean isHeaderLayoutActive;

//    /**
//     * 方向
//     */
//    private enum Orientation
//    {
//        HORIZONTAL,
//        VERTICAL
//    }
//    /**
//     * 方向
//     */
//    private Orientation orientation = HORIZONTAL;
//    private boolean pullFrontEnable = true;
//    private boolean pullBackEnable = true;

    public PullLayout(Context context)
    {
        this(context, null);
    }

    @SuppressWarnings("deprecation")
    public PullLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        //getScaledTouchSlop是一个距离，表示滑动的时候，手的移动要大于这个距离才开始移动控件。如果小于这个距离就不触发移动控件
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        touchSlop = 0;
        Log.i(TAG, "PullLayout-------------------------------------->" + touchSlop);

//        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.pullLayout);
//        int orientation = typedArray.getInt(R.styleable.pullLayout_orientation, 0);
//        boolean pullFrontEnable = typedArray.getBoolean(R.styleable.pullLayout_pull_front_enable, true);
//        boolean pullBackEnable = typedArray.getBoolean(R.styleable.pullLayout_pull_back_enable, true);
//        typedArray.recycle();

        headerLayout = new PullDownLayout(getContext());
        setHeaderLayout(headerLayout);

        footerLayout = new PullUpLayout(getContext());
        setFooterLayout(footerLayout);
    }

    /**
     * 设置顶部动画布局
     *
     * @param headerLayout
     */
    public void setHeaderLayout(PullActionLayout headerLayout)
    {
        if (this.headerLayout != null)
        {
            removeView(this.headerLayout);
        }
        this.headerLayout = headerLayout;
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(this.headerLayout, layoutParams);
        Log.i(TAG, "setHeaderLayout------------------->" + headerLayout.getMeasuredWidth() + " " + headerLayout.getMeasuredHeight());
    }

    /**
     * 设置底部动画布局
     *
     * @param footerLayout
     */
    public void setFooterLayout(PullActionLayout footerLayout)
    {
        if (this.footerLayout != null)
        {
            removeView(this.footerLayout);
        }
        this.footerLayout = footerLayout;
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(this.footerLayout, layoutParams);
        Log.i(TAG, "setFooterLayout------------------->" + footerLayout.getMeasuredWidth() + " " + footerLayout.getMeasuredHeight());
    }

    /**
     * 找到内容布局
     */
    private void ensureTarget()
    {
        if (contentView == null)
        {
            for (int i = 0; i < getChildCount(); i++)
            {
                View child = getChildAt(i);
                if (!child.equals(headerLayout) && !child.equals(footerLayout))
                {
                    contentView = child;
                    break;
                }
            }
        }
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (contentView == null)
        {
            ensureTarget();
        }
        if (contentView == null)
        {
            return;
        }
        for (int i = 0; i < getChildCount(); i++)
        {
            View view = getChildAt(i);
            measureChild(view, widthMeasureSpec, heightMeasureSpec);
            Log.i(TAG, "onMeasure-------------->"+view.getWidth()+" "+view.getMeasuredWidth()+" "+view.getHeight()+" "+view.getMeasuredHeight());
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        if (contentView == null)
        {
            ensureTarget();
        }
        if (contentView == null)
        {
            return;
        }
        updateLayout();
    }

    /**
     * 刷新布局
     */
    private void updateLayout()
    {
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        final int paddingLeft = getPaddingLeft();
        final int paddingTop = getPaddingTop();
        final int paddingRight = getPaddingRight();
        final int paddingBottom = getPaddingBottom();

        Log.i(TAG, "updateLayout------------------------>ViewGroup " + width + " " + height);
        Log.i(TAG, "updateLayout------------------------>ViewGroup " + paddingLeft + " " + paddingTop+" "+paddingRight+" "+paddingBottom);
        headerLayoutWidth = headerLayout.getMeasuredWidth();
        headerLayoutHeight = headerLayout.getMeasuredHeight();
        final int headLeft = paddingLeft;
        final int headTop = paddingTop - headerLayoutHeight + pullOffset;
        final int headRight = headLeft + headerLayoutWidth;
        final int headBottom = headTop + headerLayoutHeight;
        Log.i(TAG, "updateLayout------------------------>Header " + headerLayoutWidth + " " + headerLayoutHeight);
        Log.i(TAG, "updateLayout------------------------>Header " + headLeft + " " + headTop+" "+headRight+" "+headBottom);
        headerLayout.layout(headLeft, headTop, headRight, headBottom);

        final int contentWidth = contentView.getMeasuredWidth();
        final int contentHeigth = contentView.getMeasuredHeight();
        final int contentLeft = paddingLeft;
        final int contentTop = headBottom;
        final int contentRight = contentLeft + contentWidth;
        final int contentBottom = contentTop + height;//强制拉伸到和父控件一样的高度,contentHeigth则为自己原有的高度
        Log.i(TAG, "updateLayout------------------------>Content " + contentWidth + " " + contentHeigth);
        Log.i(TAG, "updateLayout------------------------>Content " + contentLeft + " " + contentTop+" "+contentRight+" "+contentBottom);
        contentView.layout(contentLeft, contentTop, contentRight, contentBottom);

        footerLayoutWidth = footerLayout.getMeasuredWidth();
        footerLayoutHeight = footerLayout.getMeasuredHeight();
        final int footLeft = paddingLeft;
        final int footTop = contentBottom;
        final int footRight = footLeft + footerLayoutWidth;
        final int footBottom = footTop + footerLayoutHeight;
        Log.i(TAG, "updateLayout------------------------>Footer " + footerLayoutWidth + " " + footerLayoutHeight);
        Log.i(TAG, "updateLayout------------------------>Footer " + footLeft + " " + footTop+" "+footRight+" "+footBottom);
        footerLayout.layout(footLeft, footTop, footRight, footBottom);
    }

    private String TAG = "ZVEZDA";
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        if (!isEnabled())
        {
            return super.dispatchTouchEvent(ev);
        }
        if (isFooterLayoutActive || isHeaderLayoutActive)
        {
            return true;
        }
        String fun = "dispatchTouchEvent";
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, fun+"---------------------->ACTION_DOWN");
                pullState = PULL_NORMAL;
                lastPullY = ev.getRawY();
                lastDirectionY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.i(TAG, fun+"---------------------->ACTION_MOVE");
                float rawY = ev.getRawY();
                float directionOffsetY = rawY - lastDirectionY;
                boolean isBottom = ViewScrollHelper.isChildScrollToBottom(contentView);
                boolean isTop = ViewScrollHelper.isChildScrollToTop(contentView);
                pullState = PULL_NORMAL;
                rawY = ev.getRawY();
                float pullOffsetY = rawY - lastPullY;
                lastPullY = rawY;
                if (-directionOffsetY > touchSlop && isBottom)
                {
                    Log.i(TAG, "dispatchTouchEvent---------------------->往上拖动控件A "+rawY+" "+lastDirectionY);
                    pullState = PULL_TO_HEADER;
                    updateOffset((int)pullOffsetY);
                    return true;
                }
                else if (directionOffsetY > touchSlop && isTop)
                {
                    Log.i(TAG, "dispatchTouchEvent---------------------->往下拖动控件 "+rawY+" "+lastDirectionY);
                    pullState = PULL_TO_FOOTER;
                    updateOffset((int)pullOffsetY);
                    return true;
                }
                else
                {
                    lastDirectionY = rawY;
                    if (pullOffset != 0)
                    {
                        updateOffset(-pullOffset);
                        pullOffset = 0;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, fun+"---------------------->ACTION_UP");
                if (pullState == PULL_TO_FOOTER && pullOffset >= headerLayoutHeight)
                {
                    smoothScrollTo(pullOffset, headerLayoutHeight);
                } else if (pullState == PULL_TO_HEADER && -pullOffset >= footerLayoutHeight)
                {
                    Log.i(TAG, "脚布局激活----------------->");
                    isFooterLayoutActive = true;
                    footerLayout.onActive();
                    smoothScrollTo(pullOffset, -footerLayoutHeight);
                } else
                {
                    smoothScrollTo(pullOffset, 0);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.i(TAG, fun+"---------------------->ACTION_CANCEL");
                smoothScrollTo(pullOffset, 0);
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 刷新子条目的位置
     *
     * @param offset
     */
    private void updateOffset(int offset)
    {
        pullOffset = pullOffset + offset;
        Log.i(TAG, "updateOffset--------------------------->A " + pullOffset + " " + offset + " " + contentView.getTop() + " " + contentView.getBottom());
//        if (pullState == PULL_TO_FOOTER && pullOffset <= 0)
//        {
//            pullOffset = 0;
//            offset = -contentView.getTop();
//        } else if (pullState == PULL_TO_HEADER && pullOffset >= 0)
//        {
//            pullOffset = 0;
//            offset = -contentView.getTop();
//        }
        //Log.i(TAG, "updateOffset--------------------------->B " + pullOffset + " " + offset + " " + contentView.getTop() + " " + contentView.getBottom());
        for (int i = 0; i < getChildCount(); i++)
        {
            View view = getChildAt(i);
            view.offsetTopAndBottom(offset);
            invalidate();
        }

        //同步拉的动作----------------------------------------------------
        if (pullState == PULL_TO_FOOTER)
        {
            headerLayout.onPull(pullOffset);
        } else if (pullState == PULL_TO_HEADER)
        {
            footerLayout.onPull(pullOffset);
        }
    }

    private final float DECELERATE_INTERPOLATION_FACTOR = 2f;
    private DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);

    /**
     * 回弹动画
     *
     * @param start
     * @param end
     */
    private void smoothScrollTo(final int start, int end)
    {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(250).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            int lastPullOffset = start;
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                pullOffset = (int) animation.getAnimatedValue();
                int offset = pullOffset - lastPullOffset;
                lastPullOffset = pullOffset;
                for (int i = 0; i < getChildCount(); i++)
                {
                    View view = getChildAt(i);
                    view.offsetTopAndBottom(offset);
                    invalidate();
                }


                //当顶部和底部都弹回去的时候------------------------------------
                if (pullState == PULL_TO_FOOTER)
                {
                    if (isHeaderLayoutActive && pullOffset == 0)
                    {
                        Log.i(TAG, "头布局冻结----------------->");
                        pullState = PULL_NORMAL;
                        isHeaderLayoutActive = false;
                        headerLayout.onFrozen();
                    }
                    else if (!isHeaderLayoutActive && pullOffset == headerLayoutHeight)
                    {
                        Log.i(TAG, "头布局激活----------------->");
                        isHeaderLayoutActive = true;
                        headerLayout.onActive();
                    }
                }
                else if (pullState == PULL_TO_HEADER)
                {
                    if (isFooterLayoutActive && pullOffset == 0)
                    {
                        Log.i(TAG, "脚布局冻结----------------->");
                        pullState = PULL_NORMAL;
                        isFooterLayoutActive = false;
                        footerLayout.onFrozen();
                    }
                    else if (!isFooterLayoutActive && pullOffset == -footerLayoutHeight)
                    {
                        Log.i(TAG, "脚布局激活----------------->");
                        isFooterLayoutActive = true;
                        footerLayout.onActive();
                    }
                }
            }
        });
        animator.setInterpolator(decelerateInterpolator);
        animator.start();
    }

    /**
     * 激活头布局
     */
    public void activeHeaderLayout()
    {
        if (!isFooterLayoutActive && !isHeaderLayoutActive)
        {
            pullState = PULL_TO_FOOTER;
            smoothScrollTo(pullOffset, headerLayoutHeight);
        }
    }

    /**
     * 关闭头布局
     */
    public void frozenHeaderLayout()
    {
        smoothScrollTo(pullOffset, 0);
    }

    /**
     * 激活脚布局
     */
    public void activeFooterLayout()
    {
        if (!isHeaderLayoutActive && !isFooterLayoutActive)
        {
            pullState = PULL_TO_HEADER;
            smoothScrollTo(pullOffset, -footerLayoutHeight);
        }
    }

    /**
     * 关闭脚布局
     */
    public void frozenFooterLayout()
    {
        smoothScrollTo(pullOffset, 0);
    }

    /**
     * 判断view是否可以滚动
     */
    private static class ViewScrollHelper
    {
        /**
         * 是否滚动到了顶部
         *
         * @param mTarget
         * @return true是，false否
         */
        public static boolean isChildScrollToTop(View mTarget)
        {
            if (Build.VERSION.SDK_INT < 14)
            {
                if (mTarget instanceof AbsListView)
                {
                    final AbsListView absListView = (AbsListView) mTarget;
                    return !(absListView.getChildCount() > 0 && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() < absListView.getPaddingTop()));
                } else
                {
                    return !(mTarget.getScrollY() > 0);
                }
            } else
            {
                return !ViewCompat.canScrollVertically(mTarget, -1);
            }
        }

        /**
         * 是否滚动到了底部
         *
         * @param mTarget
         * @return true是，false否
         */
        public static boolean isChildScrollToBottom(View mTarget)
        {
            if (ViewScrollHelper.isChildScrollToTop(mTarget))
            {
                return false;
            }
            if (mTarget instanceof RecyclerView)
            {
                RecyclerView recyclerView = (RecyclerView) mTarget;
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int count = recyclerView.getAdapter().getItemCount();
                if (layoutManager instanceof LinearLayoutManager && count > 0)
                {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                    if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == count - 1)
                    {
                        return true;
                    }
                } else if (layoutManager instanceof StaggeredGridLayoutManager)
                {
                    StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                    int[] lastItems = new int[2];
                    staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(lastItems);
                    int lastItem = Math.max(lastItems[0], lastItems[1]);
                    if (lastItem == count - 1)
                    {
                        return true;
                    }
                }
                return false;
            } else if (mTarget instanceof AbsListView)
            {
                final AbsListView absListView = (AbsListView) mTarget;
                int count = absListView.getAdapter().getCount();
                int fristPos = absListView.getFirstVisiblePosition();
                if (fristPos == 0 && absListView.getChildAt(0).getTop() >= absListView.getPaddingTop())
                {
                    return false;
                }
                int lastPos = absListView.getLastVisiblePosition();
                if (lastPos > 0 && count > 0 && lastPos == count - 1)
                {
                    return true;
                }
                return false;
            } else if (mTarget instanceof ScrollView)
            {
                ScrollView scrollView = (ScrollView) mTarget;
                View view = scrollView.getChildAt(scrollView.getChildCount() - 1);
                if (view != null)
                {
                    int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));
                    if (diff == 0)
                    {
                        return true;
                    }
                }
            } else if (mTarget instanceof NestedScrollView)
            {
                NestedScrollView nestedScrollView = (NestedScrollView) mTarget;
                View view = nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);
                if (view != null)
                {
                    int diff = (view.getBottom() - (nestedScrollView.getHeight() + nestedScrollView.getScrollY()));
                    if (diff == 0)
                    {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}

