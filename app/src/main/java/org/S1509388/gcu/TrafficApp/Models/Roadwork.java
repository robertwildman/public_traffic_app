package org.S1509388.gcu.TrafficApp.Models;

import java.io.Serializable;
import java.util.Date;

public class Roadwork implements Serializable {
    private String Title,Description,Comments,Delay;
    private Double Lat,Long;
    private Date StartDate,EndDate;
    private int counter;

    public Roadwork(int counter) {
        this.counter = counter;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Double getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = Double.valueOf(lat);
    }

    public Double getLong() {
        return Long;
    }

    public void setLong(String aLong) {
        Long = Double.valueOf(aLong);
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    public String getDelay() {
        return Delay;
    }

    public void setDelay(String delay) {
        Delay = delay;
    }

    public Date getStartDate() {
        return StartDate;
    }

    public void setStartDate(Date startDate) {
        StartDate = startDate;
    }

    public Date getEndDate() {
        return EndDate;
    }

    public void setEndDate(Date endDate) {
        EndDate = endDate;
    }
}
