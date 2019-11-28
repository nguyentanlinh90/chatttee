package com.teecoin.utils;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

public class TCScreenSize {
    static DisplayMetrics displayMetrics = new DisplayMetrics();
    public static int SIZE_LARGE_DEFAULT = 1790;

    public static int getWidth(Activity context) {
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        return width;
    }

    public static int getHeight(AppCompatActivity context) {
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        return height;
    }

    public static DisplayMetrics getSizeScreen(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay()
                .getMetrics(displaymetrics);
        return displaymetrics;
    }
    public static int frameImagesListReviewsHeight(int screenWidth){
        return (screenWidth * 5 / 9);
    }

    public static int frameBannerCouponHeight(int screenWidth) {
        return (screenWidth * 3 / 8);
    }

    public static int frameBannerCouponWidthHorizontion(int screenWidth) {
        return (screenWidth * 4 / 7);
    }

    public static int frameBannerCouponHeightHorizontion(int screenWidth) {
        return (screenWidth * 2 / 7);
    }

    public static int getBannerChatteApp(int screenWidth) {
        //https://stackoverflow.com/questions/36305054/how-to-set-imageview-in-percent-relative-layout-to-following-ratios-based-on-de
        return (screenWidth * 9 / 16);
    }

    public static int getScreenFavouriteWidth(int screenWidth){
        return (screenWidth * 4 / 9);
    }
    public static int getScreenFavouriteHeight(int screenWidth){
        return getScreenFavouriteWidth(screenWidth)*5/10;
    }

}
