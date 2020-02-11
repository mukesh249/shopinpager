package shoppingpager.wingstud.shopinpager.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import shoppingpager.wingstud.shopinpager.R;
import shoppingpager.wingstud.shopinpager.activities.HomeScreen;
import shoppingpager.wingstud.shopinpager.activities.SeeAllProduct;
import shoppingpager.wingstud.shopinpager.api.WebUrls;
import shoppingpager.wingstud.shopinpager.databinding.CategoryItemHomeBinding;
import shoppingpager.wingstud.shopinpager.fragments.Category;
import shoppingpager.wingstud.shopinpager.fragments.SubCategory;
import shoppingpager.wingstud.shopinpager.model.HomeGsonModel;
import shoppingpager.wingstud.shopinpager.utils.Utils;

public class CategoryHomeAdapter extends RecyclerView.Adapter<CategoryHomeAdapter.ViewHolder> {

    List<HomeGsonModel.CatDatum> arrayList;

    Context context;
    int size;

    public CategoryHomeAdapter(Context context, List<HomeGsonModel.CatDatum> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
//        this.size = size;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CategoryItemHomeBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(viewGroup.getContext()),
                        R.layout.category_item_home, viewGroup, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        HomeGsonModel.CatDatum bavergesModel = arrayList.get(i);
//        Utils.strikeText(viewHolder.binding.priveDisTv);
        viewHolder.binding.productName.setText(bavergesModel.name);
        Utils.setImage(context, viewHolder.binding.productImage, WebUrls.BASE_URL+WebUrls.Cat_Banner_URL + bavergesModel.bannerImg);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CategoryItemHomeBinding binding;

        public ViewHolder(@NonNull CategoryItemHomeBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;

            binding.productName.setVisibility(View.VISIBLE);
            binding.getRoot().setOnClickListener(view -> {
                int pos = getAdapterPosition();

                SubCategory subCategory = new SubCategory();
                Bundle bundle = new Bundle();
                bundle.putString("cat_id", arrayList.get(pos).id+"");
                bundle.putString("subCatId", "");
                subCategory.setArguments(bundle);

                ((HomeScreen)context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.containermain,subCategory)
                        .addToBackStack(null)
                        .commit();
//                context.startActivity(new Intent(context, SeeAllProduct.class)
//                        .putExtra("cat_id", arrayList.get(pos).id+"")
//                        .putExtra("subCatId","")
//                );

            });
        }
    }
}
