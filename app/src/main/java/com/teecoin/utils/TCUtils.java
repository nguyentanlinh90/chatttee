package com.teecoin.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.TextAppearanceSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.teecoin.BuildConfig;
import com.teecoin.R;
import com.teecoin.TCMainActivity;
import com.teecoin.base.TCApplication;
import com.teecoin.base.TCDecisionListener;
import com.teecoin.feature.couponSystem.user.filterCouponFlow.FilterCouponModel;
import com.teecoin.feature.couponSystem.user.filterDiscoverFlow.FilterDiscoverModel;
import com.teecoin.feature.general.changeLanguage.LanguageModel;
import com.teecoin.feature.general.listerner.TextChangeListener;
import com.teecoin.feature.general.loginwithemail.LoginForgotPasswordDialog;
import com.teecoin.feature.general.loginwithemail.LoginResetPasswordDialog;
import com.teecoin.feature.general.signupaccount.SpinnerCustomAdapter;
import com.teecoin.javastellarsdk.stellar.StellarConstant;
import com.teecoin.model.BaseResponseModel;
import com.teecoin.model.BaseResultsResponseModel;
import com.teecoin.model.ErrorModel;
import com.teecoin.model.couponsystem.CountryCodeModel;
import com.teecoin.model.couponsystem.VendorCategoryModel;
import com.teecoin.model.general.AccountModel;
import com.teecoin.model.general.ResetPasswordResponseModel;
import com.teecoin.model.reviewsystem.DiscoverBannerModel;
import com.teecoin.model.reviewsystem.SuggestItemModel;
import com.teecoin.model.reviewsystem.SuggestModel;
import com.teecoin.model.reviewsystem.VendorModel;
import com.teecoin.model.walletsystem.TransactionDetailModel;
import com.teecoin.model.walletsystem.TransactionHistoryListResponseModel;
import com.teecoin.myapi.APIResponseListener;
import com.teecoin.myapi.apirequest.general.GeneralResetPasswordByEmailRequest;
import com.teecoin.myapi.apirequest.reviewsystem.GetKeySearchRecentRequest;
import com.teecoin.myapi.apirequest.reviewsystem.GetSuggestCouponRequest;
import com.teecoin.myapi.apirequest.reviewsystem.GetSuggestVendorRequest;
import com.teecoin.myapi.apirequest.walletsystem.WalletGetTransactionHistoryListRequest;
import com.teecoin.myapi.requesttarget.BaseRequestTarget;
import com.teecoin.myapi.requesttarget.GeneralRequestTarget;
import com.teecoin.realmdb.RealmController;
import com.teecoin.ui.GridSpacingItemDecoration;
import com.teecoin.ui.TCRecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.UUID;

import static android.content.Context.LOCATION_SERVICE;
import static com.bumptech.glide.load.resource.gif.GifDrawable.LOOP_INTRINSIC;
import static com.teecoin.model.couponsystem.CountryCodeModel.DEFAULT_COUNTRY_CODE;
import static com.teecoin.model.couponsystem.CountryCodeModel.DEFAULT_COUNTRY_NAME;
import static com.teecoin.model.couponsystem.CountryCodeModel.DEFAULT_CURRENCY_CODE;
import static com.teecoin.model.couponsystem.CountryCodeModel.DEFAULT_SELECTED;
import static core.base.BaseApplication.getActiveActivity;

public class TCUtils {

    private static final double FEE_MINIMUM = 0.0000001;
    private static final String base62chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static HashMap<String, Typeface> fontCache = new HashMap<>();

    public static boolean validateEmail(String email) {
        return email.matches(TCConstant.EMAIL_REGULAR_EXPRESSION);
    }

    public static boolean validatePassword(String password) {
        return password.matches(TCConstant.PASS_REGULAR_EXPRESSION);
    }

    public static boolean isUserApp() {
        return BuildConfig.IS_APP_USER;
    }

    public static boolean isProductionMode() {
        return com.teecoin.javastellarsdk.BuildConfig.TRIAM_MODE == StellarConstant.TRIAM_PRODUCTION_MODE;
    }


    public static boolean isShowLog() {
        return BuildConfig.DEBUG;
    }

    public static String getSharedPreferenceKey() {
        PackageInfo pInfo;
        try {
            pInfo = TCApplication
                    .getContext()
                    .getPackageManager()
                    .getPackageInfo(
                            TCApplication.getContext().getPackageName(), 0);
            return TCConstant.sharePreferenceName + "." + pInfo.packageName;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return TCConstant.sharePreferenceName;
    }

    public static boolean isNetworkConnectionAvailable() {
        ConnectivityManager conMgr = (ConnectivityManager) TCApplication
                .getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo != null) {
            if (netInfo.getType() == ConnectivityManager.TYPE_WIFI
                    || netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return netInfo.isConnected();
            }
        }
        return false;
    }

    public static String getString(int stringId) {
        try {
            if (stringId <= 0) {
                return "";
            } else {
                return getActiveActivity().getResources().getString(stringId);
            }
        } catch (Exception e) {
            return "";
        }
    }

    public static int getStringIdentifier(String name) {
        return TCApplication.getContext().getResources().getIdentifier(name, "string", TCApplication.getContext().getPackageName());
    }

    public static String getCountryLocal(String languageCode) {
        Locale locale = Locale.forLanguageTag(languageCode);
        return locale.getDisplayName(locale);
    }

    public static int getColor(int colorId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return ContextCompat.getColor(TCApplication.getContext(), colorId);
            } else {
                return TCApplication.getContext().getResources().getColor(colorId);
            }

        } catch (Exception e) {
            return 0;
        }
    }

    public static int getDimension(int dimension) {
        return (int) TCApplication.getContext().getResources().getDimension(dimension);
    }

    public static Drawable getDrawable(int resourceID) {
        return ContextCompat.getDrawable(getActiveActivity(), resourceID);
    }

    public static ColorStateList getColorStateList(int resourceID) {
        return ContextCompat.getColorStateList(getActiveActivity(), resourceID);
    }

    public static boolean isEmpty(String str) {
        return (str == null) || str.equals(TCConstant.BLANK);
    }

    public static void copyStringToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) getActiveActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            ClipData clip = ClipData.newPlainText(TCConstant.COPY_LABEL, text);
            clipboard.setPrimaryClip(clip);
        }
    }

    public static String getVersionApp() {
        return BuildConfig.VERSION_NAME;
    }

    public static <T> String convertToJson(T model) {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        return builder.create().toJson(model);
    }

    public static <T> T convertToModel(String json, Class<T> cls) {
        return new Gson().fromJson(json, cls);
    }

    /**
     * Returns the largest (closest to positive infinity) double value that is less than or equal to the argument and is equal to a mathematical integer.
     * Ex: roundFloor(1.39,1) -> 1.3
     *
     * @param value
     * @param places
     * @return
     */
    public static double roundFloor(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        BigDecimal b1 = new BigDecimal(String.valueOf(value));
        BigDecimal b2 = new BigDecimal(String.valueOf(factor));
        BigDecimal b3 = b1.multiply(b2);
        double tmp = Math.floor(b3.doubleValue());
        return tmp / factor;
    }

    /**
     * Returns the smallest (closest to negative infinity) double value that is greater than or equal to the argument and is equal to a mathematical integer.
     * Ex: roundCeil(1.23,1) -> 1.3
     *
     * @param value
     * @param places
     * @return
     */
    public static double roundCeil(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        BigDecimal b1 = new BigDecimal(String.valueOf(value));
        BigDecimal b2 = new BigDecimal(String.valueOf(factor));
        BigDecimal b3 = b1.multiply(b2);
        double tmp = Math.ceil(b3.doubleValue());
        return tmp / factor;
    }

    public static double multiply(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(String.valueOf(value1));
        BigDecimal b2 = new BigDecimal(String.valueOf(value2));
        BigDecimal b3 = b1.multiply(b2);
        return b3.doubleValue();
    }

    public static double subtract(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(String.valueOf(value1));
        BigDecimal b2 = new BigDecimal(String.valueOf(value2));
        BigDecimal b3 = b1.subtract(b2);
        return b3.doubleValue();
    }

    public static double divide(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(String.valueOf(value1));
        BigDecimal b2 = new BigDecimal(String.valueOf(value2));
        BigDecimal b3 = b1.divide(b2);
        return b3.doubleValue();
    }

    public static double calculateTeeCoinFee(double fee, int places) {
        if (fee < FEE_MINIMUM) { //if fee is 0.000000009999 < 0.0000001
            fee = 0;
        } else {
            fee = TCUtils.roundCeil(fee, places);
        }
        return fee;
    }

    private static String getCode() {
        Random r = new Random();
        return String.valueOf(base62chars.charAt(r.nextInt(base62chars.length() - 1))).toUpperCase();
    }

    private static String getRandomTimestamp() {
        long timestamp2018 = Timestamp.valueOf("2018-01-01 00:00:00.0").getTime();
        long currentTimestamp = System.currentTimeMillis();
        long random = currentTimestamp - timestamp2018;
        return Long.toString(random);
    }

    public static String getInvoiceUniqueId() {
        return String.format("%s%s", getCode(), getRandomTimestamp());
    }

    public static Bitmap generateQRCode(String content) {
        QRCodeWriter writer = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
        hints.put(EncodeHintType.MARGIN, 0); /* hints use for reducing margin when generate QR code */
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        try {
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512, hints);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            return bmp;

        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static boolean isStringNumeric(String str) {
//        return str.matches("\\d+(?:[,.]\\d+)?");
//        return !isEmpty(str) && str.matches("^[0-9,.]*$");
//        return !isEmpty(str) && str.matches("-?[0-9,.]*$");
//        return !isEmpty(str) && str.matches("-?^[0-9,.]*$");
        if (isEmpty(str))
            return false;
        NumberFormat numberFormat = DecimalFormat.getInstance(Locale.US);
        try {
            numberFormat.parse(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String formatMoney(String pattern, double money) {
        /* this code is formatting with local locale */
//        DecimalFormat formatter = new DecimalFormat(pattern);
//        return formatter.format(money);
        /* this code is formatting with local locale */

        /* this code is formatting with US locale */
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat formatter = (DecimalFormat) nf;
        formatter.applyPattern(pattern);
        return formatter.format(money);
        /* this code is formatting with US locale */
    }

    public static String formatMoney(String pattern, String money) {
        if (isStringNumeric(money)) {
            return formatMoney(pattern, convertToDouble(money));
        } else {
            return "0";
        }
    }

    public static String formatMoney(String pattern, String money, Locale locale) {
        if (isStringNumeric(money)) {
            NumberFormat nf = NumberFormat.getNumberInstance(locale);
            DecimalFormat formatter = (DecimalFormat) nf;
            formatter.applyPattern(pattern);
            return formatter.format(convertToDouble(money));
        } else {
            return "0";
        }
    }


    public static String formatMoney(String pattern, double money, Locale locale) {
        NumberFormat nf = NumberFormat.getNumberInstance(locale);
        DecimalFormat formatter = (DecimalFormat) nf;
        formatter.applyPattern(pattern);
        return formatter.format(money);
    }

    public static String formatMoneyWithoutRounding(String pattern, String money) {
        if (isStringNumeric(money)) {
            double d = convertToDouble(money);
            return formatMoney(pattern, roundFloor(d, getPlaces(pattern)));
        } else {
            return "0";
        }
    }

    public static String formatMoneyWithoutRounding(String pattern, double money) {
        return formatMoney(pattern, roundFloor(money, getPlaces(pattern)));
    }

    private static int getPlaces(String pattern) {
        int places = 1;
        switch (pattern) {
            case TCConstant.ONE_DECIMAL_FORMAT:
                places = 1;
                break;
            case TCConstant.TWO_DECIMAL_FORMAT:
                places = 2;
                break;
            case TCConstant.THREE_DECIMAL_FORMAT:
            case TCConstant.THREE_DECIMAL_FULL_FORMAT:
                places = 3;
                break;
            case TCConstant.FOUR_DECIMAL_FORMAT:
            case TCConstant.FOUR_DECIMAL_NO_COMMAS_FORMAT:
                places = 4;
                break;
            case TCConstant.FIVE_DECIMAL_FORMAT:
            case TCConstant.FIVE_DECIMAL_NO_COMMAS_FORMAT:
                places = 5;
                break;
            case TCConstant.SEVEN_DECIMAL_FORMAT:
            case TCConstant.SEVEN_DECIMAL_NO_COMMAS_FORMAT:
                places = 7;
                break;
        }
        return places;
    }

    public static String scanQRImage(Bitmap bMap) {
        String contents = null;
        int[] intArray = new int[bMap.getWidth() * bMap.getHeight()];
        //copy pixel data from the Bitmap into the 'intArray' array
        bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());
        LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Reader reader = new MultiFormatReader();
        try {
            Result result = reader.decode(bitmap);
            contents = result.getText();
        } catch (Exception e) {

        }
        return contents;
    }

    public static double convertToDouble(String number) {
        if (isEmpty(number)) {
            return 0.0;
        }
        Locale theLocale = Locale.US;//Locale.getDefault();//Hung Ngo: remove default Locale (which depend on device, using Locale.US
        NumberFormat numberFormat = DecimalFormat.getInstance(theLocale);
        Number theNumber;
        try {
            theNumber = numberFormat.parse(number);
            return theNumber.doubleValue();
        } catch (ParseException e) {
            String valueWithDot = number.replaceAll(",", ".");
            try {
                return Double.valueOf(valueWithDot);
            } catch (NumberFormatException e2) {
                return 0.0;
            }
        }
    }

    public static boolean checkLimitDecimalPlaces(String str, int limit) {
        String[] a = str.split("\\.");
        return a.length < 2 || a[1].length() <= limit;
    }

    public static boolean checkLimitDigitPlaces(String str, int limit) {
        String[] a = str.split("\\.");
        return a[0].length() <= limit;
    }

    public static void checkLimitDecimalPlaces(boolean isCheckNaturalPlaces, EditText editText, int limit, EditTextFinishInputListener listener) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (isCheckNaturalPlaces) {
                    if (!TCUtils.checkLimitDigitPlaces(text, TCConstant.LIMIT_DECIMAL_NINE)) {
                        if (text.contains(".")) {
                            String[] a = text.split("\\.");
                            if (TCUtils.isEmpty(a[1])) {
                                text = a[0].substring(0, a[0].length() - 1) + ".";
                            } else {
                                text = a[0].substring(0, a[0].length() - 1) + "." + a[1];
                            }
                        } else {
                            text = text.substring(0, text.length() - 1);
                        }
                        editText.removeTextChangedListener(this);
                        editText.setText(text);
                        editText.addTextChangedListener(this);
                        editText.setSelection(editText.getText().toString().length());
                    }
                }

                if (!TCUtils.checkLimitDecimalPlaces(text, limit)) {
                    text = text.substring(0, text.length() - 1);
                    editText.removeTextChangedListener(this);
                    editText.setText(text);
                    editText.addTextChangedListener(this);
                    editText.setSelection(editText.getText().toString().length());
                } else {
                    if (listener != null)
                        listener.finishInput(text);
                }
            }
        });
    }

    public static void showHideRemoveIconInEditText(EditText editText, View iv_remove) {

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                iv_remove.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
            }
        });
        iv_remove.setOnClickListener(v -> editText.setText(""));
    }

    public static void editTextTextChange(EditText editText, ImageView ivClear, TextView tvError, View vContainer, TextChangeListener listener) {

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                ivClear.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);

                tvError.setVisibility(s.length() > 0 ? View.INVISIBLE : View.VISIBLE);

                vContainer.setBackground(s.length() > 0 ? TCUtils.getDrawable(R.drawable.bg_transparent_solid_gold_border_5_radius) :
                        TCUtils.getDrawable(R.drawable.bg_transparent_solid_red_border_5_radius));

                if (null != listener) {
                    listener.onChange();
                }

            }
        });

        ivClear.setOnClickListener(v -> editText.setText(""));
    }

    public static void editTextTextChange(EditText editText, TextView tvError, TextChangeListener listener) {

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {


                tvError.setVisibility(s.length() > 0 ? View.INVISIBLE : View.VISIBLE);

                editText.setBackground(s.length() > 0 ? TCUtils.getDrawable(R.drawable.bg_transparent_solid_gray_border_5_radius) :
                        TCUtils.getDrawable(R.drawable.bg_transparent_solid_red_border_5_radius));

                if (null != listener) {
                    listener.onChange();
                }

            }
        });

    }

    public static void viewInputsetError(View viewFocus) {
        viewFocus.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_red_border_5_radius));
    }

    public static void setBgWhenFocusView(EditText etFocus, View viewFocus, View... viewNotFocus) {

        etFocus.setOnFocusChangeListener((v, hasFocus) ->

        {
            viewFocus.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_gold_border_5_radius));

            for (View view : viewNotFocus) {

                if (view != null) {

                    view.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_gray_border_5_radius));
                }
            }
        });
    }

    public static boolean isPaymentZeroOrEmpty(EnumMgr.EditTextValidType type, String payment, EditText editText) {
        if (TCUtils.isEmpty(payment) || !TCUtils.isStringNumeric(payment)) {
            //   editText.setError(type == EnumMgr.EditTextValidType.Amount ? TCUtils.getString(R.string.pls_input_amount) : TCUtils.getString(R.string.pls_input_payment));
            ((TCMainActivity) getActiveActivity()).showBaseMessage(type == EnumMgr.EditTextValidType.Amount ? TCUtils.getString(R.string.pls_input_amount) : TCUtils.getString(R.string.pls_input_payment));

            return false;
        } else {
            editText.setError(null);
        }
        double value_payment = TCUtils.convertToDouble(payment);
        if (value_payment == 0) {
            // editText.setError(type == EnumMgr.EditTextValidType.Amount ? TCUtils.getString(R.string.pls_input_amount) : TCUtils.getString(R.string.pls_input_payment));
            ((TCMainActivity) getActiveActivity()).showBaseMessage(type == EnumMgr.EditTextValidType.Amount ? TCUtils.getString(R.string.pls_input_amount) : TCUtils.getString(R.string.pls_input_payment));
            return false;
        } else {
            editText.setError(null);
        }
        return true;
    }

    public static double convertReturnRateToDecimal(double returnRate) {
        return roundFloor(divide(returnRate, 100), TCConstant.ROUND_FOUR_DECIMAL);
    }

    public static byte[] getBytes(InputStream inputStream) {
        try {
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            return byteBuffer.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }

    public static boolean isEnableGPS() {
        LocationManager locationManager = (LocationManager) getActiveActivity().getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static LatLng getGPS(Context mContext) {
        LatLng latLng = null;
        LocationTrack gpsTracker = new LocationTrack(mContext);
        if (gpsTracker.canGetLocation()) {
            double lat = gpsTracker.getLatitude();
            double lon = gpsTracker.getLongitude();
            // TCLog.e("TCUtils", "lat = " + lat + " " + "lon =" + lon + "");
            if (lat != 0.0 && lon != 0.0) {
                latLng = new LatLng(lat, lon);
            }
        }
        return latLng;
    }

    public static Typeface getTypeface(String font, Context context) {
        Typeface typeface = null;// fontCache.get(font);

        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), font);
            } catch (Exception e) {
                return null;
            }
            //fontCache.put(font, typeface);
        }

        return typeface;
    }

    @SuppressLint("CheckResult")
    public static RequestOptions radiusConnerImage(int valueRadius) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(TCUtils.getDrawable(R.drawable.ic_cover_chattee));
        requestOptions.error(TCUtils.getDrawable(R.drawable.ic_cover_chattee));
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(valueRadius));
        return requestOptions;
    }

    @SuppressLint("CheckResult")
    public static RequestOptions optionsSquareImage() {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(TCUtils.getDrawable(R.drawable.ic_logo_chattee));
        requestOptions.error(TCUtils.getDrawable(R.drawable.ic_logo_chattee));
        return requestOptions;
    }

    public static RequestOptions optionsCircleImage() {
        return RequestOptions.circleCropTransform().placeholder(TCUtils.getDrawable(R.drawable.ic_logo_chattee)).error(TCUtils.getDrawable(R.drawable.ic_logo_chattee));
    }

    public static RequestOptions optionsCircleImageAvatar() {
        return RequestOptions.circleCropTransform().placeholder(TCUtils.getDrawable(R.drawable.ic_avatar_user_gold)).error(TCUtils.getDrawable(R.drawable.ic_avatar_user_gold));
    }

    /**
     * Return pseudo unique ID
     *
     * @return ID
     */
    public static String getUniquePseudoID() {
        // If all else fails, if the user does have lower than API 9 (lower
        // than Gingerbread), has reset their device or 'Secure.ANDROID_ID'
        // returns 'null', then simply the ID returned will be solely based
        // off their Android device information. This is where the collisions
        // can happen.
        // Thanks http://www.pocketmagic.net/?p=1662!
        // Try not to use DISPLAY, HOST or ID - these items could change.
        // If there are collisions, there will be overlapping data
        String m_szDevIDShort = "39" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);

        // Thanks to @Roman SL!
        // https://stackoverflow.com/a/4789483/950427
        // Only devices with API >= 9 have android.os.Build.SERIAL
        // http://developer.android.com/reference/android/os/Build.html#SERIAL
        // If a user upgrades software or roots their device, there will be a duplicate entry
        String serial = null;
        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();

            // Go ahead and return the serial for api => 9
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            // String needs to be initialized
            serial = "serial"; // some value
        }

        // Thanks @Joe!
        // https://stackoverflow.com/a/2853253/950427
        // Finally, combine the values we have found by using the UUID class to create a unique identifier
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    public static String hashString(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isYourselfASenderMoney(String sourcePublicKey) {
        AccountModel accountModel = RealmController.getInstance().getData(AccountModel.class);
        return accountModel.getPublic_key().equals(sourcePublicKey);
    }

    public static float convertDpToPx(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static int dpToPx(Context context, int dp) {
        int px = Math.round(dp * getPixelScaleFactor(context));
        return px;
    }

    private static float getPixelScaleFactor(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static void showToast(String message) {
        Toast.makeText(getActiveActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public static RatingBar customRating(RatingBar ratingBar) {
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(TCUtils.getColor(R.color.c_b2973f), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(TCUtils.getColor(R.color.c_e7c31b), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(TCUtils.getColor(R.color.c_c2c2c2), PorterDuff.Mode.SRC_ATOP);
        return ratingBar;
    }

    public static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    public static void changeLanguage(String languageCode, String countryCode) {
        Resources res = getActiveActivity().getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(languageCode, countryCode));
        res.updateConfiguration(conf, dm);

    }

    public static String getLanguageCode() {
        if (TCUtils.isEmpty(TCSharePreferenceManager.getInstance().getString(DataKey.SelectedLanguageCode))) {
            return getSystemLanguage();
        }
        return TCSharePreferenceManager.getInstance().getString(DataKey.SelectedLanguageCode);
    }

    public static String getSystemLanguage() {
        Locale locale = Resources.getSystem().getConfiguration().locale;
        if (locale.getLanguage().contains(LanguageModel.CHINESE_LANGUAGE_CODE)) {
            if (locale.getScript().contains(LanguageModel.CHINESE_SIMPLIFIED_SCRIPT)
                    || ArrayUtils.contains(LanguageModel.CHINESE_SIMPLIFIED_COUNTRY_CODE, locale.getCountry())) {
                return LanguageModel.CHINESE_SIMPLIFIED_LANGUAGE_CODE;
            } else if (locale.getScript().contains(LanguageModel.CHINESE_TRADITIONAL_SCRIPT)
                    || ArrayUtils.contains(LanguageModel.CHINESE_TRADITIONAL_COUNTRY_CODE, locale.getCountry())) {
                return LanguageModel.CHINESE_TRADITIONAL_LANGUAGE_CODE;
            }
        }
        return Resources.getSystem().getConfiguration().locale.getLanguage();
    }

    public static Bitmap createVideoThumbnail(String filePath) {
        return ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Video.Thumbnails.MINI_KIND);
    }

    public static boolean isVideoFileSizeLargeThan50Mb(File file) {
        long fileSizeInBytes = file.length();
        return fileSizeInBytes > TCConstant.MAX_VIDEO_SIZE;
    }

    public static boolean isImageFileSizeLargeThan10Mb(File file) {
        long fileSizeInBytes = file.length();
        return fileSizeInBytes > TCConstant.IMAGE_SIZE_10M;
    }

    public static boolean isMyselfReview(String sourcePublicKey) {
        AccountModel accountModel = RealmController.getInstance().getData(AccountModel.class);
        return accountModel.getPublic_key().equals(sourcePublicKey);
    }

    public static void playVideoReview(String path, VideoView videoView, RelativeLayout progress_bar,
                                       ImageView iv_cover_video, ImageView iv_play_video) {
        if (videoView != null) {
            Uri uri = Uri.parse(path);
            videoView.setVideoURI(uri);
            videoView.start();
            progress_bar.setVisibility(View.VISIBLE);
            iv_play_video.setVisibility(View.GONE);
            videoView.setOnPreparedListener(mp -> {
                mp.start();
                mp.setOnVideoSizeChangedListener((mp1, arg1, arg2) -> {
                    videoView.setBackgroundColor(Color.TRANSPARENT);
                    progress_bar.setVisibility(View.GONE);
                    iv_cover_video.setVisibility(View.GONE);
                    mp1.start();
                });
            });
            videoView.setOnCompletionListener(mp -> {
                iv_cover_video.setVisibility(View.VISIBLE);
                iv_play_video.setVisibility(View.VISIBLE);
                progress_bar.setVisibility(View.GONE);
                videoView.setBackgroundColor(Color.BLACK);
            });
        }
    }

    public static GridSpacingItemDecoration setupRecyclerViewColumn(int column, int spacing, boolean padding) {
        return new GridSpacingItemDecoration(column, spacing, padding);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        // We ask for the bounds if they have been set as they would be most
        // correct, then we check we are  > 0
        final int width = !drawable.getBounds().isEmpty() ?
                drawable.getBounds().width() : drawable.getIntrinsicWidth();

        final int height = !drawable.getBounds().isEmpty() ?
                drawable.getBounds().height() : drawable.getIntrinsicHeight();

        // Now we check we are > 0
        final Bitmap bitmap = Bitmap.createBitmap(width <= 0 ? 1 : width, height <= 0 ? 1 : height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static String getIDFromURL(String url) {
        String id = "";
        if (TCUtils.isEmpty(url)) {
            return id;
        }
        String[] items = url.split("/");
        if (items != null && items.length > 0) {
            id = items[items.length - 1];
        }
        return id;
    }


    public static SpannableString getStyledText(String originalText, int style, int startIndex, int endIndex) {
        SpannableString styledText = new SpannableString(originalText);
        styledText.setSpan(new TextAppearanceSpan(
                        TCApplication.getActiveActivity(), style), startIndex,
                endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return styledText;
    }

    public static boolean containsItemFilter(FilterCouponModel filterCouponModel, String filterCouponItem) {
        return filterCouponModel.getFilterCouponString().contains(filterCouponItem);
    }

    public static String getTextFilterToContains(String text) {
        return text.substring(0, text.length() - 2);
    }

    public static boolean checkNumbers(String number) {
        if (TCUtils.isEmpty(number))
            return false;
        int num = Integer.parseInt(number);
        return num > 1;
    }

    public static int getNextPageIndex(String next, String previous, int currentPageIndex) {
        /**
         * example: "http://api-dev.tee-coin.com/v2/clients/862b3ea1-904f-4fe9-a96c-942ea1cbc3d2/history/?page=12",
         * example: "http://api-dev.tee-coin.com/v3/coupons/catalogues/category/1/?page=2&sort_type=3"
         * get number 12 in last string after "="
         */
        if (!TCUtils.isEmpty(next)) {
//            return Integer.valueOf(next.substring(next.lastIndexOf("=") + 1));
            return currentPageIndex + 1;
        } else if (!TCUtils.isEmpty(previous)) {
            try {
                // http://api-dev.tee-coin.com/v2/clients/ece3ed23-e7d8-406c-b2d3-d1e46b407603/history/?page=2
//                return Integer.valueOf(previous.substring(previous.lastIndexOf("=") + 1)) + 1; //previous page plus 1 equal to current page
                return currentPageIndex; //return current page
            } catch (NumberFormatException ex) {// can't get page in previous

            }

        }
        return 1;//if only have 1 page, return first page
    }

    public static String getNameCouponDiscountType(String discountType) {
        String discountTypeName = "";
        if (EnumMgr.CouponDiscountType.BUY_1_GET_1.getValue().equals(discountType)) {
            discountTypeName = EnumMgr.CouponDiscountType.BUY_1_GET_1.getName();
        } else if (EnumMgr.CouponDiscountType.FREE_GIFT.getValue().equals(discountType)) {
            discountTypeName = EnumMgr.CouponDiscountType.FREE_GIFT.getName();
        } else if (EnumMgr.CouponDiscountType.DISCOUNT_PERCENTAGE.getValue().equals(discountType)) {
            discountTypeName = EnumMgr.CouponDiscountType.DISCOUNT_PERCENTAGE.getName();
        } else if (EnumMgr.CouponDiscountType.DISCOUNT_CASH.getValue().equals(discountType)) {
            discountTypeName = EnumMgr.CouponDiscountType.DISCOUNT_CASH.getName();
        }
        return discountTypeName;
    }

    public static void setLocale(String code) {
        Locale locale = new Locale(code);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getActiveActivity().getResources().updateConfiguration(config,
                getActiveActivity().getResources().getDisplayMetrics());
    }

    public static TCDateUtility.DateFormatDefinition getDateFormatByLanguageCode(TCDateUtility.DateFormatDefinition defaultDateFormat) {
        TCDateUtility.DateFormatDefinition returnFormat = defaultDateFormat;
        switch (defaultDateFormat) {
            case DD_MM_YYYY:
                if (TCUtils.getLanguageCode().equals(EnumMgr.LanguageAppSetting.Japanese.getValue())) {
                    returnFormat = TCDateUtility.DateFormatDefinition.YYYY_MM_DD;
                }
                break;
            case EEE_DD_MMM_YYYY_HH_MM_A:
                if (TCUtils.getLanguageCode().equals(EnumMgr.LanguageAppSetting.English.getValue())) {
                    returnFormat = TCDateUtility.DateFormatDefinition.EEE_DD_MMM_YYYY_HH_MM_A;
                } else if (TCUtils.getLanguageCode().equals(EnumMgr.LanguageAppSetting.Japanese.getValue())) {
                    returnFormat = TCDateUtility.DateFormatDefinition.YYYY_MM_DD_EEE_HH_MM_SS;
                } else {
                    returnFormat = TCDateUtility.DateFormatDefinition.DD_MM_YYYY_EEE_HH_MM_SS;
                }
                break;
            case DD_MM_YYYY_HH_MM_SS:
                if (TCUtils.getLanguageCode().equals(EnumMgr.LanguageAppSetting.Japanese.getValue())) {
                    returnFormat = TCDateUtility.DateFormatDefinition.YYYY_MM_DD_HH_MM_SS;
                }
                break;
            case DD_MM_YYYY_HYPHEN_HH_MM:
                if (TCUtils.getLanguageCode().equals(EnumMgr.LanguageAppSetting.Japanese.getValue())) {
                    returnFormat = TCDateUtility.DateFormatDefinition.YYYY_MM_DD_HYPHEN_HH_MM;
                }
                break;
            case DD_MM_YYYY_T_HH_MM_SS_SSSSSS:
                if (TCUtils.getLanguageCode().equals(EnumMgr.LanguageAppSetting.Japanese.getValue())) {
                    returnFormat = TCDateUtility.DateFormatDefinition.YYYY_MM_DD_T_HH_MM_SS_SSSSSS;
                }
                break;
            case EEE_MMM_DD_YYYY:
                if (TCUtils.getLanguageCode().equals(EnumMgr.LanguageAppSetting.Japanese.getValue())) {
                    returnFormat = TCDateUtility.DateFormatDefinition.EEE_MMM_DD_YYYY;
                }
                break;
            case DD_MM_YYYY_HYPHEN:
                if (TCUtils.getLanguageCode().equals(EnumMgr.LanguageAppSetting.Japanese.getValue())) {
                    returnFormat = TCDateUtility.DateFormatDefinition.YYYY_MM_DD_HYPHEN;
                }
                break;
            case EEE_MMM_DD_YYYY_HH_MM_A:
                if (TCUtils.getLanguageCode().equals(EnumMgr.LanguageAppSetting.English.getValue())) {
                    returnFormat = TCDateUtility.DateFormatDefinition.EEE_MMM_DD_YYYY_HH_MM_A;
                } else if (TCUtils.getLanguageCode().equals(EnumMgr.LanguageAppSetting.Japanese.getValue())) {
                    returnFormat = TCDateUtility.DateFormatDefinition.YYYY_MM_DD_EEE_HH_MM_SS;
                } else {
                    returnFormat = TCDateUtility.DateFormatDefinition.DD_MM_YYYY_EEE_HH_MM_SS;
                }
                break;
        }
        return returnFormat;
    }

    public static double getDistance(LatLng LatLng1, LatLng LatLng2) {
        double distance;
        Location locationA = new Location("A");
        locationA.setLatitude(LatLng1.latitude);
        locationA.setLongitude(LatLng1.longitude);
        Location locationB = new Location("B");
        locationB.setLatitude(LatLng2.latitude);
        locationB.setLongitude(LatLng2.longitude);
        distance = locationA.distanceTo(locationB);
        return distance;
    }

    public static String getVendorIdFromCouponDynamicLink(Uri couponDynamicLink) {
        //URL: couponDynamicLink = http://api-dev.tee-coin.com/v3/vendors/11274/coupons/?type=13
        // get id: 11274 from this couponDynamicLink
        String[] segments = couponDynamicLink.getPath().split("/");
        if (segments.length > 2) {
            return segments[segments.length - 2];
        }
        return "";
    }

    public static String getCouponIdFromCouponDynamicLink(Uri couponDynamicLink) {
        //URL: couponDynamicLink = http://api-dev.tee-coin.com/v3/coupons/catalogues/category/3/?type=14
        // get id: 3 from this couponDynamicLink
        String[] segments = couponDynamicLink.getPath().split("/");
        if (segments.length > 1) {
            return segments[segments.length - 1];
        }
        return "";
    }

    public static void openAppSetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActiveActivity().getPackageName(), null);
        intent.setData(uri);
        getActiveActivity().startActivityForResult(intent, EnumMgr.RequestCode.APPLICATION_DETAIL_SETTING_REQUEST_CODE.getValue());
    }

    public static void saveCountRequestPermissionLocation(Object onWhat) {
        TCSharePreferenceManager.getInstance().setInt(DataKey.CountRequestLocationTime, (Integer) onWhat);
    }

    public static String paramsGetVendorList(int pageIndex, int page_size, LatLng latLng, LatLng latLngSearch, String country_code,
                                             String keyword, String distance, String open_date, String category, String price_range,
                                             String sort_condition, String ratings) {

        @SuppressLint("DefaultLocale")
        String pageSize = String.format("&page_size=%d", page_size);

        String mLat = "";
        String mLng = "";
        if (latLng != null) {
            mLat = String.format("&lat=%s", latLng.latitude);
            mLng = String.format("&long=%s", latLng.longitude);
        }

        String searchLat = "";
        String searchLng = "";
        if (latLngSearch != null) {
            searchLat = String.format("&search_lat=%s", latLngSearch.latitude);
            searchLng = String.format("&search_long=%s", latLngSearch.longitude);
        }

        String mCountryCode = TCUtils.isEmpty(country_code) ? "" : String.format("&country_code=%s", country_code);
        String mKeyWord = TCUtils.isEmpty(keyword) ? "" : String.format("&keyword=%s", keyword);
        String mDistance = TCUtils.isEmpty(distance) ? "" : String.format("&distance=%s", distance);
        String mOpenDate = TCUtils.isEmpty(open_date) ? "" : String.format("&open_date=%s", open_date);
        String mCategory = TCUtils.isEmpty(category) ? "" : String.format("&category=%s", category);
        String mPriceRange = TCUtils.isEmpty(price_range) ? "" : String.format("&price_range=%s", price_range);
        String mSortCondition = TCUtils.isEmpty(sort_condition) ? "" : String.format("&sort_condition=%s", sort_condition);
        String mRatings = TCUtils.isEmpty(ratings) ? "" : ratings;

        return String.valueOf(pageIndex) + pageSize + mLat + mLng + searchLat + searchLng + mCountryCode + mKeyWord + mDistance +
                mOpenDate + mCategory + mPriceRange + mSortCondition + mRatings;
    }

    public static String paramsToGetCategory(int pageIndex, String category, LatLng latLng, String country_code, String tag, FilterCouponModel filterCouponModel) {
        String mLat = "";
        String mLng = "";
        if (latLng != null) {
            mLat = String.format("&lat=%s", latLng.latitude);
            mLng = String.format("&long=%s", latLng.longitude);
        }
        String mCategory = TCUtils.isEmpty(category) ? "" : String.format("&category=%s", category);
        String mCountryCode = TCUtils.isEmpty(country_code) ? "" : String.format("&country_code=%s", country_code);
        String mFilterCoupon = "";
        if (filterCouponModel != null) {
            mFilterCoupon = TCUtils.isEmpty(filterCouponModel.getFilterCouponString()) ? "" : filterCouponModel.getFilterCouponString();
            mFilterCoupon += TCUtils.isEmpty(filterCouponModel.getTextSearch()) ? "" : "&keyword=" + filterCouponModel.getTextSearch();
        }

        String mTag = TCUtils.isEmpty(tag) ? "" : String.format("&tag=%s", tag);
        return String.valueOf(pageIndex) + mCategory + mLat + mLng + mCountryCode + mTag + mFilterCoupon;
    }

    public static String paramsGetCategoryCoupon(LatLng latLng, String country_code, String sort_type, String keyword, String tag) {

        String mLat = "";
        String mLng = "";
        String mCountryCode;
        if (latLng != null) {
            mLat = String.format("lat=%s", latLng.latitude);
            mLng = String.format("&long=%s", latLng.longitude);
            mCountryCode = TCUtils.isEmpty(country_code) ? "" : String.format("&country_code=%s", country_code);
        } else {
            mCountryCode = TCUtils.isEmpty(country_code) ? "" : String.format("?country_code=%s", country_code);
        }

        String mSortType = TCUtils.isEmpty(sort_type) ? "" : String.format("&sort_type=%s", sort_type);

        String mKeyWord = TCUtils.isEmpty(keyword) ? "" : String.format("&keyword=%s", keyword);
        String mTag = TCUtils.isEmpty(tag) ? "" : String.format("&tag=%s", tag);

        return formatSpecialCharacter(String.valueOf(mLat + mLng + mCountryCode + mSortType + mKeyWord + mTag));
    }

    private static String formatSpecialCharacter(String s) {

        return s.replace("$", "%24").replace("+", "%2B");
    }

    public static String paramsGetRecent(int page, LatLng latLng, String country_code) {

        String mPage = String.format("?page=%s", page);

        String mLat = "";

        String mLng = "";

        String mCountryCode = TCUtils.isEmpty(country_code) ? "" : String.format("&country_code=%s", country_code);

        if (latLng != null) {

            mLat = String.format("&lat=%s", latLng.latitude);

            mLng = String.format("&long=%s", latLng.longitude);
        }

        return mPage + mLat + mLng + mCountryCode;
    }

    public static void setCuisineList(VendorModel data, TextView cuisine) {

        if (null != data.getCuisines() && 0 < data.getCuisines().size()) {
            cuisine.setVisibility(View.VISIBLE);
            StringBuilder cuisineTypeBuilder = new StringBuilder();

            for (int i = 0; i < data.getCuisines().size(); i++) {

                cuisineTypeBuilder.append(data.getCuisines().get(i).getName()).append(", ");

            }

            cuisine.setText(cuisineTypeBuilder.substring(0, cuisineTypeBuilder.length() - 2));

        } else {
            cuisine.setVisibility(View.GONE);
        }

    }

    public static void setStatusTimeVendor(VendorModel data, TextView status, TextView time, TextView tvDot) {

        //status.setVisibility(View.GONE);
        tvDot.setVisibility(View.GONE);
        time.setVisibility(View.GONE);
        if (EnumMgr.OpenStatus.Open.getValue().equals(data.getOpenStatus().getStatus())) {

            status.setText(TCUtils.getString(R.string.text_open));
            status.setTextColor(TCUtils.getColor(R.color.c_42b766));
            if (!TCUtils.isEmpty(data.getOpenStatus().getTimeClose())) {
                tvDot.setVisibility(View.VISIBLE);
                time.setVisibility(View.VISIBLE);
                time.setText(String.format("%s %s", TCUtils.getString(R.string.text_closes), data.getOpenStatus().getTimeClose()));
            }
        } else if (EnumMgr.OpenStatus.Close.getValue().equals(data.getOpenStatus().getStatus())) {
            status.setText(TCUtils.getString(R.string.text_closed));
            status.setTextColor(TCUtils.getColor(R.color.c_d0021b));
            if (!TCUtils.isEmpty(data.getOpenStatus().getTimeOpen())) {
                tvDot.setVisibility(View.VISIBLE);
                time.setVisibility(View.VISIBLE);
                time.setText(String.format("%s %s", TCUtils.getString(R.string.text_opens), data.getOpenStatus().getTimeOpen()));
            }
        } else if (EnumMgr.OpenStatus.CloseToday.getValue().equals(data.getOpenStatus().getStatus())) {
            status.setText(TCUtils.getString(R.string.text_close_today));
            status.setTextColor(TCUtils.getColor(R.color.c_d0021b));
        } else {
            status.setVisibility(View.GONE);
            tvDot.setVisibility(View.GONE);
            time.setVisibility(View.GONE);
        }

    }

    public static String getCountry() {
        return getActiveActivity().getResources().getConfiguration().locale.getCountry();
    }

    public static String getCountryCodeFromGPS(LatLng latLng) {
        if (latLng == null)
            return "";
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        // latLng = new LatLng(10.784593333333332,106.69373333333333);
        // 10.784593333333332 106.69373333333333
        // 10.784886 106.693807
        // TCLog.e("LatLng " + latLng.latitude + " " + latLng.longitude);
        Geocoder geocoder = new Geocoder(getActiveActivity(), Locale.getDefault());

        String country = "";
        //if (Geocoder.isPresent()) {
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.size() > 0) {
                country = addresses.get(0).getCountryCode();
                String adminArea = addresses.get(0).getAdminArea();
//                TCLog.e("adminArea " + adminArea);
                String locality = addresses.get(0).getLocality();
//                TCLog.e("locality " + locality);
            } else {
                // TCLog.e("list address not found");
            }
        } catch (IOException e) {
            // TCLog.e("IOException - " + e.getMessage());
            e.printStackTrace();
        }
        //  }
        //TCLog.e("country .. " + country);
        return country.toUpperCase();
    }

    private static String getCountryCodeFromLocation(LatLng latLng) {
        Handler handler = new Handler();
        //https://stackoverflow.com/questions/52827790/reverse-geocoding-android-issue
        final String[] country = {""};
        new Thread(new Runnable() {

            @Override
            public void run() {
                country[0] = "";
                Geocoder geocoder = new Geocoder(getActiveActivity(), Locale.getDefault());
                String result = null;
                Address address = null;

                try {
                    List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

                    if (addressList != null && addressList.size() > 0) {
                        address = addressList.get(0);
                    }
                } catch (Exception e) {
                    // TCLog.e("getAddressFromLocation:run: exception while getting address from location");
                    e.printStackTrace();
                } finally {

                    Message message = Message.obtain();
                    message.setTarget(handler);

                    if (address != null) {
                        if (!TCUtils.isEmpty(address.getCountryCode())) {
                            country[0] = address.getCountryCode().toUpperCase();
                        }
                    }
                    //  TCLog.e("country coe  " + address.getCountryCode());

                    // TCLog.e("country coe new " + country[0]);
                }

            }
        }).start();
        return country[0];
    }

    public static void getAddressFromLocation(final double latitude, final double longitude,
                                              final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                Address address = null;

                try {
                    List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);

                    if (addressList != null && addressList.size() > 0) {
                        address = addressList.get(0);
                    }
                } catch (Exception e) {
                    //  TCLog.e("getAddressFromLocation:run: exception while getting address from location");
                    e.printStackTrace();
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);

                    if (address != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        TCLog.e("country getAddressFromLocation  " + address.getCountryCode());
                        bundle.putString("thoroughFare", address.getThoroughfare());
                        bundle.putString("subThoroughFare", address.getSubThoroughfare());
                        bundle.putString("city", address.getLocality());
                        bundle.putString("state", address.getAdminArea());
                        bundle.putString("country", address.getCountryName());
                        bundle.putString("postalCode", address.getPostalCode());
                        bundle.putString("subAdminArea", address.getSubAdminArea());
                        bundle.putString("subLocality", address.getSubLocality());
                        message.setData(bundle);
                    } else {
                        message.what = 1;
                        Bundle bundle = new Bundle();

                        result = "Latitude: " + latitude + "Longitude: " + longitude +
                                "\n Unable to get address for this location.";

                        bundle.putString("address", result);
                        message.setData(bundle);
                    }

                    message.sendToTarget();
                }
            }
        };

        thread.start();
    }

    public static CountryCodeModel checkCountryCode(ArrayList<CountryCodeModel> listCountry, LatLng latLng) {
        CountryCodeModel countryCodeModel = new CountryCodeModel(DEFAULT_COUNTRY_CODE, DEFAULT_COUNTRY_NAME, DEFAULT_CURRENCY_CODE, DEFAULT_SELECTED);
        if (listCountry != null && latLng != null) {
            if (listCountry.size() > 0) {
                String codeCountry = getCountryCodeFromGPS(latLng);// solution 1
                if (TCUtils.isEmpty(codeCountry)) {

                    // Handler handler = new Handler();
                    //  getAddressFromLocation(latLng.latitude,latLng.longitude,getActiveActivity(),handler);//
                    codeCountry = getCountryCodeFromLocation(latLng);// // solution 2
                }
                //   TCLog.e("codeCountry " + codeCountry);
                for (CountryCodeModel code : listCountry) {
                    if (code.getCountry_code().toUpperCase().equals(codeCountry)) {
                        countryCodeModel = new CountryCodeModel(code.getCountry_code(), code.getName(), code.getCurrency_code(), true);
                        return countryCodeModel;
                    }
                }
            }

            return countryCodeModel;
        } else {
            return countryCodeModel;
        }
    }

    public static void shareLinkVendor(String link) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        // share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, "Chattee!");
        share.putExtra(Intent.EXTRA_TEXT, link);
        getActiveActivity().startActivity(Intent.createChooser(share, "Chattee!"));
    }

    public static String getUserToken() {
        return TCConstant.TOKEN + TCSharePreferenceManager.getInstance().getString(DataKey.Token);
    }

    public static String getUserAgent() {
        String userAgent = System.getProperty("http.agent");
        String app = String.format("%s/%s", BuildConfig.APPLICATION_ID, BuildConfig.VERSION_NAME);
        return String.format("%s %s", app, userAgent);
    }

    public static void setNetPageIndexRecycleVew(BaseResponseModel response, TCRecyclerView recycleVew) {
        if (null != ((BaseResultsResponseModel) response.getResult()).getNext()) {
            recycleVew.setNextPageIndex(
                    recycleVew.getNextPageIndex() + 1);
        } else {
            recycleVew.setNextPageIndex(-1);
        }
    }

    public static void getKeySearchRecent(TextView tvCancel, ArrayList<SuggestItemModel> suggestList, TCRecyclerView rcv) {

        ((TCMainActivity) getActiveActivity()).requestApi(new GetKeySearchRecentRequest(new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {

                ArrayList<String> list = ((BaseResultsResponseModel) response.getResult()).getResults();

                if (null != list && list.size() > 0) {

                    ArrayList<SuggestItemModel> listItem = new ArrayList<>();

                    int size = list.size() > TCConstant.DEFAULT_SEARCH_RECENT ? TCConstant.DEFAULT_SEARCH_RECENT : list.size();

                    for (int i = 0; i < size; i++) {

                        //when get key search recent: set param id and param categoryId is empty
                        listItem.add(new SuggestItemModel("", list.get(i), ""));
                    }

                    if (suggestList.size() > 0) suggestList.clear();

                    suggestList.addAll(listItem);

                    rcv.onLoadMoreComplete();

                    rcv.setVisibility(View.VISIBLE);

//                    if(tvCancel != null)
//                        tvCancel.setVisibility(View.VISIBLE);

                } else {

                    rcv.setVisibility(View.GONE);
//                    if(tvCancel != null)
//                        tvCancel.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {

            }
        }));
    }

    private static void getSuggestSearch(String keyword, TextView tvCancel, ArrayList<SuggestItemModel> suggestList, TCRecyclerView rcv) {
        new Handler().postDelayed(() -> ((TCMainActivity) getActiveActivity()).requestApi(
                new GetSuggestVendorRequest(keyword, ((TCMainActivity) getActiveActivity()).getCountryCodeModel().getCountry_code(), new APIResponseListener() {

                    @Override
                    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {

                        if (suggestList.size() > 0) {
                            suggestList.clear();
                        }

                        ArrayList<SuggestModel> list = ((BaseResultsResponseModel) response.getResult()).getResults();

                        if (null != list && list.size() > 0) {

                            ArrayList<SuggestItemModel> listItem = new ArrayList<>();

                            for (int i = 0; i < list.size(); i++) {

                                for (int j = 0; j < list.get(i).getOptions().size(); j++) {

                                    listItem.add(new SuggestItemModel(list.get(i).getId(), list.get(i).getOptions().get(j), list.get(i).getName()));
                                }
                            }
                            suggestList.addAll(listItem);

                            rcv.onLoadMoreComplete();

                            rcv.setVisibility(View.VISIBLE);

//                            if(tvCancel != null)
//                                tvCancel.setVisibility(View.VISIBLE);

                        } else {
                            rcv.setVisibility(View.GONE);
//                            if(tvCancel != null)
//                                tvCancel.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {

                    }
                })), 1000L);
    }

    private static void getSuggestCouponSearch(String keyword, TextView tvCancel, ArrayList<SuggestItemModel> suggestList, TCRecyclerView rcv) {

        new Handler().postDelayed(() -> ((TCMainActivity) getActiveActivity()).requestApi(

                new GetSuggestCouponRequest(keyword, ((TCMainActivity) getActiveActivity()).getCountryCodeModel().getCountry_code(), new APIResponseListener() {

                    @Override
                    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {

                        ArrayList<String> list = ((BaseResultsResponseModel) response.getResult()).getResults();

                        if (null != list && list.size() > 0) {

                            ArrayList<SuggestItemModel> listItem = new ArrayList<>();

                            for (int i = 0; i < list.size(); i++) {

                                //when get key search recent: set param id and param categoryId is empty
                                listItem.add(new SuggestItemModel("", list.get(i), ""));
                            }

                            suggestList.clear();

                            suggestList.addAll(listItem);

                            rcv.onLoadMoreComplete();

                            rcv.setVisibility(View.VISIBLE);

                        } else {
                            rcv.setVisibility(View.GONE);

                        }
                    }

                    @Override
                    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {

                        rcv.setVisibility(View.GONE);

                    }
                })), 1000L);
    }

    @SuppressLint("ClickableViewAccessibility")
    public static void scrollOnTouchListener(NestedScrollView nestedScrollView, TCRecyclerView rcv) {

        nestedScrollView.setOnTouchListener((v, event) -> {

            rcv.setVisibility(View.GONE);

            return false;

        });
    }

    public static void etSearchTextChangedListener(int typeSearch, EditText etInput, ImageView ivClear, TextView tvCancel, ArrayList<SuggestItemModel> suggestList, TCRecyclerView rcv) {

        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                ivClear.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);

                if (s.length() > 2) {

                    if (EnumMgr.SearchType.SearchVendor.getValue() == typeSearch) {

                        TCUtils.getSuggestSearch(s.toString(), tvCancel, suggestList, rcv);

                    } else {

                        TCUtils.getSuggestCouponSearch(s.toString(), tvCancel, suggestList, rcv);

                    }
                } else {
                    TCUtils.getKeySearchRecent(tvCancel, suggestList, rcv);
                }
            }
        });
    }

    public static void etOnFocusChangeListener(EditText etInput, TextView tvCancel, ArrayList<SuggestItemModel> suggestList, TCRecyclerView rcv) {
        etInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) TCUtils.getKeySearchRecent(tvCancel, suggestList, rcv);
        });
    }

    public static void nestedOnScrollChangeListener(NestedScrollView nestedScrollView, FrameLayout view) {
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

            if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.GONE);
            }
        });
    }

    public static boolean checkURLisVideo(String url) {
        if (TCUtils.isEmpty(url))
            return false;
        if (url.length() <= 4)
            return false;

        String file = url.substring(url.length() - 4, url.length());

        return file.toLowerCase().equals(".mp4");
    }

    public static boolean checkMapsApplication() {
        {
            try {
                ApplicationInfo info = getActiveActivity().getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0);
                return true;
            } catch (PackageManager.NameNotFoundException e) {
                return false;
            }
        }
    }

    public static void getLineContenReview(TextView tvContent, TextView tvMore) {
        if (tvContent == null)
            return;

        tvContent.post(() -> {
            // TCLog.e("tvContent.getLineCount() "+tvContent.getLineCount());
            if (tvContent.getLineCount() > 2) {
                tvContent.setMaxLines(2);
                tvContent.setEllipsize(TextUtils.TruncateAt.END);
                tvMore.setVisibility(View.VISIBLE);
            }
        });
    }

    public static void showMoreContenReview(TextView tvContent) {
        if (tvContent == null)
            return;
        tvContent.setMaxLines(Integer.MAX_VALUE);
        tvContent.setEllipsize(null);


    }

    public static void setTextReviewCount(int reviewCount, TextView textView) {

        if (reviewCount > 1) {

            textView.setText(String.format(TCUtils.getString(R.string.count_reviews), String.valueOf(reviewCount)));

        } else {

            textView.setText(String.format(TCUtils.getString(R.string.count_review), String.valueOf(reviewCount)));
        }
    }

    public static void setTextPurchaseCount(String purchaseCount, TextView tvCount, TextView textView) {
        if (!isEmpty(purchaseCount)) {

            tvCount.setText(purchaseCount);

            if (Integer.parseInt(purchaseCount) > 1) {

                textView.setText(TCUtils.getString(R.string.coupon_purchases));

            } else {

                textView.setText(TCUtils.getString(R.string.text_purchase).toLowerCase());
            }
        }
    }

    public static void addOnGlobalLayoutListener(View view, View viewInput) {

        view.getViewTreeObserver().addOnGlobalLayoutListener(() -> {

            int heightDiff = view.getRootView().getHeight() - view.getHeight();

            if (heightDiff > TCUtils.dpToPx(getActiveActivity(), 200)) { // if more than 200 dp, it's probably a keyboard...

                viewInput.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_gold_border_5_radius));

            } else {

                viewInput.setBackground(TCUtils.getDrawable(R.drawable.bg_transparent_solid_gray_border_5_radius));

            }
        });
    }

    public static void setNumberFilter(FilterDiscoverModel filter, VendorCategoryModel categorySelectModel, TextView textView) {
        int numberFilter = 0;
        if (filter != null) {
            numberFilter = filter.getNumberFilter(filter);
            //TCLog.e("numberFilter 1 " + numberFilter);
        }
        if (categorySelectModel != null)
            numberFilter++;
        //TCLog.e("numberFilter 2 " + numberFilter);
        textView.setText(String.valueOf(numberFilter));
        textView.setVisibility(numberFilter > 0 ? View.VISIBLE : View.GONE);
    }

    public static String getCallingPhoneCode(String countryCode) {
        return String.format("+%d", PhoneNumberUtil.getInstance().getCountryCodeForRegion(countryCode));
    }

    public static boolean validatePhoneNumber(String phoneNumber, String regionCode) {
        String fullPhoneNumber = getCallingPhoneCode(regionCode) + phoneNumber;

        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber phoneNumberProto = phoneUtil.parse(fullPhoneNumber, "SG");// default is Singapore region code
            return phoneUtil.isValidNumber(phoneNumberProto);
        } catch (NumberParseException e) {
            TCLog.d("hung NumberParseException was thrown: " + e.toString());
            return false;
        }
    }

    public static void setupPhoneSpinner(Spinner spPhoneCode) {
        int selectedPosition = 0;
        ArrayList<String> countryCodeList = new ArrayList<>(PhoneNumberUtil.getInstance().getSupportedRegions());
        Collections.sort(countryCodeList, String.CASE_INSENSITIVE_ORDER);
        countryCodeList.add(0, EnumMgr.CountryCode.Singapore.getValue());
        countryCodeList.add(1, EnumMgr.CountryCode.Japan.getValue());
        countryCodeList.add(2, EnumMgr.CountryCode.HongKong.getValue());
        for (int i = 0; i < 3; i++) {//loop for first 3 country code
            if (countryCodeList.get(i).equals(TCUtils.getCountry())) {
                selectedPosition = i;
            }
        }
        for (int i = 3; i < countryCodeList.size(); i++) {// except the first 3 items
            if (countryCodeList.get(i).equals(EnumMgr.CountryCode.Singapore.getValue())
                    || countryCodeList.get(i).equals(EnumMgr.CountryCode.Japan.getValue())
                    || countryCodeList.get(i).equals(EnumMgr.CountryCode.HongKong.getValue())) {
                countryCodeList.remove(i);
            }
            if (countryCodeList.get(i).equals(TCUtils.getCountry())) {
                selectedPosition = i;
            }
        }

        spPhoneCode.setAdapter(new SpinnerCustomAdapter(getActiveActivity(),
                R.layout.spinner_custom_view, countryCodeList));
        spPhoneCode.setSelection(selectedPosition);
    }

    public static LatLng vendorGetLocation(String location) {
        LatLng latLng = null;
        if (!TCUtils.isEmpty(location)) {
            StringTokenizer tokens = new StringTokenizer(location, ",");
            double lat = TCUtils.convertToDouble(tokens.nextToken());
            double lng = TCUtils.convertToDouble(tokens.nextToken());
            latLng = new LatLng(lat, lng);
        }
        return latLng;
    }

    public static void gotoDirection(LatLng latLng) {
        if (latLng != null) {
            if (TCUtils.checkMapsApplication()) {
                //Uri gmmIntentUri = Uri.parse("google.navigation:q=28.5675,77.3260");
                Uri uri = Uri.parse("google.navigation:q=" + String.valueOf(latLng.latitude) + "," + String.valueOf(latLng.longitude));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
                mapIntent.setPackage("com.google.android.apps.maps");
                getActiveActivity().startActivity(mapIntent);
            } else {
                TCUtils.showToast("Please install Google Maps");
            }

        }

    }

    public static RequestListener<GifDrawable> getRequestFileGif() {
        return new RequestListener<GifDrawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                resource.setLoopCount(LOOP_INTRINSIC);

                return false;
            }
        };
    }

    public static String setTextAddress(String source, String destination) {
        AccountModel accountModel = RealmController.getInstance().getAccount();
        String address = "";
        if (accountModel != null) {
            if (!TCUtils.isEmpty(source)) {
                if (accountModel.getPublic_key().equals(source)) {
                    if (!TCUtils.isEmpty(destination)) {
                        address = destination;
                        return address;
                    }

                }
            }
            if (!TCUtils.isEmpty(destination)) {
                if (accountModel.getPublic_key().equals(destination)) {
                    if (!TCUtils.isEmpty(source)) {
                        address = source;
                        return address;
                    }

                }
            }

        }
        return address;
    }

    public static Bitmap createQRCode(String content) {
        Bitmap bmp = TCUtils.generateQRCode(content);
        return bmp;
    }

    @SuppressLint("SetJavaScriptEnabled")
    public static void settingWebView(WebView webView) {
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);
        ((TCMainActivity) getActiveActivity()).showLoading(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            // For api level bellow 24
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                ((TCMainActivity) getActiveActivity()).showLoading(false);
                Intent intent;

                if (url.contains(TCConstant.HTTPS_CHATTEE_PAGE_LINK) || url.contains(TCConstant.HTTP_CHATTEE_PAGE_LINK)) {
                    intent = new Intent(getActiveActivity(), TCMainActivity.class);
                    intent.setData(Uri.parse(url));
                    (getActiveActivity()).startActivity(intent);
                }
                return true;
            }

            // From api level 24
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                view.loadUrl(url);
                Intent intent;
                ((TCMainActivity) getActiveActivity()).showLoading(false);
                if (url.contains(TCConstant.HTTPS_CHATTEE_PAGE_LINK) || url.contains(TCConstant.HTTP_CHATTEE_PAGE_LINK)) {
                    intent = new Intent(getActiveActivity(), TCMainActivity.class);
                    intent.setData(Uri.parse(url));
                    (getActiveActivity()).startActivity(intent);
                } else if (url.contains(TCConstant.CHATTEE) && url.contains(TCConstant.BANNER_ID)) {
                    String banner_id = url.substring(url.lastIndexOf("=")).replace("=", "");
                    if (!TCUtils.isEmpty(banner_id))
                        ((TCMainActivity) getActiveActivity()).submitBannerClick(new DiscoverBannerModel(banner_id));
                    return true;
                } else if (!url.contains(TCConstant.CHATTEE)) {
                    return reformatUri(view, request.getUrl());
                }
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // todo
                ((TCMainActivity) getActiveActivity()).showLoading(false);
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // todo
                ((TCMainActivity) getActiveActivity()).showLoading(false);
            }
        });
    }

    public static void setMarginRecyclerView(TCRecyclerView recyclerView, int left, int top, int right, int bottom) {
        ViewGroup.MarginLayoutParams marginLayoutParams =
                (ViewGroup.MarginLayoutParams) recyclerView.getLayoutParams();
        marginLayoutParams.setMargins(left, top, right, bottom);
        recyclerView.setLayoutParams(marginLayoutParams);
    }

    public static void popupMessageResetPassword(EditText etEmail) {
        LoginForgotPasswordDialog loginForgotPasswordDialog = new LoginForgotPasswordDialog(getActiveActivity(), new TCDecisionListener() {
            @Override
            public void onPositiveButtonClicked(int id, Object onWhat) {
                resetPassword(etEmail);
            }

            @Override
            public void onNegativeButtonClicked(int id, Object onWhat) {
            }

            @Override
            public void onNeutralButtonClicked(int id, Object onWhat) {
            }
        });
        loginForgotPasswordDialog.show();
    }

    private static void resetPassword(EditText etEmail) {
        ((TCMainActivity) getActiveActivity()).requestApi(new GeneralResetPasswordByEmailRequest(etEmail.getText().toString(), new APIResponseListener() {
            @Override
            public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                if (requestTarget == GeneralRequestTarget.RESET_PASSWORD) {
                    ResetPasswordResponseModel responseModel = (ResetPasswordResponseModel) response.getResult();
                    if (responseModel != null) {
                        new LoginResetPasswordDialog(getActiveActivity(), responseModel.getEmail()).show();
                    }
                }
            }

            @Override
            public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {
                if (requestTarget == GeneralRequestTarget.RESET_PASSWORD) {
                    ((TCMainActivity) getActiveActivity()).showBaseMessage(errorModel.getErrorMessage());
                }
            }
        }));
    }

    public static float roundRating(float rate) {
        int round = (int) rate;
        if (round != rate)
            rate = (float) (round + 0.5);
        return rate;
    }

    public static void loadAndSaveAgainTransactionList() {
        ((TCMainActivity) getActiveActivity()).requestApi(new WalletGetTransactionHistoryListRequest(1,
                new APIResponseListener() {
                    @Override
                    public void onSuccess(BaseResponseModel response, BaseRequestTarget requestTarget) {
                        TransactionHistoryListResponseModel responseModel = (TransactionHistoryListResponseModel) response.getResult();
                        if (responseModel.getTransactionDetailModelList() != null && responseModel.getTransactionDetailModelList().size() > 0) {
                            for (TransactionDetailModel model : responseModel.getTransactionDetailModelList()) {
                                if (!RealmController.getInstance().isExist(TransactionDetailModel.class,
                                        TransactionDetailModel.PRIMARY_KEY, model.getTransaction_hash())) {
                                    RealmController.getInstance().insertData(model, TransactionDetailModel.PRIMARY_KEY, model.getTransaction_hash());
                                } else {
                                    RealmController.getInstance().updateTransactionDetail(model);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFail(ErrorModel errorModel, int statusCode, BaseRequestTarget requestTarget) {

                    }
                }));
    }

    public static void handelShowPhoneNumver(String phoneNumber, EditText etCountryCode, EditText etPhone) {
        String[] splitStr = phoneNumber.split("\\s+");
        StringBuilder phone = new StringBuilder();
        String countryCode = "";
        if (splitStr.length == 1) {
            Phonenumber.PhoneNumber phoneNumberProto;
            try {
                phoneNumberProto = PhoneNumberUtil.getInstance().parse(phoneNumber, "SG");
                if (phoneNumberProto != null) {
                    etCountryCode.setText("+" + phoneNumberProto.getCountryCode() + "");
                    phone.append(phoneNumber.replace(phoneNumberProto.getCountryCode() + "", "").replace("+", ""));
                } else {
                    phone.append(phoneNumber);
                }

            } catch (NumberParseException e) {
                e.printStackTrace();
            }
        } else if (splitStr.length > 1) {
            for (int i = 0; i < splitStr.length; i++) {
                if (splitStr[i].contains("+")) {
                    countryCode = splitStr[i];

                }
            }
            phone.append(phoneNumber.replace(countryCode, ""));
            etCountryCode.setText(countryCode);
        }

        etPhone.setText(phone.toString());
    }

    public static double getBalance() {
        return TCUtils.convertToDouble(RealmController.getInstance().getBalance());
    }

    private static boolean reformatUri(WebView view, Uri uri) {
        try {
            if (!uri.getScheme().toLowerCase().equals("https") && !uri.getScheme().toLowerCase().equals("http")) {
                URI newUri = null;
                try {
                    newUri = new URI("https", uri.getHost(), uri.getPath(), uri.getQuery(), uri.getFragment());
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(newUri.toString()));
                    (getActiveActivity()).startActivity(i);
                    return true;
                } catch (URISyntaxException e) {
                    //e.printStackTrace();
                    //view.loadUrl(uri.toString());
                    return false;
                }
            } else {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri.toString()));
                (getActiveActivity()).startActivity(i);
                return true;
            }
        } catch (Exception ex) {
            //view.loadUrl(uri.toString());
            return false;
        }
    }

    public static boolean isForceLogin(ErrorModel errorModel) {
        return isUserApp() &&
                (errorModel.getError_code().equals(EnumMgr.ErrorCode.ERR203.getValue())
                        || errorModel.getError_code().equals(EnumMgr.ErrorCode.ERR204.getValue()));
    }

    public static void checkEditTextHasFocus(EditText editText) {
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                ((TCMainActivity) getActiveActivity()).hideFooter();
            } else {
                ((TCMainActivity) getActiveActivity()).showFooter();
            }
        });
    }

    public static double calculateToTecNoFeeAmount(String price) {
        double rate = ((TCMainActivity) getActiveActivity()).getRate();
        double spread = ((TCMainActivity) getActiveActivity()).getSpread();
        //calculate price Tec follow rate and spread on fire base
        double priceTec = convertToDouble(price) / (rate * (1 - (spread / 100)));
        return convertToDouble(formatMoneyWithoutRounding(TCConstant.TWO_DECIMAL_FORMAT, priceTec));
    }

    public static double calculateFeeAmount(String price) {
        double feeOnFireBase = ((TCMainActivity) getActiveActivity()).getFee();
        //calculate fee follow fee on fire base
        double tec = calculateToTecNoFeeAmount(price) * feeOnFireBase / 100;
        return convertToDouble(formatMoneyWithoutRounding(TCConstant.TWO_DECIMAL_FORMAT, tec));
    }

    public static String calculateToTecIncludeFeeAmount(String price) {
        double feeOnFireBase = ((TCMainActivity) getActiveActivity()).getFee();
        //calculate price Tec follow rate and spread on fire base
        double priceTec = calculateToTecNoFeeAmount(price);
        //calculate fee follow fee on fire base
        double fee = priceTec * feeOnFireBase / 100;
        return formatMoneyWithoutRounding(TCConstant.TWO_DECIMAL_FORMAT, priceTec + fee);
    }

    public void setFocus(EditText editText) {
        if (editText == null)
            return;

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }

    //this is for dev
    //for pd: keytool -exportcert -alias TeecoinRewardAndroidKey -keystore /Users/nguyentanlinh/Documents/pj/teecoin_reward_android/TeeCoinAndroidKeyStore.jks | openssl sha1 -binary | openssl base64
    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            TCLog.d("linhnt Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                TCLog.d("linhnt Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("linhnt Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            TCLog.d("linhnt No such an algorithm", e.toString());
        } catch (Exception e) {
            TCLog.d("linhnt Exception", e.toString());
        }

        return key;
    }
}
