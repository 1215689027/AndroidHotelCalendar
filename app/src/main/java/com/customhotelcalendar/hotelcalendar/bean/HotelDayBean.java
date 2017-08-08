package com.customhotelcalendar.hotelcalendar.bean;

/**
 * Created by Administrator on 2017/8/4.
 */

public class HotelDayBean extends HotelDateBean {
    public static final int STATE_NORMAL=0;
    public static final int STATE_STARTED=1;
    public static final int STATE_END=2;
    public static final int STATE_CHOSED=3;
    public static final int STATE_PAST=4;
    public static final int STATE_WEEKEND=5;
    public static final int STATE_PERIOD_CHOSED=6;

    private int year;
    private int month;
    private int day;
    private int state=0;  //0为正常显示，1为开始日期，2为结束日期，3为选中的日期，4为已经过去的日期,5为将来的周末
    public HotelDayBean(){}
    public HotelDayBean(int year,int month,int day,int state){
        this.year=year;
        this.month=month;
        this.day=day;
        this.state=state;
    }
    public int getMonth(){
        return month;
    }

    public void setDay(int day){
        this.day=day;
    }

    public int getDay(){
        return day;
    }

    public void setState(int state){
        this.state=state;
    }

    public int getState(){
        return state;
    }

    public void setYear(int year){
        this.year=year;
    }

    public int getYear(){
        return year;
    }

    public void setMonth(int month){
        this.month=month;
    }

    public void setData(int year,int month,int day){
        this.year=year;
        this.month=month;
        this.day=day;
    }
}

