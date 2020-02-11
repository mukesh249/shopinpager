package shoppingpager.wingstud.shopinpager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import shoppingpager.wingstud.shopinpager.R;
import shoppingpager.wingstud.shopinpager.adapter.RaisingComplAdapter;
import shoppingpager.wingstud.shopinpager.api.JsonDeserializer;
import shoppingpager.wingstud.shopinpager.api.RequestCode;
import shoppingpager.wingstud.shopinpager.api.WebCompleteTask;
import shoppingpager.wingstud.shopinpager.api.WebTask;
import shoppingpager.wingstud.shopinpager.api.WebUrls;
import shoppingpager.wingstud.shopinpager.common.Constrants;
import shoppingpager.wingstud.shopinpager.common.SharedPrefManager;
import shoppingpager.wingstud.shopinpager.databinding.ActivitySupportBinding;
import shoppingpager.wingstud.shopinpager.model.RaisingModel;
import shoppingpager.wingstud.shopinpager.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class SupportActivity extends AppCompatActivity implements WebCompleteTask {

    private ActivitySupportBinding binding;
    private ArrayList<RaisingModel.Datum> arrayList = new ArrayList<>();
    private RaisingComplAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_support);

        binding.headlyaout.searchIcon.setVisibility(View.GONE);
        binding.headlyaout.cartRL.setVisibility(View.GONE);
        binding.headlyaout.productCatName.setText(getResources().getString(R.string.raising_complaints));
        binding.headlyaout.backBtn.setOnClickListener(view->finish());

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RaisingComplAdapter(this,arrayList);
        binding.recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        raisingCompl();

        binding.liveCahtLl.setOnClickListener(v ->
                startActivity(new Intent(SupportActivity.this,LiveChat.class)
                        .putExtra("url","https://www.shopinpager.com/mobile-view-chat"))
        );
        binding.callRequestLl.setOnClickListener(view->callRequest());

    }

    private void raisingCompl(){
        binding.recyclerView.setVisibility(View.GONE);
        binding.matrialProgress.setVisibility(View.VISIBLE);
        HashMap objectNew = new HashMap();
        objectNew.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
        new WebTask(this, WebUrls.BASE_URL+WebUrls.GetRaisingComplaintList,objectNew,SupportActivity.this, RequestCode.CODE_GetRaisingComplaintList,5);
    }

    private void callRequest(){
        binding.matrialProgress.setVisibility(View.VISIBLE);
        HashMap objectNew = new HashMap();
        objectNew.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
        new WebTask(this, WebUrls.BASE_URL+WebUrls.UserCallRequest,objectNew,SupportActivity.this, RequestCode.CODE_UserCallRequest,5);
    }

    @Override
    public void onComplete(String response, int taskcode) {

        if (RequestCode.CODE_GetRaisingComplaintList==taskcode){
            Log.i("raising_compl_res",response);

            RaisingModel raisingModel = JsonDeserializer.deserializeJson(response,RaisingModel.class);
            if (raisingModel.statusCode==1){
                if (raisingModel.data.isEmpty()){
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.emptyLL.setVisibility(View.VISIBLE);
                }else {
                    binding.recyclerView.setVisibility(View.VISIBLE);
                    binding.emptyLL.setVisibility(View.GONE);
                    arrayList.addAll(raisingModel.data);
                    adapter.notifyDataSetChanged();
                }
            }
            binding.matrialProgress.setVisibility(View.GONE);
        }
        if (RequestCode.CODE_UserCallRequest==taskcode) {
            Log.i("callRequest_res", response);

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optInt("status_code")==1){

                }
                Utils.Toast(this,jsonObject.optString("message"));
                binding.matrialProgress.setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
