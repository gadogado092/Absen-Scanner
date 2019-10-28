package id.booble.absenmember.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import id.booble.absenmember.R;
import id.booble.absenmember.model.Absen;
import id.booble.absenmember.model.User;
import id.booble.absenmember.presenter.AbsenPresenter;
import id.booble.absenmember.util.MyPreference;
import id.booble.absenmember.view.AbsenView;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler, AbsenView {

    private ZBarScannerView mScannerView;
    private Boolean isCaptured;
//    private Button buttonreset;
    private FrameLayout frame_layout_camera;
    private TextView textViewName, textViewCompany;
    private LinearLayout progress;
    private AlertDialog alertDialogHasil;
    private Button buttonLogOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        buttonreset = findViewById(R.id.button_reset);
        frame_layout_camera = findViewById(R.id.frame_layout_camera);
//        text_view_qr_code_value = findViewById(R.id.text_view_qr_code_value);
        textViewName = findViewById(R.id.text_view_name);
        textViewCompany = findViewById(R.id.text_view_name2);
        buttonLogOut = findViewById(R.id.buttonLogOut);
        progress = findViewById(R.id.llprogress);
        progress.setVisibility(View.INVISIBLE);

        initScannerView();
//        initDefaultView();
        MyPreference myPreference = new MyPreference(this);
        User user = myPreference.loadUser();

        textViewName.setText(String.format("%s %s", checkTextNull(user.getUserFirstName()), checkTextNullLast(user.getUserLastName())));
        textViewCompany.setText(checkTextNull(user.getUserCompany()));

//        buttonreset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mScannerView.resumeCameraPreview(MainActivity.this);
//                text_view_qr_code_value.setVisibility(View.GONE);
//            }
//        });

        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

    }

    private void logout(){
        new AlertDialog.Builder(MainActivity.this)
//                .setTitle("Konfirmasi")
                .setMessage(textViewName.getText().toString()+" Keluar Dari Aplikasi Absen?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        MyPreference myPreference =  new MyPreference(getApplicationContext());
                        myPreference.logOut();
                        myPreference.checkLogin();
                    }
                }).create().show();
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
//        text_view_qr_code_value.setVisibility(View.GONE);
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


    @Override
    public void handleResult(final Result rawResult) {

//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                text_view_qr_code_value.setVisibility(View.VISIBLE);
//                if (rawResult.getContents().equals("12345")){
//                    text_view_qr_code_value.setText("Suksess");
//                }else {
//                    text_view_qr_code_value.setText("Gagal");
//                }
//                progress.setVisibility(View.GONE);
//                buttonreset.setVisibility(View.VISIBLE);
//            }
//        },2000L);


        //resume
        final CharSequence[] options = {"Pagi", "Malam", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Silahkan Pilih Shift");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (options[i].equals("Pagi")) {
                    prosesAbsen(rawResult.getContents(), "pagi");
                    dialogInterface.cancel();
                }else if (options[i].equals("Malam")){
                    prosesAbsen(rawResult.getContents(), "malam");
                    dialogInterface.cancel();
                }
                else if (options[i].equals("Cancel")) {
                    dialogInterface.cancel();
                    mScannerView.resumeCameraPreview(MainActivity.this);
                }
            }
        });

        builder.show();

    }

    private void prosesAbsen(String rawResult, String shift){

        progress.setVisibility(View.VISIBLE);

        HashMap<String, String> get_data = new HashMap<>();
        MyPreference myPreference = new MyPreference(getApplicationContext());
        User user = myPreference.loadUser();
        get_data.put(User.dbUserId, user.getUserId());
        get_data.put("kd_absen", rawResult);
        get_data.put("shift", shift);

        AbsenPresenter absenPresenter = new AbsenPresenter(this);
        absenPresenter.prosesAbsen(get_data);

    }

    private void showDialogSukses(Absen absen){

        final LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.dialog_hasil, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);

        TextView textViewTitle = promptsView.findViewById(R.id.hasil);
        TextView textViewName = promptsView.findViewById(R.id.name);
        TextView textViewDate = promptsView.findViewById(R.id.date);
        TextView textViewTime = promptsView.findViewById(R.id.time);

        textViewDate.setText(String.format(": %s", absen.getDate()));
        textViewTime.setText(String.format(": %s", absen.getTime()));
        textViewName.setText(String.format(absen.getMessage()));

        textViewTitle.setText("S U K S E S");


        alertDialogBuilder
                .setCancelable(false);

        alertDialogBuilder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mScannerView.resumeCameraPreview(MainActivity.this);
            }
        });


//        alertDialogBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
//
//            }
//        });

        // create alert dialog
        alertDialogHasil = alertDialogBuilder.create();
        //show it
        alertDialogHasil.show();
    }

    @Override
    public void showLoading() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progress.setVisibility(View.GONE);
    }

    @Override
    public void onSuccess(Absen absen) {
        showDialogSukses(absen);
    }

    @Override
    public void onFailed(String error) {
        mScannerView.resumeCameraPreview(MainActivity.this);
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
    }
}
