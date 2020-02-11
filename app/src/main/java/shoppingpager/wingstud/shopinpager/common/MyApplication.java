package shoppingpager.wingstud.shopinpager.common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;

import shoppingpager.wingstud.shopinpager.R;

//import com.bumptech.glide.Glide;
//
//import com.example.advosoft.vocab365.Model.DateItem;
//import com.example.advosoft.vocab365.R;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * Created by Advosoft2 on 6/27/2017.
 */

public class MyApplication extends MultiDexApplication {
    public static MyApplication myApplication = null;
    private static Context ctx;
    private String CANCER = "DoThat";
    public static SharedPreferences sp;
    private static final String isLogin = "loginVocab";
    public static final String server_updatelist="serverupdatelistVocab";
    public static TextToSpeech tts;
    Activity activity;


    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = new MyApplication();
        MultiDex.install(this);
//        Crittercism.initialize(getApplicationContext(), "9e06313bc11a42fbb3b9e92051213e9900555300");
        ctx = getApplicationContext();
        sp = ctx.getSharedPreferences(CANCER, 0);
//        loader = ImageLoader.getInstance();
//        arrDateItemwithBlank=new_image ArrayList<>();
//        arrDateItem = new_image ArrayList();
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != -1) {
                    try {
                        MyApplication.tts.setLanguage(Locale.getDefault());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    public static void showMessage(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }
    public static void showMessageOtp(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }
    public void hideSoftKeyBoard(Activity activity) {
        try {
            InputMethodManager inputManager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);

            // check if no view has focus:
            View v = activity.getCurrentFocus();
            if (v == null)
                return;

            inputManager.hideSoftInputFromWindow(v.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MyApplication getInstance() {
        if (myApplication == null) {
            myApplication = new MyApplication();
        }


        return myApplication;
    }

    public static void setUserPic(Context ctx, String type, String value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(type, value);
        editor.commit();
    }
    public static String getUserPic(Context ctx, String type) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        String e = sp.getString(type, "");
        return e;
    }


    public static void showFullImage(Context context, String imagePath) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(dialog.getWindow().FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        dialog.setContentView(R.layout.full_image);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.show();
        ImageView image = (ImageView) dialog.findViewById(R.id.image);

//        Glide.with(context)
//                .load(imagePath)
//                .apply(new_image RequestOptions().placeholder(android.R.drawable.ic_media_play).error(android.R.drawable.ic_media_play))
//                .into(image);

//        Glide.with(context)
//                .load(imagePath).placeholder(android.R.drawable.ic_media_play)
//                .into(image);
//        image.setOnClickListener(new_image View.OnClickListener() {
//
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });

    }
    public static void hideKeyBoard(Activity activity){
        InputMethodManager inputManager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        Log.e("hide kb", "1");
        if (view != null) {
            Log.e("hide kb", "2");
//            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }
//    public static void closeKeyboard(Activity activity) {
//        View view = activity.getCurrentFocus();
//        if (view != null) {
//            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
//    }
    public void StatusBarColor(Activity context){
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
            Window window = context.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.md_blue_grey_50));
        }
    }

    public static void RecyclerView(RecyclerView recyclerView){
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    }
}
