package com.drcosu.ndileber.dileberui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.drcosu.ndileber.app.ActivityManager;
import com.drcosu.ndileber.app.BaseConfiger;
import com.drcosu.ndileber.app.FrameContants;
import com.drcosu.ndileber.dileberui.R;
import com.drcosu.ndileber.mvp.acivity.BaseActivity;
import com.drcosu.ndileber.tools.HPref;
import com.drcosu.ndileber.tools.UUi;
import com.drcosu.ndileber.utils.LauncherManager;
import com.drcosu.ndileber.utils.UToolBar;

/**
 * Created by congtaowang 2016/10/29.
 */

public class AppDebugModeActivity extends BaseActivity implements View.OnClickListener{

    static final String TAG = AppDebugModeActivity.class.getSimpleName();

    private static final String NAME = "name";
    private static final String ICON = "icon";


    public static void start(Context context, String name, @DrawableRes int icon){
        if(BaseConfiger.BUG_STATIC){
            Intent it = new Intent(context,AppDebugModeActivity.class);
            it.putExtra(NAME,name);
            it.putExtra(ICON,icon);
            LauncherManager.launcher.launch(context,it);
        }
    }

    @Override
    protected void startView(Bundle savedInstanceState) {

    }

    @Override
    public int layoutViewId() {
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
        setOnClickListener(this,R.id.test,R.id.onLine);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.test) {
            HPref.getInstance().put(FrameContants.SYSTEM_PREFERANCE,FrameContants.SYSTEM_PREFERANCE_DEBUG_TYPE,FrameContants.SYSTEM_PREFERANCE_DEBUG_TEST);
        } else if (i == R.id.onLine) {
            HPref.getInstance().put(FrameContants.SYSTEM_PREFERANCE,FrameContants.SYSTEM_PREFERANCE_DEBUG_TYPE,FrameContants.SYSTEM_PREFERANCE_DEBUG_ONLINE);
        }
        UUi.toast(this,"配置完成", Toast.LENGTH_SHORT);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityManager.getInstance().finishAllActivity();
                System.exit(1);
            }
        }, 1000);
    }
}
