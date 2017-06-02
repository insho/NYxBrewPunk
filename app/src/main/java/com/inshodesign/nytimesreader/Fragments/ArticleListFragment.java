
package com.inshodesign.nytimesreader.Fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.inshodesign.nytimesreader.API_Clients.NYTimesClient;
import com.inshodesign.nytimesreader.Adapters.ArticleAdapter;
import com.inshodesign.nytimesreader.Adapters.RxBus;
import com.inshodesign.nytimesreader.BeerInfoPopup;
import com.inshodesign.nytimesreader.API_Clients.BrewDogClient;
import com.inshodesign.nytimesreader.BuildConfig;
import com.inshodesign.nytimesreader.ExternalDB;
import com.inshodesign.nytimesreader.Models.BrewDogBeer;
import com.inshodesign.nytimesreader.Models.NYTimesArticle;
import com.inshodesign.nytimesreader.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Displays a set of New York Times articles recieved from the API call (in {@link NYTimesClient}). Clicking
 * on the article will bring up the {@link BeerInfoPopup}, with a recommendation for a BrewDog beer based on the article title.
 */
public class ArticleListFragment extends Fragment  {

    private ArrayList<NYTimesArticle> loadedArticles;  // Downloaded in mainactivity and passed here
    private BrewDogBeer loadedBeer;
    private ArrayList<String> loadedBeerNames;
    private RecyclerView mRecyclerView;
    private Subscription beerInfoSubscription;
    private RxBus _rxBus = new RxBus();
    private final static String TAG = "TEST";
    private BeerInfoPopup beerInfoPopup  = null;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_articlesrecycler, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        loadedArticles = getArguments().getParcelableArrayList("loadedArticles");
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        //Use a subscriber to pull database data
        if(savedInstanceState!=null) {
            loadedArticles = savedInstanceState.getParcelableArrayList("loadedArticles");
            loadedBeerNames = savedInstanceState.getParcelable("loadedBeerNames");
        }

        if(loadedBeerNames == null) {
            loadedBeerNames = new ArrayList<>();
            ExternalDB helper = ExternalDB.getInstance(getActivity());
            SQLiteDatabase db = helper.getWritableDatabase();

            Cursor c = db.rawQuery("SELECT DISTINCT TRIM([NameUnderscored]) from BeerNames WHERE [Name] is not null", null);
            c.moveToFirst();
            if(c.getCount()>0) {
                while (!c.isAfterLast()) {
                    loadedBeerNames.add(c.getString(0));
                    c.moveToNext();
                }
            } else {
                //If there is an error, and nothing returned, the getBeerData routine will end up choosing "random"
                //So it's all good
                if(BuildConfig.DEBUG){Log.d("TEST","c is null!");};
            }
            c.close();

            db.close();
            helper.close();

        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if(BuildConfig.DEBUG){Log.d("TEST","Articles: " + loadedArticles);}
          ArticleAdapter mAdapter = new ArticleAdapter(loadedArticles, _rxBus);

        _rxBus.toObserverable()
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object event) {

                        if(event instanceof Integer) {
                            //Create beer recommendation based on nytimes article and pass it to brewdog api call to get beer info
                            NYTimesArticle article = loadedArticles.get(((Integer) event));
                            String beerRecTMP = beerRecommendation(loadedBeerNames,article.getTitle());
                            getBeerData(beerRecTMP,article );
                        }
                    }

                });

        mRecyclerView.setAdapter(mAdapter);

    }

    /**
     * Makes call to BrewDog API and returns information for
     * a given beer name (that was reocmmended based on an NY times article)
     * @param beer recommended beer
     * @param article chosen article
     */
    private void getBeerData(String beer, NYTimesArticle article) {

            beerInfoSubscription = BrewDogClient.getInstance()
                    .getBeerData(beer)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<BrewDogBeer>>() {
                        @Override public void onCompleted() {
                            if(BuildConfig.DEBUG){Log.d(TAG, "In onCompleted()");};

                            if(beerInfoPopup == null || !beerInfoPopup.isShowing()) {
                                String namematch = "Name Match";
                                if(beer.equals("random")) {
                                    namematch = "Random";
                                }
                                beerInfoPopup = new BeerInfoPopup(getActivity(), getView(),article, loadedBeer, namematch);
                                beerInfoPopup.CreateView();

                            }

                        }

                        @Override public void onError(Throwable e) {
                            e.printStackTrace();
                            if(BuildConfig.DEBUG){Log.d(TAG, "In onError()");}
                            Toast.makeText(getActivity(), "Unable to connect to BrewPunk API", Toast.LENGTH_SHORT).show();
                        }

                        @Override public void onNext(List<BrewDogBeer> beer) {
                            if(BuildConfig.DEBUG){Log.d(TAG, "In onNext()");}
                            //Get the first beer
                            loadedBeer = beer.get(0);
                        }
                    });
    }


    /**
     * Recommends a beer name for a given NYTimes article. It does so by parsing the title and trying to match
     * a word in the title with a word in the db of brewdog beers ({@link ExternalDB}). If a match is found,
     * it returns that beer. If no matches are found, it returns a random beer
     * @param beerNames Array of beer name from the external db
     * @param articleTitle Title of chosen NYTimes article
     * @return recommended beer based on title name
     */
    private String beerRecommendation(ArrayList<String> beerNames, String articleTitle) {
        //Get the title of the article, and parse the words in it into a list
        List<String> wordsFromSentence = new ArrayList<String>(Arrays.asList(articleTitle.split(" ")));
        Set<String> uniqueWords = new HashSet<String>(wordsFromSentence);
        if(BuildConfig.DEBUG) {
            Log.d("TEST", "uniqueWords: " + uniqueWords);
            Log.d("TEST", "loadedBeerNames: " + beerNames);
        }
        String beerRecomendation = "";

        //Cycles through words in article and attempts to match them with beer names
        for (String word : uniqueWords) {
            if (beerNames != null && beerNames.size() > 0) {
                for (String beer : beerNames) {

                    if (beer.toLowerCase().contains(word.trim().toLowerCase())) {
                        //Make this one the beer recommendation
                        if (beerRecomendation.equals("")) {
                            beerRecomendation = beer;
                            if(BuildConfig.DEBUG){Log.d("TEST", "beerRecommendation = " + beer);}
                        } else if (word.length() > beerRecomendation.length()) {
                            beerRecomendation = beer;
                            if(BuildConfig.DEBUG){Log.d("TEST", "beerRecommendation replaced = " + beer);}

                        }
                    }
                }

            }
            /*If nothing is found call it random, which will be passed
              to the BrewDogService, returning a random beer */
            if (beerRecomendation.length() == 0) {
                beerRecomendation = "random";
            }

        }
        return beerRecomendation;
    }


    @Override
    public void onPause() {
        if(beerInfoSubscription !=null) {
            beerInfoSubscription.unsubscribe();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if(beerInfoSubscription !=null) {
            beerInfoSubscription.unsubscribe();
        }
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("loadedBeerNames",loadedBeerNames);
        outState.putParcelableArrayList("loadedArticles",loadedArticles);
    }
}