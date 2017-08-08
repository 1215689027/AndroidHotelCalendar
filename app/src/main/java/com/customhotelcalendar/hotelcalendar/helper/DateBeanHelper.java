package com.customhotelcalendar.hotelcalendar.helper;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.customhotelcalendar.hotelcalendar.bean.HotelDateBean;
import com.customhotelcalendar.hotelcalendar.bean.HotelDayBean;
import com.customhotelcalendar.hotelcalendar.bean.HotelMonthBean;

import java.net.NoRouteToHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by 陈龙 on 2017/8/4.
 */

public class DateBeanHelper {
    private static final String TAG = DateBeanHelper.class.getSimpleName();
    protected static final int DEFAULT_DAYS_IN_WEEK = 7;
    protected static final int DEFAULT_MAX_WEEKS = 6;
    protected static final int DAY_NAMES_ROW = 1;


    /**
     * 获取每个月的日历，包括这个月的具体是几月
     *
     * @param calendar 每个月的日历
     */
    public List<HotelDateBean> getHotelDateBeans(Calendar calendar) {
        List<HotelDateBean> beans = new ArrayList<>();
        beans.add(getMonthBean(calendar));
        beans.addAll(getDaysOfMonth(calendar));
        return beans;
    }

    /**
     * 获取每个月具体的日历
     *
     * @param calendar
     * @return
     */
    public List<HotelDayBean> getDaysOfMonth(Calendar calendar) {
        List<HotelDayBean> dayBeans = new ArrayList<>();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        for (int i = 0; i < firstDayOfWeek - 1; i++) {
            HotelDayBean bean = new HotelDayBean();
            dayBeans.add(bean);
        }
        for (int i = 1; i < currentDay; i++) {
            HotelDayBean bean = new HotelDayBean(year, month, i, HotelDayBean.STATE_PAST);
            dayBeans.add(bean);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        HotelDayBean bean = new HotelDayBean(year, month, currentDay, HotelDayBean.STATE_NORMAL);
        dayBeans.add(bean);
        isWeekend(bean);
//        getPeriod(bean,start,end);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        int maxDays = calendar.get(Calendar.DAY_OF_MONTH);
        int weekdayOfLast=calendar.get(Calendar.DAY_OF_WEEK);
        for (int i = currentDay; i < maxDays; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            HotelDayBean beanL = new HotelDayBean(year, month, i + 1, HotelDayBean.STATE_NORMAL);
            dayBeans.add(beanL);
            isWeekend(beanL);
        }
        for(int i=weekdayOfLast;i<Calendar.SATURDAY;i++){
            HotelDayBean dayBean=new HotelDayBean();
            dayBeans.add(dayBean);
        }
        return dayBeans;
    }

    /**
     * 获取哪一年哪一月
     *
     * @param calendar
     * @return
     */
    public HotelMonthBean getMonthBean(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        if (month == 0) {
            month = 12;
            year--;
        }
        HotelMonthBean bean = new HotelMonthBean(year + "年" + month + "月");
        return bean;
    }

    /**
     * 判断入住时间和离店时间是否在时间范围内
     *
     * @param start
     * @param end
     */
    public void isInTimePeriod(HotelDayBean start, HotelDayBean end) {
        convert2CalendarFormat(start);
        convert2CalendarFormat(end);
        Calendar today = Calendar.getInstance();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.YEAR, start.getYear());
        startCalendar.set(Calendar.MONTH, start.getMonth());
        startCalendar.set(Calendar.DAY_OF_MONTH, start.getDay());
        endCalendar.set(Calendar.YEAR, end.getYear());
        endCalendar.set(Calendar.MONTH, end.getMonth());
        endCalendar.set(Calendar.DAY_OF_MONTH, end.getDay());
        long startMillis = startCalendar.getTimeInMillis();
        long endMillis = endCalendar.getTimeInMillis();
        long nowMillis = today.getTimeInMillis();
        if (startMillis < nowMillis) {
            Log.e(TAG, "136 isInTimePeroid out start");
            start.setData(today.get(Calendar.YEAR),
                    today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));

        }
        today.add(Calendar.DAY_OF_MONTH, 1);
        if (endMillis < today.getTimeInMillis()) {
            Log.e(TAG, "136 isInTimePeroid out end");
            end.setData(today.get(Calendar.YEAR),
                    today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
        }
    }


    /**
     * 将传统的时间换算成calendar的对应时间
     *
     * @param bean
     */
    public void convert2CalendarFormat(HotelDayBean bean) {
        if (bean.getMonth() == 12) {
            bean.setMonth(0);
            bean.setYear(bean.getYear() + 1);
        }
        bean.setMonth(bean.getMonth() - 1);
    }


    /**
     * 判断时间是否在选中的范围内
     *
     * @param bean  日历中的时间
     * @param start
     * @param end
     */
    public void getPeriod(HotelDayBean bean, HotelDayBean start, HotelDayBean end) {
        if (bean.getYear() == start.getYear() && bean.getMonth() == start.getMonth() && bean.getDay() == start.getDay()) {
            Log.e(TAG, "136 getPerioid start");
            bean.setState(HotelDayBean.STATE_STARTED);
        } else if (bean.getYear() == end.getYear() && bean.getMonth() == end.getMonth() && bean.getDay() == end.getDay()) {
            Log.e(TAG, "136 getPerioid end");
            bean.setState(HotelDayBean.STATE_END);
        } else if (start.getYear() == end.getYear()) {
            if (bean.getMonth() > start.getMonth()
                    && bean.getMonth() < end.getMonth()) {
                Log.e(TAG, "136 111 getPerioid choosed");
                bean.setState(HotelDayBean.STATE_PERIOD_CHOSED);
            } else if (bean.getMonth() == start.getMonth() && bean.getMonth() == end.getMonth()) {
                if (bean.getDay() > start.getDay() && bean.getDay() < end.getDay()) {
                    Log.e(TAG, "136 112 getPerioid choosed");
                    bean.setState(HotelDayBean.STATE_PERIOD_CHOSED);
                }
            } else if (start.getMonth() == bean.getMonth() && bean.getDay() > start.getDay()) {
                Log.e(TAG, "136 113 getPerioid choosed");
                bean.setState(HotelDayBean.STATE_PERIOD_CHOSED);
            } else if (end.getMonth() == bean.getMonth() && bean.getDay() < end.getDay()) {
                Log.e(TAG, "136 114 getPerioid choosed");
                bean.setState(HotelDayBean.STATE_PERIOD_CHOSED);
            }
        } else {
            if (start.getMonth() < bean.getMonth()) {
                Log.e(TAG, "136 115 getPerioid choosed");
                bean.setState(HotelDayBean.STATE_PERIOD_CHOSED);
            } else if (start.getMonth() == bean.getMonth() && start.getDay() < bean.getDay()) {
                Log.e(TAG, "136 116 getPerioid choosed");
                bean.setState(HotelDayBean.STATE_PERIOD_CHOSED);
            } else if (end.getMonth() > bean.getMonth()) {
                Log.e(TAG, "136 117 getPerioid choosed");
                bean.setState(HotelDayBean.STATE_PERIOD_CHOSED);
            } else if (end.getMonth() == bean.getMonth() && bean.getDay() < end.getDay()) {
                Log.e(TAG, "136 118 getPerioid choosed");
                bean.setState(HotelDayBean.STATE_PERIOD_CHOSED);
            }
        }
    }


    /**
     * 判断是否是周末
     *
     * @param bean
     */
    private void isWeekend(HotelDayBean bean) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, bean.getYear());
        calendar.set(Calendar.MONTH, bean.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, bean.getDay());
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ||
                calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            bean.setState(HotelDayBean.STATE_WEEKEND);
        }
    }


    /**
     * 将所有条目中不是过去时的状态改为普通状态
     *
     * @param items
     */
    public void setAllNormal(List<HotelDateBean> items) {
        for (HotelDateBean dateBean : items) {
            if (dateBean instanceof HotelDayBean) {
                if (((HotelDayBean) dateBean).getState() != HotelDayBean.STATE_PAST) {
                    ((HotelDayBean) dateBean).setState(HotelDayBean.STATE_NORMAL);
                    isWeekend((HotelDayBean) dateBean);
                }
            }
        }
    }

    public void solveSingleClick(List<HotelDateBean> items,int position){
        int j=0;
        for (HotelDateBean dateBean : items) {
            if (dateBean instanceof HotelDayBean) {
                if (((HotelDayBean) dateBean).getState() != HotelDayBean.STATE_PAST) {
                    ((HotelDayBean) dateBean).setState(HotelDayBean.STATE_NORMAL);
                    isWeekend((HotelDayBean) dateBean);
                    if(j==position){
                        ((HotelDayBean) dateBean).setState(HotelDayBean.STATE_CHOSED);
                    }
                }


            }
            j++;
        }
    }

    /**
     * 设置给定时间的日历calendar
     *
     * @param bean
     * @return
     */
    public Calendar setCalendar(HotelDayBean bean) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, bean.getYear());
        calendar.set(Calendar.MONTH, bean.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, bean.getDay());
        return calendar;
    }


    /**
     * 点击离店时间的处理
     *
     * @param items
     * @param start
     * @param end
     */
    public void onEndClick(List<HotelDateBean> items, HotelDayBean start, HotelDayBean end) {
        int j = 0;
        for (HotelDateBean dateBean : items) {
            if (dateBean instanceof HotelDayBean) {
                if (((HotelDayBean) dateBean).getYear() != 0)
                    getPeriod((HotelDayBean) dateBean, start, end);
                else solveBlankItem(items, (HotelDayBean) dateBean, j);
            }
            j++;
        }
    }

    /**
     * 处理空白项是否在选中的区域内
     *
     * @param items
     * @param bean
     * @param position
     */
    public void solveBlankItem(List<HotelDateBean> items, HotelDayBean bean, int position) {
        if (position - 1 < 20)
            return;
        HotelDateBean dateBean = items.get(position - 1);
        if (dateBean instanceof HotelMonthBean)
            dateBean = items.get(position - 2);
        HotelDayBean dayBean = (HotelDayBean) dateBean;
        if (dayBean.getState() == HotelDayBean.STATE_STARTED
                || dayBean.getState() == HotelDayBean.STATE_PERIOD_CHOSED) {
            bean.setState(HotelDayBean.STATE_PERIOD_CHOSED);
        }
    }

    public void isSelected(HotelDayBean bean,HotelDayBean selectBean){
        if(bean.getYear()==selectBean.getYear()&&bean.getMonth()==selectBean.getMonth()
                &&bean.getDay()==selectBean.getDay()){
            bean.setState(HotelDayBean.STATE_CHOSED);
        }
    }

    public boolean isLastItemVisible(RecyclerView recyclerView,int size){
        RecyclerView.LayoutManager layoutManager=recyclerView.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager&&
                ((GridLayoutManager) layoutManager).findLastVisibleItemPosition()>size-2) {
            return true;
        }
        return  false;
    }

    public void loadMoreItems(List<HotelDateBean> items,int monthIndex){
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.add(Calendar.MONTH,monthIndex);
        items.addAll(getHotelDateBeans(calendar));
    }
}
