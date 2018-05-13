package com.testy.latestnews;

import android.graphics.Bitmap;

/**
 * Created by Mukul Ahlawat on 3/3/2018.
 */


public class Item {
    String imageurl;
    String i;

    public Item( String imageurl,String i) {
        this.imageurl = imageurl;
        this.i = i;
    }

    public String getImage()
    {
        return imageurl;
    }
    public String index(){return i;}
}
