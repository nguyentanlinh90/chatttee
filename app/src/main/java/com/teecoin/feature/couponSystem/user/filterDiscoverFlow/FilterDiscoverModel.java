package com.teecoin.feature.couponSystem.user.filterDiscoverFlow;

import com.teecoin.model.couponsystem.VendorCategoryModel;
import com.teecoin.utils.EnumMgr;

import java.io.Serializable;

public class FilterDiscoverModel implements Serializable {

    public static final String TAG_DISTANCE_HAFT_KM = "0.5";
    public static final String TAG_DISTANCE_ONE_KM = "1";
    public static final String TAG_DISTANCE_ONE_POINT_FIVE_KM = "1.5";
    public static final String TAG_DISTANCE_TWO_KM = "2";

    public static final String TAG_PRICE_RANGE_ONE = "1";
    public static final String TAG_PRICE_RANGE_TWO = "2";
    public static final String TAG_PRICE_RANGE_THREE = "3";
    public static final String TAG_PRICE_RANGE_FOUR = "4";

    public static final String TAG_RATING = "&ratings[]=%s";
    public static final String TAG_ONE_STAR = "1";
    public static final String TAG_TWO_STAR = "2";
    public static final String TAG_THREE_STAR = "3";
    public static final String TAG_FOUR_STAR = "4";
    public static final String TAG_FIVE_STAR = "5";


    private VendorCategoryModel categoryModel;
    private String keyword = "";
    private String country_code = "";
    private String distance = "";
    private String open_date = "";
    private String category = "";
    private String price_range = "";
    private String sort_condition = "";
    private String ratings = "";
    private FilterOpenTimeType filterOpenTimeType;

    public FilterDiscoverModel() {
    }

    public VendorCategoryModel getCategoryModel() {
        return categoryModel;
    }

    public void setCategoryModel(VendorCategoryModel categoryModel) {
        this.categoryModel = categoryModel;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getOpen_date() {
        return open_date;
    }

    public void setOpen_date(String open_date) {
        this.open_date = open_date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice_range() {
        return price_range;
    }

    public void setPrice_range(String price_range) {
        this.price_range = price_range;
    }

    public String getSort_condition() {
        return sort_condition;
    }

    public void setSort_condition(String sort_condition) {
        this.sort_condition = sort_condition;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public FilterOpenTimeType getFilterOpenTimeType() {
        return filterOpenTimeType;
    }

    public void setFilterOpenTimeType(FilterOpenTimeType filterOpenTimeType) {
        this.filterOpenTimeType = filterOpenTimeType;
    }

    public void clearCondition() {
        this.keyword = "";
        this.distance = "";
        this.open_date = "";
        this.price_range = "";
        this.sort_condition = "";
        this.ratings = "";
        this.filterOpenTimeType = FilterOpenTimeType.None;
    }


    public enum FilterOpenTimeType {
        None,
        OpenNow,
        SpecificOpeningTime
    }

    public int getNumberFilter(FilterDiscoverModel filterDiscoverModel) {
        int number = 0;
        if (filterDiscoverModel == null) {
            return number;
        }

        if (filterDiscoverModel.getPrice_range().equals(FilterDiscoverModel.TAG_PRICE_RANGE_ONE))
            number++;
        if (filterDiscoverModel.getPrice_range().equals(FilterDiscoverModel.TAG_PRICE_RANGE_TWO))
            number++;
        if (filterDiscoverModel.getPrice_range().equals(FilterDiscoverModel.TAG_PRICE_RANGE_THREE))
            number++;
        if (filterDiscoverModel.getPrice_range().equals(FilterDiscoverModel.TAG_PRICE_RANGE_FOUR))
            number++;
        if (filterDiscoverModel.getPrice_range().equals(FilterDiscoverModel.TAG_DISTANCE_HAFT_KM))
            number++;
        if (filterDiscoverModel.getPrice_range().equals(FilterDiscoverModel.TAG_DISTANCE_ONE_KM))
            number++;
        if (filterDiscoverModel.getPrice_range().equals(FilterDiscoverModel.TAG_DISTANCE_ONE_POINT_FIVE_KM))
            number++;
        if (filterDiscoverModel.getPrice_range().equals(FilterDiscoverModel.TAG_DISTANCE_TWO_KM))
            number++;
        String[] ratings = filterDiscoverModel.getRatings().split("&");
        if (ratings.length > 0) {
            for (String r : ratings) {
                if (r.contains(FilterDiscoverModel.TAG_ONE_STAR))
                    number++;
                if (r.contains(FilterDiscoverModel.TAG_TWO_STAR))
                    number++;
                if (r.contains(FilterDiscoverModel.TAG_THREE_STAR))
                    number++;
                if (r.contains(FilterDiscoverModel.TAG_FOUR_STAR))
                    number++;
                if (r.contains(FilterDiscoverModel.TAG_FIVE_STAR))
                    number++;

            }
        }
        String selectedSortCondition = filterDiscoverModel.getSort_condition();
        if (selectedSortCondition.equals(EnumMgr.SortCondition.NearMe.getValue())) {
            number++;
        } else if (selectedSortCondition.equals(EnumMgr.SortCondition.NewMerchant.getValue())) {
            number++;
        } else if (selectedSortCondition.equals(EnumMgr.SortCondition.HighestRating.getValue())) {
            number++;
        } else if (selectedSortCondition.equals(EnumMgr.SortCondition.TopRatedExperiences.getValue())) {
            number++;
        } else if (selectedSortCondition.equals(EnumMgr.SortCondition.HighToLowPrice.getValue())) {
            number++;
        } else if (selectedSortCondition.equals(EnumMgr.SortCondition.LowToHighPrice.getValue())) {
            number++;
        }
        if (filterDiscoverModel.getFilterOpenTimeType() != null) {
            if (filterDiscoverModel.getFilterOpenTimeType().equals(FilterDiscoverModel.FilterOpenTimeType.OpenNow)) {
                number++;
            } else if (filterDiscoverModel.getFilterOpenTimeType().equals(FilterDiscoverModel.FilterOpenTimeType.SpecificOpeningTime)) {
                number++;
            }
        }


        return number;
    }
}
