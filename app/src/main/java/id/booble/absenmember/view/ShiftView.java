package id.booble.absenmember.view;


import java.util.ArrayList;

import id.booble.absenmember.model.Shift;

public interface ShiftView {

    void showLoading();

    void hideLoading();

    void onSuccessShift(ArrayList<Shift> listData);

    void onFailedShift(String error);

}
