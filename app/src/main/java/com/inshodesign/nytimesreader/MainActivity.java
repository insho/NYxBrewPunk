package com.inshodesign.nytimesreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.inshodesign.nytimesreader.API_Clients.NYTimesClient;
import com.inshodesign.nytimesreader.API_Interfaces.FragmentInteractionListener;
import com.inshodesign.nytimesreader.Fragments.ArticleListFragment;
import com.inshodesign.nytimesreader.Fragments.MainFragment;
import com.inshodesign.nytimesreader.Fragments.SubFragment;
import com.inshodesign.nytimesreader.Models.NYTimesArticle;
import com.inshodesign.nytimesreader.Models.NYTimesArticleWrapper;

import java.util.ArrayList;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Main Activity and traffic control between fragments (default fragment is {@link MainFragment})
 */
public class MainActivity extends AppCompatActivity implements FragmentInteractionListener {

    private static final String TAG = "TEST";
    private static final boolean debug = false;
    private Toolbar toolbar;
    private Subscription nyTimesSubscription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ExternalDB db = new ExternalDB(this);
        db.initializeDBInternalCopy();

        if (savedInstanceState == null) {
            showToolBarBackButton(false, "NYxBrewPunk");
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment())
                    .commit();
        } else {
            showToolBarBackButton(savedInstanceState.getBoolean("showbackbutton"),savedInstanceState.getString("toolbartitle"));
        }

    }

    /**
     * Replaces {@link MainFragment} with {@link SubFragment} when user
     * picks a main article category. Recieves callback
     * @param position position in main category list, passed to subfragment
     */
    public void onMainOptionSelected(int position) {
                getSupportFragmentManager().beginTransaction()
                        .addToBackStack("subFragment")
                        .replace(R.id.container, new SubFragment())
                        .commit();
                getSupportFragmentManager().executePendingTransactions();
                showToolBarBackButton(true, "Article Categories");

        }

    /**
     * Makes API call to NYTimes and returns list of articles in an {@link NYTimesArticleWrapper} object for the
     * given request criteria (most of which are currently hard-coded)
     * @param requesttype Main criteria (chosen in {@link MainFragment})
     * @param apikey sub-criteria (from {@link SubFragment}
     */
    public void getArticles(String requesttype, String apikey) {


    /*
     * Note: If this were real these would be spinners that the user could select.
     * It is possible to add a time and section chooser element as well. Articles can
     * be picked from any section in the paper. Time constraint is 1, 7 or 30 days
     */
        final String section = "all-sections";
        final String time = "1";

        nyTimesSubscription = NYTimesClient.getInstance()
                .getArticles(requesttype, section,time,apikey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NYTimesArticleWrapper>() {

                    private ArrayList<NYTimesArticle> loadedArticles;

                    @Override public void onCompleted() {
                        if(debug){Log.d(TAG, "In onCompleted()");}

                        if(loadedArticles != null) {

                                ArticleListFragment articleListFragment = new ArticleListFragment();
                                Bundle args = new Bundle();
                                args.putParcelableArrayList("loadedArticles",loadedArticles);
                                articleListFragment.setArguments(args);

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
                            Log.d(TAG, "nytimesArticles: " + nytimesArticles.getResultCount());
                        }

                        /***TMP**/
                        if(loadedArticles == null) {
                            loadedArticles = nytimesArticles.getArticles();
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

    /**
     * Shows or hides the actionbar back arrow
     * @param showBack bool true to show the button
     * @param title title to show next to button
     */
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

    /**
     * Returns the correct app title for a given requesttype (chosen in MainFragment, displayed in action bar)
     * while {@link SubFragment} is showing
     * @param rawRequestType request string representing type of article category being displayed in subfragment
     * @return clean title to display in action bar
     *
     * @see #showToolBarBackButton(Boolean, CharSequence)
     */
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

        if(getSupportFragmentManager().getBackStackEntryCount()>0){
            savedInstanceState.putBoolean("showbackbutton", true);
        } else  {
            savedInstanceState.putBoolean("showbackbutton", false);
        }
    }

    @Override
    protected void onPause() {
        if(nyTimesSubscription!=null) {
            nyTimesSubscription.unsubscribe();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if(nyTimesSubscription!=null) {
            nyTimesSubscription.unsubscribe();
        }
        super.onDestroy();
    }
}

