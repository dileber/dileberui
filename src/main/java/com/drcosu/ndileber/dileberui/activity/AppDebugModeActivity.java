package com.drcosu.ndileber.dileberui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.drcosu.ndileber.dileberui.R;
import com.drcosu.ndileber.mvp.acivity.BaseActivity;
import com.drcosu.ndileber.utils.UToolBar;

/**
 * Created by congtaowang 2016/10/29.
 */

public class AppDebugModeActivity extends BaseActivity {

    static final String TAG = AppDebugModeActivity.class.getSimpleName();

    private static final String NAME = "name";
    private static final String ICON = "icon";


    public static void start(Context context, String name, @DrawableRes int icon){
        Intent it = new Intent();
        it.putExtra(NAME,name);
        it.putExtra(ICON,icon);
        context.startActivity(it);
    }

    @Override
    protected void startView(Bundle savedInstanceState) {

    }

    @Override
    protected int layoutViewId() {
        return R.layout.activity_debug_mode;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        Intent it = getIntent();
        if(it==null){
            throw new NullPointerException();
        }
        UToolBar toolBar = new UToolBar();
        setToolBar(R.id.toolbar,toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//设置工具栏标题
        CollapsingToolbarLayout collapsingToolbar = findView(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(it.getStringExtra(NAME));
        ImageView debug_icon1 = findView(R.id.debug_icon1);
        ImageView debug_icon2 = findView(R.id.debug_icon2);
        debug_icon1.setImageResource(it.getIntExtra(ICON,0));
        debug_icon2.setImageResource(it.getIntExtra(ICON,0));
    }
}
