package com.teecoin.model.couponsystem;

import java.util.Date;

public class TimeOpenForDayMerchantModel {

    private Date timeFrom;
    private Date timeTo;

    public TimeOpenForDayMerchantModel() {
    }

    public Date getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(Date timeFrom) {
        this.timeFrom = timeFrom;
    }

    public Date getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(Date timeTo) {
        this.timeTo = timeTo;
    }

    public void updateTo(TimeOpenForDayMerchantModel data) {
        this.timeFrom = data.timeFrom;
        this.timeTo = data.timeTo;
    }
}
