package com.example.alpha.tencentnewsclientdemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alpha.tencentnewsclientdemo.JavaBean.NewsItem;
import com.example.alpha.tencentnewsclientdemo.Services.NewsInfoParser;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.NonWritableChannelException;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    private static final int SHOW_ITEM = 1;
    private static final int LOAD_ERROR = 0;
    private LinearLayout ll_loading;
    private ListView lv_news;
    private List<NewsItem> newsItemList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ll_loading= (LinearLayout) findViewById(R.id.load_view);
        lv_news= (ListView) findViewById(R.id.news_lv);

        loadNewsList();
    }

    private void loadNewsList() {
        ll_loading.setVisibility(View.VISIBLE);
        new Thread() {
            @Override
            public void run() {
                try{
                    Thread.sleep(1000);
                    URL url=new URL("http://news.qq.com/newsgn/rss_newsgn.xml");
                    HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);//连接超时
                    connection.setReadTimeout(20000);//下载超时
                    int code=connection.getResponseCode();
                    if (code==200){
                        InputStream is=connection.getInputStream();
                        newsItemList= NewsInfoParser.getAllNewsList(is);
                        Message msg=Message.obtain();
                        msg.what=SHOW_ITEM;
                        hander.sendMessage(msg);
                    }
                }catch (Exception e){
                    Message msg=Message.obtain();
                    msg.what=LOAD_ERROR;
                    hander.sendMessage(msg);
                }
            }
        }.start();
    }

    private Handler hander=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            ll_loading.setVisibility(View.INVISIBLE);
            switch (msg.what){
                case SHOW_ITEM:
                    Toast.makeText(MainActivity.this,"获取数据成功",Toast.LENGTH_SHORT).show();
                    lv_news.setAdapter(new myNewsAdapter());
                    break;
                case LOAD_ERROR:
                    Toast.makeText(MainActivity.this,"获取数据失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private class myNewsAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            if (newsItemList!=null){
                return newsItemList.size();
            }
            else return 0;

        }

        @Override
        public Object getItem(int i) {
            return null;
        }


        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            @SuppressLint("ViewHolder") View a_view= View.inflate(MainActivity.this,R.layout.news_item,null);
            TextView title_view= (TextView) a_view.findViewById(R.id.tv_title);
            TextView author_view= (TextView) a_view.findViewById(R.id.tv_author);
            TextView desc_view= (TextView) a_view.findViewById(R.id.tv_desc);
            TextView comments_view= (TextView) a_view.findViewById(R.id.tv_comments);
            TextView link_view= (TextView) a_view.findViewById(R.id.tv_link);
            TextView date_view= (TextView) a_view.findViewById(R.id.tv_date);
            final NewsItem newsItem=newsItemList.get(i);

            title_view.setText(newsItem.getTitle());
            author_view.setText(newsItem.getAuthor());
            desc_view.setText(newsItem.getDescription());
            comments_view.setText(String.valueOf(newsItem.getComments()));
            link_view.setText(newsItem.getLink());
            date_view.setText(newsItem.getDate());

            a_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(MainActivity.this,NewsActivity.class);
                    intent.putExtra("newslink",newsItem.getLink());
                    startActivity(intent);
                }
            });

            return a_view;
        }
    }
}
