package com.teecoin.feature.reviewSystem.filter;

import java.util.ArrayList;

public class FilterModel {

    private ArrayList<FilterItem> filterItems;

    public FilterModel(ArrayList<FilterItem> list) {
        filterItems = new ArrayList<>();
        filterItems.addAll(list);
    }

    public FilterModel() {
        filterItems = new ArrayList<>();
    }

    public ArrayList<FilterItem> getFilterList() {
        return filterItems;
    }

    public void remove(FilterItem filterItem) {
        if (filterItem.getFilter().equals(Filter.SortDate_NewestFirst) ||
                filterItem.getFilter().equals(Filter.SortDate_OldestFirst)) {
            filterItems.remove(filterItem);
        } else if (filterItem.getFilter().equals(Filter.SortRate_HighestRated) ||
                filterItem.getFilter().equals(Filter.SortRate_LowestRated)) {
            filterItems.remove(filterItem);
        } else {
            filterItems.remove(filterItem);
        }
    }

    public String getFilterString() {
        StringBuilder filterString = new StringBuilder();
        for (FilterItem item : filterItems) {
            filterString.append(item.getFilterString());
            filterString.append("&");
        }
        filterString.deleteCharAt(filterString.length() - 1); //remove last character "&"
        return filterString.toString();
    }

    public void removePageUpdate(ArrayList<FilterItem> list) {
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getFilter().equals(Filter.Page)) {
                    list.remove(list.get(i));
                    return;
                }
            }
        }
    }

    public enum Filter {
        Page,
        FromDate_ToDate,
        OneStar,
        TwoStar,
        ThreeStar,
        FourStar,
        FiveStar,
        SortDate_NewestFirst,
        SortDate_OldestFirst,
        SortDate_MostReviews,
        SortDate_LowestReviews,
        SortRate_HighestRated,
        SortRate_LowestRated,
        Keyword,
        NearMe,
        NewMerchant,
        OpeningTimes,
        Latitude,
        Longitude,
        Distance,
        Cuisine,
        Cuisines
    }

}
