package com.example.makabaka.interfaces;

public interface IAlbumDetailPresenter {
    void pull2Refresh();//下拉刷新
    void loadMore();//上拉加载更多内容
    void getAlbumDetail(int albumID,int page);//获取专辑详情
    void registerViewCallback(IAlbumDetailViewCallback albumDetailViewCallback);//注册UI通知的接口
    void unregisterViewCallback(IAlbumDetailViewCallback albumDetailViewCallback);//删除UI通知接口
}
