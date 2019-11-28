package com.teecoin.animation;


import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({com.teecoin.animation.AutoResetMode.NEVER, com.teecoin.animation.AutoResetMode.UNDER, com.teecoin.animation.AutoResetMode.OVER, com.teecoin.animation.AutoResetMode.ALWAYS})
public @interface AutoResetMode {

    int UNDER = 0;
    int OVER = 1;
    int ALWAYS = 2;
    int NEVER = 3;

    class Parser {

        @com.teecoin.animation.AutoResetMode
        public static int fromInt(final int value) {
            switch (value) {
                case OVER:
                    return OVER;
                case ALWAYS:
                    return ALWAYS;
                case NEVER:
                    return NEVER;
                default:
                    return UNDER;
            }
        }
    }

}
