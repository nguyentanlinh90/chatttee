package com.teecoin.utils;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class TimeAgoMessages {

    private static final String MESSAGES = "timeago.messages";

    private ResourceBundle bundle;

    private TimeAgoMessages() {
        super();
    }


    private ResourceBundle getBundle() {
        return bundle;
    }

    private void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }


    public String getPropertyValue(final String property) {
        final String propertyVal = getBundle().getString(property);
        return propertyVal;
    }

    public String getPropertyValue(final String property, Object... values) {
        String propertyVal = getPropertyValue(property);
        return MessageFormat.format(propertyVal, values);
    }


    public static final class Builder {

        private ResourceBundle innerBundle;


        public TimeAgoMessages build() {
            TimeAgoMessages resources = new TimeAgoMessages();
            resources.setBundle(this.getInnerBundle());
            return resources;
        }

        public Builder defaultLocale() {
            this.setInnerBundle(ResourceBundle.getBundle(TimeAgoMessages.MESSAGES));
            return this;
        }

        public ResourceBundle getInnerBundle() {
            return innerBundle;
        }


        public void setInnerBundle(ResourceBundle bundle) {
            this.innerBundle = bundle;
        }

        public Builder withLocale(Locale locale) {
            this.setInnerBundle(ResourceBundle.getBundle(TimeAgoMessages.MESSAGES, locale));
            return this;
        }
    }

//    public static TimeAgoMessages getLocalTime() {
//        String languageCode = TCUtils.getLanguageCode();
//        Locale localeByLanguageTag;
//        HashMap<String, LanguageModel> languageModels = ((TCMainActivity) TCApplication.getActiveActivity()).getLanguageList();
//        if (languageModels != null && languageModels.size() > 0 && !TCUtils.isEmpty(languageCode)) {
//            LanguageModel languageModel = languageModels.get(languageCode);
//            localeByLanguageTag = new Locale(languageModel.getLocaleLanguageCode(), languageModel.getLocaleCountryCode());
//        } else {
//            localeByLanguageTag = Resources.getSystem().getConfiguration().locale;
//        }
//        // Locale LocaleByLanguageTag = Locale.setDefault(localeByLanguageTag);
//        return new Builder().withLocale(localeByLanguageTag).build();
//    }

    //https://github.com/marlonlom/timeago?utm_source=android-arsenal.com&utm_medium=referral&utm_campaign=4707
}
