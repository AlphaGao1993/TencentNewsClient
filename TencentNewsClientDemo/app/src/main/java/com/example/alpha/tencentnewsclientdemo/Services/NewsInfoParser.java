package com.example.alpha.tencentnewsclientdemo.Services;

import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import com.example.alpha.tencentnewsclientdemo.JavaBean.NewsItem;

import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 新闻解析类
 * Created by Alpha on 2016/6/30.
 */
public class NewsInfoParser {

    public static List<NewsItem> getAllNewsList(InputStream is,String encoding) throws Exception {

        List<NewsItem> newsItemList=null;
        NewsItem newsItem=null;
        XmlPullParser parser= Xml.newPullParser();
        /*byte[] buffer=new byte[1024];
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        int len=0;
        while ((len=is.read(buffer))!=-1){
            bos.write(buffer,0,len);
        }
        String result=bos.toString();
        if (result.contains("gb2312")){
            parser.setInput(is,"gb2312");
            Log.d("news_tag","设置编码成功");
        }
        else if (result.contains("utf-8")||result.contains("UTF-8")){
            parser.setInput(is,"utf-8");
            Log.d("news_tag","设置编码成功");
        }*/

        parser.setInput(is,encoding);
        Log.d("news_tag","设置编码成功");
        int type=parser.getEventType();
        while (type!=XmlPullParser.END_DOCUMENT){
            String tagName=parser.getName();
            switch (type){
                case XmlPullParser.START_TAG:
                    if ("channel".equals(tagName)){
                        newsItemList=new ArrayList<NewsItem>();
                    }
                    else if("item".equals(tagName)){
                        newsItem=new NewsItem();
                    }
                    else if ("title".equals(tagName)){
                        if (newsItem != null) {
                            newsItem.setTitle(parser.nextText());
                        }
                    }
                    else if ("link".equals(tagName)){
                        if (newsItem != null) {
                            newsItem.setLink(parser.nextText());
                        }
                    }
                    else if ("author".equals(tagName)){
                        if (newsItem != null) {
                            newsItem.setAuthor(parser.nextText());
                        }
                    }
                    else if ("comments".equals(tagName)){
                        String comments=parser.nextText();
                        if (!TextUtils.isEmpty(comments)&&TextUtils.isDigitsOnly(comments)){
                            if (newsItem != null) {
                                newsItem.setComments(Integer.valueOf(comments));
                            }
                        }
                    }
                    else if ("description".equals(tagName)){
                        if (newsItem != null) {
                            newsItem.setDescription(parser.nextText());
                        }
                    }
                    else if ("pubDate".equals(tagName)){
                        if (newsItem != null) {
                            newsItem.setDate(parser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if ("item".equals(tagName)){
                        if (newsItemList != null) {
                            newsItem.setImageuri("http://cms.csdnimg.cn/articlev1/"
                                    +"uploads/allimg/120518/130_120518103752_1.jpg");
                            newsItemList.add(newsItem);
                            newsItem=null;
                        }
                    }
                    break;
            }
            type=parser.next();
        }
        return newsItemList;
    }
}
