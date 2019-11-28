package com.teecoin.utils;

import android.util.Base64;

import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encrytor {

    private static final String EXTRA_KEY = "aes128aes128aes128aes128";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String ALGORITHM = "AES";
    private static final String initVector = "teecoin2018teeco";

    public static String encrypt(String key, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            key = key + EXTRA_KEY;
            SecretKeySpec skeySpec = new SecretKeySpec(Arrays.copyOf(key.getBytes("UTF-8"), 16), ALGORITHM);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());

            return Base64.encodeToString(encrypted, Base64.DEFAULT);
            //return java.util.Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String decrypt(String key,  String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            key = key + EXTRA_KEY;
            SecretKeySpec skeySpec = new SecretKeySpec(Arrays.copyOf(key.getBytes("UTF-8"), 16), ALGORITHM);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            //byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));

            byte[] original = cipher.doFinal(Base64.decode(encrypted, Base64.DEFAULT));


            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

//    public static void main(String[] args) {
//        String key = "A11111111"; // 128 bit key
//        String initVector = "teecoin2018teeco"; // 16 bytes IV
//        String value = "SBB3DA2MWXCEKVYFALCTFNEVTDPJ2YSXEI3FJH6LGWEDB6EA5JFQRF5P";
//        String encrypted = "vmHYkp1qQy8TJllaRaEadcUQkknlFUc9qX9NwAAhyGj5OjsLqV1ZYPOKhr3oShnrF2ewxr429YsgCF6wFuLJmg==";
//
//        System.out.println(encrypt(key, initVector, value));
//        System.out.println(decrypt(key, initVector, encrypted));
//    }
}
