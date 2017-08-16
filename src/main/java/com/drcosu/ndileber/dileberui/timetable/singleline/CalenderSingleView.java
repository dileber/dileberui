package com.drcosu.ndileber.dileberui.timetable.singleline;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.drcosu.ndileber.dileberui.R;
import com.drcosu.ndileber.tools.UTime;

import java.util.Date;

/**
 * 默认从周一开始进行轮回
 * Created by WaTaNaBe on 2017/8/16.
 */

public class CalenderSingleView extends LinearLayout implements CalenderViewPageAdapter.DateSelectedDelegate {
    ViewPager timeTablePage;
    LinearLayout timeTableCalender;



    public CalenderSingleView(Context context) {
        this(context,null);
    }

    public CalenderSingleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    //起始周
    private int start;
    private int start_page;
    private int end_page;
    Context context;

    public CalenderSingleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CalenderSingleView, defStyleAttr, 0);
        start = a.getInt(R.styleable.CalenderSingleView_calender_start, 1);
        start_page = a.getInt(R.styleable.CalenderSingleView_calender_start_page, CalenderViewPageAdapter.PAGE_PER);
        end_page = a.getInt(R.styleable.CalenderSingleView_calender_end_page, CalenderViewPageAdapter.PAGE_AFT);
        a.recycle();
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.widget_calender_single_view, this, true);
        timeTablePage = (ViewPager) view.findViewById(R.id.timeTablePage);
        timeTableCalender = (LinearLayout) view.findViewById(R.id.timeTableCalender);
        //控制起始数字大小
        if(start<1){
            start = 1;
        } else if(start>7){
            start = 7;
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
        for(int i = 0; i <= 6; i++){
            int m = i+start;
            if(m>=7){
                m = m - 7;
            }
            TextView textView = new TextView(context);
            textView.setText(UTime.getWeekNumber(UTime.Week.ZHOU,m));
            textView.setTextColor(ContextCompat.getColor(context,R.color.colord0d0d0));
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(10);
            textView.setLayoutParams(layoutParams);
            timeTableCalender.addView(textView);
        }

        CalenderViewPageAdapter calenderViewPageAdapter = new CalenderViewPageAdapter(context,start,start_page,end_page);
        calenderViewPageAdapter.setDateSelectedDelegate(this);
        timeTablePage.setAdapter(calenderViewPageAdapter);
        timeTablePage.setCurrentItem(CalenderViewPageAdapter.PAGE_PER+(start>1?-1:0));
    }

    @Override
    public void onDateSelected(View view, Date date) {
        if(dateSelectedDelegate!=null){
            dateSelectedDelegate.onDateSelected(view,date);
        }
    }

    public interface DateSelectedDelegate{
        void onDateSelected(View view, Date date);
    }

    public void setDateSelectedDelegate(CalenderViewPageAdapter.DateSelectedDelegate dateSelectedDelegate) {
        this.dateSelectedDelegate = dateSelectedDelegate;
    }

    CalenderViewPageAdapter.DateSelectedDelegate dateSelectedDelegate= null;
}
