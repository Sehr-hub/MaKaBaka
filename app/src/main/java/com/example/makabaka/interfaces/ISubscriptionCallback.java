package com.example.makabaka.interfaces;

import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

public interface ISubscriptionCallback {

    void onAddResult(boolean isSuccess);//调用添加时，通知UI结果

    void onDeleteResult(boolean isSuccess);

    void onSubscriptionsLoaded(List<Album> albums);//订阅专辑加载的结果

}
