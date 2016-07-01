package com.example.alpha.tencentnewsclientdemo.Services;

import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import com.example.alpha.tencentnewsclientdemo.JavaBean.NewsItem;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 新闻解析类
 * Created by Alpha on 2016/6/30.
 */
public class NewsInfoParser {

    public static List<NewsItem> getAllNewsList(InputStream is) throws Exception {

        List<NewsItem> newsItemList=null;
        NewsItem newsItem=null;

        XmlPullParser parser= Xml.newPullParser();
        parser.setInput(is,"gb2312");
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
