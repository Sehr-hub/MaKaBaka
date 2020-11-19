package com.example.makabaka.presenters;

import com.example.makabaka.interfaces.IRecommendCallBack;
import com.example.makabaka.interfaces.IRecommendPresenter;
import com.example.makabaka.utils.Constants;
import com.example.makabaka.utils.LogUtils;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendPresenter implements IRecommendPresenter {

    private String TAG="RecommendPresenter";
    private List<IRecommendCallBack> mCallbacks=new ArrayList<>();

    private RecommendPresenter(){}                        //使用单例模式，保证一个类仅有一个实例

    private static RecommendPresenter sInstance=null;

    public static RecommendPresenter getInstance(){
        if(sInstance==null){
            synchronized (RecommendPresenter.class){   //加锁，保证同一时刻只有一个线程可以执行某个方法
                if(sInstance==null){
                    sInstance=new RecommendPresenter();
                }
            }
        }
        return sInstance;
    }

    /*
     * 获取推荐内容
     * */
    @Override
    public void getRecommendList() {
        updateLoading();
        //封装参数
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.LIKE_COUNT, Constants.RECOMMEND_COUNT+"");
        CommonRequest.getGuessLikeAlbum(map, new IDataCallBack<GussLikeAlbumList>() {
            @Override
            public void onSuccess(GussLikeAlbumList gussLikeAlbumList) {
                //数据获取成功
                if(gussLikeAlbumList!=null){
                    List<Album> albumList=gussLikeAlbumList.getAlbumList();
                    //updateRecommendUI(albumList);
                    handlerRecommendResult(albumList);
                }
            }

            @Override
            public void onError(int i, String s) {
                //获取出错
                LogUtils.d(TAG,"error-->"+i);
                LogUtils.d(TAG,"errorMessage-->"+s);
                handlerError();
            }
        });

    }

    private void handlerError() {
        if(mCallbacks!=null){
            for(IRecommendCallBack callBack:mCallbacks){
                callBack.onNetworkError();
            }
        }
    }


    private void handlerRecommendResult(List<Album> albumList) {   //对返回的推荐数据进行处理
        //通知UI进行更新
        if(albumList!=null){
            if(albumList.size()==0){
                for(IRecommendCallBack callBack:mCallbacks){
                    callBack.onEmpty();
                }
            }
            else {
                for(IRecommendCallBack callBack:mCallbacks){
                    callBack.onRecommendListLoaded(albumList);
                }
            }
        }
    }

    private void updateLoading(){
        for(IRecommendCallBack callBack:mCallbacks){
            callBack.onLoading();
        }
    }


    @Override
    public void registerViewCallBack(IRecommendCallBack callback) {
        if(mCallbacks!=null&&!mCallbacks.contains(callback)){
            mCallbacks.add(callback);
        }
    }

    @Override
    public void unRegisterViewCallBack(IRecommendCallBack callback) {
        if(mCallbacks!=null){
            mCallbacks.remove(mCallbacks);
        }
    }


}
