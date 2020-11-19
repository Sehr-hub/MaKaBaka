package com.example.makabaka.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

public interface IRecommendCallBack {
    void onRecommendListLoaded(List<Album> result);  //获取推荐内容的结果
    void onLoaderMore(List<Album> result);//加载更多结果
    void onRefreshMore(List<Album> result);//刷新结果
}
