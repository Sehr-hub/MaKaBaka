package com.example.makabaka.fragments;

import android.content.Intent;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makabaka.PlayerActivity;
import com.example.makabaka.R;
import com.example.makabaka.adapters.TrackListAdapter;
import com.example.makabaka.base.BaseApplication;
import com.example.makabaka.base.BaseFragment;
import com.example.makabaka.interfaces.IHistoryCallback;
import com.example.makabaka.presenters.HistoryPresenter;
import com.example.makabaka.presenters.PlayerPresenter;
import com.example.makabaka.views.ConfirmCheckboxDialog;
import com.example.makabaka.views.UILoader;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

public class HistoryFragment extends BaseFragment implements IHistoryCallback, TrackListAdapter.ItemClickListener, TrackListAdapter.ItemLongClickListener, ConfirmCheckboxDialog.OnDialogActionClickListener {

    private UILoader mUiLoader;
    private TrackListAdapter mTrackListAdapter;
    private HistoryPresenter mHistoryPresenter;
    private Track mCurrentClickHistoryItem =null;


    @Override
    protected View onSubViewLoaded(LayoutInflater mLayoutInflater, ViewGroup container) {
        FrameLayout rootView=(FrameLayout) mLayoutInflater.inflate(R.layout.fragment_history,container,false);
        if (mUiLoader==null) {
            mUiLoader = new UILoader(BaseApplication.getAppContext()) {
                @Override
                protected View getSuccessView(ViewGroup cotainer) {
                    return createSuccessView(container);
                }
            };
        }else {
            if (mUiLoader.getParent() instanceof ViewGroup) {
                ((ViewGroup) mUiLoader.getParent()).removeView(mUiLoader);
            }
        }
        mHistoryPresenter = HistoryPresenter.getHistoryPresenter();
        mHistoryPresenter.registerViewCallBack(this);
        mUiLoader.updateStatus(UILoader.UIStatus.LOADING);
        mHistoryPresenter.listHistories();
        rootView.addView(mUiLoader);
        return rootView;
    }

    private View createSuccessView(ViewGroup container) {
        View successView = LayoutInflater.from(container.getContext()).inflate(R.layout.item_history,container,false);
        TwinklingRefreshLayout refreshLayout=successView.findViewById(R.id.over_scroll_view);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setEnableOverScroll(true);
        RecyclerView historyList=successView.findViewById(R.id.history_list);
        historyList.setLayoutManager(new LinearLayoutManager(container.getContext()));
        //设置item上下间距
        historyList.addItemDecoration(new RecyclerView.ItemDecoration() {
            //重写方法，设置每个项目之间的间距，让其能够显示出灰色背景，显得美观一些
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top= UIUtil.dip2px(view.getContext(),3);
                outRect.bottom= UIUtil.dip2px(view.getContext(),3);
                outRect.left= UIUtil.dip2px(view.getContext(),5);
                outRect.right= UIUtil.dip2px(view.getContext(),5);
            }
        });
        //设置适配器
        mTrackListAdapter = new TrackListAdapter();
        mTrackListAdapter.setItemClickListener(this);
        mTrackListAdapter.setItemLongClickListener(this);
        historyList.setAdapter(mTrackListAdapter);
        return successView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHistoryPresenter != null) {
            mHistoryPresenter.unRegisterViewCallBack(this);
        }
    }

    @Override
    public void onHistoriesLoaded(List<Track> tracks) {
        //更新数据
        mTrackListAdapter.setData(tracks);
        mUiLoader.updateStatus(UILoader.UIStatus.SUCCESS);
    }

    @Override
    public void onItemClick(List<Track> detailData, int position) {
        //设置播放器数据
        PlayerPresenter playerPresenter=PlayerPresenter.getPlayerPresenter();
        playerPresenter.setPlayerList(detailData,position);
        //点击转跳播放页实现
        Intent intent=new Intent(getActivity(), PlayerActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(Track track) {
        this.mCurrentClickHistoryItem =track;
        //弹窗删除历史
        ConfirmCheckboxDialog dialog=new ConfirmCheckboxDialog(getActivity());
        dialog.setOnDialogActionClickListener(this);
        dialog.show();
    }

    @Override
    public void onCancelSubClick() {
        if (mCurrentClickHistoryItem != null&&mHistoryPresenter!=null) {
            mHistoryPresenter.delHistory(mCurrentClickHistoryItem);
        }
    }

    @Override
    public void onGiveUpClick() {

    }
}
