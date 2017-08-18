package com.drcosu.ndileber.dileberui.activity;

import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;

import com.xiaohe.baonahao_school.R;
import com.xiaohe.baonahao_school.api2.Api;
import com.xiaohe.baonahao_school.ui.base.BaseActivity;
import com.xiaohe.baonahao_school.ui.base.BasePresenterDecorator;
import com.xiaohe.baonahao_school.ui.base.IBaseView;
import com.xiaohe.baonahao_school.ui.mine.config.ActionRecordManager;
import com.xiaohe.baonahao_school.utils.Activities;
import com.xiaohe.baonahao_school.utils.CommonUtils;
import com.xiaohe.baonahao_school.utils.Sps;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by congtaowang 2016/10/29.
 */

public class AppDebugModeActivity extends BaseActivity<IBaseView, BasePresenterDecorator<IBaseView>> {

    static final String TAG = AppDebugModeActivity.class.getSimpleName();

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.rOffLine)
    RadioButton rOffLine;
    @Bind(R.id.rTest)
    RadioButton rTest;
    @Bind(R.id.rEmulation)
    RadioButton rEmulation;
    @Bind(R.id.rOnLine)
    RadioButton rOnLine;
    @Bind(R.id.appBarLayout)
    AppBarLayout appBarLayout;

    @Override
    protected BasePresenterDecorator createPresenterInstance() {
        return new BasePresenterDecorator();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_debug_mode;
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack(v);
            }
        });
        switchStatusBarToDark(R.color.themeColor);
        disableAll();
        enableCurrentMode();
        final String currentVersion = "爱校 " + CommonUtils.getAppVersionName(getActivity());
        toolbar.setTitle(currentVersion);
    }

    String env;

    private void enableCurrentMode() {
        env = Sps.Actions.debugMode();
        Api.ApiStatus status = Api.ApiStatus.valueOf(env);
        switch (status) {
            case OffLine:
                rOffLine.setEnabled(true);
                break;
            case Test:
                rTest.setEnabled(true);
                break;
            case Emulation:
                rEmulation.setEnabled(true);
                break;
            case OnLine:
                rOnLine.setEnabled(true);
                break;
        }
    }

    @OnClick({
            R.id.rOffLine, R.id.offline,
            R.id.rTest, R.id.test,
            R.id.rEmulation, R.id.emulation,
            R.id.rOnLine, R.id.online,
            R.id.sure
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rOffLine:
            case R.id.offline:
            case R.id.rTest:
            case R.id.test:
            case R.id.rEmulation:
            case R.id.emulation:
            case R.id.rOnLine:
            case R.id.online:
                disableAll();
                enable(view);
                break;
            case R.id.sure:
                writeEnvAndExit();
                break;
        }
    }

    private void writeEnvAndExit() {
        if (env.equalsIgnoreCase(selectedTag)) {
            return;
        }
        if (!TextUtils.isEmpty(selectedTag)) {
            Sps.Actions.recordDebugMode(selectedTag);
            ActionRecordManager.clearConfig();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Activities.clearAll();
                    System.exit(1);
                }
            }, 1000);
        }
    }

    private void disableAll() {
        rOffLine.setEnabled(false);
        rTest.setEnabled(false);
        rEmulation.setEnabled(false);
        rOnLine.setEnabled(false);
    }

    private String selectedTag = null;

    private void enable(View view) {
        if (view instanceof CardView) {
            View childView = ((CardView) view).getChildAt(1);
            childView.setEnabled(true);
            selectedTag = ((String) childView.getTag());
        } else if (view instanceof RadioButton) {
            view.setEnabled(true);
            selectedTag = ((String) view.getTag());
        }
    }
}
