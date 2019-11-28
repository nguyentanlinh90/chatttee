package com.teecoin.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SignatureCheck {

    private static final String CHATTEE_SIGNATURE = "38C2037EF2D8A68EE1117AF51A2B1DDDBB0D7F0F";

    private static SignatureCheck instance;

    public static synchronized SignatureCheck getInstance() {
        if (instance == null) {
            instance = new SignatureCheck();
        }
        return instance;
    }

    //computed the sha1 hash of the signature
    public static String getSHA1(byte[] sig) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        digest.update(sig);
        byte[] hashtext = digest.digest();
        return bytesToHex(hashtext);
    }

    //util method to convert byte array to hex string
    public static String bytesToHex(byte[] bytes) {
        final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Query the signature for this application to detect whether it matches the
     * signature of the real developer. If it doesn't the app must have been
     * resigned, which indicates it may been tampered with.
     *
     * @param context
     * @return true if the app's signature matches the expected signature.
     * @throws PackageManager.NameNotFoundException
     */
    public boolean validateAppSignature(Context context) throws PackageManager.NameNotFoundException {

        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
                context.getPackageName(), PackageManager.GET_SIGNATURES);
        //note sample just checks the first signature
        for (Signature signature : packageInfo.signatures) {
            // SHA1 the signature
            String sha1 = getSHA1(signature.toByteArray());
            // check is matches hardcoded value
            TCLog.d("hung", "signature: sha1:" + sha1);
            return CHATTEE_SIGNATURE.equals(sha1);
        }

        return false;
    }


}
