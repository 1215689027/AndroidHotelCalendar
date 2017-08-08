package com.customhotelcalendar.hotelcalendar.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.customhotelcalendar.R;
import com.customhotelcalendar.hotelcalendar.CalendarAdapter;
import com.customhotelcalendar.hotelcalendar.bean.HotelDateBean;
import com.customhotelcalendar.hotelcalendar.bean.HotelDayBean;
import com.customhotelcalendar.hotelcalendar.bean.HotelMonthBean;
import com.customhotelcalendar.hotelcalendar.helper.DateBeanHelper;
import com.customhotelcalendar.hotelcalendar.view.DividerItemDecortion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PeroidSelectedActivity extends AppCompatActivity {

    private static final String TAG = PeroidSelectedActivity.class.getSimpleName();

    RecyclerView calendarRV;
    private List<HotelDateBean> items = new ArrayList<>();  //所有的条目
    private DateBeanHelper mDateBeanHelper;   //处理日历逻辑的类
    private CalendarAdapter adapter;
    private GridLayoutManager mGridLayoutManager;
    private HotelDayBean start = new HotelDayBean(); //选中的开始时间
    private HotelDayBean end = new HotelDayBean();  //选中的结束时间
    private int maxMonthPeroid=24;           //从现在开始到结束的，月份范围
    private int monthIndex=0;          //当前最大的月份与开始的月的差
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period);
        calendarRV = (RecyclerView) findViewById(R.id.rv_calendar);
        mGridLayoutManager = new GridLayoutManager(this, 7);
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                HotelDateBean bean = items.get(position);
                if (bean instanceof HotelMonthBean)
                    return 7;
                else return 1;
            }
        });
        calendarRV.addItemDecoration(new DividerItemDecortion(this));
        calendarRV.setLayoutManager(mGridLayoutManager);
        mDateBeanHelper = new DateBeanHelper();
        initItems();
        adapter = new CalendarAdapter(this, items);
        calendarRV.setAdapter(adapter);
        adapter.setOnItemClickListener(new CalendarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                HotelDayBean bean = (HotelDayBean) items.get(position);
                if (bean == null)
                    return;
                sovleItemClick(bean);
            }
        });
        calendarRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(monthIndex>=maxMonthPeroid)
                    return;
                if(mDateBeanHelper.isLastItemVisible(recyclerView,items.size())){
                    mDateBeanHelper.loadMoreItems(items,monthIndex);
                    monthIndex++;
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * 初始化日历控件的item
     */
    private void initItems() {
        HotelDayBean start = new HotelDayBean();
        start.setData(2017, 8, 22);
        HotelDayBean end = new HotelDayBean();
        end.setData(2017, 8, 24);
        mDateBeanHelper.isInTimePeriod(start, end);
        for (int i = 0; i < 6; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, i);
            if (i != 0)
                calendar.set(Calendar.DAY_OF_MONTH, 1);
            items.addAll(mDateBeanHelper.getHotelDateBeans(calendar));
            monthIndex++;
        }
        int j=0;
        for(HotelDateBean dateBean:items){
            if(dateBean instanceof HotelDayBean){
                if(((HotelDayBean) dateBean).getYear()==0){
                    mDateBeanHelper.solveBlankItem(items,(HotelDayBean) dateBean,j);
                }else mDateBeanHelper.getPeriod((HotelDayBean) dateBean,start,end);
            }
            j++;
        }
    }

    /**
     * 解决点击时间
     * @param bean
     */
    private void sovleItemClick(HotelDayBean bean) {
        if (bean.getYear() == 0 || bean.getState() == HotelDayBean.STATE_PAST)
            return;
        Calendar calendar=mDateBeanHelper.setCalendar(bean);
        Calendar startCalendar=mDateBeanHelper.setCalendar(start);
        if(start.getYear()!=0&&end.getYear()!=0){
            start.setData(0,0,0);
            end.setData(0,0,0);
        }
        if(start.getYear()==0||(calendar.getTimeInMillis()<startCalendar.getTimeInMillis())){
            mDateBeanHelper.setAllNormal(items);
            bean.setState(HotelDayBean.STATE_CHOSED);
            adapter.notifyDataSetChanged();
            start.setData(bean.getYear(),bean.getMonth(),bean.getDay());
            return;
        }

        if(end.getYear()==0){
            end.setData(bean.getYear(),bean.getMonth(),bean.getDay());
            mDateBeanHelper.onEndClick(items,start,end);
            adapter.notifyDataSetChanged();
        }
    }

}
