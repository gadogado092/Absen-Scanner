package id.booble.absenmember.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    //filed from db
    public static final String dbStatus = "status";
    public static final String dbMessage = "message";
    public static final String dbUserName = "username";
    public static final String dbName = "nama";
    public static final String dbProduk = "produk";
    public static final String dbCompany = "company";
    public static final String dbId = "id";

    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    public String getProduk() {
        return produk;
    }

    public void setProduk(String produk) {
        this.produk = produk;
    }

    private String userName;
    private String produk;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    private String company;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public User() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.id);
        dest.writeString(this.userName);
        dest.writeString(this.produk);
        dest.writeString(this.company);
        dest.writeString(this.password);
    }

    protected User(Parcel in) {
        this.name = in.readString();
        this.id = in.readString();
        this.userName = in.readString();
        this.produk = in.readString();
        this.company = in.readString();
        this.password = in.readString();
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
