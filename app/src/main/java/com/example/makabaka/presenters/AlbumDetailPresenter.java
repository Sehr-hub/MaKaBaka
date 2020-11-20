package com.example.makabaka.presenters;

import com.example.makabaka.interfaces.IAlbumDetailPresenter;
import com.example.makabaka.interfaces.IAlbumDetailViewCallback;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

public class AlbumDetailPresenter implements IAlbumDetailPresenter {

    private Album mTargetAlbum;
    private List<IAlbumDetailViewCallback> mCallbacks=new ArrayList<>();

    private AlbumDetailPresenter(){
    }

    private static AlbumDetailPresenter sInstance=null;

    public static AlbumDetailPresenter getInstance() {
        if (sInstance == null) {
            synchronized (AlbumDetailPresenter.class){
                if(sInstance==null){
                    sInstance=new AlbumDetailPresenter();
                }
            }
        }
        return sInstance;
    }


    @Override
    public void pull2Refresh() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void getAlbumDetail(int albumID, int page) {

    }

    @Override
    public void registerViewCallback(IAlbumDetailViewCallback albumDetailViewCallback) {
        if(!mCallbacks.contains(albumDetailViewCallback)){
            mCallbacks.add(albumDetailViewCallback);
            if(mTargetAlbum!=null){
                albumDetailViewCallback.onAlbumLoaded(mTargetAlbum);
            }
        }
    }

    @Override
    public void unregisterViewCallback(IAlbumDetailViewCallback albumDetailViewCallback) {
        mCallbacks.remove(albumDetailViewCallback);
    }

    public void setTargetAlbum(Album targetAlbum){
        this.mTargetAlbum=targetAlbum;
    }

}
