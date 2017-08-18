package com.drcosu.ndileber.dileberui.emptypage;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.drcosu.ndileber.dileberui.R;
import com.drcosu.ndileber.tools.HNetwork;
import com.drcosu.ndileber.tools.android.OnOnceClickListener;


/**
 * Created by 王二蛋 on 16/5/6.
 */
public class EmptyPageLayout extends LinearLayout {

    @Bind(R.id.tipMsg)
    TextView tipMsg;
    @Bind(R.id.anchor)
    ImageView anchor;
    @Bind(R.id.refresh)
    TextView refresh;

    public enum Empty {

        NetworkError("Oops，遇到问题了，刷新试试", R.mipmap.oppos, true),
        EmptyData("暂时没有任何数据，去别处看看吧", R.mipmap.no_data, false),
        EmptyModuleLibrary("所有功能均已添加到首页", R.mipmap.gongneng, false),
        EmptyMsgCenter("暂时没有收到消息，去别处看看吧", R.mipmap.no_message, false);

        private String msg;
        private int anchor;
        private boolean refreshAble;

        Empty(String msg, int anchor, boolean refreshAble) {
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

    public EmptyPageLayout(Context context) {
        this(context, null);
    }

    public EmptyPageLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyPageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.widget_empty_page, this, true);
        ButterKnife.bind(this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EmptyPageLayout);
        if (typedArray != null) {
            if (typedArray.getInt(R.styleable.EmptyPageLayout_eplType, -1) != -1) {
                switch (typedArray.getInt(R.styleable.EmptyPageLayout_eplType, -1)) {
                    case 0:
                        setEmptyType(Empty.NetworkError);
                        break;
                    case 1:
                        setEmptyType(Empty.EmptyModuleLibrary);
                        break;
                    case 2:
                        setEmptyType(Empty.EmptyData);
                        break;
                    case 3:
                        setEmptyType(Empty.EmptyMsgCenter);
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

    public void setEmptyType(Empty empty) {
        tipMsg.setText(empty.getMsg());
        anchor.setVisibility(View.VISIBLE);
        anchor.setImageResource(empty.anchor);
        refresh.setVisibility(empty.isRefreshAble() ? VISIBLE : GONE);
    }

    public void setEmptyType(Empty empty, String tipMsg) {
        this.tipMsg.setText(tipMsg);
        anchor.setVisibility(View.VISIBLE);
        anchor.setImageResource(empty.anchor);
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
                        TipToast.shortTip("当前网络不可用，请检查网络设置");
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
