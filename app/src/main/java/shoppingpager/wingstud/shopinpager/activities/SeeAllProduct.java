package shoppingpager.wingstud.shopinpager.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dmax.dialog.SpotsDialog;
import shoppingpager.wingstud.shopinpager.R;
import shoppingpager.wingstud.shopinpager.adapter.FilterBrandAdapter;
import shoppingpager.wingstud.shopinpager.adapter.FilterBrandTypeAdapter;
import shoppingpager.wingstud.shopinpager.adapter.FilterCatAdapter;
import shoppingpager.wingstud.shopinpager.adapter.SeeAllProductAdapter;
import shoppingpager.wingstud.shopinpager.api.JsonDeserializer;
import shoppingpager.wingstud.shopinpager.api.RequestCode;
import shoppingpager.wingstud.shopinpager.api.WebCompleteTask;
import shoppingpager.wingstud.shopinpager.api.WebTask;
import shoppingpager.wingstud.shopinpager.api.WebUrls;
import shoppingpager.wingstud.shopinpager.common.BottomSheetFragment;
import shoppingpager.wingstud.shopinpager.common.Constrants;
import shoppingpager.wingstud.shopinpager.common.MyApplication;
import shoppingpager.wingstud.shopinpager.common.SharedPrefManager;
import shoppingpager.wingstud.shopinpager.databinding.ActivitySeeAllProductBinding;
import shoppingpager.wingstud.shopinpager.fragments.DailogFragment;
import shoppingpager.wingstud.shopinpager.model.SeeAllProductsModel;
import shoppingpager.wingstud.shopinpager.utils.Utils;

public class SeeAllProduct extends AppCompatActivity implements WebCompleteTask {

    private ActivitySeeAllProductBinding binding;
    private SeeAllProductAdapter allProductAdapter;
    private List<SeeAllProductsModel.ProductList> arrayList = new ArrayList<>();
    private String catid = "", subcatid = "",brand_id="", catName = "";
    private static SeeAllProduct mInstance;
    AlertDialog progressDialog;
    private int countitem = 0;
    public static String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_see_all_product);
        progressDialog = new SpotsDialog(this, R.style.Custom);
        mInstance = this;
        if (getIntent().getExtras() != null)
            binding.headlyaout.productCatName.setText(getIntent().getExtras().getString("name", ""));

        binding.productRecyView.setLayoutManager(new GridLayoutManager(this, 1));
        MyApplication.RecyclerView(binding.productRecyView);
        allProductAdapter = new SeeAllProductAdapter(this, arrayList);
        binding.productRecyView.setAdapter(allProductAdapter);
        allProductAdapter.notifyDataSetChanged();

        binding.headlyaout.searchIcon.setOnClickListener(view -> startActivity(new Intent(SeeAllProduct.this, SearchItem.class)));
        FilterCatAdapter.cate_id = "";
        FilterBrandAdapter.brand_id = "";
        BottomSheetFragment.min_price = "";
        BottomSheetFragment.max_price = "";
        BottomSheetFragment.min_ofr = "";
        BottomSheetFragment.max_ofr = "";
        binding.headlyaout.backBtn.setOnClickListener(view ->
        {
            FilterCatAdapter.cate_id = "";
            FilterBrandAdapter.brand_id = "";
            BottomSheetFragment.min_price = "";
            BottomSheetFragment.max_price = "";
            BottomSheetFragment.min_ofr = "";
            BottomSheetFragment.max_ofr = "";
            finish();
        });

        binding.headlyaout.cartIcon.setOnClickListener(view -> startActivity(new Intent(SeeAllProduct.this, Cart.class)));

        if (getIntent().getExtras() != null) {
            type = getIntent().getExtras().getString("type", "");
            catid = getIntent().getExtras().getString("cat_id", "");
            subcatid = getIntent().getExtras().getString("subCatId", "");
            brand_id = getIntent().getExtras().getString("brand_id", "");
            Log.i("brand_id_re",brand_id);
        }
        binding.headlyaout.cartItemNo.setText(SharedPrefManager.getCartItemCount(Constrants.CartItemCount) + "");

        String name = "";
        if (type.equals("is_best_selling")) {
            name = getResources().getString(R.string.best_selling_products);
            seeAllOffer(type);
        }else if (type.equals("is_today_offer")) {
            name = getResources().getString(R.string.todays_offer);
            seeAllOffer(type);
        } else if (type.equals("new")) {
            name = getResources().getString(R.string.new_product);
            seeAllOffer(type);
        } else if (type.equals("monthly_essentials")) {
            name = getResources().getString(R.string.monthly_essentials);
            seeAllOffer(type);
        }
        else if (type.equals("saving_pack")) {
            name = getResources().getString(R.string.saving_pack);
            seeAllOffer(type);
        }
        else if (type.equals("weather_special")) {
            name = getResources().getString(R.string.weather_special);
            seeAllOffer(type);
        }else if (type.equals("brand")) {
//            if (brand_id.isEmpty()){
                FilterBrandAdapter.brand_id=brand_id;
//            }
            seeAllOffer(type);
        }  else {
            if (!subcatid.isEmpty()){
                FilterCatAdapter.cate_id=subcatid;
            }
            productList(subcatid);
        }
        binding.headlyaout.productCatName.setText(name);

        binding.flitLL.setOnClickListener(v ->
        {
            if (arrayList.size()>0) {
                BottomSheetFragment fragment = new BottomSheetFragment();
                Bundle args = new Bundle();
                if (type.equals("brand")) {
                    args.putString("cat_id", brand_id);
                }else {
                    args.putString("cat_id", catid);
                }
                args.putString("subCatId", subcatid);
                args.putString("catName", catName);
                fragment.setArguments(args);
                fragment.show(getSupportFragmentManager(), "TAG");
            }
        });

        binding.flitLL.setOnClickListener(v -> {
//            DailogFragment newFragment = new DailogFragment();

//            if (arrayList != null && arrayList.size() > 0) {
//                BottomSheetFragment fragment = new BottomSheetFragment();
            DailogFragment fragment = new DailogFragment();
            Bundle args = new Bundle();
            if (type.equals("brand")) {
                args.putString("cat_id", brand_id);
            }else {
                args.putString("cat_id", catid);
            }
            args.putString("subCatId", subcatid);
            args.putString("catName", catName);
            fragment.setArguments(args);
//                fragment.show(getSupportFragmentManager(), "TAG");
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(android.R.id.content, fragment).addToBackStack(null).commit();

//            }
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction().add(android.R.id.content, newFragment).addToBackStack(null).commit();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        countitem = SharedPrefManager.getCartItemCount(Constrants.CartItemCount);
        addCart(countitem);
    }

    public void seeAllOffer(String type) {
        Utils.ProgressShow(this, binding.matrialProgress, binding.productRecyView);
        HashMap objectNew = new HashMap();
        objectNew.put("type", type);
        objectNew.put("pincode", SharedPrefManager.getPinCode(Constrants.PinCode));
        objectNew.put("brand_id",brand_id + "");
        objectNew.put("min_price", BottomSheetFragment.min_price + "");
        objectNew.put("max_price", BottomSheetFragment.max_price + "");
        objectNew.put("min_offer", BottomSheetFragment.min_ofr + "");
        objectNew.put("max_offer", BottomSheetFragment.max_ofr + "");
        Log.i("seeAllOffer_obj", objectNew + "");
        new WebTask(this, WebUrls.BASE_URL + WebUrls.ProductTypeListing, objectNew,
                SeeAllProduct.this, RequestCode.CODE_ProductTypeListing, 5);
    }

    public void productList(String subcat_id) {
        Utils.ProgressShow(this, binding.matrialProgress, binding.productRecyView);
        HashMap objectNew = new HashMap();
        objectNew.put("cat_id", catid);
        objectNew.put("sub_cat_id", subcat_id);
        objectNew.put("brand_id",  FilterBrandAdapter.brand_id + "");
        objectNew.put("min_price", BottomSheetFragment.min_price + "");
        objectNew.put("max_price", BottomSheetFragment.max_price + "");
        objectNew.put("min_offer", BottomSheetFragment.min_ofr + "");
        objectNew.put("max_offer", BottomSheetFragment.max_ofr + "");
        objectNew.put("pincode", SharedPrefManager.getPinCode(Constrants.PinCode));
        Log.i("ProductListing_obj", objectNew + "");
        new WebTask(this, WebUrls.BASE_URL + WebUrls.ProductListing, objectNew,
                SeeAllProduct.this, RequestCode.CODE_ProductListing, 5);
    }

    @Override
    public void onComplete(String response, int taskcode) {

        if (RequestCode.CODE_ProductListing == taskcode) {
            Log.i("ProductListing_res", response);
            SeeAllProductsModel seeAllProductsModel = JsonDeserializer.deserializeJson(response, SeeAllProductsModel.class);
            if (seeAllProductsModel.statusCode == 1) {
                this.arrayList.clear();
                this.arrayList.addAll(seeAllProductsModel.data.productList);
                binding.productRecyView.setVisibility(View.VISIBLE);
                binding.emptyListLL.setVisibility(View.GONE);
                if (!seeAllProductsModel.data.productList.isEmpty()) {
                    binding.headlyaout.productCatName.setText(seeAllProductsModel.data.catdata.name);
                    catName = seeAllProductsModel.data.catdata.name;
                    allProductAdapter.notifyDataSetChanged();
                } else {
                    binding.productRecyView.setVisibility(View.GONE);
                    binding.emptyListLL.setVisibility(View.VISIBLE);
                }
            }
            Utils.ProgressHide(this, binding.matrialProgress, binding.productRecyView);
        }
        if (RequestCode.CODE_ProductTypeListing == taskcode) {
            Log.i("seeAllOffer_res", response);
            SeeAllProductsModel seeAllProductsModel = JsonDeserializer.deserializeJson(response, SeeAllProductsModel.class);
            if (seeAllProductsModel.statusCode == 1) {
                this.arrayList.clear();
                this.arrayList.addAll(seeAllProductsModel.data.productList);
                binding.productRecyView.setVisibility(View.VISIBLE);
                binding.emptyListLL.setVisibility(View.GONE);
                if (!seeAllProductsModel.data.productList.isEmpty()) {
//                    if (type.equals("is_sunday_bazar")) {
//                        binding.headlyaout.productCatName.setText(getString(R.string.sunday_bazar));
//                    } else if (type.equals("is_grocito_exclusive")) {
//                        binding.headlyaout.productCatName.setText(getString(R.string.grocito_exclusive));
//                    } else if (type.equals("new")) {
//                        binding.headlyaout.productCatName.setText(getString(R.string.new_arrival));
//                    } else {
//                        binding.headlyaout.productCatName.setText(seeAllProductsModel.data.catdata.name);
//                    }
                    allProductAdapter.notifyDataSetChanged();

                } else {
                    binding.productRecyView.setVisibility(View.GONE);
                    binding.emptyListLL.setVisibility(View.VISIBLE);
                }
            }
            Utils.ProgressHide(this, binding.matrialProgress, binding.productRecyView);
        }

    }

    public static SeeAllProduct getInstance() {
        return mInstance;
    }

    public void addCart(Integer count) {
//        Utils.CartCount(this, binding.headlyaout.cartItemNo, count);
        if (binding.headlyaout.cartItemNo != null) {
            if (count > 0) {
                binding.headlyaout.cartItemNo.setVisibility(View.VISIBLE);
            } else {
                binding.headlyaout.cartItemNo.setVisibility(View.GONE);
            }
            binding.headlyaout.cartItemNo.setText(String.valueOf(count));
        }
    }

    public void proShow(Boolean value, String message) {
        if (!progressDialog.isShowing())
            progressDialog.show();
        else {
            progressDialog.dismiss();
            Utils.Toast(this, message);
        }
    }


}
