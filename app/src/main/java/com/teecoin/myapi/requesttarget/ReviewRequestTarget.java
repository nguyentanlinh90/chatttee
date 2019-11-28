package com.teecoin.myapi.requesttarget;

public enum ReviewRequestTarget implements BaseRequestTarget {

    UPLOAD_COMMENT_IMAGE {
        @Override
        public String toString() {
            return "comments/comment_image/";
        }
    },
    UPLOAD_COMMENT_VIDEO {
        @Override
        public String toString() {
            return "comments/comment_video/";
        }
    },

    SUBMIT_USER_REVIEW_SHOP {
        @Override
        public String toString() {
            return "clients/%s/comment/";
        }
    },
    GET_LOGO_REVIEW_SHOP {
        @Override
        public String toString() {
            return "clients/%s/comment/?destination=%s";
        }
    },
    GET_LIST_HIGHLIGHT {
        @Override
        public String toString() {
            return "vendors/highlight_vendors/";
        }
    },

    VENDOR_CATEGORIES {
        @Override
        public String toString() {
            return "vendors/categories/";
        }
    },
    GET_RECOMMEND_FOR_YOU {// todo: still using some screens
        @Override
        public String toString() {
            return "vendors/get_vendor_list/?page=%s";
        }
    },
    GET_RECOMMENDED_FOR_YOU {// todo: new change

        @Override
        public String toString() {
            return "vendors/recommended_for_you/?page=%s";
        }
    },

    GET_LIST_REVIEWS {
        @Override
        public String toString() {
            return "comments/list_review/?uuid=%s&page=%d";
        }
    },

    GET_NEW_MERCHANTS {
        @Override
        public String toString() {
            return "vendors/get_vendors/?page=%s&distance=%s&options[]=%s&tz=%s&lat=%s&long=%s";
        }
    },
    GET_TOP_MOST_REVIEWS {
        @Override
        public String toString() {
            return "vendors/top_most_reviews/";
        }
    },
    GET_CUISINES {
        @Override
        public String toString() {
            return "vendors/common_cuisines/?lat=%s&long=%s";
        }
    },
    CHECK_REFERRAL_CODE {
        @Override
        public String toString() {
            return "events/referral_validate/";
        }
    },
    GET_REFERRAL_INFORMATION {
        @Override
        public String toString() {
            return "clients/%s/referral_information/?page=%s";
        }
    },
    GET_REFERRAL_INFORMATION_FOR_SHOP {
        @Override
        public String toString() {
            return "shops/%s/referral_information/?page=%s";
        }
    },
    GET_LIST_CUISINES_BY_ID {
        @Override
        public String toString() {
            return "vendors/get_vendors/?page=%s&lat=%s&long=%s&distance=%s&cuisine=%s&tz=%s";
        }
    },
    GET_VENDORS_LAT_LNG {
        @Override
        public String toString() {
            return "vendors/get_vendors/?page=%s&lat=%s&long=%s&distance=%s&options[]=0&tz=%s";
        }
    },
    GET_VENDORS {
        @Override
        public String toString() {
            return "vendors/get_vendors/?page=%s&distance=%s&tz=%s";
        }
    },
    GET_FILTER_REVIEW {
        @Override
        public String toString() {
            return "comments/list_review/?%s";
        }
    },
    GET_VENDOR_DETAIL {
        @Override
        public String toString() {
            return "vendors/%s/detail/?tz=%s";
        }
    },
    GET_TIP_HISTORY_DETAIL {
        @Override
        public String toString() {
            return "transactions/tips/%s/";
        }
    },
    GET_FILTER_RESTAURANT {
        @Override
        public String toString() {
            return "vendors/get_vendors/?%s";
        }
    },
    GET_VENDOR_REVIEWS {
        @Override
        public String toString() {
            return "vendors/%s/reviews/?uuid=%s&page=%d";
        }
    },
    GET_WALLET_VENDOR {
        @Override
        public String toString() {
            return "vendors/%s/get_wallet/";
        }
    },
    GET_PROMOTION {
        @Override
        public String toString() {
            return "promotions/promotion_list/";
        }
    },

    USER_GET_NOTIFY {
        @Override
        public String toString() {
            return "clients/%s/notifications/?page=%d";
        }
    },
    USER_GET_NOTIFY_FILTER {
        @Override
        public String toString() {
            return "clients/%s/notifications/?type=%d&page=%d";
        }
    },
    SHOP_GET_NOTIFY {
        @Override
        public String toString() {
            return "shops/%s/notifications/?page=%d";
        }
    },
    SHOP_GET_NOTIFY_FILTER {
        @Override
        public String toString() {
            return "shops/%s/notifications/?type=%d&page=%d";
        }
    },

    CREATE_TIP {
        @Override
        public String toString() {
            return "transactions/tips/";
        }
    },

    SHOP_GET_VENDOR {
        @Override
        public String toString() {
            return "shops/get_vendor/%s/";
        }
    },

    USER_LIKE_VENDOR {
        @Override
        public String toString() {
            return "clients/%s/like/";
        }
    },
    GET_LIST_EVENT {
        @Override
        public String toString() {
            return "events/get_event_list/";
        }
    },
    GET_VENDOR_ID {
        @Override
        public String toString() {
            return "shops/get_vendor/%s/";
        }
    },

    SUBMIT_VENDOR_FAVORITE {
        @Override
        public String toString() {
            return "clients/%s/favorites/vendors/";
        }
    },
    GET_VENDOR_FAVORITE_LIST {
        @Override
        public String toString() {
            return "clients/%s/favorites/vendors/%s";
        }
    },
    GET_VENDOR_RECENT_LIST {
        @Override
        public String toString() {
            return "clients/%s/recent_vendors/%s";
        }
    },
    GET_VENDOR_LIST {
        @Override
        public String toString() {
            return "vendors/get_vendor_list/?page=%s";
        }
    },
    GET_VENDOR_LIST_BY_CATEGORY {
        @Override
        public String toString() {
            return "vendors/get_vendor_list_by_category/?page=%s";
        }
    },
    GET_LINK_SHARE_VENDOR {
        @Override
        public String toString() {
            return "https://api-dev.tee-coin.com/v3/vendors/%s/share/";
        }
    },
    GET_GOOGLE_REVIEW {
        @Override
        public String toString() {
            return "vendors/%s/place_details/";
        }
    },
    GET_REVIEW_STATUS {
        @Override
        public String toString() {
            return "vendors/%s/get_review_status/";
        }
    },
    GET_SUGGEST_VENDOR {
        @Override
        public String toString() {
            return "vendors/suggest/?keyword=%s&country_code=%s";
        }
    },
    GET_SUGGEST_COUPON {
        @Override
        public String toString() {
            return "coupons/suggest/?keyword=%s&country_code=%s";
        }
    },
    GET_KEY_SEARCH_RECENT_LIST {
        @Override
        public String toString() {
            return "clients/%s/recent_search/";
        }
    },
    GET_COMMENT_DETAIL {
        @Override
        public String toString() {
            return "comments/%s/detail/?uuid=%s";
        }
    },
    REVIEW_GET_VISIT_TYPE {
        @Override
        public String toString() {
            return "comments/get_visit_types/";
        }
    },
    GET_LIST_IMAGES_VENDOR {
        @Override
        public String toString() {
            return "vendors/%s/images/?page=%s&with_review=%s";
        }
    },
    GET_LIST_CHECKIN_REWARDS {
        @Override
        public String toString() {
            return "vendors/checkin_rewards/?page=%s&section=checkin";
        }
    },
    GET_LIST_CHECKIN_REWARDS_BY_CATEGORY {
        @Override
        public String toString() {
            return "vendors/checkin_rewards_by_category/?section=checkin&page=%s";
        }
    },

    GET_LIST_ARTICLES {
        @Override
        public String toString() {
            return "events/articles/?page=%s&country_code=%s";
        }
    },
    DISCOVER_BANNERS {
        @Override
        public String toString() {
            return "events/discover_banners/?country_code=%s";
        }
    },


}
