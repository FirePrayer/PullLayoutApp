package com.zvezda.pull;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.fienge.widget.PullDownLayout;
import com.fienge.widget.PullLayout;
import com.fienge.widget.PullUpLayout;

import java.util.ArrayList;
import java.util.List;


public class RecyclerViewActivity extends Activity
{
    private RecyclerView recyclerView;
    private RecyclerAdapter myAdapter;
    private LinearLayoutManager linearLayoutManager;
    private PullLayout pullLayout;

    private String TAG = "ZVEZDA";
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        myAdapter = new RecyclerAdapter(this);
        recyclerView.setAdapter(myAdapter);

        pullLayout = (PullLayout) findViewById(R.id.swipe_refresh);
        //pullLayout.activeHeaderLayout();

        PullDownLayout pullDownLayout = new PullDownLayout(this)
        {
            @Override
            public void onActive()
            {
                super.onActive();
                Log.i(TAG, "onActive----------------->");
                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        pullLayout.frozenHeaderLayout();
                    }
                }, 2000);
            }
        };
        PullUpLayout pullUpLayout = new PullUpLayout(this)
        {
            @Override
            public void onActive()
            {
                super.onActive();
                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        pullLayout.frozenFooterLayout();
                    }
                }, 2000);
            }
        };
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                pullLayout.activeHeaderLayout();
            }
        }, 2000);

        pullLayout.setFooterLayout(pullUpLayout);
        pullLayout.setHeaderLayout(pullDownLayout);
//        pullLayout.setPullDownListener(new OnPullListener()
//        {
//            @Override
//            public void onActive()
//            {
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable()
//                {
//                    @Override
//                    public void run()
//                    {
//                        pullLayout.pullDownFrozen();
//                    }
//                }, 2000);
//            }
//
//            @Override
//            public void onPull(int pullOffset)
//            {
//
//            }
//        });
//        OnPullListener onPullListener = new OnPullListener()
//        {
//            @Override
//            public void onActive()
//            {
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable()
//                {
//                    @Override
//                    public void run()
//                    {
//                        pullLayout.pullUpFrozen();
//                    }
//                }, 2000);
//            }
//
//            @Override
//            public void onPull(int pullOffset)
//            {
//
//            }
//        };
//        pullLayout.setPullUpListener(onPullListener);
        initDatas();
    }

    private void initDatas()
    {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 20; i++)
        {
            list.add("item " + i);
        }
        myAdapter.addAll(list, 0);
    }

}
