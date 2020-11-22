package com.example.makabaka;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makabaka.adapters.DetailListAdapter;
import com.example.makabaka.base.BaseActivity;
import com.example.makabaka.interfaces.IAlbumDetailViewCallback;
import com.example.makabaka.presenters.AlbumDetailPresenter;
import com.example.makabaka.presenters.PlayerPresenter;
import com.example.makabaka.utils.ImageBlur;
import com.example.makabaka.utils.LogUtils;
import com.example.makabaka.views.RoundRectImageView;
import com.example.makabaka.views.UILoader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

public class DetailActivity extends BaseActivity implements IAlbumDetailViewCallback, DetailListAdapter.ItemClickListener {

    private static final String TAG = "DetailActivity";
    private ImageView mLargeCover;
    private RoundRectImageView mSmallCover;
    private TextView mAlbumTitle;
    private TextView mAlbumAuthor;
    private AlbumDetailPresenter mAlbumDetailPresenter;
    private int mCurrentPage=1;
    private RecyclerView mDetailList;
    private DetailListAdapter mDetailListAdapter;
    private FrameLayout mDetailListContainer;
    private UILoader mUiLoader;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);//设置状态栏透明
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        initView();
        mAlbumDetailPresenter = AlbumDetailPresenter.getInstance();
        mAlbumDetailPresenter.registerViewCallBack(this);

    }

    private void initView() {
        mDetailListContainer = this.findViewById(R.id.detail_list_container);
        if(mUiLoader==null) {
            mUiLoader = new UILoader(this) {
                @Override
                protected View getSuccessView(ViewGroup container) {
                    return createSuccessView(container);
                }
            };
            mDetailListContainer.removeAllViews();
            mDetailListContainer.addView(mUiLoader);
        }
        mLargeCover = this.findViewById(R.id.iv_large_cover);
        mSmallCover = this.findViewById(R.id.iv_small_cover);
        mAlbumTitle = this.findViewById(R.id.tv_album_title);
        mAlbumAuthor = this.findViewById(R.id.tv_album_author);

    }

    private View createSuccessView(ViewGroup container) {
        View detailListView=LayoutInflater.from(this).inflate(R.layout.item_detail_list,container,false);
        mDetailList = detailListView.findViewById(R.id.album_detail_list);
        //设置布局管理器
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        mDetailList.setLayoutManager(layoutManager);
        //设置适配器
        mDetailListAdapter = new DetailListAdapter();
        mDetailList.setAdapter(mDetailListAdapter);
        //设置item上下间距
        mDetailList.addItemDecoration(new RecyclerView.ItemDecoration() {
            //重写方法，设置每个项目之间的间距，让其能够显示出灰色背景，显得美观一些
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top= UIUtil.dip2px(view.getContext(),3);
                outRect.bottom= UIUtil.dip2px(view.getContext(),3);
                outRect.left= UIUtil.dip2px(view.getContext(),5);
                outRect.right= UIUtil.dip2px(view.getContext(),5);
            }
        });
        mDetailListAdapter.setItemClickListener(this);
        return detailListView;
    }

    @Override
    public void onDetailListLoaded(List<Track> tracks) {
        //根据返回数据结果进行判断，根据结果控制UI显示
        if(tracks==null||tracks.size()==0){
            if(mUiLoader!=null){
                mUiLoader.updateStatus(UILoader.UIStatus.EMPTY);
            }
        }
        if(mUiLoader!=null){
            mUiLoader.updateStatus(UILoader.UIStatus.SUCCESS);
        }
        //设置或更新UI数据
        mDetailListAdapter.setData(tracks);
    }



    /*
    * 加载详情专辑内容
    * */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onAlbumLoaded(Album album) {
        if(mAlbumTitle!=null){
            mAlbumTitle.setText(album.getAlbumTitle());
        }
        if(mAlbumAuthor!=null){
            mAlbumAuthor.setText(album.getAnnouncer().getNickname());
        }
        if(mLargeCover!=null){
            Picasso.with(this).load(album.getCoverUrlLarge()).into(mLargeCover, new Callback() {
                @Override
                public void onSuccess() {
                    Drawable drawable = mLargeCover.getDrawable();
                    if (drawable != null) {
                        ImageBlur.makeBlur(mLargeCover,DetailActivity.this);
                    }
                }

                @Override
                public void onError() {
                    LogUtils.d(TAG,"onError");
                }
            });
        }
        if(mSmallCover!=null){
            Picasso.with(this).load(album.getCoverUrlMiddle()).into(mSmallCover);
        }
        //获取专辑详情内容
        mAlbumDetailPresenter.getAlbumDetail(album.getId(),mCurrentPage);
        //获取到数据后显示Loading状态
        if(mUiLoader!=null){
            mUiLoader.updateStatus(UILoader.UIStatus.LOADING);
        }

    }

    @Override
    public void onNetworkError(int errorCode, String errorMsg) {
            mUiLoader.updateStatus(UILoader.UIStatus.NETWORK_ERROR);
    }

    @Override
    public void onItemClick(List<Track> detailData, int position) {
        //设置播放器数据
        PlayerPresenter playerPresenter=PlayerPresenter.getPlayerPresenter();
        playerPresenter.setPlayerList(detailData,position);
        //点击转跳播放页实现
        Intent intent=new Intent(this,PlayerActivity.class);
        startActivity(intent);
    }
}
