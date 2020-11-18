package com.example.makabaka;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.makabaka.adapters.IndicatorAdapter;
import com.example.makabaka.adapters.MainContentAdapetr;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;


public class MainActivity extends FragmentActivity {

    private MagicIndicator mMagicIndicator;
    private ViewPager mContentPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    private void initView() {
        mMagicIndicator=this.findViewById(R.id.main_indicator);
        mMagicIndicator.setBackgroundColor(this.getResources().getColor(R.color.main_color));
        //创建indicator适配器
        IndicatorAdapter adapter=new IndicatorAdapter(this);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(adapter);
        mContentPager=this.findViewById(R.id.content_pager);
        //创建内容适配器
        FragmentManager supportFragmentManager= getSupportFragmentManager();
        MainContentAdapetr mainContentAdapetr=new MainContentAdapetr(supportFragmentManager);
        mContentPager.setAdapter(mainContentAdapetr);
        //把viewpager与indicator绑定到一起
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator,  mContentPager);
    }

}