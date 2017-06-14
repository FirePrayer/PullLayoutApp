package com.zvezda.pull;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button recyclerView = (Button)findViewById(R.id.recyclerView);
        recyclerView.setOnClickListener(onClickListener);
        Button scrollView = (Button)findViewById(R.id.scrollView);
        scrollView.setOnClickListener(onClickListener);
        Button webView = (Button)findViewById(R.id.webView);
        webView.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.recyclerView:
                {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, RecyclerViewActivity.class);
                    startActivity(intent);
                }
                    break;
                case R.id.scrollView:
                {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, ScrollViewActivity.class);
                    startActivity(intent);
                }
                    break;
                case R.id.webView:
                {
//                    Intent intent = new Intent();
//                    intent.setClass(MainActivity.this, WebViewActivity.class);
//                    startActivity(intent);
                }
                    break;
                case R.id.listView:
                    break;
                default:
                    break;
            }
        }
    };
}
