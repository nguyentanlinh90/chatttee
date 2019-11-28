package com.teecoin.utils;

import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.util.Base64;

import com.teecoin.base.TCApplication;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.security.auth.x500.X500Principal;


public class KeyStoreUtility {

    KeyStore keyStore;
    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    private static final String ALIAS = "ALIAS";
    private static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";
    //    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
//    private static final String PROVIDER = "AndroidKeyStoreBCWorkaround";
    private static final String UTF_8 = "UTF-8";
    private static final String KEY_STORE_ALIAS = "TeeCoinAlias";

    private static KeyStoreUtility instance;

    public static synchronized KeyStoreUtility getInstance() {
        if (instance == null) {
            instance = new KeyStoreUtility();
        }
        return instance;
    }

    public KeyStoreUtility() {
        try {
            keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
            keyStore.load(null);
            createNewKeys();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
    }

    public void createNewKeys() {
        try {
            if (!keyStore.containsAlias(ALIAS)) {
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                end.add(Calendar.YEAR, 1);

                KeyPairGenerator
                        kpg = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
                kpg.initialize(new KeyPairGeneratorSpec.Builder(TCApplication.getActiveActivity())
                        .setAlias(ALIAS)
                        .setStartDate(start.getTime())
                        .setEndDate(end.getTime())
                        .setSerialNumber(BigInteger.valueOf(1))
                        .setSubject(new X500Principal("CN=" + ALIAS))
                        .build());
                KeyPair kp = kpg.generateKeyPair();

//                KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(TCApplication.getActiveActivity())
//                        .setAlias(ALIAS)
//                        .setSubject(new X500Principal("CN=Sample Name, O=Android Authority"))
//                        .setSerialNumber(BigInteger.ONE)
//                        .setStartDate(start.getTime())
//                        .setEndDate(end.getTime())
//                        .build();
//                KeyPairGenerator generator = KeyPairGenerator.newInstance("RSA", "AndroidOpenSSL");
//                generator.initialize(spec);
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public String encryptString(String textToEncrypt) {
        try {
            PrivateKey privateKeyEntry = (PrivateKey) keyStore.getKey(ALIAS, null);
            PublicKey publicKey = keyStore.getCertificate(ALIAS).getPublicKey();

            byte[] encodedBytes = null;
            try {
                Cipher cipher = Cipher.getInstance(TRANSFORMATION, getProvider());
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);
                encodedBytes = cipher.doFinal(textToEncrypt.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return Base64.encodeToString(encodedBytes, Base64.DEFAULT);

//            Cipher input = Cipher.newInstance(TRANSFORMATION, PROVIDER);
//            input.init(Cipher.ENCRYPT_MODE, publicKey);
//
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            CipherOutputStream cipherOutputStream = new CipherOutputStream(
//                    outputStream, input);
//            cipherOutputStream.write(textToEncrypt.getBytes("UTF-8"));
//            cipherOutputStream.close();
//
//            byte[] vals = outputStream.toByteArray();
//            return Base64.encodeToString(vals, Base64.DEFAULT);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String decryptString(String encryptedString) {
        try {
//            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(ALIAS, null);
            PrivateKey privateKeyEntry = (PrivateKey) keyStore.getKey(ALIAS, null);
//            RSAPrivateKey privateKey = (RSAPrivateKey) privateKeyEntry.getPrivateKey();
            byte[] decodedBytes = null;
            Cipher c = Cipher.getInstance(TRANSFORMATION, getProvider());
            c.init(Cipher.DECRYPT_MODE, privateKeyEntry);
            decodedBytes = c.doFinal(Base64.decode(encryptedString, Base64.DEFAULT));
            return new String(decodedBytes);

            //-------------------
//            Cipher output = Cipher.newInstance(TRANSFORMATION, PROVIDER);
//            output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());
//
//            CipherInputStream cipherInputStream = new CipherInputStream(
//                    new ByteArrayInputStream(Base64.decode(encryptedString, Base64.DEFAULT)), output);
//            ArrayList<Byte> values = new ArrayList<>();
//            int nextByte;
//            while ((nextByte = cipherInputStream.read()) != -1) {
//                values.add((byte)nextByte);
//            }
//
//            byte[] bytes = new byte[values.size()];
//            for(int i = 0; i < bytes.length; i++) {
//                bytes[i] = values.get(i).byteValue();
//            }
//
//            String finalText = new String(bytes, 0, bytes.length, "UTF-8");
//            return finalText;

        } catch (Exception e) {
            return e.getMessage();
        }
    }


    private String getProvider() {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { // below android m
                return "AndroidOpenSSL"; // error in android 6: InvalidKeyException: Need RSA private or public key
            } else { // android m and above
                return "AndroidKeyStoreBCWorkaround"; // error in android 5: NoSuchProviderException: Provider not available: AndroidKeyStoreBCWorkaround
            }
        } catch (Exception exception) {
            throw new RuntimeException("getCipher: Failed to get an instance of Cipher", exception);
        }
    }
}
