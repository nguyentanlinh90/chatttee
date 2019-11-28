package com.teecoin.retrofit;

import com.teecoin.javastellarsdk.BuildConfig;

import io.michaelrocks.paranoid.Obfuscate;

import static com.teecoin.javastellarsdk.stellar.StellarConstant.TRIAM_DEV_MODE;
import static com.teecoin.javastellarsdk.stellar.StellarConstant.TRIAM_PRODUCTION_MODE;

@Obfuscate
public class URL {

    private static final String DEV_SERVER_URL = "http://api-dev.tee-coin.com/v4.1/"; //"http://192.168.0.116:8000/v3/";
    private static final String PRODUCTION_SERVER_URL = "https://api.tee-coin.com/v4.1/";

    public static String getServer(){
        switch (BuildConfig.TRIAM_MODE){
            case TRIAM_DEV_MODE:
                return DEV_SERVER_URL;
            case TRIAM_PRODUCTION_MODE:
                return PRODUCTION_SERVER_URL;
            default:
                return PRODUCTION_SERVER_URL;
        }
    }


    public static String googleServer() {
        return "https://maps.googleapis.com/";
    }

    public static String getFireBaseCloudServer() {
        switch (BuildConfig.TRIAM_MODE) {
            case TRIAM_DEV_MODE:
                return "teecoin-test";
            case TRIAM_PRODUCTION_MODE:
                return "teecoin-api";
            default:
                return "teecoin-api";
        }
    }
}
