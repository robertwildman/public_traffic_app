package org.S1509388.gcu.TrafficApp.Models;

import java.util.Date;

public class Incident {
    private String Title,Description,Comments;
    private Double Lat,Long;
    private Date TimePublished;
    private int counter;
    public Incident(int counter) {
        this.counter= counter;
    }

    public int getCounter() {
        return this.counter;
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

    public Date getTimePublished() {
        return TimePublished;
    }

    public void setTimePublished(Date timePublished) {
        TimePublished = timePublished;
    }
}
