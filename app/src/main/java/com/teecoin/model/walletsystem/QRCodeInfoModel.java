package com.teecoin.model.walletsystem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class QRCodeInfoModel implements Serializable {
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("joined_date")
    @Expose
    private String joinedDate;
    @SerializedName("public_key")
    @Expose
    private String publicKey;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("avatar")
    @Expose
    private String avatar;

    @SerializedName("full_name")
    @Expose
    private String full_name;


    public String getUuid() {
        return uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getJoinedDate() {
        return joinedDate;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getLanguage() {
        return language;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getFull_name() {
        return full_name;
    }
}
