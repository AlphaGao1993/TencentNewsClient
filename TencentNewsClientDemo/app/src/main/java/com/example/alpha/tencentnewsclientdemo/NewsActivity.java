package com.example.alpha.tencentnewsclientdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 显示新闻的详细信息
 * Created by Alpha on 2016/7/1.
 */
public class NewsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //对于AppCompatActivity无效
        //getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_news);
        Intent intent=getIntent();
        String newslink=intent.getStringExtra("newslink");
        WebView webView= (WebView) findViewById(R.id.news_web);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;   //false表示用适用于本app的响应方式打开
                                //true表示用桌面视图打开网页，不会自动缩放
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(newslink);
    }
}
