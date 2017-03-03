package com.inshodesign.nytimesreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import java.util.ArrayList;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements MainFragment.OnMainOptionSelectedListener
        , SubFragment.OnSubOptionSelectedListener {

    MainFragment mainFragment;
    SubFragment subFragment;
    ArticleListFragment articleListFragment;
    private ArrayList<NYTimesArticle> loadedArticles;
    private static final String TAG = "TEST";
    private static final boolean debug = false;
    private Toolbar toolbar;


    /**
     *  You can replace these with spinners later if you want
     */
    private String section = "all-sections";
    private String time = "1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ExternalDB db = new ExternalDB(this);
        db.initializeDBInternalCopy();

        if (savedInstanceState == null) {
            mainFragment = new MainFragment();
            showToolBarBackButton(false, "NYxBrewPunk");
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mainFragment)
                    .commit();
        } else {
            showToolBarBackButton(savedInstanceState.getBoolean("showbackbutton"),savedInstanceState.getString("toolbartitle"));
        }

    }


        public void onMainOptionSelected(int position) {

            if (position == 0) {
                if (subFragment == null) {
                    subFragment = new SubFragment();
                }

                getSupportFragmentManager().beginTransaction()
                        .addToBackStack("subFragment")
                        .replace(R.id.container, subFragment)
                        .commit();


                showToolBarBackButton(true, "Article Categories");

            } else {
                Toast.makeText(this, "Choice not linked up yet...", Toast.LENGTH_SHORT).show();
            }


        }

    /**
     * You can add a time and section chooser element to the main activity if you want
     * Articles can be picked from any section in the paper
     * Time constraint is 1, 7 or 30 days
     * */

    public void getArticles(String requesttype, String apikey) {

        NYTimesClient.getInstance()
                .getArticles(requesttype, section,time,apikey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NYTimesArticleWrapper>() {
                    @Override public void onCompleted() {
                        if(debug){Log.d(TAG, "In onCompleted()");}

                        if(loadedArticles != null) {

                            if (articleListFragment == null) {
                                articleListFragment = new ArticleListFragment();

                                Bundle args = new Bundle();
                                args.putParcelableArrayList("loadedArticles",loadedArticles);
                                articleListFragment.setArguments(args);
                            }

                            getSupportFragmentManager().beginTransaction()
                                    .addToBackStack("articlelistfrag")
                                    .replace(R.id.container, articleListFragment)
                                    .commit();


                            showToolBarBackButton(true,getArticleRequestType(requesttype));
                        }
                    }

                    @Override public void onError(Throwable e) {
                        e.printStackTrace();
                        if(debug){Log.d(TAG, "In onError()");}
                        Toast.makeText(getBaseContext(), "Unable to connect to NYTimes API", Toast.LENGTH_SHORT).show();
                    }

                    @Override public void onNext(NYTimesArticleWrapper nytimesArticles) {
                        if(debug) {
                            Log.d(TAG, "In onNext()");
                            Log.d(TAG, "nytimesArticles: " + nytimesArticles.num_results);
                        }

                        /***TMP**/
                        if(loadedArticles == null) {
                            loadedArticles = nytimesArticles.results;
                        }


                    }
                });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        onBackPressed();
return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount == 0) {
                showToolBarBackButton(false, "NYxBrewPunk");
        } else if(backStackEntryCount == 1) {
            showToolBarBackButton(true, "Article Categories");
        }
    }

    public void showToolBarBackButton(Boolean showBack, CharSequence title) {
        if (toolbar == null) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
        }
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(showBack);
            getSupportActionBar().setTitle(title);
        }

    }

    private String getArticleRequestType(String rawRequestType) {
        String articleRequestType = "";
        switch (rawRequestType) {
            case "mostemailed":
                articleRequestType =  "Most Emailed";
                break;
            case "mostshared":
                articleRequestType =  "Most Shared";
                break;
            case "mostviewed":
                articleRequestType =  "Most Viewed";
                break;
            default:
                break;
        }

        return articleRequestType;

    }


    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if(getSupportActionBar() != null && getSupportActionBar().getTitle() != null) {
            savedInstanceState.putString("toolbartitle", getSupportActionBar().getTitle().toString());
        }
        if((subFragment != null && subFragment.isVisible()) ||(articleListFragment != null && articleListFragment.isVisible())){
            savedInstanceState.putBoolean("showbackbutton", true);
        } else  {
            savedInstanceState.putBoolean("showbackbutton", false);
        }

    }

}

