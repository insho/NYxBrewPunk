package com.inshodesign.nytimesreader.Models;
import com.inshodesign.nytimesreader.API_Clients.NYTimesClient;
import com.inshodesign.nytimesreader.API_Interfaces.NYTimesService;

import java.util.ArrayList;

/**
 * A collection of New York Times articles from the API lookup. Contains
 * an array of {@link NYTimesArticle} objects, one for each article
 *
 * @see com.inshodesign.nytimesreader.MainActivity#getArticles(String, String)
 * @see NYTimesClient
 * @see NYTimesService
 */
public class NYTimesArticleWrapper  {

    private final String status;
    private final String copyright;
    private final int num_results;
    private ArrayList<NYTimesArticle> results;
    private final String[] errors;

    public NYTimesArticleWrapper(String status, String copyright, int num_results, ArrayList<NYTimesArticle> results, String[] errors) {
        this.status = status;
        this.copyright = copyright;
        this.num_results = num_results;
        this.results = results;
        this.errors = errors;
    }

    public ArrayList<NYTimesArticle> getArticles() {
        return results;
    }

    public int getResultCount() {
        return num_results;
    }
}
