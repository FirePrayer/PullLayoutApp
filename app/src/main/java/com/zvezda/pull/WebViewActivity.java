//package com.example.zvezda.myapplication02;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//
///**
// * Created by Zvezda on 2017/6/10.
// */
//
//public class WebViewActivity extends AppCompatActivity
//{
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.webview_layout);
//        WebView webView = (WebView)findViewById(R.id.webView);
//        WebSettings webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        webView.loadUrl("http://chongqing.163.com/17/0610/20/CMJJRB1704218FF3.html");
//        webView.setWebViewClient(new WebViewClient()
//        {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url)
//            {
//                view.loadUrl(url);
//                return true;
//            }
//        });
//    }
//}
