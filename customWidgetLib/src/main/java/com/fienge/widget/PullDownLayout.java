package com.fienge.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Zvezda on 2017/6/9.
 */

public class PullDownLayout extends PullActionLayout
{
    private TextView tipView = null;
    private ImageView imageView = null;
    private ProgressBar progressBar = null;
    private Animation rotateUpAnim;
    private Animation rotateDownAnim;
    public PullDownLayout(Context context)
    {
        super(context);
        LayoutInflater.from(getContext()).inflate(R.layout.pull_down_layout, this, true);

        tipView = (TextView) findViewById(R.id.textView);
        imageView = (ImageView) findViewById(R.id.imageView);
        progressBar = (ProgressBar) findViewById(R.id.progressView);
        rotateUpAnim = new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateUpAnim.setDuration(180);
        rotateUpAnim.setFillAfter(true);
        rotateDownAnim = new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateDownAnim.setDuration(180);
        rotateDownAnim.setFillAfter(true);
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
//    {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        Log.i("ZVEZDA", "PullDownLayout------------------->"+getMeasuredWidth()+" "+getMeasuredHeight());
//        Log.i("ZVEZDA", "PullDownLayout------------------->"+getChildAt(0).getMeasuredWidth()+" "+getChildAt(0).getMeasuredHeight());
//    }

    @Override
    public void onPull(int offset)
    {
        super.onPull(offset);
    }

    @Override
    public void onFrozen()
    {
        super.onFrozen();
        tipView.setText("下拉刷新");
        progressBar.setVisibility(GONE);
        imageView.setVisibility(VISIBLE);
    }

    @Override
    public void onOpen()
    {
        super.onOpen();
        tipView.setText("释放立即刷新");
        imageView.clearAnimation();
        imageView.startAnimation(rotateUpAnim);
    }

    @Override
    public void onClose()
    {
        super.onClose();
        tipView.setText("下拉刷新");
        imageView.clearAnimation();
        imageView.startAnimation(rotateDownAnim);
    }

    @Override
    public void onActive()
    {
        super.onActive();
        tipView.setText("正在刷新");
        progressBar.setVisibility(VISIBLE);
        imageView.clearAnimation();
        imageView.setVisibility(GONE);
    }
}
