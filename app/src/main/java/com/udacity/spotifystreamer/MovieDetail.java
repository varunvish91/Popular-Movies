package com.udacity.spotifystreamer;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by viswan on 4/18/16.
 */
public class MovieDetail implements Serializable{

    public String title;
    public String plot;
    public String imageUrl;
    public int id;
    public String releaseDate;
    public String voteAverage;
}
