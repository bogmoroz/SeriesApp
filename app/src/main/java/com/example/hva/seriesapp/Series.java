package com.example.hva.seriesapp;

/**
 * Created by Bogdan on 3.10.2016.
 */

public class Series {

    private long id;
    private String title;
    private int imageSource;

//    public Series (String title, int imageSource) {
//        this.title = title;
//        this.imageSource = imageSource;
//    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageSource() {
        return imageSource;
    }

    public void setImageSource(int imageSource) {
        this.imageSource = imageSource;
    }

    @Override
    public String toString() {
        return title;
    }

}
