package com.example.makabaka.interfaces;

import com.example.makabaka.base.IBasePresenter;

public interface IRecommendPresenter extends IBasePresenter<IRecommendCallBack> {
    void getRecommendList();  //获取推荐内容
}
