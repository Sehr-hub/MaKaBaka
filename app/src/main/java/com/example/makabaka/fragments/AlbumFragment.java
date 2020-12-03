package com.example.makabaka.fragments;

import android.content.Intent;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makabaka.DetailActivity;
import com.example.makabaka.R;
import com.example.makabaka.adapters.AlbumListAdapter;
import com.example.makabaka.base.BaseFragment;
import com.example.makabaka.interfaces.IRecommendCallBack;
import com.example.makabaka.presenters.AlbumDetailPresenter;
import com.example.makabaka.presenters.RecommendPresenter;
import com.example.makabaka.views.UILoader;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

public class AlbumFragment extends BaseFragment implements IRecommendCallBack, UILoader.onRetryClickListener, AlbumListAdapter.OnAlbumItemClickListener {
    private static final String TAG = "RecommendFragment";
    private View rootView;
    private RecyclerView mRecommendRecyclerView;
    private AlbumListAdapter mAlbumListAdapter;
    private RecommendPresenter mRecommendPresenter;
    private UILoader mUiLoader;

    @Override
    protected View onSubViewLoaded(LayoutInflater layoutInflater, ViewGroup container) {
        mUiLoader = new UILoader(getContext()) {
            @Override
            protected View getSuccessView(ViewGroup cotainer) {
                return createSuccessView(layoutInflater,container) ;
            }

        };
        //获取逻辑层的对象
        mRecommendPresenter = RecommendPresenter.getInstance();
        //设置通知接口的注册
        mRecommendPresenter.registerViewCallBack(this);
        //获取推荐列表
        mRecommendPresenter.getRecommendList();
        if(mUiLoader.getParent() instanceof ViewGroup){    //解绑父类，绑定时不能正确返回
            ((ViewGroup)mUiLoader.getParent()).removeView(mUiLoader);
        }
        mUiLoader.setOnRetryClickListener(this);
        return mUiLoader;
    }

    private View createSuccessView(LayoutInflater layoutInflater,ViewGroup container) {
        rootView= layoutInflater.inflate(R.layout.fragment_recommend,container,false);
        mRecommendRecyclerView = rootView.findViewById(R.id.recommend_list);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置布局为垂直方向
        mRecommendRecyclerView.setLayoutManager(linearLayoutManager);
        mRecommendRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            //重写方法，设置每个项目之间的间距，让其能够显示出灰色背景，显得美观一些
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top= UIUtil.dip2px(view.getContext(),5);
                outRect.bottom= UIUtil.dip2px(view.getContext(),5);
                outRect.left= UIUtil.dip2px(view.getContext(),5);
                outRect.right= UIUtil.dip2px(view.getContext(),5);
            }
        });
        //设置适配器
        mAlbumListAdapter = new AlbumListAdapter();
        mRecommendRecyclerView.setAdapter(mAlbumListAdapter);
        mAlbumListAdapter.setAlbumItemClickListener(this);
        return rootView;
    }


    @Override
    public void onRecommendListLoaded(List<Album> result) {
        //成功获取到推荐内容后，调用这个方法以进行UI更新
        mAlbumListAdapter.setData(result);
        mUiLoader.updateStatus(UILoader.UIStatus.SUCCESS);
    }

    @Override
    public void onNetworkError() {
        mUiLoader.updateStatus(UILoader.UIStatus.NETWORK_ERROR);
    }

    @Override
    public void onLoading() {
        mUiLoader.updateStatus(UILoader.UIStatus.LOADING);
    }

    @Override
    public void onEmpty() {
        mUiLoader.updateStatus(UILoader.UIStatus.EMPTY);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消接口注册
        if(mRecommendPresenter!=null){
            mRecommendPresenter.unRegisterViewCallBack(this);
        }
    }

    @Override
    public void onRetryClick() {
        //用户点击重试重新获取数据
        if(mRecommendPresenter!=null){
            mRecommendPresenter.getRecommendList();
        }
    }

    @Override
    public void onItemClick(int position, Album album) {
        //item被点击后跳转到详情页
        AlbumDetailPresenter.getInstance().setTargetAlbum(album);
        Intent intent=new Intent(getContext(), DetailActivity.class);
        startActivity(intent);

    }
}
