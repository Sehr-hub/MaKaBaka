package com.example.makabaka.presenters;

import android.widget.SeekBar;

import com.example.makabaka.DetailActivity;
import com.example.makabaka.base.BaseApplication;
import com.example.makabaka.interfaces.IPlayerCallback;
import com.example.makabaka.interfaces.IPlayerPresenter;
import com.example.makabaka.utils.LogUtils;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.advertis.Advertis;
import com.ximalaya.ting.android.opensdk.model.advertis.AdvertisList;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.advertis.IXmAdsStatusListener;
import com.ximalaya.ting.android.opensdk.player.constants.PlayerConstants;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;

import java.util.ArrayList;
import java.util.List;

public class PlayerPresenter implements IPlayerPresenter, IXmAdsStatusListener, IXmPlayerStatusListener {

    private String TAG="PlayerPresenter";
    private XmPlayerManager mPlayerManager;
    private List<IPlayerCallback> mIPlayerCallbacks=new ArrayList<>();
    private Track mTrack;
    private int mCurrentIndex=0;

    private PlayerPresenter(){
        //获取播放器单例
        mPlayerManager = XmPlayerManager.getInstance(BaseApplication.getAppContext());
        //*广告物理相关回调
        mPlayerManager.addAdsStatusListener(this);
        //注册播放器状态相关回调接口
        mPlayerManager.addPlayerStatusListener(this);

    }

    private static PlayerPresenter sPlayerPresenter;

    public static PlayerPresenter getPlayerPresenter(){
        if (sPlayerPresenter==null) {
            synchronized (PlayerPresenter.class){
                if (sPlayerPresenter==null) {
                    sPlayerPresenter=new PlayerPresenter();
                }
            }
        }
        return sPlayerPresenter;
    }

    //设置播放列表
    private boolean isPlayListSet=false;
    public void setPlayerList(List<Track> list, int playIndex){
        if (mPlayerManager!=null) {
            mPlayerManager.setPlayList(list,playIndex);
            isPlayListSet=true;
            mTrack = list.get(playIndex);
            mCurrentIndex = playIndex;
        }else{
            LogUtils.d(TAG,"mPlayerManager is null");
        }
    }

    @Override
    public void play() {
        if(isPlayListSet){
            mPlayerManager.play();
        }
    }

    @Override
    public void pause() {
        if(mPlayerManager!=null){
            mPlayerManager.pause();
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public void playPre() {
        if (mPlayerManager != null) {
            mPlayerManager.playPre();
        }
    }

    @Override
    public void playNext() {
        if (mPlayerManager != null) {
            mPlayerManager.playNext();
        }
    }

    @Override
    public void switchPlayMode(XmPlayListControl.PlayMode mode) {

    }

    @Override
    public void getPlayList() {
        if (mPlayerManager != null) {
            List<Track> playList=mPlayerManager.getPlayList();
            for(IPlayerCallback iPlayerCallback:mIPlayerCallbacks){
                iPlayerCallback.onListLoaded(playList);
            }
        }
    }

    @Override
    public void playByIndex(int index) {
        //切换播放器播放index位置节目
        if (mPlayerManager != null) {
            mPlayerManager.play(index);
        }
    }

    @Override
    public void seekTo(int progress) {
        //更改播放进度
        mPlayerManager.seekTo(progress);
    }

    @Override
    public boolean isPlay() {
        return mPlayerManager.isPlaying();
    }

    @Override
    public void registerViewCallBack(IPlayerCallback iPlayerCallback) {
        if (!mIPlayerCallbacks.contains(iPlayerCallback)) {
            mIPlayerCallbacks.add(iPlayerCallback);
        }
        getPlayList();
        iPlayerCallback.onTrackUpdate(mTrack,mCurrentIndex);

    }

    @Override
    public void unRegisterViewCallBack(IPlayerCallback iPlayerCallback) {
        mIPlayerCallbacks.remove(iPlayerCallback);
    }

    //==================================播放器状态回调===================================

    @Override
    public void onPlayStart() {  //开始播放
        for(IPlayerCallback iPlayerCallback:mIPlayerCallbacks){
            iPlayerCallback.onPlayStart();
        }
    }

    @Override
    public void onPlayPause() {   //暂停播放
        for(IPlayerCallback iPlayerCallback:mIPlayerCallbacks){
            iPlayerCallback.onPlayPause();
        }
    }

    @Override
    public void onPlayStop() {    //停止播放
        for(IPlayerCallback iPlayerCallback:mIPlayerCallbacks){
            iPlayerCallback.onPlayStop();
        }
    }

    @Override
    public void onSoundPlayComplete() {   //播放完成
        LogUtils.d(TAG,"onSoundPlayComplete");
    }

    @Override
    public void onSoundPrepared() {       //播放器准备完毕
            mPlayerManager.play();
    }

    /*
    切歌
     lastModel：上一首model,可能为空
     curModel：下一首model,请通过model中的kind字段来判断是track、radio和schedule；
     上一首的播放时间请通过lastPlayedMills字段来获取;
     */
    @Override
    public void onSoundSwitch(PlayableModel lastModel, PlayableModel curModel) {
        mCurrentIndex=mPlayerManager.getCurrentIndex();
        if(curModel instanceof Track){
            mTrack=(Track)curModel;
            for (IPlayerCallback iPlayerCallback : mIPlayerCallbacks) {
                iPlayerCallback.onTrackUpdate(mTrack,mCurrentIndex);
            }
        }

    }

    @Override
    public void onBufferingStart() {        //开始缓冲
        LogUtils.d(TAG,"onBufferingStart……");
    }

    @Override
    public void onBufferingStop() {         //结束缓冲
        LogUtils.d(TAG,"onBufferingStop");
    }

    @Override
    public void onBufferProgress(int percent) {    //缓冲进度回调
        LogUtils.d(TAG,"onBufferProgress");
    }

    @Override
    public void onPlayProgress(int currPos, int duration) {  //播放进度回调
        for(IPlayerCallback iPlayerCallback:mIPlayerCallbacks){
            iPlayerCallback.onProgressChange(currPos,duration);
        }
    }

    @Override
    public boolean onError(XmPlayerException e) {
        return false;
    }

    //================================播放器回调 end==========================



    //==========================广告相关的回调方法==========================

    //开始获取广告物料
    @Override
    public void onStartGetAdsInfo() {

    }

    //获取广告物料成功
    @Override
    public void onGetAdsInfo(AdvertisList advertisList) {

    }

    //广告开始缓冲
    @Override
    public void onAdsStartBuffering() {

    }

    //广告结束缓冲
    @Override
    public void onAdsStopBuffering() {

    }

    //开始播放广告
    @Override
    public void onStartPlayAds(Advertis advertis, int i) {

    }

    //广告播放完毕
    @Override
    public void onCompletePlayAds() {

    }

    //播放广告错误
    @Override
    public void onError(int i, int i1) {

    }

    //==================================广告相关回调 end=========================
}
