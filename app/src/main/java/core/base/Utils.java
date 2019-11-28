package core.base;

import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RatingBar;

public class Utils {

    public static boolean isEmpty(String str) {
        return (str == null) || str.equals("");
    }

    public static final int INTERVAL_CLICK = 50; // 500ms
    public static final int RIPPLE_EFFECT_DELAY = 50; // 500ms


    protected static boolean isExceptionalView(View view) {
        return ((view instanceof AdapterView) || (view instanceof EditText)
                || (view instanceof SwipeRefreshLayout)
                || (view instanceof DrawerLayout) || (view instanceof ViewPager)
                || (view instanceof RatingBar));
    }
}
