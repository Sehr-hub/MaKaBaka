package com.example.makabaka.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

public interface IAlbumDetailViewCallback {
    void onDetailListLoaded(List<Track> tracks);//专辑详情内容加载了
    void onAlbumLoaded(Album album); //把Album传给UI使用
    void onNetworkError(int errorCode, String errorMsg);//网路错误
}
