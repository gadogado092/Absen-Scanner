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
import id.booble.absenmember.model.Absen;
import id.booble.absenmember.util.HelperUrl;
import id.booble.absenmember.view.AbsenView;

public class AbsenPresenter {
    private AbsenView view;

    public AbsenPresenter(AbsenView absenView){
        this.view = absenView;
    }

    public void prosesAbsen(final HashMap<String,String> postData){
        view.showLoading();
        AndroidNetworking.post(HelperUrl.URL_SIMPAN_ABSEN)
                .addBodyParameter(postData)
                .addQueryParameter("passcode", BuildConfig.API_KEY)
                .setTag("CallAbsen")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        view.hideLoading();
                        try {
                            if (response.getBoolean(Absen.dbStatus)){
                                Absen absen = new Absen();
                                absen.setDate(response.getString(Absen.dbDate));
                                absen.setTime(response.getString(Absen.dbTime));
                                view.onSuccess(absen);
                            }else {
                                view.onFailed("Gagal");
                            }
                        } catch (JSONException e) {
                            view.hideLoading();
                            e.printStackTrace();
                            view.onFailed("Server Response Error 1");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        view.hideLoading();
                        Log.e("ERROR", "onError: ", anError);
                        view.onFailed("Server Response Error "+anError.getErrorDetail());
                    }
                });
    }
}
