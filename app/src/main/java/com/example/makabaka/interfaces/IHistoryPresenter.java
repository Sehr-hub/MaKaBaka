package com.example.makabaka.interfaces;

import com.example.makabaka.base.IBasePresenter;
import com.ximalaya.ting.android.opensdk.model.track.Track;

public interface IHistoryPresenter extends IBasePresenter<IHistoryCallback> {

    void listHistories();//获取历史内容
    void addHistory(Track track);//添加历史记录
    void delHistory(Track track);//删除历史记录
    void cleanHistories();//清除历史记录

}
