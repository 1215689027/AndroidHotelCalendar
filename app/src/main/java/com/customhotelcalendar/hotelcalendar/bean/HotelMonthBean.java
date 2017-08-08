package com.customhotelcalendar.hotelcalendar.bean;

/**
 * Created by Administrator on 2017/8/4.
 */

public class HotelMonthBean extends HotelDateBean{
    private String month;
    public HotelMonthBean(){}
    public HotelMonthBean(String month){
        this.month=month;
    }
    public String getMonth(){
        return month;
    }
}
