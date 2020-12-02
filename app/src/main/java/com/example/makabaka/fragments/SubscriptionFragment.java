package com.example.makabaka.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makabaka.R;
import com.example.makabaka.adapters.AlbumListAdapter;
import com.example.makabaka.base.BaseFragment;
import com.example.makabaka.interfaces.ISubscriptionCallback;
import com.example.makabaka.interfaces.ISubscriptionPresenter;
import com.example.makabaka.presenters.SubscriptionPresenter;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

public class SubscriptionFragment extends BaseFragment implements ISubscriptionCallback {

    private ISubscriptionPresenter mSubscriptionPresenter;
    private RecyclerView mSubListView;
    private AlbumListAdapter mAlbumListAdapter;

    @Override
    protected View onSubViewLoaded(LayoutInflater mLayoutInflater, ViewGroup container) {
        View rootView= mLayoutInflater.inflate(R.layout.fragment_subscription,container,false);
        mSubListView = rootView.findViewById(R.id.sub_list);
        mSubListView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        mAlbumListAdapter = new AlbumListAdapter();
        mSubListView.setAdapter(mAlbumListAdapter);
        mSubscriptionPresenter = SubscriptionPresenter.getInstance();
        mSubscriptionPresenter.registerViewCallBack(this);
        mSubscriptionPresenter.getSubscriptionList();
        return rootView;
    }

    @Override
    public void onAddResult(boolean isSuccess) {

    }

    @Override
    public void onDeleteResult(boolean isSuccess) {

    }

    @Override
    public void onSubscriptionsLoaded(List<Album> albums) {
        //更新UI
        if (mAlbumListAdapter != null) {
            mAlbumListAdapter.setData(albums);
        }

    }
}
