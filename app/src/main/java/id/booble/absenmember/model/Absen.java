package id.booble.absenmember.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Absen implements Parcelable {
    public static final String dbStatus = "status";
    public static final String dbDate = "tgl";
    public static final String dbTime = "jam";
    public static final String dbMessage = "message";

    private String date;
    private String time;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

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

    public Absen() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.date);
        dest.writeString(this.time);
        dest.writeString(this.message);
    }

    protected Absen(Parcel in) {
        this.date = in.readString();
        this.time = in.readString();
        this.message = in.readString();
    }

    public static final Creator<Absen> CREATOR = new Creator<Absen>() {
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
