package com.udacity.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        MovieDetail movieDetail = (MovieDetail) i.getSerializableExtra("MovieDetailClass");


        TextView movieTitle = (TextView) findViewById(R.id.movie_title);
        TextView movieRating = (TextView) findViewById(R.id.movie_rating);
        TextView moviePLot = (TextView) findViewById(R.id.plot_synopsis);
        TextView releaseDate = (TextView) findViewById(R.id.movie_release_Date);
        ImageView moviePoster = (ImageView) findViewById(R.id.movie_image);

        Picasso.with(this).load(movieDetail.imageUrl).into(moviePoster);

        movieTitle.setText("Title: " + movieDetail.title);
        movieRating.setText("Vote Average: " + movieDetail.voteAverage);
        moviePLot.setText(movieDetail.plot);
        releaseDate.setText("Release Date: " + movieDetail.releaseDate);
        getSupportActionBar().setTitle(movieDetail.title);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.settings:
                startActivity(new Intent(MovieViewActivity.this, SettingsActivity.class));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
