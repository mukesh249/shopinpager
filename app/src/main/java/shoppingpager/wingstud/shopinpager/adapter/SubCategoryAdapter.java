package shoppingpager.wingstud.shopinpager.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import shoppingpager.wingstud.shopinpager.R;
import shoppingpager.wingstud.shopinpager.activities.SeeAllProduct;
import shoppingpager.wingstud.shopinpager.api.WebUrls;
import shoppingpager.wingstud.shopinpager.databinding.CategoryItemHomeBinding;
import shoppingpager.wingstud.shopinpager.databinding.SubcategoryItemBinding;
import shoppingpager.wingstud.shopinpager.fragments.SubCategory;
import shoppingpager.wingstud.shopinpager.model.HomeGsonModel;
import shoppingpager.wingstud.shopinpager.model.SubCategoryModel;
import shoppingpager.wingstud.shopinpager.utils.Utils;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> {

    List<SubCategoryModel.Datum> arrayList;

    Context context;
    int size;

    public SubCategoryAdapter(Context context, List<SubCategoryModel.Datum> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
//        this.size = size;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        SubcategoryItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(viewGroup.getContext()),
                        R.layout.subcategory_item, viewGroup, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        SubCategoryModel.Datum bavergesModel = arrayList.get(i);
//        Utils.strikeText(viewHolder.binding.priveDisTv);
        viewHolder.binding.productName.setText(bavergesModel.name);
        viewHolder.binding.productName.setTextSize(15.0f);
        Utils.setImage(context, viewHolder.binding.productImage, WebUrls.BASE_URL+WebUrls.CategoryIcon + bavergesModel.image);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        SubcategoryItemBinding binding;

        public ViewHolder(@NonNull SubcategoryItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;

            binding.productName.setVisibility(View.VISIBLE);
            binding.getRoot().setOnClickListener(view -> {
                int pos = getAdapterPosition();
                context.startActivity(new Intent(context, SeeAllProduct.class)
                        .putExtra("cat_id", SubCategory.cat_id+"")
                        .putExtra("subCatId",arrayList.get(pos).id+"")
                );

            });
        }
    }
}
