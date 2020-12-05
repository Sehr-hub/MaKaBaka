package com.example.makabaka.presenters;

import com.example.makabaka.base.BaseApplication;
import com.example.makabaka.data.HistoryDao;
import com.example.makabaka.data.IHistoryDao;
import com.example.makabaka.data.IHistoryDaoCallback;
import com.example.makabaka.interfaces.IHistoryCallback;
import com.example.makabaka.interfaces.IHistoryPresenter;
import com.example.makabaka.utils.Constants;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HistoryPresenter implements IHistoryPresenter, IHistoryDaoCallback {
    private List<IHistoryCallback> mCallbacks = new ArrayList<>();
    private final IHistoryDao mHistoryDao;
    private List<Track> mCurrentHistories=null;
    private Track mCurrentAddTrack = null;
    private boolean isDoDelAsOutOfSize = false;

    private HistoryPresenter(){
        mHistoryDao = new HistoryDao();
        mHistoryDao.setCallback(this);
    }

    private static HistoryPresenter sHistoryPresenter=null;

    public static HistoryPresenter getHistoryPresenter(){
        if (sHistoryPresenter == null) {
            synchronized (HistoryPresenter.class){
                if (sHistoryPresenter==null) {
                    sHistoryPresenter=new HistoryPresenter();
                }
            }
        }
        return sHistoryPresenter;
    }

    @Override
    public void listHistories() {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> emitter) throws Throwable {
                if (mHistoryDao != null) {
                    mHistoryDao.listHistories();
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void addHistory(Track track) {
        //是否>=100条记录
        if(mCurrentHistories != null && mCurrentHistories.size() >= Constants.MAX_HISTORY_COUNT) {
            isDoDelAsOutOfSize = true;
            this.mCurrentAddTrack = track;
            //先删除最前的一条记录再添加
            delHistory(mCurrentHistories.get(mCurrentHistories.size() - 1));
        } else {
            doAddHistory(track);
        }
    }

    private void doAddHistory(final Track track) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> emitter) throws Throwable {
                if (mHistoryDao != null) {
                    mHistoryDao.addHistory(track);
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void delHistory(Track track) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> emitter) throws Throwable {
                if (mHistoryDao != null) {
                    mHistoryDao.delHistory(track);
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void cleanHistories() {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> emitter) throws Throwable {
                if (mHistoryDao != null) {
                    mHistoryDao.cleanHistory();
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void registerViewCallBack(IHistoryCallback iHistoryCallback) {
        if(!mCallbacks.contains(iHistoryCallback)) {
            mCallbacks.add(iHistoryCallback);
        }
    }

    @Override
    public void unRegisterViewCallBack(IHistoryCallback iHistoryCallback) {
        mCallbacks.remove(iHistoryCallback);
    }

    @Override
    public void onHistoryAdd(boolean isSuccess) {
        listHistories();
    }

    @Override
    public void onHistoryDel(boolean isSuccess) {
        if(isDoDelAsOutOfSize && mCurrentAddTrack != null) {
            isDoDelAsOutOfSize = false;
            //添加当前的数据进到数据库里
            addHistory(mCurrentAddTrack);
        } else {
            listHistories();
        }
    }

    @Override
    public void onHistoryLoaded(List<Track> tracks) {
        this.mCurrentHistories = tracks;
        //通知UI更新数据
        BaseApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                for (IHistoryCallback callback : mCallbacks) {
                    callback.onHistoriesLoaded(tracks);
                }
            }
        });
    }

    @Override
    public void onHistoryClean(boolean isSuccess) {
        listHistories();
    }
}
