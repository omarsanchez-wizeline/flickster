package com.wizeline.omarsanchez.flickster.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.wizeline.omarsanchez.flickster.R;
import com.wizeline.omarsanchez.flickster.WebService.WebService;
import com.wizeline.omarsanchez.flickster.adapters.MovieArrayAdapter;
import com.wizeline.omarsanchez.flickster.interfaces.ResultRequest;
import com.wizeline.omarsanchez.flickster.models.Movie;

import org.json.JSONArray;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieActivity extends AppCompatActivity {

    ArrayList<Movie> movies;
    MovieArrayAdapter movieAdapter;
    @BindView(R.id.lvMovies)
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);
        movies = new ArrayList<>();
        movieAdapter = new MovieArrayAdapter(this, movies);
        lvItems.setAdapter(movieAdapter);
        WebService.getInstance().request.getMovie(new ResultRequest.ResultMovies() {
            @Override
            public void onResultRequest(JSONArray jsonArray) {
                movies.addAll(Movie.fromJSONArray(jsonArray));
                movieAdapter.notifyDataSetChanged();
            }
        });
    }
}
