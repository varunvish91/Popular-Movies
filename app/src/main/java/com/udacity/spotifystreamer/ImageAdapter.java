package com.udacity.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Class which will display the images to the user.
 */
public class ImageAdapter extends ArrayAdapter<MovieDetail> {

    public ImageAdapter(Context context, ArrayList<MovieDetail> list) {
        super(context, 0, list);
    }


    // Just returns an imageview to inflate
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_view, parent, false);

        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.image);

        Picasso.with(getContext()).load(getItem(position).imageUrl).into(imageView);
        imageView.setPadding(1, 1, 1, 1);
        return convertView;
    }
}
