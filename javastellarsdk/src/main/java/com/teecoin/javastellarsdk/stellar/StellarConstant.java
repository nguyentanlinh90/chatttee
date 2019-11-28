package com.teecoin.javastellarsdk.stellar;

import com.teecoin.javastellarsdk.BuildConfig;

import org.stellar.sdk.Asset;
import org.stellar.sdk.KeyPair;
import org.stellar.sdk.Network;

import io.michaelrocks.paranoid.Obfuscate;

@Obfuscate
public class StellarConstant {

    public static final int TRIAM_DEV_MODE = 1;
    public static final int TRIAM_PRODUCTION_MODE = 2;

    public static final int LIMIT_FEE = 10 * 1000 ;
    protected static final String LIMIT_AMOUNT = Integer.toString(1000 * 1000 * 1000 );

    public static void useNetWork() {
        switch (BuildConfig.TRIAM_MODE) {
            case TRIAM_DEV_MODE:
                Network.useTestNetwork();
            case TRIAM_PRODUCTION_MODE:
                Network.usePublicNetwork();
            default:
                Network.usePublicNetwork();
        }
    }

    public static String getHorizonServerURL() {
        switch (BuildConfig.TRIAM_MODE) {
            case TRIAM_DEV_MODE:
                return "https://testnet-horizon.triamnetwork.com"; //https://testnet-horizon.arm-system-holdings.com";
            case TRIAM_PRODUCTION_MODE:
                return "https://horizon.triamnetwork.com";
            default:
                return "https://horizon.triamnetwork.com"; //https://testnet-horizon.arm-system-holdings.com";
        }
    }

    private static String getTeeCoinIssuerAccountId() {
        switch (BuildConfig.TRIAM_MODE) {
            case TRIAM_DEV_MODE:
                return "GASGUKGJA6I5YMLSGD2H5IYFVER4NCIUWXK3XZ6HYMYYJ4YWZD52LRID";
            case TRIAM_PRODUCTION_MODE:
                return "GBPYVBZZYIIQ5SNUZJKXXTRCMMP23SEPFMR3IDMCYKEHUJJN35XLFDUU";
            default:
                return "GBPYVBZZYIIQ5SNUZJKXXTRCMMP23SEPFMR3IDMCYKEHUJJN35XLFDUU";
        }
    }

    private static String getTeeCoinCode() {
        switch (BuildConfig.TRIAM_MODE) {
            case TRIAM_DEV_MODE:
                return "TEECOIN";
            case TRIAM_PRODUCTION_MODE:
                return "TEC";
            default:
                return "TEC";
        }
    }

    public static Asset getTeeCoinAsset() {
        return Asset.createNonNativeAsset(getTeeCoinCode(), KeyPair.fromAccountId(getTeeCoinIssuerAccountId()));
    }

    public static String getTeeCoinTransactionPaidAmountAccountId() {
        switch (BuildConfig.TRIAM_MODE) {
            case TRIAM_DEV_MODE:
                return "GDPBWDF7HC24C5WKONESEGMWZORK3XOUYKXNJMM6YSONDVWPMJEQTAJW";
            case TRIAM_PRODUCTION_MODE:
                return "GBIFOCQHYTRJAYOX4NSONWPSNFUWHOTUVHXT5B22E7YOJ6YJC7YGHBYI";
            default:
                return "GBIFOCQHYTRJAYOX4NSONWPSNFUWHOTUVHXT5B22E7YOJ6YJC7YGHBYI";
        }
    }

    public static String getTeeCoinTransactionFeeAmountAccountId() {
        switch (BuildConfig.TRIAM_MODE) {
            case TRIAM_DEV_MODE:
                return "GBWFRUMDA3GGMO4DJNDKH2NY3HWO7PHI6OCMSQ7WE3RQ6YTK2ANOCNFV";
            case TRIAM_PRODUCTION_MODE:
                return "GB527A2U6RGOKINW2MFIZIZOJNFS4UFDNVGTM5SWWSS6NW7O3RITVO4R";
            default:
                return "GB527A2U6RGOKINW2MFIZIZOJNFS4UFDNVGTM5SWWSS6NW7O3RITVO4R";
        }
    }

    public static String getTeeCoinTransactionBasicFeeAmountAccountId() {
        switch (BuildConfig.TRIAM_MODE) {
            case TRIAM_DEV_MODE:
                return "GBQAU5S3XH7ZC47GMJZMB2NSMZRZKJQ2FY2LXB4CYJRLBPQZU2XOY5JQ";
            case TRIAM_PRODUCTION_MODE:
                return "GABW2NQQS5HEPUBRFYIRGTLOFCFUJAUFB2BGFSHPTQJDDPCEQYJAC4OP";
            default:
                return "GABW2NQQS5HEPUBRFYIRGTLOFCFUJAUFB2BGFSHPTQJDDPCEQYJAC4OP";
        }
    }

}
