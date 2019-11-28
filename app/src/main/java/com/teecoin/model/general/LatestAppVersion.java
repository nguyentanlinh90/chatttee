package com.teecoin.model.general;

import com.teecoin.BuildConfig;
import com.teecoin.utils.TCUtils;

import java.util.Map;

public class LatestAppVersion {

    private static final String LATEST_APP_VERSION = "lastest_app_version";
    private static final String ANDROID = "android";
    private static final String LINK = "link";
    private static final String VERSION = "version";

    private String link;
    private String version;


    public LatestAppVersion(Map<String, Object> data) {
        try {
            Map<String, Object> latestAppVersion = (Map<String, Object>) data.get(LATEST_APP_VERSION);
            if (latestAppVersion != null) {
                Map<String, Object> android = (Map<String, Object>) latestAppVersion.get(ANDROID);
                if (android != null) {
                    Map<String, Object> myPackageName = (Map<String, Object>) android.get(BuildConfig.APPLICATION_ID);
                    if (myPackageName != null) {
                        this.link = (String) myPackageName.get(LINK);
                        this.version = (String) myPackageName.get(VERSION);
                    }
                }
            }
        } catch (Exception e) {

        }

    }

    /**
     * this code was copy from
     *
     * @param existingVersion
     * @param newVersion
     * @return
     */
    private static boolean compareVersion(String existingVersion, String newVersion) {

        boolean newVersionIsGreater = false;
        String[] existingVersionArray = existingVersion.split("\\.");
        String[] newVersionArray = newVersion.split("\\.");

        int maxIndex = Math.max(existingVersionArray.length, newVersionArray.length);
        for (int i = 0; i < maxIndex; i++) {
            int newValue;
            int oldValue;
            try {
                oldValue = Integer.parseInt(existingVersionArray[i]);
            } catch (ArrayIndexOutOfBoundsException e) {
                oldValue = 0;
            }
            try {
                newValue = Integer.parseInt(newVersionArray[i]);
            } catch (ArrayIndexOutOfBoundsException e) {
                newValue = 0;
            }
            if (oldValue < newValue) {
                newVersionIsGreater = true;
                continue;
            }
        }
        return newVersionIsGreater;

    }

    public boolean hasNewUpdate() {

        if (TCUtils.isEmpty(this.link) || TCUtils.isEmpty(this.version))
            return false;
        return compareVersion(BuildConfig.VERSION_NAME, this.version);
    }

    public String getLink() {
        return link;
    }

    public String getVersion() {
        return version;
    }
}
