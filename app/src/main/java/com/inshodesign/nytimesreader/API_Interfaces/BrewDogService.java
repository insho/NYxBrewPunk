package com.inshodesign.nytimesreader.API_Interfaces;

import com.inshodesign.nytimesreader.API_Clients.BrewDogClient;
import com.inshodesign.nytimesreader.Models.BrewDogBeer;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Get requests to the BrewDog API service. Used in {@link BrewDogClient} retrofit call.
 */
public interface BrewDogService {

    //Returns a beer by name
    @GET("beers")
    Observable<List<BrewDogBeer>> getBeers(@Query("beer_name") String beer);

    //Returns a random beer
    @GET("beers/random")
    Observable<List<BrewDogBeer>> getRandomBeer();

}