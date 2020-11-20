package com.example.makabaka;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.RequiresApi;

import com.example.makabaka.base.BaseActivity;
import com.example.makabaka.interfaces.IAlbumDetailPresenter;
import com.example.makabaka.interfaces.IAlbumDetailViewCallback;
import com.example.makabaka.presenters.AlbumDetailPresenter;
import com.example.makabaka.views.RoundRectImageView;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.List;

public class DetailActivity extends BaseActivity implements IAlbumDetailViewCallback {

    private ImageView mLargeCover;
    private RoundRectImageView mSmallCover;
    private TextView mAlbumTitle;
    private TextView mAlbumAuthor;
    private AlbumDetailPresenter mAlbumDetailPresenter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);//设置状态栏透明
        // getWindow().setStatusBarColor(Color.TRANSPARENT);
        initView();
        mAlbumDetailPresenter = AlbumDetailPresenter.getInstance();
        mAlbumDetailPresenter.registerViewCallback(this);
    }

    private void initView() {
        mLargeCover = this.findViewById(R.id.iv_large_cover);
        mSmallCover = this.findViewById(R.id.iv_small_cover);
        mAlbumTitle = this.findViewById(R.id.tv_album_title);
        mAlbumAuthor = this.findViewById(R.id.tv_album_author);
    }

    @Override
    public void onDetailListLoaded(List<Track> tracks) {

    }

    @Override
    public void onAlbumLoaded(Album album) {
        if(mAlbumTitle!=null){
            mAlbumTitle.setText(album.getAlbumTitle());
        }
        if(mAlbumAuthor!=null){
            mAlbumAuthor.setText(album.getAnnouncer().getNickname());
        }
        if(mLargeCover!=null){
            Picasso.with(this).load(album.getCoverUrlLarge()).into(mLargeCover);
        }
        if(mSmallCover!=null){
            Picasso.with(this).load(album.getCoverUrlMiddle()).into(mSmallCover);
        }

    }
}
