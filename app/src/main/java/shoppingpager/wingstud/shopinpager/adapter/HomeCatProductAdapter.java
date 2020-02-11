package shoppingpager.wingstud.shopinpager.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import shoppingpager.wingstud.shopinpager.R;
import shoppingpager.wingstud.shopinpager.activities.ProductDetail;
import shoppingpager.wingstud.shopinpager.api.WebUrls;
import shoppingpager.wingstud.shopinpager.databinding.BavergesItemBinding;
import shoppingpager.wingstud.shopinpager.model.HomeGsonModel;
import shoppingpager.wingstud.shopinpager.utils.Utils;

import java.util.List;

public class HomeCatProductAdapter extends RecyclerView.Adapter<HomeCatProductAdapter.ViewHolder> {

    List<HomeGsonModel> arrayList;

    Context context;

    public HomeCatProductAdapter(Context context, List<HomeGsonModel> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        BavergesItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(viewGroup.getContext()),
                        R.layout.baverges_item,viewGroup,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        HomeGsonModel homeCatProductModel = arrayList.get(i);
        Utils.strikeText(viewHolder.binding.priveDisTv);
//        viewHolder.binding.productName.setText(homeCatProductModel.pName);
//        Utils.setImage(context,viewHolder.binding.productImage, WebUrls.BASE_URL+WebUrls.IMAGE_PRODUCT+ homeCatProductModel.pImage);
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
            binding.offTv.setVisibility(View.GONE);

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    context.startActivity(new Intent(context, ProductDetail.class)
//                            .putExtra("product_slug",arrayList.get(getAdapterPosition()).pSlug));
                }
            });
        }
    }
}
