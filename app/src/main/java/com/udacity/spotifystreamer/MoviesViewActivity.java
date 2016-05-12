package com.udacity.spotifystreamer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MoviesViewActivity extends AppCompatActivity implements ActionBar.OnNavigationListener {
    private static String API_KEY = "df33dbf9a5dbeccbac9e8c0ffd96969d";
    private static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static String TAG = "MoviesViewActivity";

    // JSON Parsing private members
    private static String RESULTS = "results";
    private static String POSTER_PATH = "poster_path";
    private static String ID = "id";
    private static String TITLE = "original_title";
    private static String POPULARITY = "popularity";
    private static String VOTE = "vote_count";
    private static String VOTE_AVG = "vote_average";
    private static String RELEASE_DATE = "release_date";
    private static String PLOT = "overview";

    String mCurrentSortMethod;
    SharedPreferences mPrefs;


    private ArrayList<MovieDetail> mMovieList;

    private class MovieDownloadTask extends AsyncTask<Void, Integer, ArrayList<MovieDetail>> {
        @Override
        protected ArrayList<MovieDetail> doInBackground(Void... params) {
            // Make request to the Movie Database
            StringBuilder result = new StringBuilder();
            HttpURLConnection urlConnection = null;
            mMovieList = new ArrayList<MovieDetail>();
            try {

                URL url = new URL(getURLPreference());
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                String resultString = result.toString();

                // Build our List of movies
                try {
                    JSONObject jsonObject = new JSONObject(resultString);
                    JSONArray movieDetails = jsonObject.getJSONArray(RESULTS);
                    for (int i = 0; i < movieDetails.length(); i++) {
                        MovieDetail movieDetail = new MovieDetail();
                        movieDetail.imageUrl = IMAGE_BASE_URL + "/w500" + movieDetails.getJSONObject(i).getString(POSTER_PATH);
                        movieDetail.id = movieDetails.getJSONObject(i).getInt(ID);
                        movieDetail.title = movieDetails.getJSONObject(i).getString(TITLE);
                        movieDetail.plot = movieDetails.getJSONObject(i).getString(PLOT);
                        movieDetail.voteAverage = movieDetails.getJSONObject(i).getString(VOTE_AVG);
                        movieDetail.releaseDate = movieDetails.getJSONObject(i).getString(RELEASE_DATE);

                        mMovieList.add(movieDetail);
                    }

                } catch (JSONException e) {
                    // If there is an exception, log it but do not display anything or count
                    // it towards our total movies displayed
                }



            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }

            return mMovieList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<MovieDetail> list) {
            super.onPostExecute(list);

            // Update our Grid adapter with the contents of the map
            Log.i(TAG, "OnPostExecute");
            GridView gridView = (GridView) findViewById(R.id.gridView);
            gridView.setAdapter(new ImageAdapter(getApplicationContext(), list));
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    MovieDetail movieDetail = ((MovieDetail) parent.getItemAtPosition(position));
                    Intent intent = new Intent(getApplicationContext(), MovieViewActivity.class);
                    intent.putExtra("MovieDetailClass", movieDetail);
                    startActivity(intent);
                }
            });
        }

        private String getURLPreference() {
            final String sortPreference = mPrefs.getString(getString(R.string.sort), getString(R.string.sort_ratings));
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    changeToolbar(sortPreference);
                }
            });
            StringBuilder sbb = new StringBuilder();
            sbb.append("http://api.themoviedb.org/3/movie/");
            sbb.append(sortPreference);
            sbb.append("?api_key=" + API_KEY);
            Log.i(TAG, "url= " + sbb.toString());
            return sbb.toString();
        }
    }

    private void changeToolbar(String sort) {
        if (sort.equals(getString(R.string.sort_popularity))) {
            getSupportActionBar().setTitle(getString(R.string.popular_movies));
        } else {
            getSupportActionBar().setTitle(getString(R.string.top_rated_movies));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_view);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (mMovieList == null || mMovieList.isEmpty()) {
            loadMovies();
        }

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
                startActivity(new Intent(MoviesViewActivity.this, SettingsActivity.class));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadMovies() {

        // Make a request to the Movies DB to get the data, so we can populate
        // our Movie Detail List object
        // Default sort by popular
        new MovieDownloadTask().execute();
    }

    @Override
    public boolean onNavigationItemSelected(int position, long id) {
        // When the given dropdown item is selected, show its contents in the
        // container view.

        return true;
    }

    @Override
    protected void onResume() {
        loadMovies();
        super.onResume();
    }
}
