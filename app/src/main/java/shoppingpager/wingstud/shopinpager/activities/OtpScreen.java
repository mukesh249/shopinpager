package shoppingpager.wingstud.shopinpager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import shoppingpager.wingstud.shopinpager.R;
import shoppingpager.wingstud.shopinpager.common.Constrants;
import shoppingpager.wingstud.shopinpager.common.SharedPrefManager;
import shoppingpager.wingstud.shopinpager.databinding.ActivityOtpScreenBinding;
import shoppingpager.wingstud.shopinpager.presenter.OtpPresenter;
import shoppingpager.wingstud.shopinpager.utils.Utils;
import shoppingpager.wingstud.shopinpager.viewmodel.OtpViewModel;

public class OtpScreen extends AppCompatActivity {

    ActivityOtpScreenBinding binding;
    OtpViewModel otpViewModel;
    String fname, lname, mobile, referral, email, response = "", activity;
    public static String otp;

    private SmsVerifyCatcher smsVerifyCatcher;
    private static CountDownTimer countDownTimer;
    private static OtpScreen mInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp_screen);
        databaseList();

        otpViewModel = new OtpViewModel(this);
        binding.setOtpViewModel(otpViewModel);

        mInstance = this;
        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
//                String code = parseCode(message);//Parse verification code
                smsVerifyCatcher.setPhoneNumberFilter("BWshopin");
                String abcd = message.replaceAll("[^0-9]", "");
                binding.firstPinView.setText(abcd);
            }
        });

        if (getIntent().getExtras() != null) {
            activity = getIntent().getExtras().getString("activity", "");
            if (activity.equals("login")) {
                response = getIntent().getExtras().getString("response", "");
            } else if (activity.equals("signup")) {
                fname = getIntent().getExtras().getString("fname", "");
                lname = getIntent().getExtras().getString("lname", "");
                email = getIntent().getExtras().getString("email", "");
                referral = getIntent().getExtras().getString("referral", "");
                referral = getIntent().getExtras().getString("referral", "");
//                city_name = getIntent().getExtras().getString("city", "");

            }
            mobile = getIntent().getExtras().getString("mobile", "");
            otp = getIntent().getExtras().getString("otp", "");
            Log.i("sdfasf", otp + "  " + mobile);
//            binding.otpString.setText(otp);

        }
        counterMethod();
        binding.setViewPresenter(new OtpPresenter() {
            @Override
            public void OtpResend() {
                Log.d("tv_view: ", binding.resendTv.getText().toString()+"=="+getResources().getString(R.string.resend_otp));
                if (binding.resendTv.getText().toString().equals(getResources().getString(R.string.resend_otp))){
                    otpViewModel.ResendOtp(mobile);
                }else {
                    counterMethod();
                }
            }

            @Override
            public void OptVerifiy() {
                Log.i("otp_code", otp + "=" + binding.firstPinView.getText());
                if (otp.compareTo(binding.firstPinView.getText().toString()) == 0) {
                    if (activity.equals("login")) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optInt("status_code") == 1) {

                                JSONArray dataArray = jsonObject.optJSONArray("data");
                                SharedPrefManager.setLogin(Constrants.IsLogin, true);
                                SharedPrefManager.setUserName(Constrants.UserName, dataArray.getJSONObject(0).optString("username"));
                                SharedPrefManager.setUserID(Constrants.UserId, dataArray.getJSONObject(0).optString("id"));
                                SharedPrefManager.setUserEmail(Constrants.UserEmail, dataArray.getJSONObject(0).optString("email"));
//                                SharedPrefManager.setUserEmail(Constrants.UserEmail,dataArray.getJSONObject(0).optString("email" ));
                                SharedPrefManager.setUserMobile(Constrants.UserMobile, dataArray.getJSONObject(0).optString("mobile"));
                                SharedPrefManager.setUserPic(Constrants.UserPic, dataArray.getJSONObject(0).optJSONObject("user_kyc").optString("profile_image"));
                                SharedPrefManager.setUserFirstName(Constrants.UserFirstName, dataArray.getJSONObject(0).optJSONObject("user_kyc").optString("f_name"));
                                SharedPrefManager.setUserLastName(Constrants.UserLastName, dataArray.getJSONObject(0).optJSONObject("user_kyc").optString("l_name"));

                                startActivity(new Intent(OtpScreen.this, HomeScreen.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else if (activity.equals("signup")) {
                        otpViewModel.OtpVerifiyRequest(mobile, fname, lname, email, referral);
                    }
                } else {
                    Utils.Toast(OtpScreen.this, "OTP not Match");
                }
            }
        });

        binding.backBtn.setOnClickListener(v -> finish());

    }

    public static OtpScreen getInstance(){
        return mInstance;
    }

    public void counterMethod(){
        //If CountDownTimer is null then start timer
        if (countDownTimer == null) {
            String getMinutes = "1";//Get minutes from edittexf

            //Check validation over edittext
            if (!getMinutes.equals("") && getMinutes.length() > 0) {
                int noOfMinutes = Integer.parseInt(getMinutes) * 60 * 1000;//Convert minutes into milliseconds

                startTimer(noOfMinutes);//start countdown
//                binding.resendTv.setText(getString(R.string.stop_timer));//Change Text

            } else
                Toast.makeText(OtpScreen.this, "Please enter no. of Minutes.", Toast.LENGTH_SHORT).show();//Display toast if edittext is empty
        } else {
            //Else stop timer and change text
            stopCountdown();
            binding.resendTv.setText(R.string.resend_otp);
        }
    }
    //Stop Countdown method
    private void stopCountdown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    //Start Countodwn method
    private void startTimer(int noOfMinutes) {
        countDownTimer = new CountDownTimer(noOfMinutes, 1000) {
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                //Convert milliseconds into hour,minute and seconds
                String ms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                binding.resendTv.setText("You can resend OTP in "+ms);//set text
            }

            public void onFinish() {

                binding.resendTv.setText(R.string.resend_otp); //On finish change timer text
                countDownTimer = null;//set CountDownTimer to null
//                startTimer.setText(getString(R.string.start_timer));//Change button text
            }
        }.start();

    }
    @Override
    protected void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
        countDownTimer.cancel();
    }

    /**
     * need for Android 6 real time permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
