package com.example.makabaka.base;

public interface IBasePresenter<T> {
    void registerViewCallBack(T t);//注册UI回调的接口
    void unRegisterViewCallBack(T t);//删除UI通知接口
}
