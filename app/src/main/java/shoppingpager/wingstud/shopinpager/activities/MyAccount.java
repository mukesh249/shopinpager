package shoppingpager.wingstud.shopinpager.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import shoppingpager.wingstud.shopinpager.R;
import shoppingpager.wingstud.shopinpager.api.ApiConfig;
import shoppingpager.wingstud.shopinpager.api.AppConfig;
import shoppingpager.wingstud.shopinpager.api.RequestCode;
import shoppingpager.wingstud.shopinpager.api.WebCompleteTask;
import shoppingpager.wingstud.shopinpager.api.WebTask;
import shoppingpager.wingstud.shopinpager.api.WebUrls;
import shoppingpager.wingstud.shopinpager.common.Constrants;
import shoppingpager.wingstud.shopinpager.common.SharedPrefManager;
import shoppingpager.wingstud.shopinpager.databinding.ActivityMyAccountBinding;
import shoppingpager.wingstud.shopinpager.databinding.MobileChangeLayoutBinding;
import shoppingpager.wingstud.shopinpager.utils.Utils;

public class MyAccount extends AppCompatActivity implements WebCompleteTask {
    private ActivityMyAccountBinding myAccountBinding;
    private android.app.AlertDialog dialog;
    private int REQUEST_CAMERA_PERMISSION = 1;
    Uri imageUri;
    MobileChangeLayoutBinding bindingr;
    private String mobile = "", otp = "";
    private AlertDialog retryCustomAlert;
    private SmsVerifyCatcher smsVerifyCatcher;
    private static CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myAccountBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_account);

        myAccountBinding.headlyaout.cartRL.setVisibility(View.GONE);
        myAccountBinding.headlyaout.searchIcon.setVisibility(View.GONE);

        myAccountBinding.headlyaout.productCatName.setText(getResources().getString(R.string.my_account));

        myAccountBinding.headlyaout.backBtn.setOnClickListener(view -> finish());

        myAccountBinding.editIcon.setOnClickListener(view -> startActivity(new Intent(MyAccount.this, EditProfile.class)));

        myAccountBinding.changeTV.setOnClickListener(view -> startActivity(new Intent(MyAccount.this, AddNewAddress.class)));

        myAccountBinding.fbIcon.setOnClickListener(v -> {
                    if (isAppInstalled()) {
                        Toast.makeText(getApplicationContext(), "facebook app already installed", Toast.LENGTH_SHORT).show();
                        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                        String facebookUrl = getFacebookPageURL(this);
                        facebookIntent.setData(Uri.parse(facebookUrl));
                        startActivity(facebookIntent);

                    } else {
                        Toast.makeText(getApplicationContext(), "facebook app not installing", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        myAccountBinding.instaIcon.setOnClickListener(v -> {
                    Uri uri = Uri.parse("http://instagram.com/_u/shopinpager");
                    Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                    likeIng.setPackage("com.instagram.android");

                    try {
                        startActivity(likeIng);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://instagram.com/shopinpager")));
                    }
                }
        );
        myAccountBinding.twitterIcon.setOnClickListener(v -> {
                    Intent intent = null;
                    try {
                        // get the Twitter app if possible
                        this.getPackageManager().getPackageInfo("com.twitter.android", 0);
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=shopinpager"));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    } catch (Exception e) {
                        // no Twitter app, revert to browser
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/shopinpager"));
                    }
                    this.startActivity(intent);
                }
        );
        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
//                String code = parseCode(message);//Parse verification code
                smsVerifyCatcher.setPhoneNumberFilter("BWshopin");
                String abcd = message.replaceAll("[^0-9]", "");
                bindingr.pinView.setText(abcd);
            }
        });
//        if (SharedPrefManager.getUserMobile(Constrants.UserMobile).isEmpty()){
//            myAccountBinding.edit.setVisibility(View.GONE);
//        }else {
//            myAccountBinding.edit.setVisibility(View.VISIBLE);
//        }
        myAccountBinding.edit.setOnClickListener(v -> {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setCancelable(false);
            LayoutInflater inflater = getLayoutInflater();
            bindingr = DataBindingUtil.inflate(inflater, R.layout.mobile_change_layout, null, false);

            dialogBuilder.setView(bindingr.getRoot());
            retryCustomAlert = dialogBuilder.create();

            bindingr.close.setOnClickListener(v1 -> retryCustomAlert.dismiss());

            bindingr.submitBtn.setOnClickListener(v12 -> {
                if (bindingr.mobileEt.getText().toString().isEmpty()) {
                    bindingr.mobileEt.setError("Please enter your mobile");
                    bindingr.mobileEt.requestFocus();
                } else {
                    if (bindingr.submitBtn.getText().toString().equals(getResources().getString(R.string.verify_otp))) {
                        Log.i("opmatche", otp + "==" + bindingr.pinView.getText().toString());
                        if (!otp.isEmpty() && !bindingr.pinView.getText().toString().isEmpty() && otp.equals(Objects.requireNonNull(bindingr.pinView.getText()).toString()))
                            verifiyOtp();
                        else {
                            Utils.Toast(MyAccount.this, "Otp not match please enter valid Otp");
                        }
                    } else {
                        mobileChange(bindingr.mobileEt.getText().toString());
                    }
                }
            });

            bindingr.resendTv.setOnClickListener(view -> {
                if (!mobile.isEmpty())
                    mobileChange(mobile);
            });


            retryCustomAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            retryCustomAlert.show();
        });


        myAccountBinding.userEmailTv.setText(SharedPrefManager.getUserEmail(Constrants.UserEmail));
        myAccountBinding.mobileTv.setText(SharedPrefManager.getUserMobile(Constrants.UserMobile));
//        myAccountBinding.addressTv.setText(SharedPrefManager.getAddress(Constrants.));

        if (SharedPrefManager.getUserName(Constrants.UserName) != null) {
            String[] name = SharedPrefManager.getUserName(Constrants.UserName).split(" ");
            if (Utils.checkEmptyNull(name[0])) {
                myAccountBinding.userNameTv.setText(Utils.FirstLatterCap(name[0]));
            }
        }

        if (SharedPrefManager.isFBLogin(Constrants.IsFBLogin)||SharedPrefManager.isGoogleLogin(Constrants.IsGoogleLogin)){
            Glide.with(this).load(SharedPrefManager.getUserPic(Constrants.UserPic))
                    .placeholder(R.drawable.user_a)
                    .into(myAccountBinding.userPic);
            myAccountBinding.imageEdit.setVisibility(View.GONE);
        }else {
            myAccountBinding.imageEdit.setVisibility(View.VISIBLE);
            String url = WebUrls.BASE_URL + WebUrls.UserProfileImageURL + SharedPrefManager.getUserPic(Constrants.UserPic);
            Log.i("sadfasfsdafsa", url);
            Glide.with(this).load(url).placeholder(R.drawable.user_a).into(myAccountBinding.userPic);
        }


        myAccountBinding.myOrderLL.setOnClickListener(v -> startActivity(new Intent(MyAccount.this, MyOrderList.class)));

        myAccountBinding.imageEdit.setOnClickListener(v -> ImagePopup());
    }

    public void mobileChange(String mobile_a) {
        HashMap objectNew = new HashMap();
        objectNew.put("mobile", mobile_a);
        Log.i("mobile_obj", objectNew + "");
        new WebTask(MyAccount.this, WebUrls.BASE_URL + WebUrls.UserUpdateMobile, objectNew,
                MyAccount.this, RequestCode.CODE_UserUpdateMobile, 1);
    }

    public void verifiyOtp() {
        HashMap objectNew = new HashMap();
        objectNew.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
        objectNew.put("mobile", mobile);
        Log.i("mobile_obj", objectNew + "");
        new WebTask(MyAccount.this, WebUrls.BASE_URL + WebUrls.VerifyOtpForMobileUpdate,
                objectNew, MyAccount.this, RequestCode.CODE_VerifyOtpForMobileUpdate, 1);
    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_UserUpdateMobile == taskcode) {
            Log.i("mobile_res", response);

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optInt("status_code") == 1) {
                    JSONObject dataObj = jsonObject.optJSONObject("data");

                    otp = dataObj.optString("otp");
                    mobile = dataObj.optString("mobile");

                    bindingr.mobileEt.setVisibility(View.GONE);
                    bindingr.pinView.setVisibility(View.VISIBLE);
                    bindingr.resendTv.setVisibility(View.VISIBLE);

                    bindingr.titleTv.setText("Verifiy Otp");
                    bindingr.submitBtn.setText(getResources().getString(R.string.verify_otp));
                    counterMethod();
                    Utils.Toast(MyAccount.this, jsonObject.optString("message"));
                } else {
                    Utils.Toast(MyAccount.this, jsonObject.optString("message"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (RequestCode.CODE_VerifyOtpForMobileUpdate == taskcode) {
            Log.i("Verifiy_res", response);

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optInt("status_code") == 1) {
                    SharedPrefManager.setUserMobile(Constrants.UserMobile, mobile);
                    retryCustomAlert.dismiss();
                    Utils.Toast(MyAccount.this, jsonObject.optString("message"));
                } else {
                    Utils.Toast(MyAccount.this, jsonObject.optString("message"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public static String FACEBOOK_URL = "https://www.facebook.com/shopinpager";
    public static String FACEBOOK_PAGE_ID = "shopinpager";

    //method to get the right URL to use in the intent
    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.orca", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }

    public boolean isAppInstalled() {
        try {
            getApplicationContext().getPackageManager().getApplicationInfo("com.facebook.katana", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
        myAccountBinding.userEmailTv.setText(SharedPrefManager.getUserEmail(Constrants.UserEmail));
        myAccountBinding.mobileTv.setText(SharedPrefManager.getUserMobile(Constrants.UserMobile));
//        myAccountBinding.addressTv.setText(SharedPrefManager.getAddress(Constrants.));

        if (SharedPrefManager.getUserName(Constrants.UserName) != null) {
//        String[] name = SharedPrefManager.getUserName(Constrants.UserName).split("");
//        if (Utils.checkEmptyNull(name[0])) {
            myAccountBinding.userNameTv.setText(Utils.FirstLatterCap(SharedPrefManager.getUserName(Constrants.UserName)));
//        }
        }
    }

    private void ImagePopup() {
        try {
            final Dialog dialog = new Dialog(MyAccount.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.pop_profile);
            dialog.show();

            LinearLayout txtGallery = (LinearLayout) dialog.findViewById(R.id.layoutGallery);
            LinearLayout txtCamera = (LinearLayout) dialog.findViewById(R.id.layoutCamera);
            txtCamera.setOnClickListener(v -> {
                int currentAPIVersion = Build.VERSION.SDK_INT;
                if (currentAPIVersion >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(MyAccount.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MyAccount.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                    } else {
                        selectCameraImage();
                        dialog.dismiss();
                    }
                } else {
                    selectCameraImage();
                    dialog.dismiss();
                }
            });
            txtGallery.setOnClickListener(v -> {
                int currentAPIVersion = Build.VERSION.SDK_INT;
                if (currentAPIVersion >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(MyAccount.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MyAccount.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                    } else {
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_PICK);
                        startActivityForResult(Intent.createChooser(intent, "Select Image"), 100);
                    }
                } else {
                    dialog.dismiss();
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_PICK);
                    startActivityForResult(Intent.createChooser(intent, "Select Image"), 100);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void selectCameraImage() {
        // TODO Auto-generated method stub
        String fileName = "new-photo-name.jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image captured by camera");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, 300);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            final InputStream imageStream;
            try {
                imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                myAccountBinding.userPic.setImageBitmap(selectedImage);
                productUploadMethod();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } else if (requestCode == 300) {
            Uri selectedImageUri = null;
            selectedImageUri = imageUri;
            final InputStream imageStream;
            try {
                imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                myAccountBinding.userPic.setImageBitmap(selectedImage);
                productUploadMethod();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getPath(Context context, Uri uri) {
        String[] data = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, uri, data, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void productUploadMethod() {
        if (imageUri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading, please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            MultipartBody.Part filePartmultipleImages;
            Log.d("upload_image_array", imageUri.toString());

            File file = new File(getPath(MyAccount.this, imageUri));
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            filePartmultipleImages = MultipartBody.Part.createFormData("profile_image", file.getName(), requestBody);

            RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), SharedPrefManager.getUserID(Constrants.UserId));

            ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
            Call<JsonObject> call = getResponse.uploadProfile(
                    user_id,
                    filePartmultipleImages
            );
            Log.d("profile_OBj", call.toString());
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject obj = new JSONObject(response.body().toString());
                            if (obj.optInt("status_code") == 1) {

                                SharedPrefManager.setUserPic(Constrants.UserPic, obj.optString("image_name"));
                                Log.d("sdafsafsafsa", obj.optString("image_name"));
                                Toast.makeText(MyAccount.this, obj.optString("message"), Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(MyAccount.this, obj.optString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {

                        try {
                            JSONObject responseJ = new JSONObject(response.errorBody().string());
                            System.out.println("error response " + responseJ.toString());
                            progressDialog.dismiss();

                        } catch (Exception e) {

                        }

                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(MyAccount.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                    System.out.println("fail response.." + t.toString());
//                        dismissProgressBar();
                }
            });

        } else {
            Utils.Toast(this, "Please Select Image");
        }
    }

    public void counterMethod() {
        //If CountDownTimer is null then start timer
        if (countDownTimer == null) {
            String getMinutes = "1";//Get minutes from edittexf

            //Check validation over edittext
            if (!getMinutes.equals("") && getMinutes.length() > 0) {
                int noOfMinutes = Integer.parseInt(getMinutes) * 60 * 1000;//Convert minutes into milliseconds

                startTimer(noOfMinutes);//start countdown
//                binding.resendTv.setText(getString(R.string.stop_timer));//Change Text

            } else
                Toast.makeText(MyAccount.this, "Please enter no. of Minutes.", Toast.LENGTH_SHORT).show();//Display toast if edittext is empty
        } else {
            //Else stop timer and change text
            stopCountdown();
            bindingr.resendTv.setText(R.string.resend_otp);
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
                bindingr.resendTv.setText("You can resend OTP in " + ms);//set text
            }

            public void onFinish() {

                bindingr.resendTv.setText(R.string.resend_otp); //On finish change timer text
                countDownTimer = null;//set CountDownTimer to null
//                startTimer.setText(getString(R.string.start_timer));//Change button text
            }
        }.start();

    }

    @Override
    protected void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
        if (countDownTimer != null)
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
