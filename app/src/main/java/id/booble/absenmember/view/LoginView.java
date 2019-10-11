package id.booble.absenmember.view;

import id.booble.absenmember.model.User;

public interface LoginView {

    void showLoading();

    void hideLoading();

    void onSuccess(User user);

    void onFailed(String error);

    void onFailedLogin();
}
