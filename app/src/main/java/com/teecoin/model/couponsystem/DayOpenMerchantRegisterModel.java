package com.teecoin.model.couponsystem;

import com.teecoin.R;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

public class DayOpenMerchantRegisterModel {
    private String day;
    private boolean isOpen;
    private boolean isOpen24Hours;
    private String from;
    private String to;

    private ArrayList<TimeOpenForDayMerchantModel> listTimeOpen = new ArrayList<>();

    public static ArrayList<DayOpenMerchantRegisterModel> listDayOpenDefault() {
        ArrayList<DayOpenMerchantRegisterModel> listDayOpen = new ArrayList<>();
        DayOpenMerchantRegisterModel monday = new DayOpenMerchantRegisterModel();
        monday.setDay(TCUtils.getString(R.string.monday));
        monday.setOpen(true);

        DayOpenMerchantRegisterModel tuesday = new DayOpenMerchantRegisterModel();
        tuesday.setDay(TCUtils.getString(R.string.tuesday));
        tuesday.setOpen(true);

        DayOpenMerchantRegisterModel wednesday = new DayOpenMerchantRegisterModel();
        wednesday.setDay(TCUtils.getString(R.string.wednesday));
        wednesday.setOpen(true);

        DayOpenMerchantRegisterModel thursday = new DayOpenMerchantRegisterModel();
        thursday.setDay(TCUtils.getString(R.string.thursday));
        thursday.setOpen(true);

        DayOpenMerchantRegisterModel friday = new DayOpenMerchantRegisterModel();
        friday.setDay(TCUtils.getString(R.string.friday));
        friday.setOpen(true);


        DayOpenMerchantRegisterModel saturday = new DayOpenMerchantRegisterModel();
        saturday.setDay(TCUtils.getString(R.string.saturday));
        saturday.setOpen(true);

        DayOpenMerchantRegisterModel sunday = new DayOpenMerchantRegisterModel();
        sunday.setDay(TCUtils.getString(R.string.sunday));
        sunday.setOpen(true);

        listDayOpen.add(monday);
        listDayOpen.add(tuesday);
        listDayOpen.add(wednesday);
        listDayOpen.add(thursday);
        listDayOpen.add(friday);
        listDayOpen.add(friday);
        listDayOpen.add(saturday);
        listDayOpen.add(sunday);
        return listDayOpen;
    }

    public static ArrayList<DayOpenMerchantRegisterModel> listWeekensOpenDefault() {
        ArrayList<DayOpenMerchantRegisterModel> listWeedken = new ArrayList<>();

        DayOpenMerchantRegisterModel weekdays = new DayOpenMerchantRegisterModel();
        weekdays.setDay(TCUtils.getString(R.string.weekdays));
        weekdays.setOpen(true);

        DayOpenMerchantRegisterModel saturday = new DayOpenMerchantRegisterModel();
        saturday.setDay(TCUtils.getString(R.string.saturday));
        saturday.setOpen(false);

        DayOpenMerchantRegisterModel sunday = new DayOpenMerchantRegisterModel();
        sunday.setDay(TCUtils.getString(R.string.sunday));
        sunday.setOpen(false);

        listWeedken.add(weekdays);
        listWeedken.add(saturday);
        listWeedken.add(sunday);

        return listWeedken;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean isOpen24Hours() {
        return isOpen24Hours;
    }

    public void setOpen24Hours(boolean open24Hours) {
        isOpen24Hours = open24Hours;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public ArrayList<TimeOpenForDayMerchantModel> getListTimeOpen() {
        return listTimeOpen;
    }

    public void setListTimeOpen(ArrayList<TimeOpenForDayMerchantModel> listTimeOpen) {
        this.listTimeOpen = listTimeOpen;
    }
}
