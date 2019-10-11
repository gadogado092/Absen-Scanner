package id.booble.absenmember.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import id.booble.absenmember.MainActivity;
import id.booble.absenmember.activity.LoginActivity;
import id.booble.absenmember.model.User;

public class MyPreference {

    private static final String MyPREFERENCES = "MyPrefs" ;
    private static final String KEY_IS_LOGIN = "IS_LOGIN";
    private static final String KEY_NAME = "NAME";
    private static final String KEY_USER_NAME = "USER_NAME";
    private static final String KEY_USER_PASSWORD = "USER_PASSWORD";
    private static final String KEY_PRODUK = "PRODUK";
    private static final String KEY_COMPANY = "COMPANY";
    private static final String KEY_ID = "ID";

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public MyPreference(Context context){
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = this.sharedPreferences.edit();
    }

    public void checkLogin() {
        if (!this.is_login()) {
            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        } else {
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

    public boolean is_login() {
        return sharedPreferences.getBoolean(KEY_IS_LOGIN, false);
    }

    public void saveUser(User user){
        editor.putBoolean(KEY_IS_LOGIN, true);
        editor.putString(KEY_NAME, user.getName());
        editor.putString(KEY_USER_NAME, user.getUserName());
        editor.putString(KEY_USER_PASSWORD, user.getPassword());
        editor.putString(KEY_PRODUK, user.getProduk());
        editor.putString(KEY_COMPANY, user.getCompany());
        editor.putString(KEY_ID, user.getId());
        editor.apply();
    }

    public void saveCompany(String company){
        editor.putString(KEY_COMPANY, company);
        editor.apply();
    }


    public User loadUser(){
        User user = new User();
        if (!is_login()){
            Intent i = new Intent(context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }else {
            user.setUserName(sharedPreferences.getString(KEY_USER_NAME, ""));
            user.setName(sharedPreferences.getString(KEY_NAME, ""));
            user.setPassword(sharedPreferences.getString(KEY_USER_PASSWORD,""));
            user.setProduk(sharedPreferences.getString(KEY_PRODUK,""));
            user.setCompany(sharedPreferences.getString(KEY_COMPANY,""));
            user.setId(sharedPreferences.getString(KEY_ID,""));
        }
        return user;
    }

    public void logOut(){
        editor.putBoolean(KEY_IS_LOGIN, false);
        editor.putString(KEY_NAME, "");
        editor.putString(KEY_USER_NAME, "");
        editor.putString(KEY_USER_PASSWORD, "");
        editor.putString(KEY_PRODUK, "");
        editor.putString(KEY_COMPANY, "");
        editor.apply();

        Intent i = new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
