package com.inshodesign.nytimesreader;

/**
 * Created by JClassic on 2/21/2017.
 */

public class BrewPunkBeer {

    public final int id;
    public final String name;
    public final String tagline;
    public final String first_brewed;
    public final String description;;
    public final String image_url;
    public final Double abv;
    public final Double ibu;
    public final String[] food_pairing;
    public final String brewers_tips;

    public BrewPunkBeer(int id, String name, String tagline, String first_brewed, String description,String image_url,Double abv,Double ibu,String[] food_pairing,String brewers_tips) {
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
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public String getImage_url() {return image_url;}

    public String getABV()
    {
        return String.format("%.1f",abv);
    }

    public String getIBU()
    {
        return String.valueOf(ibu);
    }


}

