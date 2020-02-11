package shoppingpager.wingstud.shopinpager.activities;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

import shoppingpager.wingstud.shopinpager.R;
import shoppingpager.wingstud.shopinpager.api.JsonDeserializer;
import shoppingpager.wingstud.shopinpager.api.RequestCode;
import shoppingpager.wingstud.shopinpager.api.WebCompleteTask;
import shoppingpager.wingstud.shopinpager.api.WebTask;
import shoppingpager.wingstud.shopinpager.api.WebUrls;
import shoppingpager.wingstud.shopinpager.common.Constrants;
import shoppingpager.wingstud.shopinpager.common.SharedPrefManager;
import shoppingpager.wingstud.shopinpager.databinding.ActivityLoginBinding;
import shoppingpager.wingstud.shopinpager.model.SocailLoginResponse;
import shoppingpager.wingstud.shopinpager.viewmodel.LoginViewModel;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener , WebCompleteTask {


    private GoogleApiClient googleApiClient;
    CallbackManager callbackManager;
    ActivityLoginBinding binding;
    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;
    GoogleSignInResult result;
    LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginViewModel = new LoginViewModel(this);
        binding.setLoginViewModel(loginViewModel);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.i("newToken", newToken);
                SharedPrefManager.setDeviceToken(Constrants.Token, newToken);
            }
        });
        binding.setLoginPresenter(() -> {
            if (binding.phoneNoEt.getText().toString().isEmpty()) {
                binding.phoneNoEt.setError("Please Enter Mobile Number");
                binding.phoneNoEt.requestFocus();
            } else if (binding.passwordEt.getText().toString().isEmpty()) {
                binding.passwordEt.setError("Please Enter Password");
                binding.passwordEt.requestFocus();
            } else {
                loginViewModel.LoginRequest(SharedPrefManager.getDeviceToken(Constrants.Token));
            }
        });

//        try {
//            PackageInfo info = getPackageManager().getPackageInfo("shoppingpager.wingstud.shopinpager", PackageManager.GET_SIGNATURES);
//
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.e("MY_KEY_HASH:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//
//        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }

        ((GradientDrawable) binding.LoginBackLL.getBackground()).setColor(getResources().getColor(R.color.white));

        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

            }
        };


        binding.fbIcon.setOnClickListener(v -> {
//                LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("public_profile"));
            FacebookSignIn();

        });

        binding.backBtn.setOnClickListener(v -> finish());
        binding.bottomLL.setOnClickListener(v -> startActivity(new Intent(Login.this, SignUp.class)));
        binding.loginusingoptTv.setOnClickListener(v -> startActivity(new Intent(Login.this, OtpScreen.class)));

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();

        binding.googleIcon.setOnClickListener(v -> GoogleSignIn());

    }

    @Override
    protected void onStart() {
        super.onStart();
        Profile profile = Profile.getCurrentProfile();
        updateUI(profile, GoogleSignIn.getLastSignedInAccount(this));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }


    private void FacebookSignIn() {
        LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("user_photos", "email", "public_profile", "user_posts"));
//        LoginManager.getInstance().logInWithPublishPermissions(Login.this, Arrays.asList("publish_actions"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Profile profile = Profile.getCurrentProfile();
                        updateUI(profile, null);
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
    }

    private void GoogleSignIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, 10);
    }

    public void GoogleSignOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
//                updateUI(false);
            }
        });
    }

    private void handleResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            updateUI(null, account);
        } else {
//            updateUI(false);
        }
    }

    private void updateUI(Profile profile, GoogleSignInAccount account) {

        if (account != null) {

            Log.i("google_res", account.getId()+"\n"+account.getDisplayName()+"\n"+account.getEmail()+
                    "\n"+ account.getPhotoUrl().toString()
            );
            socailLogin( account.getId(), account.getPhotoUrl().toString(), account.getDisplayName(), "google", account.getEmail());

//            SharedPrefManager.setUserName(Constrants.UserName, account.getDisplayName());
//            SharedPrefManager.setUserEmail(Constrants.UserEmail, account.getEmail());
//            SharedPrefManager.setUserPic(Constrants.UserPic, account.getPhotoUrl().toString());
//            SharedPrefManager.setUserMobile(Constrants.UserMobile,"");
//            SharedPrefManager.setGoogleLogin(Constrants.IsGoogleLogin, true);
//            SharedPrefManager.setFbLogin(Constrants.IsFBLogin, false);
//            startActivity(new Intent(Login.this, HomeScreen.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
//            finish();

        } else if (profile != null) {

            Log.i("facebook_res",profile.getName()+"\n"+profile.getId()+
                    "\n"+profile.getProfilePictureUri(500,500).toString()
            );
            socailLogin(profile.getId(),  profile.getProfilePictureUri(500, 500).toString(),
                    profile.getName(), "facebook","");

//            SharedPrefManager.setUserName(Constrants.UserName, profile.getName());
//            SharedPrefManager.setUserEmail(Constrants.UserEmail, profile.getId());
//            SharedPrefManager.setUserMobile(Constrants.UserMobile,"");
//            SharedPrefManager.setUserPic(Constrants.UserPic, profile.getProfilePictureUri(500, 500).toString());
//            SharedPrefManager.setGoogleLogin(Constrants.IsGoogleLogin, false);
//            SharedPrefManager.setFbLogin(Constrants.IsFBLogin, true);
//            startActivity(new Intent(Login.this, HomeScreen.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
//            finish();


        } else if (SharedPrefManager.isLogin(Constrants.IsLogin)) {
            startActivity(new Intent(Login.this, HomeScreen.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        } else {

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    public void socailLogin(String socailId,String imageUrl, String name,String socailTyp,String email){

        HashMap objectNew = new HashMap();
        objectNew.put("device_token",SharedPrefManager.getDeviceToken(Constrants.Token)+"");
        objectNew.put("email",email+"");
        objectNew.put("social_id",socailId+"");
        objectNew.put("image",imageUrl+"");
        objectNew.put("login_type",socailTyp+"");
        objectNew.put("name",name+"");

        Log.i("socailogin_obj",objectNew+"");
        new WebTask(Login.this, WebUrls.BASE_URL+WebUrls.socialLoginApi,objectNew,
                Login.this, RequestCode.CODE_socialLoginApi,1);
    }



    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_socialLoginApi==taskcode){
            Log.i("socailogin_res",response);

            SocailLoginResponse socailLoginResponse = JsonDeserializer.deserializeJson(response,SocailLoginResponse.class);

            if (socailLoginResponse.statusCode == 1){
                SharedPrefManager.setUserName(Constrants.UserName,socailLoginResponse.data.username);
                SharedPrefManager.setUserEmail(Constrants.UserEmail, socailLoginResponse.data.email);
                SharedPrefManager.setUserMobile(Constrants.UserMobile, socailLoginResponse.data.mobile);
                SharedPrefManager.setUserPic(Constrants.UserPic, socailLoginResponse.data.userKyc.profileImage);
                SharedPrefManager.setreferCode(Constrants.ReferCode, socailLoginResponse.data.reffCode);
                SharedPrefManager.setUserID(Constrants.UserId, socailLoginResponse.data.id);
                SharedPrefManager.setUserFirstName(Constrants.UserFirstName, socailLoginResponse.data.userKyc.fName);
                SharedPrefManager.setUserLastName(Constrants.UserLastName, socailLoginResponse.data.userKyc.lName);

                if (socailLoginResponse.data.loginType.equalsIgnoreCase("facebook")){
                    SharedPrefManager.setFbLogin(Constrants.IsFBLogin, true);
                    SharedPrefManager.setGoogleLogin(Constrants.IsGoogleLogin, false);
                }else if (socailLoginResponse.data.loginType.equalsIgnoreCase("google")){
                    SharedPrefManager.setFbLogin(Constrants.IsFBLogin, false);
                    SharedPrefManager.setGoogleLogin(Constrants.IsGoogleLogin, true);
                }

                startActivity(new Intent(Login.this, HomeScreen.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }


        }
    }

}
