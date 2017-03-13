package com.wizeline.omarsanchez.flickster.Activities;

import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.wizeline.omarsanchez.flickster.R;
import com.wizeline.omarsanchez.flickster.WebService.WebService;
import com.wizeline.omarsanchez.flickster.interfaces.ResultRequest;
import com.wizeline.omarsanchez.flickster.models.Movie;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends YouTubeBaseActivity implements ResultRequest.ResultVideos {
    public static final String EXTRA_MOVIE = "extra_movie";
    public static final String EXTRA_VIDEO = "extra_video";
    private static final int RECOVERY_REQUEST = 1;
    Movie movie;
    List<String> videos;
    @BindView(R.id.player)
    YouTubePlayerView youTubePlayerView;
    @BindView(R.id.detail_title)
    TextView title;
    @BindView(R.id.detail_overview)
    TextView overview;
    @BindView(R.id.detail_release)
    TextView release;
    @BindView(R.id.ratingBar)
    RatingBar rate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            movie = (Movie) getIntent().getExtras().getSerializable(EXTRA_MOVIE);
        } else {
            movie = (Movie) savedInstanceState.getSerializable(EXTRA_MOVIE);
            videos = savedInstanceState.getStringArrayList(EXTRA_VIDEO);
        }
        title.setText(movie.getOriginalTitle());
        overview.setText(movie.getOverview());
        rate.setRating(movie.getAverage());
        release.setText(String.format(getString(R.string.releaseDate), movie.getRelease()));
        if (videos == null) {
            WebService.getInstance().request.getVideo(this, movie.getId());
        } else {
            initYoutube(videos);
        }
    }

    private void initYoutube(final List<String> idsYoutube) {
        youTubePlayerView.initialize("AIzaSyBALFIOzm9fx_BxVZs6rtqG0f8dTQsnK9k",
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {
                        if (!b) {
                            if (movie.getPopularity() == Movie.Popularity.HIGH) {
                                youTubePlayer.loadVideos(idsYoutube);
                                youTubePlayer.setFullscreen(true);

                            } else {
                                youTubePlayer.cueVideos(idsYoutube);
                            }
                        } else {
                            youTubePlayer.play();
                        }
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult errorReason) {
                        if (errorReason.isUserRecoverableError()) {
                            errorReason.getErrorDialog(DetailActivity.this, RECOVERY_REQUEST).show();
                        } else {
                            String error = String.format(getString(R.string.player_error), errorReason.toString());
                            Toast.makeText(DetailActivity.this, error, Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }


    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putSerializable(EXTRA_MOVIE, movie);
        bundle.putStringArrayList(EXTRA_VIDEO, (ArrayList<String>) videos);

    }

    @Override
    protected void onStop() {
        youTubePlayerView.destroyDrawingCache();
        super.onStop();

    }

    @Override
    public void onResultVideo(List<String> videos) {
        this.videos = videos;
        initYoutube(videos);
    }
}
