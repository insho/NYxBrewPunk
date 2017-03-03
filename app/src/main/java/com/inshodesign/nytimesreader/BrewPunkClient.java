package com.inshodesign.nytimesreader;


import android.support.annotation.NonNull;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by JClassic on 2/21/2017.
 */

public class BrewPunkClient {

    private static final String BrewPunk_Base_URL = "https://api.punkapi.com/v2/";
    private static BrewPunkClient instance;
    private BrewDogService brewdogService;

    private BrewPunkClient() {
        final Gson gson =
                new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

        OkHttpClient.Builder client = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.addInterceptor(loggingInterceptor);

        final Retrofit retrofit = new Retrofit.Builder().baseUrl(BrewPunk_Base_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client.build())
                .build();

        brewdogService = retrofit.create(BrewDogService.class);
    }
    public static BrewPunkClient getInstance() {
        if (instance == null) {
            instance = new BrewPunkClient();
        }
        return instance;
    }

    public Observable<List<BrewPunkBeer>> getBeerData(@NonNull String beer) {
        if(beer.equals("random")) {
            return brewdogService.getRandomBeer();
        } else {
            return brewdogService.getBeers(beer);
        }
    }

}

