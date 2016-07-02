package com.example.alpha.tencentnewsclientdemo;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.alpha.tencentnewsclientdemo.Fragment.DM_News_Fragment;
import com.example.alpha.tencentnewsclientdemo.Fragment.DY_News_Fragment;
import com.example.alpha.tencentnewsclientdemo.Fragment.GJ_News_Fragment;
import com.example.alpha.tencentnewsclientdemo.Fragment.GN_News_Fragment;
import com.example.alpha.tencentnewsclientdemo.Fragment.YX_News_Fragment;
import com.example.alpha.tencentnewsclientdemo.JavaBean.NewsItem;
import com.example.alpha.tencentnewsclientdemo.Services.NewsInfoParser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FragmentPagerAdapter adapter;
    private GN_News_Fragment gnNewsFragment;
    private GJ_News_Fragment gjNewsFragment;
    private DY_News_Fragment dyNewsFragment;
    private YX_News_Fragment yxNewsFragment;
    private DM_News_Fragment dmNewsFragment;
    private List<Fragment> fragmentList;
    private List<String> titlelist;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar=getSupportActionBar();
        Drawable drawable=getResources().getDrawable(R.mipmap.qqq);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(drawable);

        mTabLayout= (TabLayout) findViewById(R.id.tab_title);
        mTabLayout.setTabTextColors(Color.BLUE,getResources().getColor(R.color.colorAccent));
        mViewPager= (ViewPager) findViewById(R.id.view_pager);

        fragmentList=new ArrayList<>();
        gnNewsFragment=new GN_News_Fragment();
        gjNewsFragment=new GJ_News_Fragment();
        dyNewsFragment=new DY_News_Fragment();
        yxNewsFragment=new YX_News_Fragment();
        dmNewsFragment=new DM_News_Fragment();
        fragmentList.add(gnNewsFragment);
        fragmentList.add(gjNewsFragment);
        fragmentList.add(dyNewsFragment);
        fragmentList.add(yxNewsFragment);
        fragmentList.add(dmNewsFragment);

        titlelist=new ArrayList<>();
        titlelist.add("国内新闻");
        titlelist.add("国际新闻");
        titlelist.add("电影资讯");
        titlelist.add("游戏竞技");
        titlelist.add("动漫番剧");

        mTabLayout.addTab(mTabLayout.newTab().setText(titlelist.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titlelist.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titlelist.get(2)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titlelist.get(3)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titlelist.get(4)));

        adapter= new FragmentPagerAdapter(this.getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return fragmentList.size();
            }

            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titlelist.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                super.destroyItem(container, position, object);
            }
        };

        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

}
