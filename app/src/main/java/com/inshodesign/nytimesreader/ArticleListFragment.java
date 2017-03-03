
package com.inshodesign.nytimesreader;

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


public class ArticleListFragment extends Fragment  {

    List<NYTimesArticle> loadedArticles;  // Downloaded in mainactivity and passed here
    
    private BrewPunkBeer loadedBeer;
    private ArrayList<String> loadedBeerNames;
    private RecyclerView mRecyclerView;
    private ArticleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Subscription subscription;
    private RxBus _rxBus = new RxBus();
    private final static String TAG = "TEST";
    private boolean debug = false;

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
                if(debug){Log.d("TEST","c is null!");};
            }
            c.close();

            db.close();
            helper.close();

        }

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        if(debug){Log.d("TEST","Articles: " + loadedArticles);}
           mAdapter = new ArticleAdapter(loadedArticles, _rxBus);

        _rxBus.toObserverable()
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object event) {

                        if(event instanceof Integer) {
                            if(debug){Log.d(TAG,"BUS INT: " + event);}
                            NYTimesArticle article = loadedArticles.get(((Integer) event));
                            if(debug){Log.d(TAG,"ARTICLE TITLE SENT: " + article.title);}
                            String beerRecTMP = beerRecommendation(loadedBeerNames,article.title);
                            if(debug){Log.d(TAG,"Recommended Beer: " + beerRecTMP);}
                            getBeerData(beerRecTMP,article );



                        }
                    }

                });

        mRecyclerView.setAdapter(mAdapter);

    }

    private void getBeerData(String beer, NYTimesArticle article) {

            subscription = BrewPunkClient.getInstance()
                    .getBeerData(beer)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<BrewPunkBeer>>() {
                        @Override public void onCompleted() {
                            if(debug){Log.d(TAG, "In onCompleted()");};

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
                            if(debug){Log.d(TAG, "In onError()");}
                            Toast.makeText(getActivity(), "Unable to connect to BrewPunk API", Toast.LENGTH_SHORT).show();
                        }

                        @Override public void onNext(List<BrewPunkBeer> beer) {
                            if(debug){Log.d(TAG, "In onNext()");}

                            //Get the first beer
                            loadedBeer = beer.get(0);



                        }
                    });

    }



    private String beerRecommendation(ArrayList<String> beerNames, String articleTitle) {
        //Get the title of the article, and parse the words in it into a list
        List<String> wordsFromSentence = new ArrayList<String>(Arrays.asList(articleTitle.split(" ")));
        Set<String> uniqueWords = new HashSet<String>(wordsFromSentence);
        if(debug) {
            Log.d("TEST", "uniqueWords: " + uniqueWords);
            Log.d("TEST", "loadedBeerNames: " + beerNames);
        }
        String beerRecomendation = "";

        for (String word : uniqueWords) {
            if (beerNames != null && beerNames.size() > 0) {
                for (String beer : beerNames) {

                    if (beer.toLowerCase().contains(word.trim().toLowerCase())) {
                        //Make this one the beer recommendation
                        if (beerRecomendation.equals("")) {
                            beerRecomendation = beer;
                            if(debug){Log.d("TEST", "beerRecommendation = " + beer);}
                        } else if (word.length() > beerRecomendation.length()) {
                            beerRecomendation = beer;
                            if(debug){Log.d("TEST", "beerRecommendation replaced = " + beer);}

                        }
                    }
                }

            }

            //IF we didn't find anything call it random
            if (beerRecomendation.length() == 0) {
                beerRecomendation = "random";
            }

        }
        return beerRecomendation;
    }




}