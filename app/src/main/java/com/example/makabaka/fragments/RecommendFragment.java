package com.example.makabaka.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makabaka.R;
import com.example.makabaka.adapters.RecommendListAdapter;
import com.example.makabaka.base.BaseFragment;
import com.example.makabaka.utils.Constants;
import com.example.makabaka.utils.LogUtils;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.httputil.httpswitch.OkHttpRequest;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.GussLikeAlbumList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommendFragment extends BaseFragment {
    private static final String TAG = "RecommendFragment";
    private RecyclerView mRecommendRecyclerView;
    private RecommendListAdapter mRecommendListAdapter;

    @Override
    protected View onSubViewLoaded(LayoutInflater mLayoutInflater, ViewGroup container) {
        View rootView= mLayoutInflater.inflate(R.layout.fragment_recommend,container,false);
        mRecommendRecyclerView = rootView.findViewById(R.id.recommend_list);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置布局为垂直方向
        mRecommendRecyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器
        mRecommendListAdapter = new RecommendListAdapter();
        mRecommendRecyclerView.setAdapter(mRecommendListAdapter);
        getRecommendData();
        return rootView;
    }

    /*
    * 获取推荐内容
    * */
    private void getRecommendData() {
        //封装参数
        Map<String, String> map = new HashMap<>();
        map.put(DTransferConstants.LIKE_COUNT, Constants.RECOMMEND_COUNT+"");
        CommonRequest.getGuessLikeAlbum(map, new IDataCallBack<GussLikeAlbumList>() {
            @Override
            public void onSuccess(GussLikeAlbumList gussLikeAlbumList) {
                //数据获取成功
                if(gussLikeAlbumList!=null){
                    List<Album> albumList=gussLikeAlbumList.getAlbumList();
                    updateRecommendUI(albumList);
                }
            }

            @Override
            public void onError(int i, String s) {
                //获取出错
                LogUtils.d(TAG,"error-->"+i);
                LogUtils.d(TAG,"errorMessage-->"+s);
            }
        });
    }

    private void updateRecommendUI(List<Album> albumList) {
        mRecommendListAdapter.setData(albumList);
    }
}
