package id.booble.absenmember.presenter;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import id.booble.absenmember.BuildConfig;
import id.booble.absenmember.model.Absen;
import id.booble.absenmember.model.Shift;
import id.booble.absenmember.util.HelperUrl;
import id.booble.absenmember.view.AbsenView;
import id.booble.absenmember.view.ShiftView;

public class ShiftPresenter {
    private ShiftView view;

    public ShiftPresenter(ShiftView view){
        this.view = view;
    }

    public void getShift(){
//        Shift shift = new Shift();
//        view.onSuccessShift(shift);
        view.showLoading();
        AndroidNetworking.post(HelperUrl.URL_GET_SHIFT)
//                .addBodyParameter(postData)
                .addQueryParameter("passcode", BuildConfig.API_KEY)
                .setTag("CallShift")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        view.hideLoading();
                        try {
                            ArrayList<Shift> list = new ArrayList<>();
                            for (int i=0; i<response.length(); i++){
                                JSONObject jsonObject = response.getJSONObject(i);
                                Shift shift = new Shift();
                                shift.setId(jsonObject.getString(Shift.dbId));
                                shift.setName(jsonObject.getString(Shift.dbNama));
                                list.add(shift);
                            }

                            view.onSuccessShift(list);

                        } catch (JSONException e) {
                            view.hideLoading();
                            e.printStackTrace();
                            view.onFailedShift("Server Response Error 1");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        view.hideLoading();
                        Log.e("ERROR", "onError: ", anError);
                        view.onFailedShift("Server Response Error "+anError.getErrorDetail());
                    }
                });
    }
}
