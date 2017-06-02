package com.inshodesign.nytimesreader.API_Clients;


import android.support.annotation.NonNull;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inshodesign.nytimesreader.API_Interfaces.BrewDogService;
import com.inshodesign.nytimesreader.Models.BrewDogBeer;

import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Client for retrofit call to BrewDog API
 * @see BrewDogService
 */
public class BrewDogClient {

    private static final String BrewPunk_Base_URL = "https://api.punkapi.com/v2/";
    private static BrewDogClient instance;
    private BrewDogService brewdogService;

    private BrewDogClient() {
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
    public static BrewDogClient getInstance() {
        if (instance == null) {
            instance = new BrewDogClient();
        }
        return instance;
    }

    //Returns a BrewDogBeer object with data for a particular BrewDog beer (chosen at random or by beer name)

    /**
     * Observable of a BrewDogBeer objects, containing data for a particular BrewDog beer (chosen at random or by beer name)
     * @param beer beer to choose ("random" will return random beer. A name may have more than one match)
     * @return brewdog beer observable
     */
    public Observable<List<BrewDogBeer>> getBeerData(@NonNull String beer) {
        if(beer.equals("random")) {
            return brewdogService.getRandomBeer();
        } else {
            return brewdogService.getBeers(beer);
        }
    }

}

