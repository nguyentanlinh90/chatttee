package com.teecoin.utils;

import android.Manifest;

import com.google.android.gms.maps.model.LatLng;

import io.michaelrocks.paranoid.Obfuscate;

@Obfuscate
public class TCConstant {
    public static final String BLANK = "";
    public static final String TOKEN = "token ";

    public static final String sharePreferenceName = "sharePrefenceFile";
    public static final String FONT_PATH = "fonts/";

    public static final String DRAWABLE = "drawable";
    public static final String ICON_CONVENIENCE_PREFIX = "ic_convenience_";

    /**
     * Password Regular Expression <br>
     * ^                 # start-of-string <br>
     * (?=.*[A-Z])       # an upper case letter must occur at least once <br>
     * (?=\S+$)          # no whitespace allowed in the entire string <br>
     * .{8,}             # anything, at least eight places though <br>
     * $                 # end-of-string
     */
    public static final String PASS_REGULAR_EXPRESSION = "^(?=.*[A-Z])(?=\\S+$).{8,}$";

    /**
     * EMAIL_REGULAR_EXPRESSION
     * use from https://emailregex.com/
     */
    public static final String EMAIL_REGULAR_EXPRESSION = "(?:[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?\\.)+[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[A-Za-z0-9-]*[A-Za-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    public static final String COPY_LABEL = "SecretToken";
    public static final int WALLET_MAX_TRANSACTION_SIZE = 5;
    public static final double TEECOIN_FEE_RATE = (double) 1 / 100;
    public static final double TEECOIN_FEE = 0.01;
    public static final double MINIMUM_AMOUNT_WITHDRAW = 1.00;
    public static final int ZERO = 0;
    public static final int ROUND_TWO_DECIMAL = 2;
    public static final int ROUND_THREE_DECIMAL = 3;
    public static final int ROUND_FOUR_DECIMAL = 4;
    public static final int ROUND_FIVE_DECIMAL = 5;
    public static final int ROUND_SEVEN_DECIMAL = 7;
    public static final String ONE_DECIMAL_FORMAT = "#,###,###,###.#";
    public static final String TWO_DECIMAL_FORMAT = "#,###,###,###.##";
    public static final String TWO_DECIMAL_NO_COMMAS_FORMAT = "##########.##";
    public static final String THREE_DECIMAL_FORMAT = "#,###,###,###.###";
    public static final String THREE_DECIMAL_NO_COMMAS_FORMAT = "##########.###";
    public static final String FOUR_DECIMAL_FORMAT = "#,###,###,###.####";
    public static final String FOUR_DECIMAL_NO_COMMAS_FORMAT = "#########.####";
    public static final String FIVE_DECIMAL_FORMAT = "#,###,###,###.#####";
    public static final String FIVE_DECIMAL_NO_COMMAS_FORMAT = "##########.#####";
    public static final String SEVEN_DECIMAL_FORMAT = "#,###,###,###.#######";//double: 0.0001000 --> string: 0.0001
    public static final String SEVEN_DECIMAL_NO_COMMAS_FORMAT = "##########.#######";//double: 0.0001000 --> string: 0.0001

    public static final String FIVE_DECIMAL_FULL_FORMAT = "#,###,###,##0.00000";
    public static final String SEVEN_DECIMAL_FULL_FORMAT = "#,###,###,##0.0000000";
    public static final String THREE_DECIMAL_FULL_FORMAT = "#,###,###,##0.000";
    public static final String TWO_DECIMAL_FULL_FORMAT = "#,###,###,##0.00";

    public static final String URL_TERMS = "https://tee-coin.com/termsofuse/";
    public static final String URL_EARN_TEC = "https://page.chattee.reviews/earntec/";
    public static final String URL_AGODA_WEBSITE = "https://www.agoda.com/chattee";

    public static final int LIMIT_DECIMAL_THREE = 3;
    public static final int LIMIT_DECIMAL_FIVE = 5;
    public static final int LIMIT_DECIMAL_TWO = 2;
    public static final int MAX_MEDIA_UPLOAD_FILES = 5;
    public static final int COLUMN_SHOW_PICK_IMAGE = 3;
    public static final int COLUMN_TW0_IMAGE = 2;
    public static final double COIN_RECEIVE_AFTER_REVIEW = 0.002;
    public static final int LIMIT_DECIMAL_SEVEN = 7;
    public static final int LIMIT_DECIMAL_NINE = 9;
    public static final int MAX_REGISTRATION_TIMES = 3;
    public static final float MAP_ZOOM_DEFAULT = 17;
    public static final String TEE_COIN_FOR_REVIEW = "0.0000001";
    public static final String TIP_FOR_REVIEW = "0.002";
    public static final double LIKE_AND_TIP_FEE_AMOUNT = 0.01;
    public static final int DEFAULT_MAX_PAYMENT_RATE = 100; //100%
    public static final int DEFAULT_MIN_COIN_BACK_REWARD_PERCENT = 1; //1%
    public static final int MAX_VIDEO_RECORD_TIME = 10 * 1000; //10 seconds in millisecond
    public static final int ONE_SECOND_IN_MILLISECOND = 1 * 1000; //1 seconds in millisecond
    public static final int SIXTY_SECOND_IN_MILLISECOND = 60 * 1000; //60 seconds in millisecond
    public static final int SIX_HUNDRED_SECOND_IN_MILLISECOND = 600 * 1000; //60 seconds in millisecond
    public static final int TEN_SECOND_IN_MILLISECOND = 10 * 1000; //10 seconds in millisecond
    public static final long MAX_VIDEO_SIZE = 50 * 1024 * 1024; //50 MB
    public static final long IMAGE_SIZE_10M = 10 * 1024 * 1024; //10 MB
    public static final double MILLIONAIRE_AMOUNT = 1000000;

    public static final double DISTANT_MIN_TO_LOAD_MORE_PLACE = 2000.0;
    public static final String DEFAULT_SHOP_AVATAR_URL = "https://s3-ap-southeast-1.amazonaws.com/teecoin-test/images/placeholders/lg_shop_icon.png";
    public static final String DEFAULT_USER_AVATAR_URL = "https://s3-ap-southeast-1.amazonaws.com/teecoin-test/images/placeholders/user_icon.png";
    public static final String DEFAULT_FAQ_URL = "https://tee-coin.com/faq/";
    public static final String DEFAULT_SUPPORT_URL = "https://page.chattee.reviews/contact-us/";
    public static final String TOP_UP_PROFILE_VERIFY = "https://tee-coin.com/kyc-form/";
    public static final String SHOP_REGISTER = "https://docs.google.com/forms/d/e/1FAIpQLScgXcxy1nymvJJg9MAkBW4_MlJA0G5buWePNMgwipuJK7sd_g/viewform";

    //    public static final LatLng LOCATION_DEFAULT = new LatLng(1.3098767999999998, 103.7775006);// center singapore
    public static final LatLng LOCATION_DEFAULT = new LatLng(1.2814802, 103.8497209);// center singapore
    public static final String KM_DEFAULT = "5";
    public static final int LIST_DEFAULT_5 = 5;
    public static final int LIST_DEFAULT_10 = 10;
    public static final int VIEW_PAGER_LIMIT_PAGE = 2;
    public static final int VIEW_PAGER_LIMIT_PAGE_THREE = 3;

    public static final String[] CAMERA_WRITE_EXTERNAL_PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final String[] LOCATION_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};

    //    public static String SHARE_SOCIAL_LINK_DEFAULT = "https://review-dev.tee-coin.com/restaurant/detail/4437";
    public static final String VENDOR_UNKNOWN = "Unknown";

    public static final String LAT_LNG_CURRENT = "lat_lng_current";
    public static final String EXCHANGE_CODE_USD = "USD";
    //    public static final String EXCHANGE_CODE_SGD = "SGD";
    public static final String DATE_TIME_PICKER_FROM_FILTER = "DATE_TIME_PICKER_FROM_FILTER";
    public static final String DATE_TIME_PICKER_FROM_COUPON = "DATE_TIME_PICKER_FROM_COUPON";
    public static final String OPEN_LIBRARY_FROM_USER_REVIEW = "OPEN_LIBRARY_FROM_USER_REVIEW";
    public static final String OPEN_LIBRARY_FROM_ADD_COUPON = "OPEN_LIBRARY_FROM_ADD_COUPON";
    public static final String SCAN_QR_CODE_WITH_VALIDATE_CODE = "SCAN_QR_CODE_WITH_VALIDATE_CODE";
    public static final String SCAN_QR_CODE_FOR_TRANSFER_MONEY = "SCAN_QR_CODE_FOR_TRANSFER_MONEY";
    public static final String SCAN_QR_CODE_FOR_PAYMENT = "SCAN_QR_CODE_FOR_PAYMENT";
    public static final String KEY_PUT_TO_IMPORT_WALLET = "KEY_PUT_TO_IMPORT_WALLET";
    public static final int REVIEW_MIN_CHARACTERS = 50;

    public static final String MEDIA_FIELD = "files";
    public static final int MAX_REQUEST_LOCATION_TIMES = 3;
    public static final String PARAM_GET_LIST_FAVORITE = "%s&lat=%s&long=%s";
    public static final String TAG_GOOD_DEALS = "1";
    public static final String TAG_COUPONS = "2";
    public static final String TAG_NEW_ARRIVALS = "3";
    public static final String KEYWORD = "KEYWORD";
    public static final String TAG_SUCCESS = "Success";
    public static final String TAG_SUCCESS_UPPERCASE = "SUCCESS";
    public static final String TAG_FAILED = "FAILED";
    public static final String TAG_PAYMENT_UNKNOWN = "UNKNOWN";
    public static final String TAG_PAYMENT_PROCESSING = "PROCESSING";
    public static final String PAYMENT_TRY_AGIAN = "PAYMENT_TRY_AGIAN";
    public static final int DEFAULT_SEARCH_RECENT = 5;
    public static final int PAGE_SIZE_10 = 10;
    public static final int PAGE_SIZE_30 = 30;
    public static final String HTTPS_CHATTEE_PAGE_LINK = "https://chattee.page.link";
    public static final String HTTP_CHATTEE_PAGE_LINK = "http://chattee.page.link";
    public static final String BANNER_ID = "tec_banner_id=";
    public static final String CHATTEE = "chattee";
    public static final long TIME_COUPON_RELOAD = 100000;
    public static String[] VENDOR_INFO;
    public static final String INPUT_WALLET = "INPUT_WALLET";
    public static final String USER_PAYMENT_REVIEW = "PAYMENT_REVIEW";
    public static final int SEND_MONEY_NOTE_MAX_LENGTH_IN_BYTES = 26;  //26 bytes
}
