package com.teecoin.model.general;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.teecoin.utils.EnumMgr;

public class FacebookInfoModel {

    @SerializedName("provider")
    @Expose
    private String provider;

    @SerializedName("provider_id")
    @Expose
    private String provider_id;

    @SerializedName("access_token")
    @Expose
    private String access_token;


    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("email")
    @Expose
    private String email;

    public FacebookInfoModel(String access_token, String provider_id) {
        this.provider_id = provider_id;
        this.access_token = access_token;
        this.provider = EnumMgr.SignUpType.Facebook.name();
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
