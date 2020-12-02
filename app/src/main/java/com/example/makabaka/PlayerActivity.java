package com.example.makabaka;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.example.makabaka.adapters.PlayerTrackPagerAdapter;
import com.example.makabaka.base.BaseActivity;
import com.example.makabaka.interfaces.IPlayerCallback;
import com.example.makabaka.presenters.PlayerPresenter;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.text.SimpleDateFormat;
import java.util.List;

public class PlayerActivity extends BaseActivity implements IPlayerCallback {

    private ImageView mControlBtn;
    private PlayerPresenter mPlayerPresenter;
    private SimpleDateFormat mMinFormat=new SimpleDateFormat("mm:ss");
    private SimpleDateFormat mHourFormat=new SimpleDateFormat("hh:mm:ss");
    private TextView mTotalDuration;
    private TextView mCurrentPosition;
    private SeekBar mSeekBar;
    private int mCurrentProgress=0;
    private boolean mIsUserTouchProgressBar=false;
    private ImageView mPlayNextBtn;
    private ImageView mPlayPreBtn;
    private TextView mTrackTitle;
    private ViewPager mTrackPageView;
    private PlayerTrackPagerAdapter mTrackPagerAdapter;
    private boolean mIsUserSlidePager=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initView();
        mPlayerPresenter = PlayerPresenter.getPlayerPresenter();
        mPlayerPresenter.registerViewCallBack(this);
        mPlayerPresenter.getPlayList();
        initEvent();
    }


    @SuppressLint("ClickableViewAccessibility")
    private void initEvent() {
        //给控件设置相关事件
        //播放暂停事件
        mControlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPlayerPresenter.isPlay()){
                    mPlayerPresenter.pause();
                }else{
                    mPlayerPresenter.play();
                }
            }
        });
        //拖动进度条修改播放进度事件
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){    //是用户拖动的进度条，则进行修改
                    mCurrentProgress=progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mIsUserTouchProgressBar=true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //用户手离开进度条时更新进度
                mIsUserTouchProgressBar=false;
                mPlayerPresenter.seekTo(mCurrentProgress);
            }
        });
        //点击上一首图标更改播放节目
        mPlayPreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayerPresenter != null) {
                    mPlayerPresenter.playPre();
                }
            }
        });
        //点击下一首图标更改播放节目
        mPlayNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayerPresenter != null) {
                    mPlayerPresenter.playNext();
                }
            }
        });
        //用户滑动播放器图片监听
        mTrackPageView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //页面选中时切换播放内容
                if (mPlayerPresenter != null&& mIsUserSlidePager==true) {
                    mPlayerPresenter.playByIndex(position);
                }
                mIsUserSlidePager=false;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTrackPageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action=event.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        mIsUserSlidePager=true;
                    break;
                }
                return false;
            }
        });
    }

    private void initView() {
        mControlBtn = this.findViewById(R.id.play_or_pause_btn);
        mTotalDuration = this.findViewById(R.id.track_duration);
        mCurrentPosition = this.findViewById(R.id.current_position);
        mSeekBar = this.findViewById(R.id.track_seek_bar);
        mPlayNextBtn = this.findViewById(R.id.play_next);
        mPlayPreBtn = this.findViewById(R.id.play_pre);
        mTrackTitle = this.findViewById(R.id.track_title);
        mTrackPageView = this.findViewById(R.id.track_page_view);
        //创建适配器
        mTrackPagerAdapter = new PlayerTrackPagerAdapter();
        //配置适配器
        mTrackPageView.setAdapter(mTrackPagerAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //释放资源
        if (mPlayerPresenter!=null) {
            mPlayerPresenter.unRegisterViewCallBack(this);
            mPlayerPresenter=null;
        }
    }

    //==============播放器相关回调函数================
    @Override
    public void onPlayStart() {
        //开始播放，将Ui修改成暂停按钮
        if (mControlBtn!=null) {
            mControlBtn.setImageResource(R.mipmap.stop);
        }
    }

    @Override
    public void onPlayPause() {
        if (mControlBtn!=null) {
            mControlBtn.setImageResource(R.mipmap.play1);
        }
    }

    @Override
    public void onPlayStop() {
        if (mControlBtn!=null) {
            mControlBtn.setImageResource(R.mipmap.play1);
        }
    }

    @Override
    public void onPlayError() {

    }

    @Override
    public void onNextPlay(Track track) {

    }

    @Override
    public void onPrePlay(Track track) {

    }

    @Override
    public void onListLoaded(List<Track> list) {
        //设置数据到适配器
        if (mTrackPagerAdapter != null) {
            mTrackPagerAdapter.setData(list);
        }
    }

    @Override
    public void onPlayModeChange(XmPlayListControl.PlayMode playMode) {

    }

    @Override
    public void onProgressChange(int currPos, int duration) {
        String totalDuration;
        String currentPosition;
        mSeekBar.setMax(duration);
        //更新播放进度，更新进度条
        if (duration>1000*60*60) {
            totalDuration = mHourFormat.format(duration);
            currentPosition=mHourFormat.format(currPos);
        }else {
            totalDuration=mMinFormat.format(duration);
            currentPosition=mMinFormat.format(currPos);
        }
        if (mTotalDuration != null) {
            mTotalDuration.setText(totalDuration);
        }
        //更新当前时间
        if(mCurrentPosition!=null){
            mCurrentPosition.setText(currentPosition);
        }
        //用户没有拖动时更新播放进度
        if(!mIsUserTouchProgressBar){
            mSeekBar.setProgress(currPos);
        }

    }

    @Override
    public void onAdLoading() {

    }

    @Override
    public void onAdFinished() {

    }

    @Override
    public void onTrackUpdate(Track track, int playIndex) {
        //设置当前界面标题
        if (mTrackTitle != null) {
            mTrackTitle.setText(track.getTrackTitle());
        }
        //节目改变后，也需要更改播放器页面的图片
        if(mTrackPageView!=null){
            mTrackPageView.setCurrentItem(playIndex,true);
        }
    }

    //========================播放器相关回调函数=================
}
