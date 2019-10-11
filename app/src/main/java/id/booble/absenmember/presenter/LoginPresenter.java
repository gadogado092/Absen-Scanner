package id.booble.absenmember.presenter;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import id.booble.absenmember.BuildConfig;
import id.booble.absenmember.model.User;
import id.booble.absenmember.util.HelperUrl;
import id.booble.absenmember.view.LoginView;

public class LoginPresenter {
    private LoginView view;

    public LoginPresenter(LoginView loginView){
        this.view = loginView;
    }

    public void prosesLogin(final HashMap<String,String> postData){
        view.showLoading();
        AndroidNetworking.post(HelperUrl.URL_LOGIN)
                .addBodyParameter(postData)
                .addQueryParameter("passcode", BuildConfig.API_KEY)
                .setTag("CallLogin")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        view.hideLoading();
                        try {
                            if (response.getBoolean(User.dbStatus)){
                                User user = new User();
                                user.setUserId(response.getString(User.dbUserId));
                                user.setUserFirstName(response.getString(User.dbUserFirstName));
                                user.setUserLastName(response.getString(User.dbUserLastName));
                                user.setUserCompany(response.getString(User.dbUserCompany));
                                view.onSuccess(user);
                            }else {
                                view.onFailedLogin();
                            }
                        } catch (JSONException e) {
                            view.hideLoading();
                            e.printStackTrace();
                            view.onFailed("Server Response Error");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        view.hideLoading();
                        Log.e("ERROR", "onError: ", anError);
                        view.onFailed("Server Response Error");
                    }
                });
    }
}
