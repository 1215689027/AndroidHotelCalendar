package com.customhotelcalendar.hotelcalendar.helper;

import android.content.Context;

/**
 * Created by Administrator on 2017/8/5.
 */

public class DeviceHelper {
    public static int dp2px(Context context,int dpi) {
        float density;
        if (context != null) {
            density = context.getResources().getDisplayMetrics().density;
        } else density = 2;
        return (int) (dpi * density + 0.5);
    }
}
