package com.teecoin.myapi.requesttarget;

public enum CouponRequestTarget implements BaseRequestTarget {


    GET_COINBACK_REDEMPTION {
        @Override
        public String toString() {
            return "coupons/coinback_redemption/";
        }
    },

    GET_DAILY_REWARD {
        @Override
        public String toString() {
            return "events/daily_giveaway/";
        }
    },

    TOP_RATED_EXPERIENCES {
        @Override
        public String toString() {
            return "vendors/get_vendor_list/?page=%s";
        }
    },

    COUPON_USER_GET_COUPON_CATALOGUES {
        @Override
        public String toString() {
            return "coupons/recommendations/categories/?%s";
        }
    },
    COUPON_USER_GET_COUPON_CATALOGUES_LIST {
        @Override
        public String toString() {
            return "coupons/catalogues/category/%s/?page=%s";
        }
    },
    COUPON_USER_GET_COUPON_LIST {
        @Override
        public String toString() {
            return "clients/%s/coupons/?page=%d";
        }
    },
    COUPON_USER_GET_COUPON_CATALOGUE_DETAIL {
        @Override
        public String toString() {
            return "coupons/catalogues/%s/?serial=%s";
        }
    },
    COUPON_USER_GET_COUPON_EXPIRED_REDEEMED_LIST {
        @Override
        public String toString() {
            return "clients/%s/coupons/?page=%d&active=%d";
        }
    },
    COUPON_USER_GET_COUPON_NOTIFICATION_DETAIL {
        @Override
        public String toString() {
            return "coupons/notifications/%s/?serial=%s";
        }
    },
    COUPON_USER_GET_COUPON {
        @Override
        public String toString() {
            return "coupons/notifications/%s/get/";
        }
    },
    COUPON_USER_GET_COUPON_FILTER {
        @Override
        public String toString() {
            return "clients/%s/coupons/?%s";
        }
    },
    COUPON_SHOP_GET_COUPON_FILTER {
        @Override
        public String toString() {
            return "shops/%s/coupons/?%s";
        }
    },
    COUPON_USER_GET_COUPON_FILTER_CATALOGUES {
        @Override
        public String toString() {
            return "coupons/catalogues/category/%s/?%s&page=%s";
        }
    },
    COUPON_USER_REDEEM_COUPON {
        @Override
        public String toString() {
            return "coupons/%s/%s/redeem/";
        }
    },

    COUPON_USER_GET_COUPON_CHECK_IN_LIST {
        @Override
        public String toString() {
            return "clients/%s/check_in/";
        }
    },
    COUPON_USER_CATALOGUE_PURCHASE {
        @Override
        public String toString() {
            return "transactions/coupons/";
        }
    },
    COUPON_USER_CHECK_INS_NOTIFICATION_DETAIL {
        @Override
        public String toString() {
            return "transactions/check_ins/%s/";
        }
    },
    COUPON_USER_GET_COUPON_DETAIL_FROM_TRANSACTION {
        @Override
        public String toString() {
            return "transactions/coupons/%s/?serial=%s";
        }

    },
    COUPON_SHOP_ADD_COUPON {
        @Override
        public String toString() {
            return "shops/%s/coupons/";
        }
    },
    COUPON_USER_GET_VENDOR_COUPON {
        @Override
        public String toString() {
            return "vendors/%s/coupons/";
        }
    },
    COUPON_SHOP_GET_COUPON_LIST {
        @Override
        public String toString() {
            return "shops/%s/coupons/?page=%d";
        }
    },
    COUPON_SHOP_INFO_QR_CODE {
        @Override
        public String toString() {
            return "shops/%s/get_vendor_code";
        }
    },

    COUPON_SHOP_GET_COUPON_DETAIL {
        @Override
        public String toString() {
            return "shops/%s/coupons/%d";
        }
    },

    COUPON_RECOMMEND {
        @Override
        public String toString() {
            return "coupons/recommendations/?page=%s";
        }
    },


    GET_OUTLET_LIST {
        @Override
        public String toString() {
            return "vendors/%s/outlets?page=%s&lat=%s&long=%s";
        }
    },

    GET_LIST_COUPON_DEFAULT {
        @Override
        public String toString() {
            return "coupons/catalogues/";
        }
    },

    GET_LIST_COUPON_SORT_TYPE {
        @Override
        public String toString() {
            return "coupons/catalogues/?%s";
        }
    },

    GET_GIVE_AWAY {
        @Override
        public String toString() {
            return "events/random_daily_giveaway/";
        }
    },

    GET_LIST_COUNTRY {
        @Override
        public String toString() {
            return "get_country_list/";
        }
    },

    GET_LIST_RECOMMMENDE_COUPON {
        @Override
        public String toString() { return "coupons/recommended_coupons/%s/?%s";
        }
    },
}
