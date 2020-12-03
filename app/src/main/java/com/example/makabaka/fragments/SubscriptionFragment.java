package com.example.makabaka.fragments;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makabaka.DetailActivity;
import com.example.makabaka.R;
import com.example.makabaka.adapters.AlbumListAdapter;
import com.example.makabaka.base.BaseApplication;
import com.example.makabaka.base.BaseFragment;
import com.example.makabaka.interfaces.ISubscriptionCallback;
import com.example.makabaka.interfaces.ISubscriptionPresenter;
import com.example.makabaka.presenters.AlbumDetailPresenter;
import com.example.makabaka.presenters.SubscriptionPresenter;
import com.example.makabaka.views.ConfirmDialog;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.List;

public class SubscriptionFragment extends BaseFragment implements ISubscriptionCallback, AlbumListAdapter.OnAlbumItemClickListener, AlbumListAdapter.OnAlbumItemLongClickListener, ConfirmDialog.OnDialogActionClickListener {

    private ISubscriptionPresenter mSubscriptionPresenter;
    private RecyclerView mSubListView;
    private AlbumListAdapter mAlbumListAdapter;
    private Album mCurrentClickAlbum=null;

    @Override
    protected View onSubViewLoaded(LayoutInflater mLayoutInflater, ViewGroup container) {
        View rootView= mLayoutInflater.inflate(R.layout.fragment_subscription,container,false);
        mSubListView = rootView.findViewById(R.id.sub_list);
        mSubListView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        mAlbumListAdapter = new AlbumListAdapter();
        mAlbumListAdapter.setAlbumItemClickListener(this);
        mAlbumListAdapter.setOnAlbumItemLongClickListener(this);
        mSubListView.setAdapter(mAlbumListAdapter);
        mSubscriptionPresenter = SubscriptionPresenter.getInstance();
        mSubscriptionPresenter.registerViewCallBack(this);
        mSubscriptionPresenter.getSubscriptionList();
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消接口注册
        if(mSubscriptionPresenter!=null){
            mSubscriptionPresenter.unRegisterViewCallBack(this);
        }
        mAlbumListAdapter.setAlbumItemClickListener(null);
    }

    @Override
    public void onAddResult(boolean isSuccess) {

    }

    @Override
    public void onDeleteResult(boolean isSuccess) {
        //给出取消订阅的提示
        String tips=isSuccess?"取消订阅成功":"取消订阅失败";
        Toast.makeText(BaseApplication.getAppContext(),tips,Toast.LENGTH_SHORT);
    }

    @Override
    public void onSubscriptionsLoaded(List<Album> albums) {
        //更新UI
        if (mAlbumListAdapter != null) {
            mAlbumListAdapter.setData(albums);
        }
    }

    @Override
    public void onItemClick(int position, Album album) {
        //item被点击后跳转到详情页
        AlbumDetailPresenter.getInstance().setTargetAlbum(album);
        Intent intent=new Intent(getContext(), DetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(Album album) {
        this.mCurrentClickAlbum=album;
        //订阅的item被长按了
        ConfirmDialog confirmDialog=new ConfirmDialog(getActivity());
        confirmDialog.setOnDialogActionClickListener(this);
        confirmDialog.show();
    }

    @Override
    public void onCancelSubClick() {
        //取消订阅
        if (mCurrentClickAlbum != null&&mSubscriptionPresenter!=null) {
            mSubscriptionPresenter.deleteSubscription(mCurrentClickAlbum);
        }
    }

    @Override
    public void onGiveUpClick() {
        //取消按钮无操作
    }
}
