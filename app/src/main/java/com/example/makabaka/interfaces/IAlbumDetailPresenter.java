package com.example.makabaka.interfaces;

import com.example.makabaka.base.IBasePresenter;

public interface IAlbumDetailPresenter extends IBasePresenter<IAlbumDetailViewCallback> {
    void pull2Refresh();//下拉刷新
    void loadMore();//上拉加载更多内容
    void getAlbumDetail(long albumID,int page);//获取专辑详情
}
