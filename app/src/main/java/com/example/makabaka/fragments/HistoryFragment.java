package com.example.makabaka.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.makabaka.R;
import com.example.makabaka.base.BaseFragment;

public class HistoryFragment extends BaseFragment {
    @Override
    protected View onSubViewLoaded(LayoutInflater mLayoutInflater, ViewGroup container) {
        View rootView= mLayoutInflater.inflate(R.layout.fragment_history,container,false);
        return rootView;
    }
}
