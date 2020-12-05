package com.example.makabaka.interfaces;

import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

public interface IHistoryCallback {
    void onHistoriesLoaded(List<Track> tracks);//加载历史内容结果

}
