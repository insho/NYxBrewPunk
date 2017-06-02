package com.inshodesign.nytimesreader.API_Interfaces;

/**
 * Interface between fragments and mainactivity
 */
public interface FragmentInteractionListener {
    void onMainOptionSelected(int position);
    void getArticles(String requesttype, String apikey);

}