package id.booble.absenmember.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import id.booble.absenmember.R;
import id.booble.absenmember.model.User;
import id.booble.absenmember.util.MyPreference;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler {

    private ZBarScannerView mScannerView;
    private Boolean isCaptured;
    private Button buttonreset;
    private FrameLayout frame_layout_camera;
    private TextView text_view_qr_code_value, textViewName, textViewCompany;
    private LinearLayout progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonreset = findViewById(R.id.button_reset);
        frame_layout_camera = findViewById(R.id.frame_layout_camera);
        text_view_qr_code_value = findViewById(R.id.text_view_qr_code_value);
        textViewName = findViewById(R.id.text_view_name);
        textViewCompany = findViewById(R.id.text_view_name2);
        progress = findViewById(R.id.llprogress);
        progress.setVisibility(View.INVISIBLE);

        initScannerView();
//        initDefaultView();
        MyPreference myPreference = new MyPreference(this);
        User user = myPreference.loadUser();

        textViewName.setText(String.format("%s %s", checkTextNull(user.getUserFirstName()), checkTextNullLast(user.getUserLastName())));
        textViewCompany.setText(checkTextNull(user.getUserCompany()));

        buttonreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mScannerView.resumeCameraPreview(MainActivity.this);
                text_view_qr_code_value.setVisibility(View.GONE);
            }
        });
    }

    private String checkTextNull(String s){
        if (s.trim().equals("null") || s.trim().equals("")){
            s="-";
        }
        return s;
    }

    private String checkTextNullLast(String s){
        if (s.trim().equals("null")){
            s="";
        }
        return s;
    }


    @Override
    protected void onStart() {
        Permission();
        mScannerView.startCamera();
        super.onStart();
    }

    @Override
    protected void onPause() {
        mScannerView.stopCamera();
        super.onPause();
    }

    private void initScannerView(){
        text_view_qr_code_value.setVisibility(View.GONE);
        mScannerView = new ZBarScannerView(this);
        mScannerView.setAutoFocus(true);
        mScannerView.setResultHandler(MainActivity.this);
        frame_layout_camera.addView(mScannerView);
    }

    private void Permission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==100){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                initScannerView();
                mScannerView.startCamera();
            } else {
                Toast.makeText(this, "Membutuhkan Akses Camera", Toast.LENGTH_LONG).show();
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initDefaultView(){
        text_view_qr_code_value.setText("Result");
        buttonreset.setVisibility(View.GONE);
    }


    @Override
    public void handleResult(final Result rawResult) {
        progress.setVisibility(View.VISIBLE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text_view_qr_code_value.setVisibility(View.VISIBLE);
                if (rawResult.getContents().equals("12345")){
                    text_view_qr_code_value.setText("Suksess");
                }else {
                    text_view_qr_code_value.setText("Gagal");
                }
                progress.setVisibility(View.GONE);
                buttonreset.setVisibility(View.VISIBLE);
            }
        },2000L);


        //resume

    }

}
