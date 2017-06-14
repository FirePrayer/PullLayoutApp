package com.fienge.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

/**
 * Created by Zvezda on 2017/6/9.
 */

public class PullUpLayout extends PullActionLayout
{
    private TextView tipView = null;
    public PullUpLayout(Context context)
    {
        super(context);
        LayoutInflater.from(getContext()).inflate(R.layout.pull_up_layout, this, true);
        tipView = (TextView) findViewById(R.id.textView);
    }

    @Override
    public void onFrozen()
    {
        super.onFrozen();
        tipView.setText("上拉刷新ABC");
    }

    @Override
    public void onOpen()
    {
        super.onOpen();
        tipView.setText("释放立即刷新ABC");
    }

    @Override
    public void onClose()
    {
        super.onClose();
        tipView.setText("上刷新ABC");
    }

    @Override
    public void onActive()
    {
        super.onActive();
        tipView.setText("正在刷新ABC");
    }
}
