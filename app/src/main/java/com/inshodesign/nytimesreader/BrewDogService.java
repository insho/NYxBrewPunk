package com.inshodesign.nytimesreader;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by JClassic on 2/23/2017.
 */

public interface BrewDogService {

    @GET("beers")
    Observable<List<BrewPunkBeer>> getBeers(@Query("beer_name") String beer);

    @GET("beers/random")
    Observable<List<BrewPunkBeer>> getRandomBeer();

}