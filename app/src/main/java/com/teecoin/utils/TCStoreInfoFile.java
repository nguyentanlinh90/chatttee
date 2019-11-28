package com.teecoin.utils;

import android.os.Environment;

import com.teecoin.model.general.DeviceRegistrationModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class TCStoreInfoFile {

    private static final String path = Environment.getExternalStorageDirectory() + "/Android/data/" + TCUtils.getUniquePseudoID();
    private static final String myFile = TCUtils.getUniquePseudoID(); //"my";
    private static final String key ="TeeCoinReadStoreInfoKey";

    private static String readFromFile(File file) {
        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            //You'll need to add proper error handling here
        }
        return text.toString();
    }


    private static void writeToFile(String data) {
        File folder = new File(path);
        folder.mkdirs();
        File file = new File(folder, myFile);
        // Save your stream, don't forget to flush() it before closing it.
        try {
            if (!file.exists())
                file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(data);
            myOutWriter.close();
            fOut.flush();
            fOut.close();
        } catch (IOException e) {

        }
    }

    public static boolean checkRegistration() {
        // {"create_date":"2018/08/03","create_wallet_count":0,"device_id":"397631353650243"}
        File folder = new File(path);
        File file = new File(folder, myFile);
        String currentDate = TCDateUtility.formatDate(TCDateUtility.getCurrentDate(), TCDateUtility.DateFormatDefinition.DD_MM_YYYY);
        if (file.exists()) {
            String json = Encrytor.decrypt(key, readFromFile(file));
            DeviceRegistrationModel deviceRegistrationModel = new DeviceRegistrationModel();
            if(deviceRegistrationModel.validate(json)){
                deviceRegistrationModel = TCUtils.convertToModel(json, DeviceRegistrationModel.class);
                return deviceRegistrationModel.getCreateWalletCount() < TCConstant.MAX_REGISTRATION_TIMES
                        || !deviceRegistrationModel.getCreateDate().equals(currentDate);
            }else{
                return true;
            }
        }else{
            return true;
        }
    }

    public static void saveLoginInfo(){
        File folder = new File(path);
        File file = new File(folder, myFile);
        String currentDate = TCDateUtility.formatDate(TCDateUtility.getCurrentDate(), TCDateUtility.DateFormatDefinition.DD_MM_YYYY);
        if (file.exists()) {
            String json = Encrytor.decrypt(key, readFromFile(file));
            DeviceRegistrationModel deviceRegistrationModel = new DeviceRegistrationModel();
            if(deviceRegistrationModel.validate(json)){
                deviceRegistrationModel = TCUtils.convertToModel(json, DeviceRegistrationModel.class);
                writeToFile(Encrytor.encrypt(key,TCUtils.convertToJson(new DeviceRegistrationModel(
                        TCUtils.getUniquePseudoID(),
                        deviceRegistrationModel.getCreateWalletCount()+1, currentDate))));
            }else{
                writeToFile(Encrytor.encrypt(key,TCUtils.convertToJson(
                        new DeviceRegistrationModel(TCUtils.getUniquePseudoID(), 0, currentDate))));
            }
        }else{
            writeToFile(Encrytor.encrypt(key,TCUtils.convertToJson(
                    new DeviceRegistrationModel(TCUtils.getUniquePseudoID(), 0, currentDate))));
        }
    }
}
