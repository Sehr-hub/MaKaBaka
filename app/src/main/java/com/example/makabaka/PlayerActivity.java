package com.example.makabaka;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        mPlayerPresenter = PlayerPresenter.getPlayerPresenter();
        mPlayerPresenter.registerViewCallBack(this);
        initView();
        initEvent();
        startPlay();           //进入播放页后默认开始播放
    }

    private void startPlay() {
        if (mPlayerPresenter!=null) {
            mPlayerPresenter.play();
        }
    }

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
    }

    private void initView() {
        mControlBtn = this.findViewById(R.id.play_or_pause_btn);
        mTotalDuration = this.findViewById(R.id.track_duration);
        mCurrentPosition = this.findViewById(R.id.current_position);
        mSeekBar = this.findViewById(R.id.track_seek_bar);

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
    //========================播放器相关回调函数=================
}
