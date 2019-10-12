package id.booble.absenmember.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Absen implements Parcelable {
    public static final String dbStatus = "status";
    public static final String dbDate = "tgl";
    public static final String dbTime = "jam";

    private String date;
    private String time;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.date);
        dest.writeString(this.time);
    }

    public Absen() {
    }

    protected Absen(Parcel in) {
        this.date = in.readString();
        this.time = in.readString();
    }

    public static final Parcelable.Creator<Absen> CREATOR = new Parcelable.Creator<Absen>() {
        @Override
        public Absen createFromParcel(Parcel source) {
            return new Absen(source);
        }

        @Override
        public Absen[] newArray(int size) {
            return new Absen[size];
        }
    };
}
