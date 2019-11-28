package com.teecoin.model.general;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProfileModel implements Serializable {
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("first_name")
    @Expose
    private String first_name;
    @SerializedName("last_name")
    @Expose
    private String last_name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("joined_date")
    @Expose
    private String joined_date;
    @SerializedName("public_key")
    @Expose
    private String public_key;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("full_name")
    @Expose
    private String full_name;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("dob")
    @Expose
    private String dob;

    @SerializedName("vendor")
    @Expose
    private Vendor vendor;

    public ProfileModel() {
    }

    public String getUuid() {
        return uuid;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public String getJoined_date() {
        return joined_date;
    }

    public String getPublic_key() {
        return public_key;
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

    public String getGender() {
        return gender;
    }

    public String getDob() {
        return dob;
    }

    public Vendor getVendor() {
        return vendor;
    }
}

