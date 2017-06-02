package com.inshodesign.nytimesreader.API_Interfaces;

import com.inshodesign.nytimesreader.API_Clients.NYTimesClient;
import com.inshodesign.nytimesreader.Models.NYTimesArticleWrapper;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Get requests to the NYTimes API service. Used in {@link NYTimesClient} retrofit call.
 */
public interface NYTimesService {

        //Requests a set of articles based on article category (chosen in MainFragment), article section (SubFragment), and a time range
        @GET("{requesttype}/{section}/{time}.json")
        Observable<NYTimesArticleWrapper> getArticles(@Path("requesttype") String requesttype
                , @Path("section") String section
                , @Path("time") String time
                , @Query("api-key") String apiKey);

}
