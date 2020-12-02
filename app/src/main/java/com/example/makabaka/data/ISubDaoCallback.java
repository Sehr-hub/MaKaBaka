package com.example.makabaka.data;

import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

public interface ISubDaoCallback {
    void onAddResult(boolean isSuccess);//添加的结果回调方法
    void onDelResult(boolean isSuccess);//删除结果
    void onSubLoaded(List<Album> result);//加载的结果
}
