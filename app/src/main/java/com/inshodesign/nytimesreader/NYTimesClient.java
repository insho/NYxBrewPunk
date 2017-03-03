package com.inshodesign.nytimesreader;

import android.support.annotation.NonNull;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by JClassic on 2/21/2017.
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

    public Observable<NYTimesArticleWrapper> getArticles(@NonNull String requesttype, @NonNull String section, @NonNull String time, @NonNull String apikey) {
        return nytimesService.getArticles(requesttype, section, time,apikey);
    }


}

