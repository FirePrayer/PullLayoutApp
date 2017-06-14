//package com.fienge.widget;
//
//import android.os.Build;
//import android.support.v4.view.ViewCompat;
//import android.support.v4.widget.NestedScrollView;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.StaggeredGridLayoutManager;
//import android.view.View;
//import android.widget.AbsListView;
//import android.widget.ScrollView;
//
///**
// * Created by Zvezda on 2017/6/8.
// */
//
//public class ViewScrollHelper
//{
//    /**
//     * 是否滚动到了顶部
//     * @param mTarget
//     * @return true是，false否
//     */
//    public static boolean isChildScrollToTop(View mTarget)
//    {
//        if (Build.VERSION.SDK_INT < 14)
//        {
//            if (mTarget instanceof AbsListView)
//            {
//                final AbsListView absListView = (AbsListView) mTarget;
//                return !(absListView.getChildCount() > 0 && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() < absListView.getPaddingTop()));
//            } else
//            {
//                return !(mTarget.getScrollY() > 0);
//            }
//        } else
//        {
//            return !ViewCompat.canScrollVertically(mTarget, -1);
//        }
//    }
//
//    /**
//     * 是否滚动到了底部
//     * @param mTarget
//     * @return true是，false否
//     */
//    public static boolean isChildScrollToBottom(View mTarget)
//    {
//        if (ViewScrollHelper.isChildScrollToTop(mTarget))
//        {
//            return false;
//        }
//        if (mTarget instanceof RecyclerView)
//        {
//            RecyclerView recyclerView = (RecyclerView) mTarget;
//            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//            int count = recyclerView.getAdapter().getItemCount();
//            if (layoutManager instanceof LinearLayoutManager && count > 0)
//            {
//                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
//                if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == count - 1)
//                {
//                    return true;
//                }
//            } else if (layoutManager instanceof StaggeredGridLayoutManager)
//            {
//                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
//                int[] lastItems = new int[2];
//                staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(lastItems);
//                int lastItem = Math.max(lastItems[0], lastItems[1]);
//                if (lastItem == count - 1)
//                {
//                    return true;
//                }
//            }
//            return false;
//        } else if (mTarget instanceof AbsListView)
//        {
//            final AbsListView absListView = (AbsListView) mTarget;
//            int count = absListView.getAdapter().getCount();
//            int fristPos = absListView.getFirstVisiblePosition();
//            if (fristPos == 0 && absListView.getChildAt(0).getTop() >= absListView.getPaddingTop())
//            {
//                return false;
//            }
//            int lastPos = absListView.getLastVisiblePosition();
//            if (lastPos > 0 && count > 0 && lastPos == count - 1)
//            {
//                return true;
//            }
//            return false;
//        } else if (mTarget instanceof ScrollView)
//        {
//            ScrollView scrollView = (ScrollView) mTarget;
//            View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);
//            if (view != null)
//            {
//                int diff = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));
//                if (diff == 0)
//                {
//                    return true;
//                }
//            }
//        } else if (mTarget instanceof NestedScrollView)
//        {
//            NestedScrollView nestedScrollView = (NestedScrollView) mTarget;
//            View view = (View) nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);
//            if (view != null)
//            {
//                int diff = (view.getBottom() - (nestedScrollView.getHeight() + nestedScrollView.getScrollY()));
//                if (diff == 0)
//                {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//}
