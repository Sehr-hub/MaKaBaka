package com.example.makabaka.interfaces;

import android.os.Trace;

import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

import java.util.List;

public interface IPlayerCallback {
    void onPlayStart();//开始播放
    void onPlayPause();//播放暂停
    void onPlayStop();//播放停止
    void onPlayError();//播放错误
    void onNextPlay(Track track);//播放下一首
    void onPrePlay(Track track);//播放上一首
    void onListLoaded(List<Track> list);//播放列表数据加载完成
    void onPlayModeChange(XmPlayListControl.PlayMode playMode);//切换播放模式
    void onProgressChange(int currPos,int duration);//切换播放进度
    void onAdLoading();//广告正在加载
    void onAdFinished();//广告结束
    void onTrackTitleUpdate(String title);//更新当前节目标题

}
