package com.teecoin.utils;

import com.teecoin.R;

public class EnumMgr {


    public enum ClickType {
        None,
        CoinBack,
        RemovePhoto,
        WriteReview,
        CouponShopMerchantOpenTime_RemoveItem,
        CouponShopMerchantOpenTime_EditItem,
        ReviewList_LikeClicked,
        Review_ShowReviewDetail,
        ViewAll,
        Vendor_WriteReview,
        Vendor_Share,
        Vendor_Favourite,
        Country,
        ImageReview,
        ShowWallet,
        ShowQrCode,
        CopyAddress,
        ShareTopUp
    }

    public enum EditTextValidType {
        Payment,
        Amount,
    }


    public enum RequestCode {
        CHOOSE_PHOTO(1001),
        READ_EXTERNAL_STORAGE(1002),
        WRITE_EXTERNAL_STORAGE(1003),
        SCAN_INVOICE_FOR_SEND_MONEY(1004),
        SCAN_INVOICE_FOR_REWARD(1005),
        SCAN_INVOICE_FOR_ORDER(1006),
        SCAN_USER_PUBLIC_KEY(1007),
        TURN_ON_GPS(1008),
        OPEN_CAMERA(1009),
        CAPTURE_VIDEO(1010),
        LOCATION(1011),
        APPLICATION_DETAIL_SETTING_REQUEST_CODE(1012),
        APPLICATION_DETAIL_SETTING_REQUEST_CODE_CHECK_IN(1030),
        CHANGE_LANGUAGE_REQUEST_CODE(1013),
        GOTO_MAP_FROM_RESTAURANT(1014),
        GOTO_RESTAURANT_FROM_LOCATION(1015),
        GOTO_MAP_FROM_HOME_REVIEW(1016),
        GOTO_GOOGLE_PLAY_UPDATE_APP(1017),
        GOTO_USER_REVIEW_SHOP(1018),
        GOTO_USER_REVIEW_SHOP_RESULT(1019),
        SCAN_COUPON_CODE(1020),
        SCAN_COUPON_CODE_CHECK_IN(1021),
        GOTO_COUPON_USER_MY_COUPON_DETAIL(1022),
        GOTO_COUPON_USER_PURCHASE_COUPON_RESULT(1023),
        GOTO_COUPON_USER_CATALOGUE_LIST(1024),
        GOTO_MAP_FROM_SEND_NOTIFICATION(1025),
        GOTO_SEARCH_COUPON(1026),
        GOTO_DETAIL_VENDOR_CATEGORY(1027),
        GOOGLE_LOGIN(1028),
        IMPORT_EXIST_WALLET(1029),
        GOTO_DAILY_REWARD(1030),
        SCAN_INVOICE_FOR_PAYMENT(1031),
        CONVERT_TO_TEC(1032);

        private int value;

        RequestCode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum PushNotification {
        Reward("0"),
        Payment("1"),
        CoinBack("2"),
        Tip("3"),
        TransferMoney("4"),
        Review("5"),
        Promotion("6"),
        Event("7"),
        Referral("8"),
        Coupon("9"),
        CheckInCoupon("10"),
        CatalogueCouponByCountry("11"),
        VendorDetail("12"),
        VendorCouponList("13"),
        CategoryCatalogue("14"),
        UserRegister("15"),
        Incoming("19"),
        ConvertToTEC("20"),
        WithdrawRequest("21"),
        WithdrawCompleted("22");

        private String value;

        PushNotification(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum TransactionType {
        Reward("0"),
        Payment("1"),
        CoinBack("2"),
        Tip("3"),
        TransferMoney("4"),
        Review("5"),
        Checkin("6"),
        CouponPurchase("7"),
        ConvertToTEC("8");

        private String value;

        TransactionType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum FiatTransactionType {

        Payment("1"),
        ConvertToTEC("3"),
        WithdrawRequest("4");

        private String value;

        FiatTransactionType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }


    public enum CalculationType {
        NotEnoughTEC(1),
        OverTeeCoin(2),
        OverInvoiceAmount(3),
        OverMaxPaymentAllow(4),
        PaymentOK(5);

        private int value;

        CalculationType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum TransactionStatus {
        Loading,
        Success,
        Warning,
        Fail,
        NotPaymentYet
    }

    public enum PhotoMediaFrom {
        Library(1),
        CapturePhoto(2),
        CaptureVideo(3);
        private int value;

        PhotoMediaFrom(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum TypeRestaurants {
        Restaurants(1),
        TopReviews(2),
        NewMerchants(3),
        NearYou(4);
        private int value;

        TypeRestaurants(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum ReviewVendor {
        List(1),
        Thumbnail(2);
        private int value;

        ReviewVendor(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum LanguageAppSetting {
        English("en"),
        ChineseHant("zh-Hant"),//Chinese (Traditional) TW
        ChineseHans("zh-Hans"),// Chinese （Simplified) CN
        Japanese("ja"),
        Vietnam("vi");
        private String value;

        LanguageAppSetting(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum CouponScanQRCodeResult {
        processing,
        Successful,
        Unsuccessful,
        Congratulation
    }

    public enum CouponDetail {
        Processing,
        Catalogue,
        Coupon,
        Notification,
        ScanQRCode,
        getSuccess
    }

    public enum CouponType {
        Notification("1"),
        Catalogues("2");
        private String value;

        CouponType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum FilterNotification {
        General(1),
        Transactions(2),
        Coupons(3);

        private int value;

        FilterNotification(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum CouponUserType {
        In_Progress("2"),
        UsedUp("3"),
        Expired("4");
        private String value;

        CouponUserType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum CouponShopType {
        Pending(0),
        Running(2),
        UsedUp(3),
        Expired(4);
        private int value;

        CouponShopType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum CouponDiscountType {
        BUY_1_GET_1(TCUtils.getString(R.string.buy_1_get_1), "1"),
        FREE_GIFT(TCUtils.getString(R.string.free_gift), "2"),
        DISCOUNT_PERCENTAGE(TCUtils.getString(R.string.discount_percentage), "3"),
        DISCOUNT_CASH(TCUtils.getString(R.string.discount_cash), "4");

        private String name;
        private String value;

        CouponDiscountType(String name, String value) {
            this.name = name;
            this.value = value;
        }


        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

    }

    public enum TypeCouponAction {
        SEND_NOTIFICATION(1),
        PUBLISH_TO_CATALOGUE(2);

        private int value;

        TypeCouponAction(int value) {
            this.value = value;
        }

        public int isValue() {
            return value;
        }
    }

    public enum CountryCode {
        Singapore("SG"),
        Japan("JP"),
        HongKong("HK");
        private String value;

        CountryCode(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum OpenStatus {
        Unknown("Unknown"),
        Open("Open"),
        Close("Close"),
        CloseToday("Close Today");

        private String value;

        OpenStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum LoggerEventAction {
        ScreenIn("screen_in"),
        ScreenOut("screen_out"),
        Press("press"),
        Search("search"),
        Focus("focus");

        private String value;

        LoggerEventAction(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum SortCondition {
        NearMe("1", TCUtils.getString(R.string.filter_sort_by_near_by)),
        NewMerchant("2", TCUtils.getString(R.string.text_new_merchant)),
        HighestRating("3", TCUtils.getString(R.string.filter_sort_by_highest_rating)),
        TopRatedExperiences("4", TCUtils.getString(R.string.top_rated_experiences)),
        HighToLowPrice("5", TCUtils.getString(R.string.filter_sort_by_high_to_low_price)),
        LowToHighPrice("6", TCUtils.getString(R.string.filter_sort_by_low_to_high_price));

        private String value;
        private String name;

        SortCondition(String value, String name) {
            this.value = value;
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    public enum ParamToSortCondition {
        Discover("1"),
        RecommendedForYou("2"),
        TopRate("3"),
        RecommendCouponCategory("7");

        private String value;

        ParamToSortCondition(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum FilterCoupons {
        Discover(1),
        Coupons(2);

        private int value;

        FilterCoupons(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum TypeDialog {
        ChangePassword(1),
        InputPassword(2),
        PaymentThreshold(3),
        ConfirmPaymentGreaterThanPaymentThreshold(4);

        private int value;

        TypeDialog(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum EnumGender {
        Male("1", TCUtils.getString(R.string.male)),
        Female("2", TCUtils.getString(R.string.female)),
        Unknown("0", "Unknown");

        private String value;

        private String valueName;


        EnumGender(String value, String valueName) {
            this.value = value;
            this.valueName = valueName;
        }

        public String getValue() {
            return value;
        }

        public String getValueName() {
            return valueName;
        }
    }

    public enum EnumAlert {
        Success(true),
        UnSuccess(false);

        private boolean value;

        EnumAlert(boolean value) {
            this.value = value;
        }

        public boolean getValue() {
            return value;
        }

    }

    public enum SignUpType {
        Facebook(1),
        Google(2),
        Email(3);

        private int value;

        SignUpType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum SignUpFlow {
        Email,
        Social
    }

    public enum SocialProvider {
        Email("email"),
        Facebook("facebook"),
        Google("google");

        private String value;

        SocialProvider(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum ErrorCodeSocialRegister {
        ERR004("ERR004"),
        ERR071("ERR071"),
        ERR072("ERR072"),
        ERR073("ERR073"),
        ERR074("ERR074"),
        ERR075("ERR075"),
        ERR076("ERR076");

        private String value;

        ErrorCodeSocialRegister(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        /*
        ERR004 = _('The email address has been already taken.')
        ERR071 = _('The email address or password entered is invalid.')
        ERR072 = _('Email login fail.')
        ERR073 = _('The Facebook account has been registered.')
        ERR074 = _('The Google account has been registered.')
        ERR075 = _('The Facebook account does not exist.')
        ERR076 = _('The Google account does not exist.')
        */
    }

    public enum ErrorCode {
        ERR081("ERR081"),//exceeded_monthly_limit
        ERR203("ERR203"),//force login if deleted token on BE
        ERR204("ERR204");//force login if token empty or different with BE
        private String value;

        ErrorCode(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum SortByTypeCoupon {
        GoodDeals("1"),
        Coupons("2"),
        NewArrivals("3");

        private String value;

        SortByTypeCoupon(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum SearchType {
        SearchVendor(1),
        SearchCoupon(2),
        SearchCouponByCatalogue(3);

        private int value;

        SearchType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum GoogleSignInStatusCode {
        SignInCancel(12501),
        SignInCurrentlyInProgress(12502),
        SignInFail(12500);

        private int value;

        GoogleSignInStatusCode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum DiscoverBanner {
        BaseURL("1"),
        UniversalURL("2");
        private String value;

        DiscoverBanner(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum Currentcy {
        TEECOIN("TEECOIN"),
        TEC("TEC"),
        SGD("SGD");

        private String value;

        Currentcy(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum FiatWalletInfo {
        TOP_UP("0"),
        PAYMENT("1"),
        COINBACK("2");

        private String value;

        FiatWalletInfo(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
