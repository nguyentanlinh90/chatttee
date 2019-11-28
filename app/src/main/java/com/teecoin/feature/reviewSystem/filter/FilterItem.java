package com.teecoin.feature.reviewSystem.filter;

import com.teecoin.R;
import com.teecoin.utils.TCUtils;

public class FilterItem {
    private static final String FIVE_STAR = "5";
    private static final String FOUR_STAR = "4";
    private static final String THREE_STAR = "3";
    private static final String TWO_STAR = "2";
    private static final String ONE_STAR = "1";
    private static final String FILTER_STRING_STAR = "ratings[]=%s";
    private static final String FILTER_STRING_CUISINE = "cuisines[]=%s";
    private static final String ASCENDING = "asc";
    private static final String DESCENDING = "desc";
    private static final String FILTER_STRING_SORT_DATE = "sort_date=%s";
    private static final String FILTER_STRING_SORT_RATE = "sort_rate=%s";
    private static final String FILTER_STRING_FROM_TO_DATE = "from_date=%s&to_date=%s";
    private static final String FILTER_STRING_KEYWORD = "keyword=%s";
    private static final String FILTER_STRING_OPEN_DATE = "open_date=%s";
    private static final String FILTER_STRING_OPTION = "options[]=%s";
    private static final String NEAR_ME = "0";
    private static final String NEW_MERCHANT = "1";
    private static final String FILTER_STRING_LAT = "lat=%s";
    private static final String FILTER_STRING_LONG = "long=%s";
    private static final String FILTER_DISTANCE = "distance=%s";
    private static final String FILTER_PAGE = "page=%s";
    private static final String FILTER_CUISINE_ID = "cuisine=%s";
    private FilterModel.Filter filter;
    private String name;
    private int value;
    private String filterString;
    private String fromDate;
    private String toDate;

    public FilterItem(FilterModel.Filter filter, String fromDate, String toDate) {
        this.filter = filter;
        switch (filter) {
            case FromDate_ToDate:
                name = String.format("From %s to %s", fromDate, toDate);
                filterString = String.format(FILTER_STRING_FROM_TO_DATE, fromDate, toDate);
                setFromDate(fromDate);
                setToDate(toDate);
                break;
        }
    }

    public FilterItem(FilterModel.Filter filter, String data) {
        this.filter = filter;
        switch (filter) {
            case OpeningTimes:
                name = data;
                filterString = String.format(FILTER_STRING_OPEN_DATE, data);
                break;
            case Keyword:
                name = data;
                filterString = String.format(FILTER_STRING_KEYWORD, data);
                break;
            case Latitude:
                filterString = String.format(FILTER_STRING_LAT, data);
                break;
            case Longitude:
                filterString = String.format(FILTER_STRING_LONG, data);
                break;
            case Distance:
                name = String.format(TCUtils.getString(R.string.review_filter_distance), data);
                value = Integer.parseInt(data);
                filterString = String.format(FILTER_DISTANCE, data);
                break;
            case Page:
                filterString = String.format(FILTER_PAGE, data);
                break;
            case Cuisine:
                filterString = String.format(FILTER_CUISINE_ID, data);
                break;
            case Cuisines:
                filterString = String.format(FILTER_STRING_CUISINE, data);
                break;
        }
    }

    public FilterItem(FilterModel.Filter filter) {
        this.filter = filter;
        switch (filter) {
            case OneStar:
                name = String.format(TCUtils.getString(R.string.star), ONE_STAR);
                filterString = String.format(FILTER_STRING_STAR, ONE_STAR);
                break;
            case TwoStar:
                name = String.format(TCUtils.getString(R.string.stars), TWO_STAR);
                filterString = String.format(FILTER_STRING_STAR, TWO_STAR);
                break;
            case ThreeStar:
                name = String.format(TCUtils.getString(R.string.stars), THREE_STAR);
                filterString = String.format(FILTER_STRING_STAR, THREE_STAR);
                break;
            case FourStar:
                name = String.format(TCUtils.getString(R.string.stars), FOUR_STAR);
                filterString = String.format(FILTER_STRING_STAR, FOUR_STAR);
                break;
            case FiveStar:
                name = String.format(TCUtils.getString(R.string.stars), FIVE_STAR);
                filterString = String.format(FILTER_STRING_STAR, FIVE_STAR);
                break;
            case SortDate_NewestFirst:
                name = TCUtils.getString(R.string.review_filter_sort_date_newest_first);
                filterString = String.format(FILTER_STRING_SORT_DATE, DESCENDING);
                break;
            case SortDate_MostReviews:
                name = TCUtils.getString(R.string.review_filter_sort_date_most_reviews);
                filterString = String.format(FILTER_STRING_SORT_DATE, DESCENDING);
                break;
            case SortDate_OldestFirst:
                name = TCUtils.getString(R.string.review_filter_sort_date_oldest_first);
                filterString = String.format(FILTER_STRING_SORT_DATE, ASCENDING);
                break;
            case SortDate_LowestReviews:
                name = TCUtils.getString(R.string.review_filter_sort_date_lowest_reviews);
                filterString = String.format(FILTER_STRING_SORT_DATE, ASCENDING);
                break;
            case SortRate_HighestRated:
                name = TCUtils.getString(R.string.review_filter_sort_rate_highest);
                filterString = String.format(FILTER_STRING_SORT_RATE, DESCENDING);
                break;
            case SortRate_LowestRated:
                name = TCUtils.getString(R.string.review_filter_sort_rate_lowest);
                filterString = String.format(FILTER_STRING_SORT_RATE, ASCENDING);
                break;
            case NearMe:
                name = String.format(TCUtils.getString(R.string.review_filter_near_me), NEAR_ME);
                filterString = String.format(FILTER_STRING_OPTION, NEAR_ME);
                break;
            case NewMerchant:
                name = String.format(TCUtils.getString(R.string.text_new_merchant), NEW_MERCHANT);
                filterString = String.format(FILTER_STRING_OPTION, NEW_MERCHANT);
                break;
        }
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public FilterModel.Filter getFilter() {
        return filter;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public String getFilterString() {
        return filterString;
    }
}
