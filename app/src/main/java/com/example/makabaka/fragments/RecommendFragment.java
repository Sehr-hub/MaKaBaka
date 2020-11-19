package com.example.makabaka.fragments;

import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makabaka.R;
import com.example.makabaka.adapters.RecommendListAdapter;
import com.example.makabaka.base.BaseFragment;
import com.example.makabaka.interfaces.IRecommendCallBack;
import com.example.makabaka.presenters.RecommendPresenter;
import com.example.makabaka.utils.Constants;
import com.example.makabaka.utils.LogUtils;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.httputil.httpswitch.OkHttpRequest;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendFragment extends BaseFragment implements IRecommendCallBack {
    private static final String TAG = "RecommendFragment";
    private RecyclerView mRecommendRecyclerView;
    private RecommendListAdapter mRecommendListAdapter;
    private RecommendPresenter mRecommendPresenter;

    @Override
    protected View onSubViewLoaded(LayoutInflater mLayoutInflater, ViewGroup container) {
        View rootView= mLayoutInflater.inflate(R.layout.fragment_recommend,container,false);
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
        mRecommendListAdapter = new RecommendListAdapter();
        mRecommendRecyclerView.setAdapter(mRecommendListAdapter);
        //获取逻辑层的对象
        mRecommendPresenter = RecommendPresenter.getInstance();
        //设置通知接口的注册
        mRecommendPresenter.registerViewCallBack(this);
        //获取推荐列表
        mRecommendPresenter.getRecommendList();
        return rootView;
    }


    @Override
    public void onRecommendListLoaded(List<Album> result) {
        //成功获取到推荐内容后，调用这个方法以进行UI更新
        mRecommendListAdapter.setData(result);
    }

    @Override
    public void onLoaderMore(List<Album> result) {

    }

    @Override
    public void onRefreshMore(List<Album> result) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消接口注册
        if(mRecommendPresenter!=null){
            mRecommendPresenter.unRegisterViewCallBack(this);
        }
    }
}
