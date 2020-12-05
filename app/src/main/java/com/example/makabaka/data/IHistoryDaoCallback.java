package com.example.makabaka.data;

import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

public interface IHistoryDaoCallback {
    void onHistoryAdd(boolean isSuccess);//添加历史结果
    void onHistoryDel(boolean isSuccess);//删除历史结果
    void onHistoryLoaded(List<Track> tracks);//历史记录加载结果
    void onHistoryClean(boolean isSuccess);//清除历史结果
}
