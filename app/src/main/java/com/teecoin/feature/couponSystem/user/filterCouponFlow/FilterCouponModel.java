package com.teecoin.feature.couponSystem.user.filterCouponFlow;

import java.util.ArrayList;

import static com.teecoin.utils.TCUtils.containsItemFilter;
import static com.teecoin.utils.TCUtils.getTextFilterToContains;

public class FilterCouponModel {
    private String id;
    private String textSearch;
    private String minDisCount;
    private String maxDisCount;
    private String minPrice;
    private String maxPrice;
    private String minCash;
    private String maxCash;
    private String dayRemain;
    private String startDay;
    private String endDay;
    private String sortType;
    private ArrayList<FilterCouponItem> filterCouponItems;

    public FilterCouponModel() {
        filterCouponItems = new ArrayList<>();
    }

    public FilterCouponModel(ArrayList<FilterCouponItem> list) {
        filterCouponItems = new ArrayList<>();
        filterCouponItems.addAll(list);
    }

    public String getFilterCouponString() {
        StringBuilder filterString = new StringBuilder();
        for (FilterCouponItem item : filterCouponItems) {
            filterString.append("&");
            filterString.append(item.getFilterCouponString());
        }
//        if (filterCouponItems.size() > 0) {
//            filterString.deleteCharAt(filterString.length() - 1); //remove last character "&"
//        }
        return filterString.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTextSearch() {
        return textSearch;
    }

    public void setTextSearch(String textSearch) {
        this.textSearch = textSearch;
    }

    public String getMinDisCount() {
        return minDisCount;
    }

    public void setMinDisCount(String minDisCount) {
        this.minDisCount = minDisCount;
    }

    public String getMaxDisCount() {
        return maxDisCount;
    }

    public void setMaxDisCount(String maxDisCount) {
        this.maxDisCount = maxDisCount;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getMinCash() {
        return minCash;
    }

    public void setMinCash(String minCash) {
        this.minCash = minCash;
    }

    public String getMaxCash() {
        return maxCash;
    }

    public void setMaxCash(String maxCash) {
        this.maxCash = maxCash;
    }

    public String getDayRemain() {
        return dayRemain;
    }

    public void setDayRemain(String dayRemain) {
        this.dayRemain = dayRemain;
    }

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public String getEndDay() {
        return endDay;
    }

    public void setEndDay(String endDay) {
        this.endDay = endDay;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }
    public int getNumberFilter(FilterCouponModel filterCouponModel){
        int number = 0;
        if(filterCouponModel==null)
            return number;
        if(containsItemFilter(filterCouponModel, FilterCouponItem.ENDING_SOON))
            number++;
        if(containsItemFilter(filterCouponModel, FilterCouponItem.LATEST))
            number++;
        if(containsItemFilter(filterCouponModel, FilterCouponItem.POPULAR))
            number++;
        if(containsItemFilter(filterCouponModel, FilterCouponItem.RECOMMENDED_COUPON))
            number++;
        if(containsItemFilter(filterCouponModel, FilterCouponItem.LOW_TO_HIGH))
            number++;
        if(containsItemFilter(filterCouponModel, FilterCouponItem.HIGH_TO_LOW))
            number++;
        if(containsItemFilter(filterCouponModel, getTextFilterToContains(FilterCouponItem.MIN_PRICE)))
            number++;
        if(containsItemFilter(filterCouponModel, getTextFilterToContains(FilterCouponItem.MAX_PRICE)))
            number++;
        if(containsItemFilter(filterCouponModel, getTextFilterToContains(FilterCouponItem.DISCOUNT)))
            number++;

        return number;

    }
}
