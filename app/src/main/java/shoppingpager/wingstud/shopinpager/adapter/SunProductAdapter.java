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
import shoppingpager.wingstud.shopinpager.activities.ProductDetail;
import shoppingpager.wingstud.shopinpager.api.WebUrls;
import shoppingpager.wingstud.shopinpager.databinding.BavergesItemBinding;
import shoppingpager.wingstud.shopinpager.model.HomeGsonModel;
import shoppingpager.wingstud.shopinpager.utils.Utils;

public class SunProductAdapter extends RecyclerView.Adapter<SunProductAdapter.ViewHolder> {

    List<HomeGsonModel.MonthlyEssentialsProduct> arrayList;

    Context context;

    public SunProductAdapter(Context context, List<HomeGsonModel.MonthlyEssentialsProduct> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        BavergesItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(viewGroup.getContext()),
                        R.layout.baverges_item, viewGroup, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        HomeGsonModel.MonthlyEssentialsProduct homeCatProductModel = arrayList.get(i);
        if (homeCatProductModel.offer != null) {
            viewHolder.binding.offTv.setVisibility(View.VISIBLE);
            viewHolder.binding.offTv.setText("₹" + homeCatProductModel.offer + " off ");
        } else {
            viewHolder.binding.offTv.setVisibility(View.GONE);
        }
        Utils.strikeText(viewHolder.binding.priveDisTv);
        if (Utils.checkEmptyNull(homeCatProductModel.offerName))
            viewHolder.binding.productName.setText(homeCatProductModel.offerName);
        else
            viewHolder.binding.productName.setText(homeCatProductModel.name);

        if (Utils.checkEmptyNull(homeCatProductModel.schemeImage))
            Utils.setImage(context, viewHolder.binding.productImage, WebUrls.BASE_URL + WebUrls.SchemeImgUrl + homeCatProductModel.schemeImage);
        else
            Utils.setImage(context, viewHolder.binding.productImage, WebUrls.BASE_URL + WebUrls.IMAGE_PRODUCT + homeCatProductModel.image);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        BavergesItemBinding binding;

        public ViewHolder(@NonNull BavergesItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;

            binding.getRoot().setOnClickListener(view -> context.startActivity(new Intent(context, ProductDetail.class)
                    .putExtra("product_slug",arrayList.get(getAdapterPosition()).slug)));
        }
    }
}
