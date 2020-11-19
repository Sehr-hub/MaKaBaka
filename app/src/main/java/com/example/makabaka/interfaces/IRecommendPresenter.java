package com.example.makabaka.interfaces;

public interface IRecommendPresenter {
    void getRecommendList();  //获取推荐内容
    void pull2RefreshMore();  //下拉刷新内容
    void loadMore();          //加载更多
    void registerViewCallBack(IRecommendCallBack callback);  //注册UI的回调
    void unRegisterViewCallBack(IRecommendCallBack callback);//取消注册回调
}
