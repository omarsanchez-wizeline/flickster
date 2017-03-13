package com.wizeline.omarsanchez.flickster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by omarsanchez on 3/9/17.
 */

public class Movie implements Serializable {

    public enum Popularity {
        LOW, HIGH
    }

    private final String imageUrl = "https://image.tmdb.org/t/p/w342/%s";
    private String posterPath;
    private String originalTitle;
    private String overview;
    private String backdropPath;
    private float average;
    private Popularity popularity;
    private String release;

    private int id;


    public int getId() {
        return id;
    }

    public String getImagePath() {
        if (popularity == Popularity.LOW) {
            return String.format(imageUrl, posterPath);
        } else {
            return String.format(imageUrl, backdropPath);
        }
    }

    public float getAverage() {
        return average;
    }

    public Popularity getPopularity() {
        return popularity;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease() {
        String result = release;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat df2 = new SimpleDateFormat("dd MMM yyyy");
        try {
            Date startDate = df.parse(release);
            result = df2.format(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    public Movie(JSONObject jsonObject) throws JSONException {
        this.posterPath = jsonObject.getString("poster_path");
        this.originalTitle = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.average = (float) jsonObject.getDouble("vote_average");
        if (average >= 5) {
            this.popularity = Popularity.HIGH;
        } else {
            this.popularity = Popularity.LOW;
        }
        this.id = jsonObject.getInt("id");
        this.release = jsonObject.getString("release_date");

    }

    public static ArrayList<Movie> fromJSONArray(JSONArray array) {
        ArrayList<Movie> results = new ArrayList<>();
        for (int x = 0; x < array.length(); x++) {
            try {
                results.add(new Movie(array.getJSONObject(x)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
