package com.drcosu.ndileber.dileberui.emptypage;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.drcosu.ndileber.dileberui.R;
import com.drcosu.ndileber.tools.HNetwork;
import com.drcosu.ndileber.tools.UUi;
import com.drcosu.ndileber.tools.android.OnOnceClickListener;
import com.drcosu.ndileber.view.DileberBaseView;



/**
 * 使用方法
 emptyPage.setOnRefreshDelegate(new EmptyPageLayout.OnRefreshDelegate() {
@Override
public void onRefresh() {

}
}); *

 emptyPage.setEmptyType(empty);
 * Created by 王二蛋 on 16/5/6.
 */
public class EmptyPageLayout extends DileberBaseView {

    private Builder.Empty networkError = new Builder().build("Oops，遇到问题了，刷新试试", R.mipmap.empty_page_fail, true).create();
    private Builder.Empty emptyData = new Builder().build("暂时没有任何数据，去别处看看吧", R.mipmap.empty_page_tip, false).create();

    public final class Builder{
        private String msg = "暂时没有任何数据，去别处看看吧";
        private int anchor = R.mipmap.empty_page_tip;
        private boolean refreshAble = false;

        public Builder build(String msg,int anchor,boolean refreshAble) {
            this.msg = msg;
            this.anchor = anchor;
            this.refreshAble = refreshAble;
            return this;
        }

        public Builder msg(String msg) {
            this.msg = msg;
            return this;
        }

        public Builder anchor(int anchor) {
            this.anchor = anchor;
            return this;
        }

        public Builder refreshAble(boolean refreshAble) {
            this.refreshAble = refreshAble;
            return this;
        }

        public Empty create(){
            return new Empty(msg,anchor,refreshAble);
        }

        class Empty{
            private String msg ;
            private int anchor ;
            private boolean refreshAble ;

            public Empty(String msg, int anchor, boolean refreshAble) {
                this.msg = msg;
                this.anchor = anchor;
                this.refreshAble = refreshAble;
            }

            public String getMsg() {
                return msg;
            }

            public int getAnchor() {
                return anchor;
            }

            public boolean isRefreshAble() {
                return refreshAble;
            }
        }
    }

    Context mContext;

    public EmptyPageLayout(Context context) {
        this(context, null);
    }

    public EmptyPageLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    TextView tipMsg;
    ImageView anchor;
    TextView refresh;
    public EmptyPageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.widget_empty_page, this, true);
        tipMsg = findView(R.id.tipMsg);
        anchor = findView(R.id.anchor);
        refresh = findView(R.id.refresh);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EmptyPageLayout);
        if (typedArray != null) {
            if (typedArray.getInt(R.styleable.EmptyPageLayout_eplType, -1) != -1) {
                switch (typedArray.getInt(R.styleable.EmptyPageLayout_eplType, -1)) {
                    case 0:
                        setEmptyType(networkError);
                        break;
                    case 1:
                        setEmptyType(emptyData);
                        break;
                }
                typedArray.recycle();
                return;
            }
            Drawable anchorDrawable = typedArray.getDrawable(R.styleable.EmptyPageLayout_anchor);
            if (anchorDrawable != null) {
                anchor.setImageDrawable(anchorDrawable);
            } else {
                anchor.setVisibility(View.GONE);
            }
            String tipMsgStr = typedArray.getString(R.styleable.EmptyPageLayout_tipMsg);
            if (TextUtils.isEmpty(tipMsgStr)) {
                tipMsgStr = "暂无";
            }
            if (typedArray.getBoolean(R.styleable.EmptyPageLayout_refreshEnable, false)) {
                refresh.setVisibility(View.VISIBLE);
            } else {
                refresh.setVisibility(View.GONE);
            }
            tipMsg.setText(tipMsgStr);
            typedArray.recycle();
        }
    }

    public void setEmptyType(Builder.Empty empty) {
        tipMsg.setText(empty.getMsg());
        anchor.setVisibility(View.VISIBLE);
        anchor.setImageResource(empty.anchor);
        refresh.setVisibility(empty.isRefreshAble() ? VISIBLE : GONE);
    }

    public void setEmptyType(Builder.Empty empty, String tipMsg) {
        this.tipMsg.setText(tipMsg);
        anchor.setVisibility(View.VISIBLE);
        anchor.setImageResource(empty.anchor);
        refresh.setVisibility(empty.isRefreshAble() ? VISIBLE : GONE);
    }

    public void setEmptyType(Builder.Empty empty, @DrawableRes int image) {
        this.tipMsg.setText(empty.getMsg());
        anchor.setVisibility(View.VISIBLE);
        anchor.setImageResource(image);
        refresh.setVisibility(empty.isRefreshAble() ? VISIBLE : GONE);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        refresh.setOnClickListener(new OnOnceClickListener() {
            @Override
            public void onOnceClick(View v) {
                if (onRefreshDelegate != null) {
                    if (!HNetwork.checkNetwork()) {
                        UUi.toast((Activity) mContext,"当前网络不可用，请检查网络设置", Toast.LENGTH_SHORT);
                        return;
                    }
                    onRefreshDelegate.onRefresh();
                }
            }
        });
    }

    public interface OnRefreshDelegate {

        void onRefresh();
    }

    private OnRefreshDelegate onRefreshDelegate;

    public void setOnRefreshDelegate(OnRefreshDelegate onRefreshDelegate) {
        this.onRefreshDelegate = onRefreshDelegate;
    }
}
