package com.example.makabaka;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.makabaka.base.BaseActivity;

public class PlayerActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
    }
}
