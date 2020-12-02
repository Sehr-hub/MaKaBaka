package com.example.makabaka.presenters;

import com.example.makabaka.base.BaseApplication;
import com.example.makabaka.data.ISubDaoCallback;
import com.example.makabaka.data.SubscriptionDao;
import com.example.makabaka.interfaces.ISubscriptionCallback;
import com.example.makabaka.interfaces.ISubscriptionPresenter;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SubscriptionPresenter implements ISubscriptionPresenter, ISubDaoCallback {

    private final SubscriptionDao mSubscriptionDao;
    private Map<Long,Album> mData=new HashMap<>();
    private List<ISubscriptionCallback> mCallbacks=new ArrayList<>();

    private SubscriptionPresenter(){
        mSubscriptionDao = SubscriptionDao.getInstance();
        mSubscriptionDao.setCallback(this);
    };

    private void listSubscriptions(){
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> emitter) throws Exception {
                //只调用，不处理结果
                if (mSubscriptionDao != null) {
                    mSubscriptionDao.listAlbums();
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    private static SubscriptionPresenter sInstance=null;

    public static SubscriptionPresenter getInstance(){
        if(sInstance==null){
            synchronized (SubscriptionPresenter.class){
                sInstance=new SubscriptionPresenter();
            }
        }
        return sInstance;
    }

    @Override
    public void addSubscription(final Album album) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> emitter) throws Exception {
                if (mSubscriptionDao != null) {
                    mSubscriptionDao.addAlbum(album);
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void deleteSubscription(final Album album) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> emitter) throws Exception {
                if (mSubscriptionDao != null) {
                    mSubscriptionDao.delAlbum(album);
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void getSubscriptionList() {
        listSubscriptions();
    }

    @Override
    public boolean isSub(Album album) {
        Album result=mData.get(album.getId());
        //result不为空，说明已订阅
        return result!=null;
    }

    @Override
    public void registerViewCallBack(ISubscriptionCallback iSubscriptionCallback) {
        if (!mCallbacks.contains(iSubscriptionCallback)) {
            mCallbacks.add(iSubscriptionCallback);
        }
    }

    @Override
    public void unRegisterViewCallBack(ISubscriptionCallback iSubscriptionCallback) {
        mCallbacks.remove(iSubscriptionCallback);
    }

    @Override
    public void onAddResult(final boolean isSuccess) {
        listSubscriptions();
        //添加结果的回调
        BaseApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                for (ISubscriptionCallback callback : mCallbacks) {
                    callback.onAddResult(isSuccess);
                }
            }
        });
    }

    @Override
    public void onDelResult(final boolean isSuccess) {
        listSubscriptions();
        //删除订阅的回调
        BaseApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                for (ISubscriptionCallback callback : mCallbacks) {
                    callback.onDeleteResult(isSuccess);
                }
            }
        });
    }

    @Override
    public void onSubLoaded(final List<Album> result) {
        //加载数据的回调
        mData.clear();
        for (Album album : result) {
            mData.put(album.getId(),album);
        }
        //通知UI更新
        BaseApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                for (ISubscriptionCallback callback : mCallbacks) {
                    callback.onSubscriptionsLoaded(result);
                }
            }
        });
    }
}
