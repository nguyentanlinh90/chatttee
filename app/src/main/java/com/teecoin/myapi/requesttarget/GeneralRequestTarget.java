package com.teecoin.myapi.requesttarget;

public enum GeneralRequestTarget implements BaseRequestTarget {

    GET_PROFILE_USER {
        @Override
        public String toString() {
            return "clients/%s/";
        }
    },
    GET_PROFILE_SHOP {
        @Override
        public String toString() {
            return "shops/%s/";
        }
    },
    GET_EVENT_REFERRAL {
        @Override
        public String toString() {
            return "events/get_event_referral/";
        }
    },

    UPDATE_USER_AVATAR {
        @Override
        public String toString() {
            return "clients/%s/avatar/";
        }
    },
    CHANGE_RECOVERY_PASSWORD {
        @Override
        public String toString() {
            return "clients/%s/change_password/";
        }
    },
    CHANGE_RECOVERY_PASSWORD_FOR_SHOP {
        @Override
        public String toString() {
            return "shops/%s/change_password/";
        }
    },

    USER_REGISTER_NOTIFY {
        @Override
        public String toString() {
            return "clients/%s/register_notify/";
        }
    },
    SHOP_REGISTER_NOTIFY {
        @Override
        public String toString() {
            return "shops/%s/register_notify/";
        }
    },

    USER_READ_NOTIFY {
        @Override
        public String toString() {
            return "clients/%s/notifications/read/";
        }
    },
    SHOP_READ_NOTIFY {
        @Override
        public String toString() {
            return "shops/%s/notifications/read/";
        }
    },

    USER_LOGOUT {
        @Override
        public String toString() {
            return "clients/%s/logout/";
        }
    },
    SHOP_LOGOUT {
        @Override
        public String toString() {
            return "shops/%s/logout/";
        }
    },

    RESET_PASSWORD {
        @Override
        public String toString() {
            return "clients/request_reset_password/";
        }
    },


    GENERAL_GET_VERIFICATION_CODE {
        @Override
        public String toString() {
            return "clients/%s/request_recovery_code/";
        }
    },
    GENERAL_GET_VERIFICATION_CODE_FOR_SHOP {
        @Override
        public String toString() {
            return "shops/%s/request_recovery_code/";
        }
    },
    GENERAL_CHECK_VERIFICATION_CODE {
        @Override
        public String toString() {
            return "clients/%s/check_verify_code/";
        }
    },
    GENERAL_CHECK_VERIFICATION_CODE_FOR_SHOP {
        @Override
        public String toString() {
            return "shops/%s/check_verify_code/";
        }
    },
    GENERAL_SET_NEW_RECOVERY_PASSWORD {
        @Override
        public String toString() {
            return "clients/%s/set_new_password/";
        }
    },
    GENERAL_SET_NEW_RECOVERY_PASSWORD_FOR_SHOP {
        @Override
        public String toString() {
            return "shops/%s/set_new_password/";
        }
    },

    SOCIAL_REGISTER {
        @Override
        public String toString() {
            return "clients/social_register/";
        }
    },
    SOCIAL_LOGIN {
        @Override
        public String toString() {
            return "clients/social_login/";
        }
    },
    GENERAL_CHECK_REWARD_STATUS {
        @Override
        public String toString() {
            return "clients/check_reward_status/?device_id=%s";
        }
    }, GENERAL_INIT_SOCIAL_PASSWORD {
        @Override
        public String toString() {
            return "clients/%s/set_initial_social_password/";
        }
    }, GENERAL_VERIFY_PASSWORD {
        @Override
        public String toString() {
            return "clients/%s/verify_password/";
        }
    },
    GENERAL_VERIFY_PASSWORD_FOR_SHOP {
        @Override
        public String toString() {
            return "shops/%s/verify_password/";
        }
    },
    GENERAL_CHECK_SECRET_KEY {
        @Override
        public String toString() {
            return "clients/%S/check_sk/";
        }
    }, GENERAL_STORE_SECRET_KEY {
        @Override/**/
        public String toString() {
            return "clients/%S/store_sk/";
        }
    },SHOP_GET_BALANCE {
        @Override
        public String toString() {
            return "shops/%s/balances";
        }
    },
}
