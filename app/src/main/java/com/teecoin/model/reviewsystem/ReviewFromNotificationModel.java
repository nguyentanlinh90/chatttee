package com.teecoin.model.reviewsystem;

import java.io.Serializable;

public class ReviewFromNotificationModel implements Serializable {
    private String transaction_id;
    private String destination;

    public ReviewFromNotificationModel(String transaction_id, String destination) {
        this.transaction_id = transaction_id;
        this.destination = destination;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "ReviewFromNotificationModel{" +
                "transaction_id='" + transaction_id + '\'' +
                ", destination='" + destination + '\'' +
                '}';
    }
}
