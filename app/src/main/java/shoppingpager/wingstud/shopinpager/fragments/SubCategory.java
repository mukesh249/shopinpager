package shoppingpager.wingstud.shopinpager.fragments;


import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import shoppingpager.wingstud.shopinpager.R;
import shoppingpager.wingstud.shopinpager.activities.SupportActivity;
import shoppingpager.wingstud.shopinpager.adapter.CategoryHomeAdapter;
import shoppingpager.wingstud.shopinpager.adapter.SubCategoryAdapter;
import shoppingpager.wingstud.shopinpager.api.JsonDeserializer;
import shoppingpager.wingstud.shopinpager.api.RequestCode;
import shoppingpager.wingstud.shopinpager.api.WebCompleteTask;
import shoppingpager.wingstud.shopinpager.api.WebTask;
import shoppingpager.wingstud.shopinpager.api.WebUrls;
import shoppingpager.wingstud.shopinpager.common.Constrants;
import shoppingpager.wingstud.shopinpager.common.SharedPrefManager;
import shoppingpager.wingstud.shopinpager.databinding.FragmentSubCategoryBinding;
import shoppingpager.wingstud.shopinpager.model.RaisingModel;
import shoppingpager.wingstud.shopinpager.model.SubCategoryModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubCategory extends Fragment implements WebCompleteTask {


    public SubCategory() {
        // Required empty public constructor
    }

    private FragmentSubCategoryBinding binding;
    public static String cat_id="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  DataBindingUtil.inflate(inflater,R.layout.fragment_sub_category, container, false);

        if (getArguments()!=null){
            cat_id = getArguments().getString("cat_id","");
            subCategory();
        }
        return binding.getRoot();
    }

    private void subCategory(){
        binding.recyclerView.setVisibility(View.GONE);
        binding.matrialProgress.setVisibility(View.VISIBLE);
        HashMap objectNew = new HashMap();
        objectNew.put("cat_id",cat_id);
        Log.i("subCategory_obj", objectNew+"");
        new WebTask(getActivity(), WebUrls.BASE_URL+WebUrls.UseGetSubCatList,
                objectNew, SubCategory.this, RequestCode.CODE_UseGetSubCatList,5);
    }

    @Override
    public void onComplete(String response, int taskcode) {

        if (RequestCode.CODE_UseGetSubCatList == taskcode) {
            Log.i("subCategory_res", response);

            SubCategoryModel subCategoryModel = JsonDeserializer.deserializeJson(response, SubCategoryModel.class);
            if (subCategoryModel.statusCode == 1) {
                if (subCategoryModel.data.isEmpty()) {
                    binding.recyclerView.setVisibility(View.GONE);
//                    binding.emptyLL.setVisibility(View.VISIBLE);
                } else {
                    binding.recyclerView.setVisibility(View.VISIBLE);
//                    binding.emptyLL.setVisibility(View.GONE);
//                    arrayList.addAll(subCategoryModel.data);
                    SubCategoryAdapter subCategoryAdapter = new SubCategoryAdapter(getActivity(),subCategoryModel.data);
                    binding.recyclerView.setAdapter(subCategoryAdapter);
                    subCategoryAdapter.notifyDataSetChanged();
                }
            }
            binding.matrialProgress.setVisibility(View.GONE);
        }
    }

}
