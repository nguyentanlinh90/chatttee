package com.teecoin.model.general;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.model.TeeCoinModel;
import com.teecoin.utils.TCConstant;
import com.teecoin.utils.TCLog;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;
import java.util.Map;

import static com.teecoin.model.general.CouponsConfigModel.FLOATING_ICON;
import static com.teecoin.utils.TCUtils.*;


public class AppConfigModel extends TeeCoinModel {

    public static final String GOOGLE_KEY = "google_key";
    public static final String TOP_UP_COUNTRY = "tec_topup_country";
    private static final String FEE_CONFIG = "fee_config";
    private static final String EXCHANGE_RATE = "exchange_rate";
    private static final String APP_BANNER_LIST = "app_banners";
    private static final String GUIDELINE_LIST = "guideline";
    private static final String GUIDELINE_CONTENT = "contents";
    private static final String COUPONS = "coupons";
    private static final String FLOATING_ICON = "floating_icon";

    @SerializedName("fee_config")
    @Expose
    private FeeConfigModel feeConfigModel;

    @SerializedName("exchange_rate")
    @Expose
    private String exchangeRate;


    @SerializedName("app_banners")
    @Expose
    private ArrayList<AppBannerModel> appBannerModelList;

    @SerializedName("guideline")
    @Expose
    private ArrayList<GuidelineModel> guidelineModelArrayList;

    @SerializedName("google_key")
    @Expose
    private String google_key;

    @SerializedName("tec_topup_country")
    @Expose
    private String tec_topup_country;

    @SerializedName("coupons")
    @Expose
    private CouponsConfigModel coupons;


    public AppConfigModel(Map<String, Object> data) {
        Map<String, Object> feeConfig = (Map<String, Object>) data.get(FEE_CONFIG);
        this.feeConfigModel = new FeeConfigModel(
                (String) feeConfig.get(FeeConfigModel.FEE_RATE),
                (String) feeConfig.get(FeeConfigModel.USER_FEE_AMOUNT),
                (String) feeConfig.get(FeeConfigModel.USER_REVIEW_AMOUNT),
                (String) feeConfig.get(FeeConfigModel.USER_REVIEW_AMOUNT_ATTACHMENT)
        );

        Map<String, Object> exchangeRateMap = (Map<String, Object>) data.get(EXCHANGE_RATE);
        this.exchangeRate = (String) exchangeRateMap.get(TCConstant.EXCHANGE_CODE_USD);

        // get list banner
        ArrayList<Map<String, Object>> banners = (ArrayList<Map<String, Object>>) data.get(APP_BANNER_LIST);
        this.appBannerModelList = new ArrayList<>();
        if (banners != null && banners.size() > 0) {
            for (int i = 0; i < banners.size(); i++) {
                AppBannerModel appBannerModel = new AppBannerModel(
                        (long) banners.get(i).get(AppBannerModel.HEIGHT),
                        (String) banners.get(i).get(AppBannerModel.URL),
                        (long) banners.get(i).get(AppBannerModel.WIDTH));
                this.appBannerModelList.add(appBannerModel);
            }
        }

        // get list guideline
        ArrayList<Map<String, Object>> guidelines = (ArrayList<Map<String, Object>>) data.get(GUIDELINE_LIST);
        this.guidelineModelArrayList = new ArrayList<>();
        if (guidelines != null && guidelines.size() > 0) {
            ArrayList<Map<String, Object>> guidelineContent = (ArrayList<Map<String, Object>>) guidelines.get(0).get(GUIDELINE_CONTENT);
            if (guidelineContent != null) {
                for (int i = 0; i < guidelineContent.size(); i++) {
                    Map<String, String> contentFollowLanguage = (Map<String, String>) guidelineContent.get(i).get(getLanguageCode());
                    if (contentFollowLanguage != null) {
                        GuidelineModel guidelineModel = new GuidelineModel(
                                (String) guidelineContent.get(i).get(GuidelineModel.IMAGE),
                                contentFollowLanguage.get(GuidelineModel.TITLE),
                                contentFollowLanguage.get(GuidelineModel.CONTENT));

                        this.guidelineModelArrayList.add(guidelineModel);
                    }
                }
            }
        }
        // get google key
        String google_key = (String) data.get(GOOGLE_KEY);
        if (!isEmpty(google_key)) {
            this.google_key = google_key;
        }

        //get top up country
        ArrayList<String> topUpCountries = (ArrayList<String>) data.get(TOP_UP_COUNTRY);
        StringBuilder stringBuilder = new StringBuilder();
        for (String country : topUpCountries) {
            stringBuilder.append(country);
        }
        this.tec_topup_country = stringBuilder.toString();

        Map<String, Object> coupons = (Map<String, Object>) data.get(COUPONS);
        if(coupons!=null){
            Map<String, Object> floatingIcon = (Map<String, Object>) coupons.get(FLOATING_ICON);
            if(floatingIcon!=null){
                FloatingIconModel floatingIconModel = new FloatingIconModel((String) floatingIcon.get(FloatingIconModel.IMAGE),(String)floatingIcon.get(FloatingIconModel.URL),(String)floatingIcon.get(FloatingIconModel.TITLE));
                this.coupons =new CouponsConfigModel();
                this.coupons.setFloating_icon(floatingIconModel);
            }
        }

    }

    public FeeConfigModel getFeeConfigModel() {
        return feeConfigModel;
    }

    public ArrayList<AppBannerModel> getAppBannerModelList() {
        return appBannerModelList;
    }

    public ArrayList<GuidelineModel> getGuidelineModelArrayList() {
        return guidelineModelArrayList;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public String getGoogle_key() {
        return google_key;
    }

    public String getTec_topup_country() {
        return tec_topup_country;
    }

    public CouponsConfigModel getCoupons() {
        return coupons;
    }
}
