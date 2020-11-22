package com.example.makabaka.interfaces;

import com.example.makabaka.base.IBasePresenter;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl;

public interface IPlayerPresenter extends IBasePresenter<IPlayerCallback> {

    void play();//播放器播放
    void pause();//暂停
    void stop();//停止
    void playPre();//上一首
    void playNext();//下一首
    void switchPlayMode(XmPlayListControl.PlayMode mode);//切换播放模式
    void getPlayList();//获取播放列表
    void playByIndex(int index);//根据播放列表的节目位置切换节目
    void seekTo(int progress);//拖动切换播放进度

}
