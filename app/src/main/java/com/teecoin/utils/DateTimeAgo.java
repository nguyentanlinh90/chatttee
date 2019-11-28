package com.teecoin.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;

import com.teecoin.R;
import com.teecoin.base.TCApplication;

import java.util.Date;

public class DateTimeAgo {
    //https://github.com/bancek/android-timeago/blob/master/src/com/lukazakrajsek/timeago/TimeAgo.java
    private static Context context;

    public DateTimeAgo(Context context) {
        DateTimeAgo.context = context;
    }

    @SuppressLint("StringFormatInvalid")
    public static String timeAgo(long millis) {
        long diff = new Date().getTime() - millis;

        Resources r = TCApplication.getActiveActivity().getResources();

        double seconds = Math.abs(diff) / 1000;
        double minutes = seconds / 60;
        double hours = minutes / 60;
        double days = hours / 24;
        double weeks = days / 7;
        double months = days / 30;
        double years = days / 365;

        if (seconds < 60) {
            return r.getString(R.string.time_ago_minute);
        } else if (minutes < 60) {
            return r.getString(R.string.time_ago_minutes, Math.round(minutes));
        } else if (hours < 2) {
            return r.getString(R.string.time_ago_hour);
        } else if (hours < 24) {
            return r.getString(R.string.time_ago_hours, Math.round(hours));
        } else if (days < 2) {
            return r.getString(R.string.time_ago_day);
        } else if (days < 7) {
            return r.getString(R.string.time_ago_days, Math.round(days));
        } else if (weeks < 2) {
            return r.getString(R.string.time_ago_week);
        } else if (weeks <= 4) {
            return r.getString(R.string.time_ago_weeks, Math.round(weeks));
        } else if (days < 60) {
            return r.getString(R.string.time_ago_month);
        } else if (months < 12) {
            return r.getString(R.string.time_ago_months, Math.round(months));
        } else if (years < 2) {
            return r.getString(R.string.time_ago_year);
        } else if (years >= 2) {
            return r.getString(R.string.time_ago_years, Math.round(years));
        }
        
        return "";
    }

    public String timeAgo(Date date) {
        return timeAgo(date.getTime());
    }
}