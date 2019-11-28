package com.teecoin.feature.couponSystem.user.filterCouponFlow;

public class FilterCouponItem {
    public static final String INPUT_TEXT_SEARCH = "keyword=%s";
    public static final String ENDING_SOON = "sort_type=1";
    public static final String LATEST = "sort_type=2";
    public static final String POPULAR = "sort_type=3";
    public static final String LOW_TO_HIGH = "sort_type=4";
    public static final String HIGH_TO_LOW = "sort_type=5";
    public static final String MOST_EFFICIENT= "sort_type=6";
    public static final String RECOMMENDED_COUPON= "sort_type=7";
    public static final String MIN_PRICE = "min_price=%s";
    public static final String MAX_PRICE = "max_price=%s";
    public static final String BUY_1_GET_1 = "one_get_one=true";
    public static final String FREE_GIFT = "free_gift=true";
    public static final String DISCOUNT = "discount[]=%s";
    public static final String CASH = "currency=SGD";
    public static final String MIN_CASH = "min_cash=%s";
    public static final String MAX_CASH = "max_cash=%s";
    public static final String DAY_REMAIN = "day_remain=%s";
    public static final String START = "start=%s";
    public static final String END = "end=%s";
    public static final String RUNNING_NOW = "status=2";
    public static final String USED_UP = "status=3";
    public static final String EXPIRED = "status=4";

    private String filterCouponString;

    public FilterCouponItem(String filterCouponString) {
        this.filterCouponString = filterCouponString;
    }

    String getFilterCouponString() {
        return filterCouponString;
    }

    public enum SortType {
        ENDING_SOON(1),
        LATEST(2),
        POPULAR(3);
        private int value;

        SortType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}
