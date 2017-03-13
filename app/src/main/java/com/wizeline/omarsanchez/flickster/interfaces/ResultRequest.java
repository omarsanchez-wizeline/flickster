package com.wizeline.omarsanchez.flickster.interfaces;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by omarsanchez on 3/10/17.
 */

public interface ResultRequest {
    interface ResultMovies {
        void onResultRequest(JSONArray jsonArray);
    }

    interface ResultVideos {
        void onResultVideo(List<String> videos);
    }
}
