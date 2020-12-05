package com.example.makabaka.data;

import com.ximalaya.ting.android.opensdk.model.track.Track;

public interface IHistoryDao {

    void setCallback(IHistoryDaoCallback callback);//设置回调接口
    void addHistory(Track track);//添加历史
    void delHistory(Track  track);//删除历史
    void cleanHistory();//清除历史
    void listHistories();//获取历史内容
}
