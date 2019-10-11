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
                                JSONObject data = response.getJSONObject("data");
                                User user = new User();
                                user.setName(data.getString(User.dbName));
                                user.setUserName(data.getString(User.dbUserName));
                                user.setProduk(data.getString(User.dbProduk));
                                user.setPassword(postData.get("password"));
                                user.setCompany(data.getString(User.dbCompany));
                                user.setId(data.getString(User.dbId));
                                view.onSuccess(user);
                            }else {
                                view.onFailed(response.getString("message"));
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
