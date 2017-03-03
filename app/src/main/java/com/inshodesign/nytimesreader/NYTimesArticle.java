package com.inshodesign.nytimesreader;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by JClassic on 2/21/2017.
 */

public class NYTimesArticle implements Parcelable {

    public final String url;
    public final String count_type;
    public final String column;
    public final String section;
    public final String byline;
    public final String title;
    public final String published_date;
    public final String source;

//    public NYTimesArticle(String url, String count_type, String column, String section, String byline,String title,String published_date,String source) {
//        this.url = url;
//        this.count_type = count_type;
//        this.column = column;
//        this.section = section;
//        this.byline = byline;
//        this.title = title;
//        this.published_date = published_date;
//        this.source = source;
//    }

    public String getTitle()
    {
        return title;
    }

    public String getUrl()
    {
        return url;
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
