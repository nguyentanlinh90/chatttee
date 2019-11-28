package com.teecoin.model.general;

import java.io.Serializable;

import okhttp3.MultipartBody;


public class UpdateAvatarModel implements Serializable {

    public static String MEDIA_FIELD="avatar";
    public static String FILE_EXTENSION="jpg";
    private String uuid;
    private MultipartBody.Part avatar;

    public UpdateAvatarModel() {
    }

    public UpdateAvatarModel(String uuid, MultipartBody.Part avatar) {
        this.uuid = uuid;
        this.avatar = avatar;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public MultipartBody.Part getAvatar() {
        return avatar;
    }

    public void setAvatar(MultipartBody.Part avatar) {
        this.avatar = avatar;
    }
}
