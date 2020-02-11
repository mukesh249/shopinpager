package shoppingpager.wingstud.shopinpager.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import shoppingpager.wingstud.shopinpager.R;
import shoppingpager.wingstud.shopinpager.databinding.ActivityWelcomeBinding;

public class Welcome extends AppCompatActivity {

    private ActivityWelcomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_welcome);

        binding.signinBtn.setOnClickListener(view ->
                startActivity(new Intent(Welcome.this,Login.class)));
        binding.createAccountBtn.setOnClickListener(view ->
                startActivity(new Intent(Welcome.this,SignUp.class)));



    }
}
