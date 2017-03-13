package com.wizeline.omarsanchez.flickster.interfaces;

/**
 * Created by omarsanchez on 3/10/17.
 */

public interface MovieRequest {

    void getMovie(ResultRequest.ResultMovies resultRequest);

    void getVideo(ResultRequest.ResultVideos resultVideos, int idVideo);
}
