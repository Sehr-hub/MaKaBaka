package com.example.makabaka.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

public interface IRecommendCallBack {
    void onRecommendListLoaded(List<Album> result);  //获取推荐内容的结果
    void onNetworkError();//网络错误
    void onLoading();//加载中
    void onEmpty();//内容为空
}
