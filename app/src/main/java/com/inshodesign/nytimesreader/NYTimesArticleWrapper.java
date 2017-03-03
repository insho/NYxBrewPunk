package com.inshodesign.nytimesreader;
import java.util.ArrayList;

/**
 * Created by JClassic on 2/21/2017.
 */

public class NYTimesArticleWrapper  {


    public final String status;
    public final String copyright;
    public final int num_results;
    public ArrayList<NYTimesArticle> results;

    public final String[] errors;

    public NYTimesArticleWrapper(String status, String copyright, int num_results, ArrayList<NYTimesArticle> results, String[] errors) {
        this.status = status;
        this.copyright = copyright;
        this.num_results = num_results;
        this.results = results;
        this.errors = errors;
    }
}
