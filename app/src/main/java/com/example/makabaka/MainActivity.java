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
    private IndicatorAdapter mIndicatorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }

    private void initEvent() {
        mIndicatorAdapter.setOnIndicatorTapClickListener(new IndicatorAdapter.OnIndicatorTapClickListener() {
            @Override
            public void onTabClick(int index) {
                if(mContentPager!=null){
                    mContentPager.setCurrentItem(index);
                }
            }

        });
    }

    private void initView() {
        mMagicIndicator=this.findViewById(R.id.main_indicator);
        mMagicIndicator.setBackgroundColor(this.getResources().getColor(R.color.main_color));
        //创建indicator适配器
        mIndicatorAdapter = new IndicatorAdapter(this);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(mIndicatorAdapter);
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