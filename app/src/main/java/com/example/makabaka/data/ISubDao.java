package com.example.makabaka.data;

import com.ximalaya.ting.android.opensdk.model.album.Album;

public interface ISubDao {

    void setCallback(ISubDaoCallback callback);
    void addAlbum(Album album);//添加专辑订阅
    void delAlbum(Album album);//删除订阅内容
    void listAlbums();//获取订阅内容

}
