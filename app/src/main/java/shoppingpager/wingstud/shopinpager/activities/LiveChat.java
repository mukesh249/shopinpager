package shoppingpager.wingstud.shopinpager.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


import shoppingpager.wingstud.shopinpager.R;
import shoppingpager.wingstud.shopinpager.databinding.ActivityLiveChatBinding;

public class LiveChat extends AppCompatActivity {

    private ActivityLiveChatBinding binding;
    String url;
    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_live_chat);

        if (getIntent().getExtras()!=null){
           url = getIntent().getExtras().getString("url","");
        }

        WebSettings webSettings =  binding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        binding.webView.loadUrl(url);
    }
}
