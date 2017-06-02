package com.inshodesign.nytimesreader.Models;

import java.util.Locale;

/**
 * Object representing a beer from the BrewDog database
 */
public class BrewDogBeer {

    private final int id;
    private final String name;
    private final String tagline;
    private final String first_brewed;
    private final String description;;
    private final String image_url;
    private final Double abv;
    private final Double ibu;
    private final String[] food_pairing;
    private final String brewers_tips;

    public BrewDogBeer(int id, String name, String tagline, String first_brewed, String description, String image_url, Double abv, Double ibu, String[] food_pairing, String brewers_tips) {
        this.id=id;
        this.name=name;
        this.tagline=tagline;
        this.first_brewed=first_brewed;
        this.description=description;
        this.image_url=image_url;
        this.abv=abv;
        this.ibu=ibu;
        this.food_pairing = food_pairing;
        this.brewers_tips = brewers_tips;
    }

    public String getName()
    {
        try {
            return name;
        } catch ( NullPointerException e) {
            return "?";
        }
    }

    public String getDescription()
    {
        return description;
    }

    public String getImage_url() {return image_url;}

    public String getABV()
    {
        try {
            return String.format(Locale.US,"%.1f",abv);
        } catch ( NullPointerException e) {
            return "?";
        }
    }

    public String getIBU()
    {
        try {
            return String.valueOf(ibu);
        } catch ( NullPointerException e) {
            return "?";
        }

    }


}

