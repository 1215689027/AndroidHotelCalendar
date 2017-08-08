package com.customhotelcalendar.hotelcalendar;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.customhotelcalendar.R;
import com.customhotelcalendar.hotelcalendar.bean.HotelDateBean;
import com.customhotelcalendar.hotelcalendar.bean.HotelDayBean;
import com.customhotelcalendar.hotelcalendar.bean.HotelMonthBean;
import com.customhotelcalendar.hotelcalendar.view.CustomCalendarTextView;

import java.util.List;

/**
 * Created by Administrator on 2017/8/5.
 */

public class CalendarAdapter extends RecyclerView.Adapter<ViewHolder> {
    private static final String TAG=CalendarAdapter.class.getSimpleName();
    private static final int DAY_VT=1003;
    private static final int MONTH_VT=1005;
    private List<HotelDateBean> items;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener; //区间点击监听
    private OnSingleItemClickListener mOnSingleItemClickListener;
    public CalendarAdapter(Context mContext,List<HotelDateBean> items){
        this.items=items;
        this.mContext=mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType== DAY_VT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_day, null);
            view.setTag(DAY_VT);
            return new DayViewHolder(view);
        }else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_month, null);
            view.setTag(MONTH_VT);
            return new MonthViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Resources  resources=mContext.getResources();
        if(holder instanceof DayViewHolder){
            HotelDayBean item=(HotelDayBean)items.get(position);
            if(item==null)
                return;
            if(item!=null&&item.getDay()>0)
            ((DayViewHolder) holder).day.setText(""+item.getDay());
            else ((DayViewHolder) holder).day.setText("");
            if(item.getState()==HotelDayBean.STATE_PAST) {
                ((DayViewHolder) holder).day.setTextColor(resources.getColor(R.color.past_gray));
            }else if(item.getState()==HotelDayBean.STATE_NORMAL){
                ((DayViewHolder) holder).day.setTextColor(resources.getColor(R.color.normal_black));
            }else if(item.getState()==HotelDayBean.STATE_WEEKEND){
                ((DayViewHolder) holder).day.setTextColor(resources.getColor(R.color.weekend_fore));
            }
            ((DayViewHolder) holder).day.setBackgroundDrawable(null);
            ((DayViewHolder) holder).dayContainer.setBackgroundDrawable(null);
            ((DayViewHolder) holder).hint.setVisibility(View.GONE);
            ((DayViewHolder) holder).day.setBackType(0);
            if(item.getState()==HotelDayBean.STATE_STARTED){
                ((DayViewHolder) holder).day.setTextColor(resources.getColor(R.color.white));
                ((DayViewHolder) holder).day.setBackgroundResource(R.drawable.back_start_selected);
                ((DayViewHolder) holder).hint.setText("入住");
                ((DayViewHolder) holder).hint.setVisibility(View.VISIBLE);
            }else if(item.getState()==HotelDayBean.STATE_END){
                ((DayViewHolder) holder).day.setTextColor(resources.getColor(R.color.white));
                ((DayViewHolder) holder).day.setBackgroundResource(R.drawable.back_end_selected);
                ((DayViewHolder) holder).hint.setText("离店");
                ((DayViewHolder) holder).hint.setVisibility(View.VISIBLE);
            }else if(item.getState()==HotelDayBean.STATE_CHOSED){
                ((DayViewHolder) holder).day.setBackType(CustomCalendarTextView.BACK_YELLOW_CIRCLE);
                ((DayViewHolder) holder).day.setTextColor(resources.getColor(R.color.white));
                ((DayViewHolder) holder).day.setBackgroundDrawable(null);
            }else if(item.getState()==HotelDayBean.STATE_PERIOD_CHOSED){
                ((DayViewHolder) holder).day.setTextColor(resources.getColor(R.color.white));
                ((DayViewHolder) holder).dayContainer.setBackgroundResource(R.drawable.back_period_selected);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnItemClickListener!=null)
                    mOnItemClickListener.onItemClick(position);
                    if(mOnSingleItemClickListener!=null){
                        mOnSingleItemClickListener.onSingleItemClick(position);
                    }
                }
            });
        }else if(holder instanceof MonthViewHolder){
            HotelMonthBean item=(HotelMonthBean)items.get(position);
            if(item!=null&&item.getMonth()!=null){
                ((MonthViewHolder) holder).month.setText(item.getMonth());
            }
        }
    }

    @Override
    public int getItemViewType(int position){
        HotelDateBean item=items.get(position);
        if(item instanceof HotelDayBean){
            return DAY_VT;
        }else{
            return MONTH_VT;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener=mOnItemClickListener;
    }

    public void setSingleClickListener(OnSingleItemClickListener mOnSingleItemClickListener){
        this.mOnSingleItemClickListener=mOnSingleItemClickListener;
    }

    class DayViewHolder extends ViewHolder{
        TextView hint;
        CustomCalendarTextView day;
        FrameLayout dayContainer;
        public DayViewHolder(View view){
            super(view);
            day=(CustomCalendarTextView) view.findViewById(R.id.day);
            hint=(TextView)view.findViewById(R.id.hint);
            dayContainer=(FrameLayout)view.findViewById(R.id.day_container);
        }
    }

    class MonthViewHolder extends ViewHolder{
        TextView month;
        public MonthViewHolder(View view){
            super(view);
            month=(TextView)view;
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public interface OnSingleItemClickListener{
        void onSingleItemClick(int position);
    }
}
