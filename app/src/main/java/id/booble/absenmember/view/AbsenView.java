package id.booble.absenmember.view;

import id.booble.absenmember.model.Absen;

public interface AbsenView {

    void showLoading();

    void hideLoading();

    void onSuccess(Absen absen);

    void onFailed(String error);

}
