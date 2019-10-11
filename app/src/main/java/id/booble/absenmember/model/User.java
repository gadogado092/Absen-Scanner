package id.booble.absenmember.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    //filed from db
    public static final String dbStatus = "status";
    public static final String dbUserId = "user_id";
    public static final String dbUserFirstName = "nm_depan";
    public static final String dbUserLastName = "nm_belakang";
    public static final String dbUserCompany = "nm_perusahaan";

    private String userId;
    private String userFirstName;
    private String userLastName;
    private String userCompany;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserCompany() {
        return userCompany;
    }

    public void setUserCompany(String userCompany) {
        this.userCompany = userCompany;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.userFirstName);
        dest.writeString(this.userLastName);
        dest.writeString(this.userCompany);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.userId = in.readString();
        this.userFirstName = in.readString();
        this.userLastName = in.readString();
        this.userCompany = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
