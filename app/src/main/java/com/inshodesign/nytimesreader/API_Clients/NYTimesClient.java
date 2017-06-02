package com.inshodesign.nytimesreader.API_Clients;

import android.support.annotation.NonNull;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inshodesign.nytimesreader.API_Interfaces.NYTimesService;
import com.inshodesign.nytimesreader.Models.NYTimesArticleWrapper;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Client for retrofit call to NYTimes API
 * @see NYTimesService
 */
public class NYTimesClient {

    private static final String NYTIMES_BASE_URL = "https://api.nytimes.com/svc/mostpopular/v2/";
    private static NYTimesClient instance;
    private NYTimesService nytimesService;

    private NYTimesClient() {
        final Gson gson =
                new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

        OkHttpClient.Builder client = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.addInterceptor(loggingInterceptor);

        final Retrofit retrofit = new Retrofit.Builder().baseUrl(NYTIMES_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client.build())
                .build();

        nytimesService = retrofit.create(NYTimesService.class);
    }

    public static NYTimesClient getInstance() {
        if (instance == null) {
            instance = new NYTimesClient();
        }
        return instance;
    }

    /**
     * Creates observable set of NY Times articles contained in {@link NYTimesArticleWrapper} (an object with the array of {@link com.inshodesign.nytimesreader.Models.NYTimesArticle} and
     * info about the set)
     * @param requesttype Article category  (chosen in {@link com.inshodesign.nytimesreader.Fragments.MainFragment}
     * @param section article section ({@link com.inshodesign.nytimesreader.Fragments.SubFragment}
     * @param time time range of articles to pull
     * @param apikey api key
     * @return set of ny times articles
     */
    public Observable<NYTimesArticleWrapper> getArticles(@NonNull String requesttype, @NonNull String section, @NonNull String time, @NonNull String apikey) {
        return nytimesService.getArticles(requesttype, section, time,apikey);
    }


}

