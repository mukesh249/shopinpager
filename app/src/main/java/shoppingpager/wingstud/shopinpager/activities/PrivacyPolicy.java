package shoppingpager.wingstud.shopinpager.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import shoppingpager.wingstud.shopinpager.R;
import shoppingpager.wingstud.shopinpager.common.Constrants;
import shoppingpager.wingstud.shopinpager.common.SharedPrefManager;
import shoppingpager.wingstud.shopinpager.databinding.ActivityPrivacyPolicyBinding;
import shoppingpager.wingstud.shopinpager.model.UserPagesModel;

public class PrivacyPolicy extends AppCompatActivity {

    private ActivityPrivacyPolicyBinding binding;
    UserPagesModel userPagesModel = new UserPagesModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_privacy_policy);

        binding.headlyaout.productCatName.setText(getResources().getString(R.string.privacy_policy));
        binding.headlyaout.cartRL.setVisibility(View.GONE);
        binding.headlyaout.searchIcon.setVisibility(View.GONE);
        binding.headlyaout.backBtn.setOnClickListener(v -> finish());
        String des = SharedPrefManager.getPrivacyPolicy(Constrants.PrivacyPolicy);

        binding.webView.loadData(des, "text/html", null);
    }
}
