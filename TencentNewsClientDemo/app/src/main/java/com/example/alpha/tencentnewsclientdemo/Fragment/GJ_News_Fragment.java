package com.example.alpha.tencentnewsclientdemo.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.alpha.tencentnewsclientdemo.JavaBean.NewsItem;
import com.example.alpha.tencentnewsclientdemo.NewsActivity;
import com.example.alpha.tencentnewsclientdemo.R;
import com.example.alpha.tencentnewsclientdemo.Services.NewsInfoParser;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * 国际新闻
 * Created by Alpha on 2016/7/2.
 */
public class GJ_News_Fragment extends Fragment {
    private static final int SHOW_ITEM = 1;
    private static final int LOAD_ERROR = 0;
    private LinearLayout ll_loading;
    private ListView lv_news;
    private List<NewsItem> newsItemList;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            ll_loading.setVisibility(View.INVISIBLE);
            switch (msg.what){
                case SHOW_ITEM:
                    //Toast.makeText(getActivity(),"获取数据成功",Toast.LENGTH_SHORT).show();
                    lv_news.setAdapter(new myNewsAdapter());
                    break;
                case LOAD_ERROR:
                    Toast.makeText(getActivity(),"获取数据失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View news_View=inflater.inflate(R.layout.gn_news_fragment,container,false);
        ll_loading= (LinearLayout)news_View.findViewById(R.id.load_view);
        lv_news= (ListView)news_View.findViewById(R.id.news_lv);

        loadNewsList();

        return news_View;
    }

    private void loadNewsList() {
        ll_loading.setVisibility(View.VISIBLE);
        new Thread() {
            @Override
            public void run() {
                try{
                    URL url=new URL("http://news.qq.com/newsgj/rss_newswj.xml");
                    HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);//连接超时
                    connection.setReadTimeout(20000);//下载超时
                    int code=connection.getResponseCode();
                    if (code==HttpURLConnection.HTTP_OK){
                        InputStream is=connection.getInputStream();
                        newsItemList= NewsInfoParser.getAllNewsList(is,"utf-8");
                        Message msg=Message.obtain();
                        msg.what=SHOW_ITEM;
                        handler.sendMessage(msg);
                    }
                }catch (Exception e){
                    Message msg=Message.obtain();
                    msg.what=LOAD_ERROR;
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    private class myNewsAdapter extends BaseAdapter {

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
            @SuppressLint("ViewHolder") View a_view= View.inflate(getActivity(),
                    R.layout.news_item,null);
            TextView title_view= (TextView) a_view.findViewById(R.id.tv_title);
            TextView author_view= (TextView) a_view.findViewById(R.id.tv_author);
            TextView desc_view= (TextView) a_view.findViewById(R.id.tv_desc);
            TextView comments_view= (TextView) a_view.findViewById(R.id.tv_comments);
            TextView link_view= (TextView) a_view.findViewById(R.id.tv_link);
            TextView date_view= (TextView) a_view.findViewById(R.id.tv_date);
            ImageView image_view= (ImageView) a_view.findViewById(R.id.tv_image);
            final NewsItem newsItem=newsItemList.get(i);

            title_view.setText(newsItem.getTitle());
            author_view.setText(newsItem.getAuthor());
            desc_view.setText(newsItem.getDescription());
            comments_view.setText(String.valueOf(newsItem.getComments()));
            link_view.setText(newsItem.getLink());
            date_view.setText(newsItem.getDate());

            //loadImageWithUIL(image_view,newsItem.getImageuri());
            //loadImageWithPicasso(image_view,newsItem.getImageuri());
            loadImageWithGlide(image_view,newsItem.getImageuri());

            a_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(getActivity(),NewsActivity.class);
                    intent.putExtra("newslink",newsItem.getLink());
                    startActivity(intent);
                }
            });

            return a_view;
        }
    }

    private void loadImageWithGlide(ImageView imageView,String uri){
        Glide.with(getActivity())
                .load(uri)
                .into(imageView);
    }
}
