package shoppingpager.wingstud.shopinpager.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.HashMap;

import shoppingpager.wingstud.shopinpager.R;
import shoppingpager.wingstud.shopinpager.api.JsonDeserializer;
import shoppingpager.wingstud.shopinpager.api.RequestCode;
import shoppingpager.wingstud.shopinpager.api.WebCompleteTask;
import shoppingpager.wingstud.shopinpager.api.WebTask;
import shoppingpager.wingstud.shopinpager.api.WebUrls;
import shoppingpager.wingstud.shopinpager.common.Constrants;
import shoppingpager.wingstud.shopinpager.common.SharedPrefManager;
import shoppingpager.wingstud.shopinpager.model.UserPagesModel;

public class Splash extends AppCompatActivity implements WebCompleteTask {

    private static String TAG = "Splash";

    SharedPrefManager sharedPrefManager;
    private Boolean mLocationPermissionsGranted = false;
    String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_SMS,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPrefManager = new SharedPrefManager(this);

        if (getLocationPermission()) {
            CommonPages();
            initApp();
        }
    }

    private boolean getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.d(TAG, "getLocationPermission: mLocationPermissionsGranted = false");
                ActivityCompat.requestPermissions(this, permissions, RequestCode.LOCATION_PERMISSION_REQUEST_CODE);
                // Permission Denied
                Toast.makeText(Splash.this, "Some Permission is Denied, please allow permission for that the app can work.",
                        Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this, permissions, RequestCode.LOCATION_PERMISSION_REQUEST_CODE);
                Log.d(TAG, "getLocationPermission: mLocationPermissionsGranted = false");
            }
        } else {
            mLocationPermissionsGranted = true;
            Log.d(TAG, "getLocationPermission: mLocationPermissionsGranted = true");
        }
        return mLocationPermissionsGranted;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case RequestCode.LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            finish();
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    initApp();
                    //initialize our map
                }
            }
        }
    }

//    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
//        new AlertDialog.Builder(this)
//                .setMessage(message)
//                .setPositiveButton("OK", okListener)
//                .setNegativeButton("Cancel", okListener)
//                .create()
//                .show();
//    }
//    private void explain(String msg){
//        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//        dialog.setMessage(msg)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                        //  permissionsclass.requestPermission(type,code);
//                        startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:com.exampledemo.parsaniahardik.marshmallowpermission")));
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                        finish();
//                    }
//                });
//        dialog.show();
//    }

    private void initApp() {
        new Handler().postDelayed(() -> {
            Intent intent;
            if (SharedPrefManager.isIntro(Constrants.IsIntro)) {
                if (SharedPrefManager.isLogin(Constrants.IsLogin)) {
                    intent = new Intent(Splash.this, HomeScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    intent = new Intent(Splash.this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

            } else {
                intent = new Intent(Splash.this, IntroActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }, 2500);
    }

    public void CommonPages() {
        HashMap objectNew = new HashMap();
//        objectNew.put("")
        new WebTask(this, WebUrls.BASE_URL + WebUrls.UserPages, objectNew, Splash.this, RequestCode.CODE_UserPages, 5);
    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_UserPages == taskcode) {
            Log.i("user_pages", response);
            UserPagesModel userPagesModel = JsonDeserializer.deserializeJson(response, UserPagesModel.class);
            if (userPagesModel.statusCode == 1) {
                SharedPrefManager.setAboutUsDes(Constrants.AboutUs, userPagesModel.data.aboutUs.description);
                SharedPrefManager.setPrivacyPolicy(Constrants.PrivacyPolicy, userPagesModel.data.privacyPolicy.description);
                SharedPrefManager.setTermCondition(Constrants.TermCondition, userPagesModel.data.termCondition.description);
                SharedPrefManager.setCancelReturn(Constrants.CancelReturn, userPagesModel.data.returnPolicy.description);
                SharedPrefManager.setFaq(Constrants.Faq, userPagesModel.data.toString());
            }
        }
    }
}
