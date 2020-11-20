package com.example.makabaka.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.makabaka.R;
import com.example.makabaka.base.BaseApplication;

public abstract class UILoader extends FrameLayout {

    private View mLoadingView;
    private View mSuccessView;
    private View mNetworkErrorView;
    private View mEmptyView;
    private onRetryClickListener mOnRetryClickListener=null;

    public enum UIStatus{
        LOADING,SUCCESS,NETWORK_ERROR,EMPTY,NONE    //定义枚举类说明状态
    }

    public UIStatus mCurrentStatus=UIStatus.NONE;  //初始无状态

    public UILoader(@NonNull Context context) {
        this(context,null);
    }

    public UILoader(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public UILoader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void updateStatus(UIStatus status){
        mCurrentStatus=status;
        //在主线程上更新UI
        BaseApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                switchUIByCurrentStatus();
            }
        });
    }

    //初始化UI
    private void init() {
        switchUIByCurrentStatus();
    }

    private void switchUIByCurrentStatus() {
        //加载中
        if(mLoadingView==null) {
            mLoadingView = getLoadingView();
            addView(mLoadingView);
        }
        //根据状态设置是否可见
        mLoadingView.setVisibility(mCurrentStatus==UIStatus.LOADING?VISIBLE:GONE);

        //成功
        if(mSuccessView==null) {
            mSuccessView = getSuccessView(this);
            addView(mSuccessView);
        }
        //根据状态设置是否可见
        mSuccessView.setVisibility(mCurrentStatus==UIStatus.SUCCESS?VISIBLE:GONE);

        //网络错误
        if(mNetworkErrorView ==null) {
            mNetworkErrorView = getNetworkErrorView();
            addView(mNetworkErrorView);
        }
        //根据状态设置是否可见
        mNetworkErrorView.setVisibility(mCurrentStatus==UIStatus.NETWORK_ERROR?VISIBLE:GONE);

        //数据为空
        if(mEmptyView ==null) {
            mEmptyView = getNetworkEmptyView();
            addView(mEmptyView);
        }
        //根据状态设置是否可见
        mEmptyView.setVisibility(mCurrentStatus==UIStatus.EMPTY?VISIBLE:GONE);
    }

    protected  View getNetworkEmptyView(){
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_empty_view,this,false);
    }

    protected  View getNetworkErrorView(){
        View netWorkError=LayoutInflater.from(getContext()).inflate(R.layout.fragment_error_view,this,false);
        netWorkError.findViewById(R.id.netWork_error).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //重新获取数据
                if(mOnRetryClickListener!=null){
                    mOnRetryClickListener.onRetryClick();
                }
            }
        });
        return netWorkError;
    }

    protected abstract View getSuccessView(ViewGroup cotainer);

    private View getLoadingView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_loading_view,this,false);
    }

    public void setOnRetryClickListener(onRetryClickListener listener){
        this.mOnRetryClickListener=listener;
    }
    public interface onRetryClickListener{
        void onRetryClick();
    }

}
