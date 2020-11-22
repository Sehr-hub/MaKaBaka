package com.example.makabaka.presenters;

import android.widget.SeekBar;

import com.example.makabaka.base.BaseApplication;
import com.example.makabaka.interfaces.IPlayerCallback;
import com.example.makabaka.interfaces.IPlayerPresenter;
import com.example.makabaka.utils.LogUtils;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerControl;

import java.util.List;

public class PlayerPresenter implements IPlayerPresenter {

    private String TAG="PlayerPresenter";
    private XmPlayerManager mPlayerManager;

    private PlayerPresenter(){
        //获取播放器单例
        mPlayerManager = XmPlayerManager.getInstance(BaseApplication.getAppContext());
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

    }

    @Override
    public void stop() {

    }

    @Override
    public void playPre() {

    }

    @Override
    public void playNext() {

    }

    @Override
    public void switchPlayMode(XmPlayListControl.PlayMode mode) {

    }

    @Override
    public void getPlayList() {

    }

    @Override
    public void playByIndex(int index) {

    }

    @Override
    public void seekTo(int progress) {

    }

    @Override
    public void registerViewCallBack(IPlayerCallback iPlayerCallback) {

    }

    @Override
    public void unRegisterViewCallBack(IPlayerCallback iPlayerCallback) {

    }
}
