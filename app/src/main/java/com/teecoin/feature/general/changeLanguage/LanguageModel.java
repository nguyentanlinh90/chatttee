package com.teecoin.feature.general.changeLanguage;

import com.teecoin.R;
import com.teecoin.utils.DataKey;
import com.teecoin.utils.TCSharePreferenceManager;
import com.teecoin.utils.TCUtils;

import java.util.ArrayList;

public class LanguageModel {

    public static final String LANGUAGE_ITEM_NAME = "item";
    public static final String LANGUAGE_ITEM_LOCALE_LANGUAGE_CODE = "locale_language_code";
    public static final String LANGUAGE_ITEM_LOCALE_COUNTRY_CODE = "locale_country_code";
    public static final String LANGUAGE_ITEM_LOCALE_SCRIPT = "locale_script";
    public static final String LANGUAGE_ITEM_LANGUAGE_CODE = "language_code";
    public static final String LANGUAGE_ITEM_LANGUAGE_NAME = "language_name";
    public static final String LANGUAGE_ITEM_TEXT_CODE = "text_code";


    public static final String CHINESE_LANGUAGE_CODE = "zh";
    public static final String CHINESE_SIMPLIFIED_SCRIPT = "Hans";
    public static final String CHINESE_TRADITIONAL_SCRIPT = "Hant";
    public static final String CHINESE_SIMPLIFIED_LANGUAGE_CODE = "zh-Hans";
    public static final String CHINESE_TRADITIONAL_LANGUAGE_CODE = "zh-Hant";
    public static final String[] CHINESE_SIMPLIFIED_COUNTRY_CODE = {"CN"};
    public static final String[] CHINESE_TRADITIONAL_COUNTRY_CODE = {"TW", "HK"};


    private String languageCode;
    private String languageName;
    private String localeLanguageCode;
    private String localeCountryCode;
    private String localeScript;
    private String textCode;
    private boolean isSelected;

    public LanguageModel(String languageCode, String localeLanguageCode,
                         String localeCountryCode, String localeScript, String languageName, String textCode) {
        this.languageCode = languageCode;
        this.localeLanguageCode = localeLanguageCode;
        this.localeCountryCode = localeCountryCode;
        this.localeScript = localeScript;
        this.languageName = languageName;
        this.textCode = textCode;
        String selectedLanguageCode = TCSharePreferenceManager.getInstance().getString(DataKey.SelectedLanguageCode);
        this.isSelected = selectedLanguageCode.equals(languageCode);
    }

    public LanguageModel() {
    }

    public static ArrayList<LanguageModel> getListLanguages() {
        ArrayList<LanguageModel> list = new ArrayList<>();
        list.add(new LanguageModel("en", "US", "en", "", TCUtils.getString(R.string.language_english), "language_english"));
        list.add(new LanguageModel("zh-Hans", "CN", "zh", "Hans", TCUtils.getString(R.string.language_chinese_simplified), "language_chinese_simplified"));
        list.add(new LanguageModel("zh-Hant", "TW", "zh", "Hant", TCUtils.getString(R.string.language_chinese_tranditional), "language_chinese_traditional"));
        list.add(new LanguageModel("ja", "JP", "ja", "", TCUtils.getString(R.string.language_japanese), "language_japanese"));
        list.add(new LanguageModel("vi", "VI", "vn", "", TCUtils.getString(R.string.language_vietnamese), "language_vietnamese"));
        return list;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getTextCode() {
        return textCode;
    }

    public void setTextCode(String textCode) {
        this.textCode = textCode;
    }

    public String getLocaleLanguageCode() {
        return localeLanguageCode;
    }

    public void setLocaleLanguageCode(String localeLanguageCode) {
        this.localeLanguageCode = localeLanguageCode;
    }

    public String getLocaleCountryCode() {
        return localeCountryCode;
    }

    public void setLocaleCountryCode(String localeCountryCode) {
        this.localeCountryCode = localeCountryCode;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getLanguageName() {
        return languageName;
    }


    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

}
