package com.drcosu.ndileber.dileberui.timetable.singleline;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.drcosu.ndileber.dileberui.R;

import java.util.Calendar;
import java.util.Date;



/**
 * Created by WaTaNaBe on 2017/8/15.
 */

public class CalenderPageLayout extends LinearLayout {

    CalenderTextView calenderNum;
    TextView calenderHaveClass;

    public ICalender iCalender;

    public interface ICalender{
        void onClickCalender(View view, Date date);
    }

    public void setiCalender(ICalender iCalender) {
        this.iCalender = iCalender;
    }

    public CalenderPageLayout(Context context) {
        this(context,null);
    }

    public CalenderPageLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CalenderPageLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.widget_calender_page, this, true);
        calenderNum = (CalenderTextView) findViewById(R.id.calenderNum);
        calenderHaveClass = (TextView) findViewById(R.id.calenderHaveClass);
    }

    public boolean isToday() {
        return calenderNum.isToday();
    }

    public void setToday(boolean today) {
        calenderNum.setToday(today);
    }

    public void setCalendar(Calendar calendar) {
        calenderNum.setCalendar(calendar);
    }

    public Calendar getCalendar() {
        return calenderNum.getCalendar();
    }

    boolean select;

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
        calenderNum.setSelect(select);
        if( iCalender!= null){
            iCalender.onClickCalender(calenderNum,calenderNum.getCalendar().getTime());
        }
    }

    public void setText(String s){
        calenderNum.setText(s);
    }

    public void setTextSize(float size){
        calenderNum.setTextSize(size);
    }

    public void clearn(){

    }

}
