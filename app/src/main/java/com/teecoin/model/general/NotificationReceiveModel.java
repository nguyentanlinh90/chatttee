package com.teecoin.model.general;

import java.io.Serializable;

public class NotificationReceiveModel implements Serializable {
    private String title;
    private String body;

    public NotificationReceiveModel(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


    @Override
    public String toString() {
        return "NotificationRecieModel{" +
                "title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
