package shoppingpager.wingstud.shopinpager.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import shoppingpager.wingstud.shopinpager.R;
import shoppingpager.wingstud.shopinpager.activities.SeeAllProduct;
import shoppingpager.wingstud.shopinpager.api.WebUrls;
import shoppingpager.wingstud.shopinpager.databinding.CategoryItemHomeBinding;
import shoppingpager.wingstud.shopinpager.databinding.OfferItemHomeBinding;
import shoppingpager.wingstud.shopinpager.model.HomeGsonModel;
import shoppingpager.wingstud.shopinpager.utils.Utils;

public class OfferHomeAdapter extends RecyclerView.Adapter<OfferHomeAdapter.ViewHolder> {

    List<HomeGsonModel.FirstBanner> arrayList;

    Context context;
    int size;

    public OfferHomeAdapter(Context context, List<HomeGsonModel.FirstBanner> arrayList, int size) {
        this.arrayList = arrayList;
        this.context = context;
        this.size = size;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        OfferItemHomeBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(viewGroup.getContext()),
                        R.layout.offer_item_home, viewGroup, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        HomeGsonModel.FirstBanner bavergesModel = arrayList.get(i);
//        Utils.strikeText(viewHolder.binding.priveDisTv);
        viewHolder.binding.productName.setText(bavergesModel.title);

        Log.d("banner_image_offer_zone",WebUrls.BASE_URL+WebUrls.Banner_Image_URL + bavergesModel.images);
        Utils.setImage(context, viewHolder.binding.productImage, WebUrls.BASE_URL+WebUrls.Banner_Image_URL + bavergesModel.images);
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        OfferItemHomeBinding binding;

        public ViewHolder(@NonNull OfferItemHomeBinding itemView) {
            super(itemView.getRoot());

            this.binding = itemView;
            binding.getRoot().setOnClickListener(view -> {
                int pos = getAdapterPosition();
                context.startActivity(new Intent(context, SeeAllProduct.class)
                        .putExtra("cat_id", arrayList.get(pos).mainCategory.id+"")
                        .putExtra("subCatId","")
                );

            });
        }
    }
}
