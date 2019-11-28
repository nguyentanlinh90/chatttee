package com.teecoin.myapi.requesttarget;

public enum WalletRequestTarget implements BaseRequestTarget {


    LOGIN_EMAIL {
        @Override
        public String toString() {
            return "clients/email_login/";
        }
    },

    GET_CURRENCY {
        @Override
        public String toString() {
            return "get_currency_list";
        }
    },
    CREATE_USER {
        @Override
        public String toString() {
            return "clients/";
        }
    },

    CREATE_PAYMENT {
        @Override
        public String toString() {
            return "transactions/payments/";
        }
    },
    CHECK_EXIST_EMAIL {
        @Override
        public String toString() {
            return "clients/validate?email=%s";
        }
    },
    CHECK_EXIST_PHONE {
        @Override
        public String toString() {
            return "clients/validate_phone?phone=%s&country_code=%s";
        }
    },
    CREATE_SHOP {
        @Override
        public String toString() {
            return "shops/";
        }
    },
    GET_TRANSACTION_DETAIL {
        @Override
        public String toString() {
            return "transactions/?hash=%s";
        }
    },
    CREATE_COIN_BACK {
        @Override
        public String toString() {
            return "transactions/coinbacks/";
        }
    },
    GET_PAYMENT_DETAIL {
        @Override
        public String toString() {
            return "transactions/payments/%s/";
        }
    },
    UPDATE_SHOP_ACCOUNT {
        @Override
        public String toString() {
            return "shops/%s/";
        }
    },
    UPDATE_USER_ACCOUNT {
        @Override
        public String toString() {
            return "clients/%s/";
        }
    },
    CHECK_PAYMENT_STATUS_BY_INVOICE_ID {
        @Override
        public String toString() {
            return "shops/%1$s/check_payment?invoice=%2$s";
        }
    },
    GET_COIN_BACK_DETAIL {
        @Override
        public String toString() {
            return "transactions/coinbacks/%s/";
        }
    },
    GET_REWARD_DETAIL {
        @Override
        public String toString() {
            return "transactions/rewards/%s/";
        }
    },
    CREATE_REWARD {
        @Override
        public String toString() {
            return "transactions/rewards/";
        }
    },
    GET_SHOP_TRANSACTION_HISTORY_LIST {
        @Override
        public String toString() {
            return "shops/%s/history/?page=%d";
        }
    },

    GET_USER_TRANSACTION_HISTORY_LIST {
        @Override
        public String toString() {
            return "clients/%s/history/?page=%d";
        }
    },
    CHECK_SECRET_KEY_EXIST {
        @Override
        public String toString() {
            return "clients/import/%s/";
        }
    },
    LOGIN_USER_ACCOUNT {
        @Override
        public String toString() {
            return "clients/login/";
        }
    },
    LOGIN_SHOP_ACCOUNT {
        @Override
        public String toString() {
            return "shops/login/";
        }
    },

    GET_APP_CONFIG {
        @Override
        public String toString() {
            return "get_app_config/";
        }
    },

    GET_NEW_MERCHANTS_LAT_LNG {
        @Override
        public String toString() {
            return "vendors/get_vendors/?page=%s&lat=%s&long=%s&distance=%s&options[]=%s&tz=%s";
        }
    },


    TRANSFER_MONEY {
        @Override
        public String toString() {
            return "transactions/transfers/";
        }
    },
    GET_TRANSFER_MONEY_DETAIL {
        @Override
        public String toString() {
            return "transactions/transfers/%s/";
        }
    },

    GET_QR_CODE_INFO {
        @Override
        public String toString() {
            return "clients/%s/get_qr_info/";
        }
    },
    LOCATION_TRACK {
        @Override
        public String toString() {
            return "clients/%s/location_track/";
        }
    },

    WALLET_SEND_OTP {
        @Override
        public String toString() {
            return "send_otp/";
        }
    },
    WALLET_VERIFY_OTP {
        @Override
        public String toString() {
            return "verify_otp/";
        }
    },
    GET_CRYPTO_LIST {
        @Override
        public String toString() {
            return "crypto_list/";
        }
    },
    POST_CRYPTO {
        @Override
        public String toString() {
            return "clients/%s/top_up/";
        }
    },
    SHOP_LOGIN_EMAIL {
        @Override
        public String toString() {
            return "shops/email_login/";
        }
    },
    SHOP_COIN_BACK_PERCENTAGE {
        @Override
        public String toString() {
            return "shops/%s/";
        }
    },
    FIAT_WALLET_INFO {
        @Override
        public String toString() {
            return "shops/%s/fiat_wallet_info/?type=%s";
        }
    },
    SHOP_PAYMENT_INVOICE {
        @Override
        public String toString() {
            return "transactions/payment_invoice/";
        }
    },
    USER_GET_PAYMENT_INVOICE_DETAIL {
        @Override
        public String toString() {
            return "transactions/payment_invoice/?invoice=%s";
        }
    },
    CONVERT_CURRENCIES {
        @Override
        public String toString() {
            return "currencies/latest_rate/?base=%s&to=%s&amount=%s";
        }
    },
    SHOP_CHECK_PAYMENT {
        @Override
        public String toString() {
            return "shops/check_payment/?invoice=%s";
        }
    },
    SHOP_SGD_TRANSACTION_HISTORY_LIST {
        @Override
        public String toString() {
            return "shops/%s/fiat_history/?page=%d";
        }
    },
    CONVERT_TO_TEC {
        @Override
        public String toString() {
            return "transactions/convert_to_tec/";
        }
    },
    WITHDRAW {
        @Override
        public String toString() {
            return "transactions/fiat_withdrawals/";
        }
    },
    GET_CONVERT_TO_TEC_DETAIL {
        @Override
        public String toString() {
            return "transactions/convert_to_tec/%s/";
        }
    },
    GET_WITHDRAW_COMPLETED_DETAIL {
        @Override
        public String toString() {
            return "transactions/fiat_withdrawals/%s/";
        }
    },
    /* ***************************************************       */
}
