package com.drcosu.ndileber.dileberui.timetable.singleline;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;


import com.drcosu.ndileber.dileberui.R;

import java.util.Calendar;

/**
 * Created by WaTaNaBe on 2017/8/14.
 */

public class CalenderTextView extends android.support.v7.widget.AppCompatTextView{
    public CalenderTextView(Context context) {
        this(context,null);
    }

    public CalenderTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0 );
    }

    public CalenderTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private static final int[] STATE_TODAY = {
            R.attr.state_calendar_today
    };

    private Calendar calendar;

    private boolean today;

    public boolean isToday() {
        return today;
    }

    public void setToday(boolean today) {
        this.today = today;
        refreshDrawableState();
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    boolean select;

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
        this.setSelected(select);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (today) {
            mergeDrawableStates(drawableState, STATE_TODAY);
        }
        return drawableState;
    }

}
