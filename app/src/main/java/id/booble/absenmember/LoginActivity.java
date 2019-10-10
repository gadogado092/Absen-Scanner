package id.booble.absenmember;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class LoginActivity extends AppCompatActivity {

    private LinearLayout progress;
    private EditText userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button login = findViewById(R.id.login);
        progress = findViewById(R.id.llprogress);
        progress.setVisibility(View.INVISIBLE);
        userName = findViewById(R.id.userName);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login.setEnabled(false);
                progress.setVisibility(View.VISIBLE);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent create = new Intent(LoginActivity.this, MainActivity.class);
                        create.putExtra("EXTRA_SESSION_NAME", userName.getText().toString());
                        startActivity(create);
                        finish();
                    }
                },2000L);
            }
        });
    }
}
