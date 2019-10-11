package id.booble.absenmember.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import id.booble.absenmember.activity.MainActivity;
import id.booble.absenmember.activity.LoginActivity;
import id.booble.absenmember.model.User;

public class MyPreference {

    private static final String MyPREFERENCES = "MyPrefs" ;
    private static final String KEY_IS_LOGIN = "IS_LOGIN";
    private static final String KEY_USER_ID = "USER_ID";
    private static final String KEY_USER_FIRST_NAME = "USER_FIRST_NAME";
    private static final String KEY_USER_LAST_NAME = "USER_LAST_NAME";
    private static final String KEY_USER_COMPANY = "USER_COMPANY";

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
        editor.putString(KEY_USER_ID, user.getUserId());
        editor.putString(KEY_USER_FIRST_NAME, user.getUserFirstName());
        editor.putString(KEY_USER_LAST_NAME, user.getUserLastName());
        editor.putString(KEY_USER_COMPANY, user.getUserCompany());
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
            user.setUserId(sharedPreferences.getString(KEY_USER_ID, ""));
            user.setUserFirstName(sharedPreferences.getString(KEY_USER_FIRST_NAME, ""));
            user.setUserLastName(sharedPreferences.getString(KEY_USER_LAST_NAME, ""));
            user.setUserCompany(sharedPreferences.getString(KEY_USER_COMPANY, ""));
        }
        return user;
    }

    public void logOut(){
        editor.putBoolean(KEY_IS_LOGIN, false);
        editor.putString(KEY_USER_ID, "");
        editor.putString(KEY_USER_FIRST_NAME, "");
        editor.putString(KEY_USER_LAST_NAME, "");
        editor.putString(KEY_USER_COMPANY, "");
        editor.apply();

        Intent i = new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
