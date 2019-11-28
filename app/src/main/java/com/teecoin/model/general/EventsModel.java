package com.teecoin.model.general;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;


public class EventsModel extends RealmObject implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("start_date")
    @Expose
    private String start_date;
    @SerializedName("end_date")
    @Expose
    private String end_date;
    @SerializedName("register_amount")
    @Expose
    private String register_amount;
    @SerializedName("release_date")
    @Expose
    private String release_date;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("event_url")
    @Expose
    private String event_url;

    @SerializedName("image")
    @Expose
    private ImageEvent image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getRegister_amount() {
        return register_amount;
    }

    public void setRegister_amount(String register_amount) {
        this.register_amount = register_amount;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEvent_url() {
        return event_url;
    }

    public void setEvent_url(String event_url) {
        this.event_url = event_url;
    }

    public ImageEvent getImage() {
        return image;
    }

    public void setImage(ImageEvent image) {
        this.image = image;
    }
}
