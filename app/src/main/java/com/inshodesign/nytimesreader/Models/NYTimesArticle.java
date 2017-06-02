package com.inshodesign.nytimesreader.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Object representing a New York Times article
 */
public class NYTimesArticle implements Parcelable {

    private final String url;
    private final String count_type;
    private final String column;
    private final String section;
    private final String byline;
    private final String title;
    private final String published_date;
    private final String source;

    public String getTitle()
    {
        return title;
    }

    public String getUrl()
    {
        return url;
    }

    public String getByLine() {
        return byline;
    }

    // Parcelling part
    public NYTimesArticle(Parcel in){
        String[] data = new String[8];

        in.readStringArray(data);
        this.url = data[0];
        this.count_type = data[1];
        this.column = data[2];
        this.section = data[3];
        this.byline = data[4];
        this.title = data[5];
        this.published_date = data[6];
        this.source = data[7];

    }

    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.url,
                this.count_type,
                this.column,
                this.section,
                this.byline,
                this.title,
                this.published_date,
                this.source

        });
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public NYTimesArticle createFromParcel(Parcel in) {
            return new NYTimesArticle(in);
        }

        public NYTimesArticle[] newArray(int size) {
            return new NYTimesArticle[size];
        }
    };
}
