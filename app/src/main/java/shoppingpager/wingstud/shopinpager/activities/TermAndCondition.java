package shoppingpager.wingstud.shopinpager.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import shoppingpager.wingstud.shopinpager.R;
import shoppingpager.wingstud.shopinpager.common.Constrants;
import shoppingpager.wingstud.shopinpager.common.SharedPrefManager;
import shoppingpager.wingstud.shopinpager.databinding.ActivityTermAndConditionBinding;
import shoppingpager.wingstud.shopinpager.model.UserPagesModel;

public class TermAndCondition extends AppCompatActivity {

    private ActivityTermAndConditionBinding binding;
    UserPagesModel userPagesModel = new UserPagesModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_term_and_condition);

        binding.headlyaout.productCatName.setText(getResources().getString(R.string.term_and_condition));
        binding.headlyaout.cartRL.setVisibility(View.GONE);
        binding.headlyaout.searchIcon.setVisibility(View.GONE);
        binding.headlyaout.backBtn.setOnClickListener(v -> finish());
        String des = SharedPrefManager.getTermCondition(Constrants.TermCondition);

        binding.webView.loadData(des, "text/html", null);
    }
}
