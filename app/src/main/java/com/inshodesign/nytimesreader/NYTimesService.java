package com.inshodesign.nytimesreader;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by JClassic on 2/21/2017.
 */

public interface NYTimesService {

        @GET("{requesttype}/{section}/{time}.json") Observable<NYTimesArticleWrapper> getArticles(@Path("requesttype") String requesttype, @Path("section") String section, @Path("time") String time,  @Query("api-key") String apiKey);

}
