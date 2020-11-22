package com.example.makabaka.presenters;

import com.example.makabaka.interfaces.IAlbumDetailPresenter;
import com.example.makabaka.interfaces.IAlbumDetailViewCallback;
import com.example.makabaka.utils.LogUtils;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlbumDetailPresenter implements IAlbumDetailPresenter {

    private static final String TAG ="AlbumDetailPresenter" ;
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
    public void getAlbumDetail(long albumID, int page) {
        //根据专辑ID和页码获取列表
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.ALBUM_ID, albumID+"");
        map.put(DTransferConstants.SORT, "asc");
        map.put(DTransferConstants.PAGE, page+"");
        CommonRequest.getTracks(map, new IDataCallBack<TrackList>() {
            @Override
            public void onSuccess(TrackList trackList) {
                if(trackList!=null){
                    List<Track> tracks=trackList.getTracks();
                    handlerAlbumDetailResult(tracks);
                }
            }

            @Override
            public void onError(int i, String s) {
                LogUtils.d(TAG,"error code-->"+i);
                LogUtils.d(TAG,"error message-->"+s);
                handlerError(i,s);
            }
        });

    }

    /*
    发生错误则将错误码和错误消息返回UI
     */
    private void handlerError(int i, String s) {
        for(IAlbumDetailViewCallback callback:mCallbacks){
            callback.onNetworkError(i,s);
        }
    }

    private void handlerAlbumDetailResult(List<Track> tracks) {
        for(IAlbumDetailViewCallback mCallback:mCallbacks){
            mCallback.onDetailListLoaded(tracks);
        }
    }

    @Override
    public void registerViewCallBack(IAlbumDetailViewCallback albumDetailViewCallback) {
        if(!mCallbacks.contains(albumDetailViewCallback)){
            mCallbacks.add(albumDetailViewCallback);
            if(mTargetAlbum!=null){
                albumDetailViewCallback.onAlbumLoaded(mTargetAlbum);
            }
        }
    }

    @Override
    public void unRegisterViewCallBack(IAlbumDetailViewCallback albumDetailViewCallback) {
        mCallbacks.remove(albumDetailViewCallback);
    }

    public void setTargetAlbum(Album targetAlbum){
        this.mTargetAlbum=targetAlbum;
    }

}
