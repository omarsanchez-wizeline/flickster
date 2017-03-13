package com.wizeline.omarsanchez.flickster.WebService;

import android.os.Handler;
import android.os.Looper;

import com.wizeline.omarsanchez.flickster.interfaces.MovieRequest;
import com.wizeline.omarsanchez.flickster.interfaces.ResultRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by omarsanchez on 3/10/17.
 */

public class WebService implements MovieRequest {
    public MovieRequest request;

    private static OkHttpClient client;
    private final String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    private final String urlVideos = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    JSONArray movieJsonResults = null;

    private WebService() {
        request = this;
    }

    public static WebService getInstance() {
        return new WebService();
    }

    private OkHttpClient getClient() {

        if (client == null) {
            client = new OkHttpClient();
        }

        return client;
    }

    private String getUrlVideos(int idVideo) {
        return String.format(urlVideos, idVideo);
    }

    private Request getRequest(String url) {
        return new Request.Builder()
                .url(url)
                .build();
    }

    ///request
    private void getMovies(final ResultRequest.ResultMovies resultRequest) {

        getClient().newCall(getRequest(url)).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    String responseData = response.body().string();
                    JSONObject json = new JSONObject(responseData);
                    movieJsonResults = json.getJSONArray("results");
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            resultRequest.onResultRequest(movieJsonResults);
                        }
                    });
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void getYoutubeVideo(final ResultRequest.ResultVideos resultVideos, int idVideo) {
        getClient().newCall(getRequest(getUrlVideos(idVideo))).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final List<String> videos = new ArrayList<String>();
                    String responseData = response.body().string();
                    JSONObject json = new JSONObject(responseData);
                    movieJsonResults = json.getJSONArray("results");
                    for (int x = 0; x < movieJsonResults.length(); x++) {
                        try {
                            videos.add(movieJsonResults.getJSONObject(x).getString("key"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            resultVideos.onResultVideo(videos);
                        }
                    });
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

///interfaces

    @Override
    public void getMovie(ResultRequest.ResultMovies resultRequest) {
        getMovies(resultRequest);
    }

    @Override
    public void getVideo(ResultRequest.ResultVideos resultVideos, int idVideo) {
        getYoutubeVideo(resultVideos, idVideo);
    }

}
