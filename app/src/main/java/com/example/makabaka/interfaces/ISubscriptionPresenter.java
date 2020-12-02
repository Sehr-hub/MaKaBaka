package com.example.makabaka.interfaces;

import com.example.makabaka.base.IBasePresenter;
import com.ximalaya.ting.android.opensdk.model.album.Album;

public interface ISubscriptionPresenter extends IBasePresenter<ISubscriptionCallback> {

    void addSubscription(Album album);//添加订阅
    void deleteSubscription(Album album);//删除订阅
    void getSubscriptionList();//获取订阅列表
    boolean isSub(Album album);//判断当前专辑是否收藏
}
