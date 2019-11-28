package com.teecoin.feature.general.languagesetting;

import android.content.Context;

import com.teecoin.R;
import com.teecoin.feature.general.changeLanguage.LanguageModel;
import com.teecoin.model.general.AccountModel;
import com.teecoin.utils.DataKey;
import com.teecoin.utils.TCSharePreferenceManager;
import com.teecoin.utils.TCUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MyLanguage {

    private Context mContext;
    private LinkedHashMap<String, LanguageModel> languageList;

    public MyLanguage(Context mContext) {
        this.mContext = mContext;
        getLanguageData();
        setLanguage();
        //((TCMainActivity)mContext).refreshBottomLayoutWhenChangeLanguage();
    }


    private void getLanguageData() {
        languageList = new LinkedHashMap<>();
        try {
            XmlPullParser xpp = mContext.getResources().getXml(R.xml.language_code);
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xpp.getEventType() == XmlPullParser.START_TAG) {
                    if (xpp.getName().equals(LanguageModel.LANGUAGE_ITEM_NAME)) {
                        String languageCode = xpp.getAttributeValue(null, LanguageModel.LANGUAGE_ITEM_LANGUAGE_CODE);
                        languageList.put(languageCode,
                                new LanguageModel(languageCode,
                                        xpp.getAttributeValue(null, LanguageModel.LANGUAGE_ITEM_LOCALE_LANGUAGE_CODE),
                                        xpp.getAttributeValue(null, LanguageModel.LANGUAGE_ITEM_LOCALE_COUNTRY_CODE),
                                        xpp.getAttributeValue(null, LanguageModel.LANGUAGE_ITEM_LOCALE_SCRIPT),
                                        xpp.getAttributeValue(null, LanguageModel.LANGUAGE_ITEM_LANGUAGE_NAME),
                                        xpp.getAttributeValue(null, LanguageModel.LANGUAGE_ITEM_TEXT_CODE)));
                    }
                }
                xpp.next();
            }
        } catch (XmlPullParserException | IOException e) {
        }
    }

    private void setLanguage() {
        if (languageList != null && languageList.size() > 0) {
            String selectedLanguageCode = TCSharePreferenceManager.getInstance().getString(DataKey.SelectedLanguageCode);
            if (!TCUtils.isEmpty(selectedLanguageCode)) {
                LanguageModel languageModel = languageList.get(selectedLanguageCode);
                TCUtils.changeLanguage(languageModel.getLocaleLanguageCode(), languageModel.getLocaleCountryCode());
            }
        }
    }

    public void updateLanguage(AccountModel accountModel) {
        if (!TCUtils.isEmpty(accountModel.getLanguage())) {
            if (languageList == null) {
                getLanguageData();
            }
            if (languageList != null && languageList.size() > 0) {
                LanguageModel languageModel = languageList.get(accountModel.getLanguage());
                if (languageModel != null) {
                    languageModel.setSelected(true);
                    TCSharePreferenceManager.getInstance().setString(DataKey.SelectedLanguageCode, languageModel.getLanguageCode());
                    TCUtils.changeLanguage(languageModel.getLocaleLanguageCode(), languageModel.getLocaleCountryCode());
                }
            }
        }

    }

    public ArrayList<LanguageModel> getLanguageList() {
        if (languageList != null)
            return new ArrayList<>(languageList.values());
        return null;
    }

    public void updateLanguage() {
        if (languageList == null) {
            getLanguageData();
        }
        if (languageList != null && languageList.size() > 0) {
            LanguageModel languageModel = languageList.get(TCUtils.getLanguageCode());
            if (languageModel != null) {
                languageModel.setSelected(true);
                TCSharePreferenceManager.getInstance().setString(DataKey.SelectedLanguageCode, languageModel.getLanguageCode());
                TCUtils.changeLanguage(languageModel.getLocaleLanguageCode(), languageModel.getLocaleCountryCode());
            }
        }
    }
}
