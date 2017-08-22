package com.drcosu.ndileber.dileberui.timetable.singleline;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.drcosu.ndileber.tools.UTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by WaTaNaBe on 2017/8/14.
 */

public class CalenderViewPageAdapter extends PagerAdapter implements View.OnClickListener, CalenderPageLayout.ICalender {
    /**
     * 一页有多少
     */
    public static final int PAGE_NUM = 7;
    /**
     * 当前页之前有多少(默认)
     */
    public static int PAGE_PER = 20;
    /**
     * 当前页和后面的有多少(默认)
     */
    public static int PAGE_AFT = 20;

    /**
     * 每一页的view
     */
    List<LinearLayout> linearLayouts = new ArrayList<>(PAGE_PER+PAGE_AFT);
    List<Calendar> calendars = null;

    public static int COUNT = PAGE_PER + PAGE_AFT;

    public Context context;

    private int mStart;

    public CalenderViewPageAdapter(Context context,Integer start){
        this(context,start,null,null);
    }
    public CalenderViewPageAdapter(Context context,Integer start,Integer startPage,Integer endPage){
        this(context,start,null,null,null);
    }

    public CalenderViewPageAdapter(Context context,Integer start,Integer startPage,Integer endPage,List<Date> date){
        this.context = context;
        this.mStart = start;
        if(startPage!=null){
            PAGE_PER = startPage;
        }
        if(endPage!=null){
            PAGE_AFT = endPage;
        }
        if(date!=null){
            mDate = date;
        }
        COUNT = PAGE_PER + PAGE_AFT;
        initView();
    }
    List<Date> mDate;
    public void setGuanxiDate(List<Date> date){
        mDate = date;
        linearLayouts = new ArrayList<>();
        for(int i=0;i<COUNT;i++){
            linearLayouts.add(null);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        // 最简单解决 notifyDataSetChanged() 页面不刷新问题的方法
        return POSITION_NONE;
    }


    private void initView(){


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);

        Calendar current = Calendar.getInstance();
        //获取当前周几
        int intZhou = current.get(Calendar.DAY_OF_WEEK) - 2;
        if(intZhou==-1){
            intZhou = 6;
        }
        int startDate = PAGE_PER*7+intZhou;
        current.add(Calendar.DATE,-startDate+mStart-1);
        calendars = new ArrayList<>();
        for(int i=0;i<COUNT*PAGE_NUM;i++){
            Calendar tempCalendar = (Calendar) current.clone();
            calendars.add(tempCalendar);
            current.add(Calendar.DATE,1);
        }
        for(int i=0;i<COUNT;i++){
            linearLayouts.add(null);
        }
    }

    public interface DateSelectedDelegate{
        void onDateSelected(View view, Date date);
    }

    public void setDateSelectedDelegate(DateSelectedDelegate dateSelectedDelegate) {
        this.dateSelectedDelegate = dateSelectedDelegate;
    }

    DateSelectedDelegate dateSelectedDelegate= null;

    @Override
    public void onClickCalender(View view, Date date) {
        if(dateSelectedDelegate!=null){
            dateSelectedDelegate.onDateSelected(view, date);
        }
    }

    @Override
    public int getCount() {
        return linearLayouts.size();
    }

    CalenderPageLayout temp = null;

    @Override
    public void onClick(View v) {
        if(v instanceof CalenderPageLayout){
            CalenderPageLayout mc = (CalenderPageLayout) v;
            if(temp == null){
                temp = mc;
                temp.setSelect(true);
            }else{
                temp.setSelect(false);
                mc.setSelect(true);
                temp = mc;
            }
        }
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object)   {
        container.removeView(linearLayouts.get(position));//删除页卡
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        if(linearLayouts.get(position) == null){
            LinearLayout linearLayout = new LinearLayout(context);
            for(int j = 0; j< PAGE_NUM;j++){
                Calendar calendar = calendars.get(position*PAGE_NUM +j);
                CalenderPageLayout calenderPageLayout = new CalenderPageLayout(context);
                if(DateUtils.isToday(calendar.getTime().getTime())){
                    calenderPageLayout.setToday(true);
                    //calenderPageLayout.setSelect(true);
                }else{
                    calenderPageLayout.setToday(false);
                    calenderPageLayout.setSelect(false);
                }
                if(mDate!=null){
                    for(Date m:mDate){
                        if(UTime.isSameDate(m,calendar.getTime())){
                            calenderPageLayout.setHaveDate(true);
                            break;
                        }
                    }
                }
                calenderPageLayout.setText(String.valueOf(calendar.get(Calendar.DATE)));
                calenderPageLayout.setCalendar(calendar);
                calenderPageLayout.setOnClickListener(this);
                calenderPageLayout.setTextSize(18);
                calenderPageLayout.setLayoutParams(layoutParams);
                calenderPageLayout.setiCalender(this);
                linearLayout.addView(calenderPageLayout);

            }
            linearLayouts.set(position,linearLayout);
        }
        container.addView(linearLayouts.get(position), 0);//添加页卡
        return linearLayouts.get(position);

    }


    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0==arg1;//官方提示这样写
    }

}
