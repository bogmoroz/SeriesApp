package com.example.hva.seriesapp;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by Bogdan on 3.10.2016.
 */

public class GridItem {

    private String title;
    private int imageSource;
    private String url;
    private ImageView imageView;


//    @Override
//    protected Bitmap doInBackground(Void... params) {
//        try {
//
//        }
//    }

    public GridItem(String title, int imageSource) {
        this.title = title;
        this.imageSource = imageSource;
    }

    public String getTitle() {
        return title;
    }

    public int getImageSource() {
        return imageSource;
    }

}
