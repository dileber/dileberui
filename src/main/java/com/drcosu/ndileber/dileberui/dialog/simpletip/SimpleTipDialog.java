package com.drcosu.ndileber.dileberui.dialog.simpletip;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.drcosu.ndileber.dileberui.R;


/**
 * Created by congtaowang on 16/9/2.
 */
public class SimpleTipDialog extends Dialog {

    private Delegate delegate;
    private boolean backPressDismissEnable = true;

    public SimpleTipDialog(Context context, Delegate delegate) {
        this(context, delegate,R.layout.dialog_simple_tip);
    }

    public SimpleTipDialog(Context context, Delegate delegate, int layout){
        super(context, R.style.SimpleTipDialog);
        if (delegate == null) {
            delegate = new SimpleDelegate();
        }
        this.delegate = delegate;
        setContentView(layout);
        WindowManager.LayoutParams lp = getWindow().getAttributes();

        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        lp.width = dm.widthPixels * 3 / 4;
        getWindow().setAttributes(lp);
        findViewById(R.id.left).setOnClickListener(clickListener);
        findViewById(R.id.right).setOnClickListener(clickListener);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (SimpleTipDialog.this.delegate != null) {
                    SimpleTipDialog.this.delegate.onDismiss(dialog);
                }
            }
        });

    }

    public void setBackPressDismissEnable(boolean backPressDismissEnable) {
        this.backPressDismissEnable = backPressDismissEnable;
    }

    public void show(String tipMsg) {
        ((TextView) findViewById(R.id.tipMsg)).setText(tipMsg);
        show();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.left) {
                delegate.onLeftClick(SimpleTipDialog.this);
            }
            if (v.getId() == R.id.right) {
                delegate.onRightClick(SimpleTipDialog.this);
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!backPressDismissEnable && keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public interface Delegate extends OnDismissListener {

        void onLeftClick(DialogInterface dialog);

        void onRightClick(DialogInterface dialog);

    }

    public abstract static class SimpleCanceledDelegate implements Delegate {
        @Override
        public void onDismiss(DialogInterface dialog) {

        }
    }

    public static class SimpleDelegate implements Delegate {
        @Override
        public void onLeftClick(DialogInterface dialog) {
            dialog.dismiss();
        }

        @Override
        public void onRightClick(DialogInterface dialog) {
            dialog.dismiss();
        }

        @Override
        public void onDismiss(DialogInterface dialog) {

        }
    }

    public static abstract class SimpleLeftClickDelegate implements Delegate {

        @Override
        public void onDismiss(DialogInterface dialog) {

        }

        @Override
        public void onRightClick(DialogInterface dialog) {
            dialog.dismiss();
        }
    }

    public static abstract class SimpleRightClickDelegate implements Delegate {

        @Override
        public void onDismiss(DialogInterface dialog) {

        }

        @Override
        public void onLeftClick(DialogInterface dialog) {
            dialog.dismiss();
        }
    }

    public static class EmptyClickDelegate implements Delegate {
        @Override
        public void onLeftClick(DialogInterface dialog) {
            dialog.dismiss();
        }

        @Override
        public void onRightClick(DialogInterface dialog) {
            dialog.dismiss();
        }

        @Override
        public void onDismiss(DialogInterface dialog) {

        }
    }

    public static final class Builder {

        private Context context;
        private String title = "提示";
        private String tipMsg = "提示内容";
        private String leftButtonText = "取消";
        private String rightButtonText = "确定";
        private Delegate delegate = new SimpleDelegate();
        private boolean canceledOnTouchOutSide = true;
        private boolean backPressDismissEnable = true;
        private boolean singleActionButton = false;
        //有点击线
        private int msgAutoLink = -1;
        private int layout = -1;

        public Builder attach(@NonNull Context context) {
            this.context = context;
            return this;
        }

        public Builder layout(@LayoutRes int layout) {
            this.layout = layout;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder tipMsg(String tipMsg) {
            this.tipMsg = tipMsg;
            return this;
        }

        public Builder leftButtonText(String leftButtonText) {
            this.leftButtonText = leftButtonText;
            return this;
        }

        public Builder rightButtonText(String rightButtonText) {
            this.rightButtonText = rightButtonText;
            return this;
        }

        public Builder delegate(Delegate delegate) {
            this.delegate = delegate;
            return this;
        }

        public Builder canceledOnTouchOutSide(boolean canceledOnTouchOutSide) {
            this.canceledOnTouchOutSide = canceledOnTouchOutSide;
            return this;
        }

        public Builder backPressDismissEnable(boolean backPressDismissEnable) {
            this.backPressDismissEnable = backPressDismissEnable;
            return this;
        }

        /**
         * Right button will be enable.
         *
         * @param singleActionButton
         * @return
         */
        public Builder singleActionButton(boolean singleActionButton) {
            this.singleActionButton = singleActionButton;
            return this;
        }

        public Builder msgAutoLink(int msgAutoLink) {
            this.msgAutoLink = msgAutoLink;
            return this;
        }

        public SimpleTipDialog create() {
            SimpleTipDialog dialog;
            if(layout!=-1){
                dialog = new SimpleTipDialog(context, delegate,layout);
            }else{
                dialog = new SimpleTipDialog(context, delegate);
            }
            TextView left = ((TextView) dialog.findViewById(R.id.left));
            TextView right = ((TextView) dialog.findViewById(R.id.right));
            TextView tipMsg = ((TextView) dialog.findViewById(R.id.tipMsg));
            if (singleActionButton) {
                left.setVisibility(View.GONE);
                dialog.findViewById(R.id.divider).setVisibility(View.GONE);
                right.setBackgroundResource(R.drawable.selector_simple_dialog_single_button);
            } else {
                left.setText(leftButtonText);
            }
            right.setText(rightButtonText);
            ((TextView) dialog.findViewById(R.id.title)).setText(title);
            tipMsg.setText(this.tipMsg);
            if (msgAutoLink != -1) {
                tipMsg.setAutoLinkMask(msgAutoLink);
            }
            dialog.setCanceledOnTouchOutside(canceledOnTouchOutSide);
            dialog.setBackPressDismissEnable(backPressDismissEnable);
            return dialog;
        }
    }
}
