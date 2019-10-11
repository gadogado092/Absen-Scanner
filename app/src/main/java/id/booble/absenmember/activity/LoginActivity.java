package id.booble.absenmember.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.HashMap;

import id.booble.absenmember.R;
import id.booble.absenmember.model.User;
import id.booble.absenmember.presenter.LoginPresenter;
import id.booble.absenmember.util.MyPreference;
import id.booble.absenmember.view.LoginView;

public class LoginActivity extends AppCompatActivity implements LoginView {

    private LinearLayout progress;
    private EditText editTextUserName, editTextPassword;
    private Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.login);
        progress = findViewById(R.id.llprogress);
        progress.setVisibility(View.INVISIBLE);
        editTextUserName = findViewById(R.id.userName);
        editTextPassword = findViewById(R.id.passwrd);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermissionCamera()){
                    prosesLogin();
                }
            }
        });
    }

    private void prosesLogin(){

        if (checkEditTextEmpty(editTextPassword, "Wajib") | checkEditTextEmpty(editTextUserName, "Wajib")){
            return;
        }

        LoginPresenter loginPresenter = new LoginPresenter(LoginActivity.this);

        HashMap<String, String> get_data = new HashMap<>();

        get_data.put("username", editTextUserName.getText().toString().trim());
        get_data.put("password", editTextPassword.getText().toString().trim() );

        loginPresenter.prosesLogin(get_data);
        hiddenInputMethod();

//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent create = new Intent(LoginActivity.this, MainActivity.class);
//                create.putExtra("EXTRA_SESSION_NAME", editTextUserName.getText().toString());
//                startActivity(create);
//                finish();
//            }
//        },2000L);


    }

    private boolean checkEditTextEmpty(EditText editText, String sMessageError){
        String sEditText= editText.getText().toString();
        if (TextUtils.isEmpty(sEditText)){
            editText.setError(sMessageError);
            requestFocus(editText);
            return true;
        }
        return false;
    }

    private void requestFocus(View view){
        if (view.requestFocus()){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean checkPermissionCamera(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
            }else {
                return true;
            }
        }else {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==100){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                prosesLogin();
            } else {
                Toast.makeText(this, "Membutuhkan Akses Camera", Toast.LENGTH_LONG).show();
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void hiddenInputMethod() {
        InputMethodManager manager = (InputMethodManager) LoginActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.hideSoftInputFromWindow(LoginActivity.this.findViewById(android.R.id.content).getWindowToken(), 0);
        }

    }

    @Override
    public void showLoading() {
        login.setEnabled(false);
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        login.setEnabled(true);
        progress.setVisibility(View.GONE);
    }

    @Override
    public void onSuccess(User user) {

        MyPreference myPreference = new MyPreference(LoginActivity.this);
        myPreference.saveUser(user);

        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        LoginActivity.this.startActivity(i);
        finish();

    }

    @Override
    public void onFailed(String error) {
        Toast.makeText(getApplicationContext(),error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailedLogin() {
        Toast.makeText(getApplicationContext(), "Gagal Login Check User Name atau Password", Toast.LENGTH_SHORT).show();
    }
}
