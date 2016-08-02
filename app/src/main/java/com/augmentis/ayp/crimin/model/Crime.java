package com.augmentis.ayp.crimin.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.Phaser;

/**
 * Created by Tanaphon on 7/18/2016.
 */
public class Crime {
    private UUID id;
    private String title;
    private Date crimeDate;
    private boolean solved;
    private String suspect;

    public String getSuspect() {
        return suspect;
    }

    public void setSuspect(String suspect) {
        this.suspect = suspect;
    }

    public Crime() {
        this(UUID.randomUUID());
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCrimeDate() {
        return crimeDate;
    }


    public void setCrimeDate(Date crimeDate) {
        this.crimeDate = crimeDate;
    }

    public Crime(UUID uuid){
        this.id = uuid;
        crimeDate = new Date();
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public String getSimpleDateFormat(Date date) {
        return new SimpleDateFormat("dd MMMM yyyy").format(date);
    }

    public String getSimpleTimeFormat(Date date){
        return new SimpleDateFormat("hh : mm a").format(date);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UUID ").append(id);
        builder.append(",Title ").append(title);
        builder.append(",Crime Date ").append(getSimpleDateFormat(crimeDate));
        builder.append(",Solved ").append(solved);
        builder.append(",Suspect ").append(suspect);
        return builder.toString();
    }
}
