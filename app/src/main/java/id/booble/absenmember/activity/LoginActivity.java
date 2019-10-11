package id.booble.absenmember.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import id.booble.absenmember.MainActivity;
import id.booble.absenmember.R;

public class LoginActivity extends AppCompatActivity {

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

        login.setEnabled(false);
        progress.setVisibility(View.VISIBLE);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent create = new Intent(LoginActivity.this, MainActivity.class);
                create.putExtra("EXTRA_SESSION_NAME", editTextUserName.getText().toString());
                startActivity(create);
                finish();
            }
        },2000L);
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
}
