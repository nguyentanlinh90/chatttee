package com.teecoin.utils;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.widget.TextView;

import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.model.couponsystem.TimeOpenForDayMerchantModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.teecoin.utils.TCUtils.getLanguageCode;
import static core.base.BaseApplication.getActiveActivity;

public class TCDateUtility {

    private static Calendar currentCalendar = Calendar.getInstance();


    public static int getCurrentDayOfMonth() {
        return currentCalendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getCurrentMonth() {
        return currentCalendar.get(Calendar.MONTH);
    }

    public static int getCurrentHourOfDay() {
        return currentCalendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getCurrentMinute() {
        return currentCalendar.get(Calendar.MINUTE);
    }

    public static int getCurrentYear() {
        return currentCalendar.get(Calendar.YEAR);
    }

    public static int getDayOfWeek() {
        return currentCalendar.get(Calendar.DAY_OF_WEEK);
    }

    public static int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    public static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

    public static int getDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static Date getCurrentDate() {
        return currentCalendar.getTime();
    }

    public static String getDateBefore(int before) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat(DateFormatDefinition.YYYY_MM_DD_HYPHEN.getFormat());
        cal.add(Calendar.DAY_OF_YEAR, before);
        String day = s.format(new Date(cal.getTimeInMillis()));
        return day;
    }

    public static String formatDate(Date date, String format) {
        if (date == null) {
            return "";
        }
        DateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    public static String formatDate(Date date, DateFormatDefinition format) {
        if (date == null) {
            return "";
        }
        @SuppressLint("SimpleDateFormat")
        DateFormat df = new SimpleDateFormat(format.getFormat());
        return df.format(date);
    }

    public static Date toDate(String str, DateFormatDefinition format) {
        return parse(str, format.getFormat(), TCConstant.BLANK);
    }

    public static Date toDate(String str, TCTimeZone timeZone, DateFormatDefinition format) {
        return parse(str, format.getFormat(), timeZone.getValue());
    }

    public static String formatDate(int year, int month, int day, DateFormatDefinition format) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return formatDate(calendar.getTime(), format);
    }

    public static String formatPeriodDate(String date) {
        return String.format(TCUtils.getString(R.string.time_format),
                convertToCurrentTimeZoneDate(date,
                        TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_Z,
                        TCDateUtility.DateFormatDefinition.HH_MM_A),
                convertToCurrentTimeZoneDate(date,
                        TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_Z,
                        TCDateUtility.DateFormatDefinition.EEE_MMM_DD_YYYY));
    }

    public static String formatDate(String date) {
        return TCDateUtility.convertToCurrentTimeZoneDate(date,
                TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_Z,
                TCDateUtility.DateFormatDefinition.HH_MM_EEE_YYYY_MM_DD);
    }

    private static Date parse(String src, String format, String timeZone) {
        if (src == null) {
            return null;
        }
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false);
        if (!TCUtils.isEmpty(timeZone)) {
            sdf.setTimeZone(java.util.TimeZone.getTimeZone(timeZone));
        }
        return sdf.parse(src, new ParsePosition(0));
    }

    public static Date toCouponDate(String src, DateFormatDefinition format) throws ParseException {
        if (src == null) {
            return null;
        }
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat(format.getFormat());
        return sdf.parse(src);
    }

    public enum TCTimeZone {
        UTC("UTC"),
        GMT("GMT");

        private String value;

        TCTimeZone(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static String getTimeZone() {
        return TimeZone.getDefault().getID();
    }

    public static String convertToCurrentTimeZoneDate(String dateString, DateFormatDefinition inputFormat, DateFormatDefinition outputFormat) {
        @SuppressLint("SimpleDateFormat")
        DateFormat utcFormat = new SimpleDateFormat(inputFormat.getFormat());
        utcFormat.setTimeZone(TimeZone.getTimeZone(TCTimeZone.UTC.getValue()));

        @SuppressLint("SimpleDateFormat")
        DateFormat pstFormat = new SimpleDateFormat(outputFormat.getFormat());
        pstFormat.setTimeZone(TimeZone.getTimeZone(getCurrentTimeZone()));

        Date date = null;
        try {
            date = utcFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return pstFormat.format(date);
    }

    public static String formatTimeForMyCoupon(String dateString, DateFormatDefinition inputFormat, DateFormatDefinition outputFormatTime, DateFormatDefinition outputFormatTimeDate) {
        String value;
        @SuppressLint("SimpleDateFormat")
        DateFormat utcFormat = new SimpleDateFormat(inputFormat.getFormat());
//        utcFormat.setTimeZone(TimeZone.getTimeZone(TCTimeZone.UTC.getValue()));

        Date date = null;
        try {
            date = utcFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        @SuppressLint("SimpleDateFormat")
        DateFormat timeFormat = new SimpleDateFormat(outputFormatTime.getFormat());
//        timeFormat.setTimeZone(TimeZone.getTimeZone(TCTimeZone.UTC.getValue()));

        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat(outputFormatTimeDate.getFormat());
//        dateFormat.setTimeZone(TimeZone.getTimeZone(TCTimeZone.UTC.getValue()));

        String dayOfWeek = "";
        if (date != null) {
            switch (date.getDay()) {
                case 0:
                    dayOfWeek = TCUtils.getString(R.string.sunday);
                    break;
                case 1:
                    dayOfWeek = TCUtils.getString(R.string.monday);
                    break;
                case 2:
                    dayOfWeek = TCUtils.getString(R.string.tuesday);
                    break;
                case 3:
                    dayOfWeek = TCUtils.getString(R.string.wednesday);
                    break;
                case 4:
                    dayOfWeek = TCUtils.getString(R.string.thursday);
                    break;
                case 5:
                    dayOfWeek = TCUtils.getString(R.string.friday);
                    break;
                case 6:
                    dayOfWeek = TCUtils.getString(R.string.saturday);
                    break;
            }
        }
        if (TCUtils.getLanguageCode().equals(EnumMgr.LanguageAppSetting.Japanese.getValue())) {
            //value =  dateFormat.format(date) + "  " + dayOfWeek + "   "+timeFormat.format(date) ;
            value = formatDate(date, DateFormatDefinition.YYYY_MM_DD) + "  " + dayOfWeek + "   " + timeFormat.format(date);
        } else {
            value = timeFormat.format(date) + "   " + dayOfWeek + ", " + dateFormat.format(date);
        }

        return value;
    }

    private static String getCurrentTimeZone() {
        TimeZone tz = Calendar.getInstance().getTimeZone();
        return tz.getID();
    }

//    public static String getCurrentDay() {
//        return formatDate(getCurrentDate(), DateFormatDefinition.YYYY_MM_DD_HH_MM_SS.getFormat());
//    }

    public static Context changLocalTimeZonetoLanguageApp(Context context) {
        Locale locale = new Locale(TCUtils.getLanguageCode());
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;

        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        return context;
    }

    public static void openDateTimePicker(String type, TextView tvDate, Context context) {
        TCDateUtility.changLocalTimeZonetoLanguageApp(context);
        Calendar calendar = Calendar.getInstance();

        new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(year, monthOfYear, dayOfMonth);
            new TimePickerDialog(context, (view1, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                Date date = calendar.getTime();
                tvDate.setText(TCDateUtility.formatDate(date,
                        type.equals(TCConstant.DATE_TIME_PICKER_FROM_FILTER) ?
                                TCUtils.getDateFormatByLanguageCode(DateFormatDefinition.DD_MM_YYYY_HH_MM_SS) :
                                TCUtils.getDateFormatByLanguageCode(DateFormatDefinition.EEE_MMM_DD_YYYY_HH_MM_A)));
                if (type.equals(TCConstant.DATE_TIME_PICKER_FROM_COUPON)) {
                    tvDate.setTextColor(TCUtils.getColor(R.color.c_b2973f));
                }
            }, TCDateUtility.getCurrentHourOfDay(), TCDateUtility.getCurrentMinute(), true).show();
        }, TCDateUtility.getCurrentYear(), TCDateUtility.getCurrentMonth(), TCDateUtility.getCurrentDayOfMonth()).show();
        //https://stackoverflow.com/questions/2055509/datetime-picker-in-android-application
    }

    public static void openTimePicker(TimeOpenForDayMerchantModel timeOpenForDayMerchantModel, boolean timeFrom, TextView tvDate, Context context) {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(context, (view1, hourOfDay, minute) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            Date date = calendar.getTime();
            if (timeFrom) {
                timeOpenForDayMerchantModel.setTimeFrom(date);
            } else {
                timeOpenForDayMerchantModel.setTimeTo(date);
            }
            tvDate.setText(TCDateUtility.formatDate(date,
                    TCDateUtility.DateFormatDefinition.HH_MM.getFormat()));
            tvDate.setTextColor(TCUtils.getColor(R.color.c_b2973f));
        }, TCDateUtility.getCurrentHourOfDay(), TCDateUtility.getCurrentMinute(), true).show();

    }

    public static void openCalendar(TextView tvDate, TCDateUtility.DateFormatDefinition formatDefinition) {

        TCDateUtility.changLocalTimeZonetoLanguageApp(getActiveActivity());

        Date date;

        if (TCUtils.isEmpty(tvDate.getText().toString())) {

            Date c = Calendar.getInstance().getTime();

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat df = new SimpleDateFormat(DateFormatDefinition.DD_MM_YYYY.getFormat());

            date = TCDateUtility.toDate(df.format(c), formatDefinition);

        } else {

            date = TCDateUtility.toDate(tvDate.getText().toString(), formatDefinition);
        }

        int mYear = TCDateUtility.getYear(date);

        int mMonth = TCDateUtility.getMonth(date);

        int mDay = TCDateUtility.getDayOfMonth(date);

        DatePickerDialog dialog = new DatePickerDialog(getActiveActivity(), (view, year, month, dayOfMonth) ->
                tvDate.setText(TCDateUtility.formatDate(year, month, dayOfMonth, DateFormatDefinition.DD_MM_YYYY)),
                mYear, mMonth, mDay);
        dialog.show();
    }

    public static boolean isCurrentDayOfWeek(int position) {
        //6 == position: sunday in list
        return TCDateUtility.getDayOfWeek() == position + 2 || (6 == position && Calendar.SUNDAY == TCDateUtility.getDayOfWeek());
    }

    public static String formatTimeToModel(String timeString) {
        return convertToCurrentTimeZoneDate(
                timeString, getLanguageCode().equals(EnumMgr.LanguageAppSetting.English.getValue()) ?
                        TCDateUtility.DateFormatDefinition.EEE_MMM_DD_YYYY_HH_MM_A
                        : TCDateUtility.DateFormatDefinition.DD_MM_YYYY_EEE_HH_MM_SS,
                TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_SSSSSS);
    }

    public static String formatTimeToUTC(String timeString) {
        return convertToCurrentTimeZoneDate(
                timeString, getLanguageCode().equals(EnumMgr.LanguageAppSetting.English.getValue()) ?
                        TCDateUtility.DateFormatDefinition.EEE_MMM_DD_YYYY_HH_MM_A
                        : TCDateUtility.DateFormatDefinition.DD_MM_YYYY_EEE_HH_MM_SS,
                TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_Z);
    }

    public enum DateFormatDefinition {
        YYYY("yyyy"),
        MM("MM"),
        DD("dd"),
        //        YY_MM("yy/MM"),
//        YYYY_MM("yyyy/MM"),
//        YYYY_MM_NO_SLASH("yyyyMM"),
//        MM_DD("MM/dd"),
//        MM_DD_YYYY("MM/dd/yyyy"),
        DD_MMM("dd MMM"),
        //        DD_MMM_YYYY("dd MMM yyyy"),
//        MM_DD_NO_SLASH("MMdd"),
//        YY_MM_DD("yy/MM/dd"),
        YYYY_MM_DD("yyyy/MM/dd"),
        HH_MM_EEE_YYYY_MM_DD("HH:mm EEE, yyyy/MM/dd"),
        YYYY_MM_DD_("yyyy-MM-dd"),
        DD_MM_YYYY("dd/MM/yyyy"),
        YYYY_MM_DD_HYPHEN("yyyy-MM-dd"),
        DD_MM_YYYY_HYPHEN("yyyy-MM-dd"),
        //        YYYY_MM_DD_NO_SLASH("yyyyMMdd"),
        HH_MM("HH:mm"),
        HH_MM_SS("HH:mm:ss"),
        //        HH_MM_SS("HH:mm:ss"),
//        YYYY_MM_DD_HH_MM("yyyy/MM/dd HH:mm"),
        YYYY_MM_DD_HYPHEN_HH_MM("yyyy/MM/dd - HH:mm"),
        DD_MM_YYYY_HYPHEN_HH_MM("dd/MM/yyyy - HH:mm"),
        //        YYYY_MM_DD_HYPHEN_HH_MM_("yyyy-MM-dd HH:mm"),
//        MM_DD_YYYY_HH_MM("MM/dd/yyyy HH:mm"),
        YYYY_MM_DD_HH_MM_SS("yyyy/MM/dd HH:mm:ss"),
        DD_MM_YYYY_HH_MM_SS("dd/MM/yyyy HH:mm:ss"),
        DD_MM_YYYY_HH_MM("dd/MM/yyyy   HH:mm"),
        //        YYYY_MM_DD_HH_MM_SS_AM_PM("yyyy/MM/dd  hh:mm:ss a"),
//        YYYY_MM_DD_HH_MM_NO_SLASH("yyyyMMddHHmm"),
//        YYYY_MM_DD_HH_MM_SS_NO_SLASH("yyyyMMddHHmmss"),
//        YYYY_MM_DD_HH_MM_SS_SSS("yyyy/MM/dd HH:mm:ss SSS"),
//        YYYY_MM_DD_HH_MM_SS_SSS_NO_SLASH("yyyyMMddHHmmssSSS"),
        YYYY_MM_DD_T_HH_MM_SS_SSSSSS("yyyy-MM-dd'T'HH:mm:ss.SSSSSS"),
        DD_MM_YYYY_T_HH_MM_SS_SSSSSS("dd-MM-yyyy'T'HH:mm:ss.SSSSSS"),
        //        YYYY_MM_DD_HYPHEN("yyyy-MM-dd"),
        YYYY_MM_DD_T_HH_MM_SS_Z("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
        //        YYYY_MM_DD_T_HH_MM_SS_POWER("yyyy-MM-dd'T'HH:mm:ssZ"),
//        YYYY_MM_DD_T_HH_MM_SSS_Z("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"),
//        DD_MMMM_YYYY("dd MMMM yyyy"),
//        YYYY_MM_DD_T_HH_MM_SS("yyyy-MM-dd'T'HH:mm:ss"),
//        DD_MMM_at_H_MM_A("dd MMM 'at' h:mm a"),
//        HH_MM_A_EEE_DD_MMM_YYYY("hh.mm a, EEEE dd MMM yyyy"),
//        HH_MM_EEE_DD_MM_YYYY("hh:mm      EEEE, yyyy/MM/dd"),
        EEE_MMM_DD_YYYY_HH_MM_A("EEE MMM, dd, yyyy h:mm a"),
        EEE_DD_MMM_YYYY_HH_MM_A("EEE dd, MMM, yyyy h:mm a"),
        YYYY_MM_DD_EEE_HH_MM_SS("yyyy/MM/dd EEE HH:mm:ss"),
        DD_MM_YYYY_EEE_HH_MM_SS("dd/MM/yyyy EEE HH:mm:ss"),
        EEE_MMM_DD_YYYY("EEE, yyyy/MM/dd"),
        HH_MM_A("h:mm a");


        private String format;

        DateFormatDefinition(String format) {
            this.format = format;
        }

        public String getFormat() {
            return format;
        }

        @Override
        public String toString() {
            return format;
        }
    }

    public static void checkAndCompareDate(TextView tvStartDate, TextView tvEndDate, boolean compareWithCurrentDate) {
        if (TCUtils.isEmpty(tvStartDate.getText().toString())) {
            ((TCMainActivity) getActiveActivity()).showBaseMessage(TCUtils.getString(R.string.please_select_time_start_coupon_validity));
            return;
        }

        if (TCUtils.isEmpty(tvEndDate.getText().toString())) {
            ((TCMainActivity) getActiveActivity()).showBaseMessage(TCUtils.getString(R.string.please_select_time_end_coupon_validity));
            return;
        }

        compareDate(tvStartDate, tvEndDate, compareWithCurrentDate);
    }

    @SuppressLint("SimpleDateFormat")
    private static void compareDate(TextView tvStartDate, TextView tvEndDate, boolean compareWithCurrentDate) {
        SimpleDateFormat dateFormat;
        if (TCUtils.getLanguageCode().equals(EnumMgr.LanguageAppSetting.English.getValue())) {
            dateFormat = new SimpleDateFormat(TCDateUtility.DateFormatDefinition.EEE_MMM_DD_YYYY_HH_MM_A.getFormat());
        } else {
            dateFormat = new SimpleDateFormat(TCDateUtility.DateFormatDefinition.DD_MM_YYYY_EEE_HH_MM_SS.getFormat());
        }

        try {
            long timeStart = dateFormat.parse(tvStartDate.getText().toString()).getTime();
            long timeEnd = dateFormat.parse(tvEndDate.getText().toString()).getTime();

            if (compareWithCurrentDate) {
                long timeCurrent = new Date().getTime();
                if (timeStart <= timeCurrent) {
                    ((TCMainActivity) getActiveActivity()).showBaseMessage(TCUtils.getString(R.string.please_select_start_date_after_current_date));
                    return;
                }
            }

            if (timeEnd <= timeStart) {
                ((TCMainActivity) getActiveActivity()).showBaseMessage(TCUtils.getString(R.string.please_select_start_date_before_selecting_end_date));
                return;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static void compareMiddleDate(TextView tvStartDate, TextView tvEndDate, TextView tvCompareDate) {
        SimpleDateFormat dateFormat;
        SimpleDateFormat dateFormatComape;
        if (TCUtils.getLanguageCode().equals(EnumMgr.LanguageAppSetting.English.getValue())) {
            dateFormat = new SimpleDateFormat(TCDateUtility.DateFormatDefinition.HH_MM_EEE_YYYY_MM_DD.getFormat());
            dateFormatComape = new SimpleDateFormat(TCDateUtility.DateFormatDefinition.EEE_MMM_DD_YYYY_HH_MM_A.getFormat());
        } else {
            dateFormat = new SimpleDateFormat(TCDateUtility.DateFormatDefinition.DD_MM_YYYY_EEE_HH_MM_SS.getFormat());
            dateFormatComape = new SimpleDateFormat(TCDateUtility.DateFormatDefinition.DD_MM_YYYY_EEE_HH_MM_SS.getFormat());
        }

        try {
            long timeStart = dateFormat.parse(tvStartDate.getText().toString()).getTime();
            long timeEnd = dateFormat.parse(tvEndDate.getText().toString()).getTime();
            long timeCompare = dateFormatComape.parse(tvCompareDate.getText().toString()).getTime();

            if (timeCompare <= timeStart || timeCompare >= timeEnd) {
                ((TCMainActivity) getActiveActivity()).showBaseMessage(TCUtils.getString(R.string.please_select_date_in_the_middle_validity_date));
                return;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static String formatHoursMinueFromString(String time, DateFormatDefinition fromFormat, DateFormatDefinition toFormat) {
        SimpleDateFormat date12Format = new SimpleDateFormat(fromFormat.format, Locale.ENGLISH);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat date24Format = new SimpleDateFormat(toFormat.format);
        String hours = "";
        try {
            hours = (date24Format.format(date12Format.parse(time)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return hours;
    }

}
