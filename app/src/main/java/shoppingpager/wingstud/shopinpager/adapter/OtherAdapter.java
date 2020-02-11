package shoppingpager.wingstud.shopinpager.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import shoppingpager.wingstud.shopinpager.R;
import shoppingpager.wingstud.shopinpager.activities.SeeAllProduct;
import shoppingpager.wingstud.shopinpager.api.WebUrls;
import shoppingpager.wingstud.shopinpager.databinding.OtherItemBinding;
import shoppingpager.wingstud.shopinpager.model.HomeGsonModel;
import shoppingpager.wingstud.shopinpager.utils.Utils;

import java.util.List;

public class OtherAdapter extends RecyclerView.Adapter<OtherAdapter.ViewHolder> {

    List<HomeGsonModel.FooterBanner> arrayList ;

    Context context;
    int size;

    public OtherAdapter(Context context, List<HomeGsonModel.FooterBanner> arrayList,int size) {
        this.arrayList = arrayList;
        this.context = context;
        this.size = size;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        OtherItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                R.layout.other_item,viewGroup,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        HomeGsonModel.FooterBanner isSpecialModel = arrayList.get(i);

        if (isSpecialModel.type!=null){
            android.view.ViewGroup.LayoutParams layoutParams = viewHolder.binding.productImage.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = 300;
            viewHolder.binding.productImage.setLayoutParams(layoutParams);
            viewHolder.binding.productImage.setScaleType(ImageView.ScaleType.FIT_XY);
            Utils.setImage(context,viewHolder.binding.productImage, WebUrls.BASE_URL+WebUrls.Banner_Image_URL+isSpecialModel.images);
        }else {
            if (!isSpecialModel.images.isEmpty())
            Utils.setImage(context,viewHolder.binding.productImage, WebUrls.BASE_URL+WebUrls.Brand_icon+isSpecialModel.images);
            else {
                viewHolder.binding.viewItemRl.setVisibility(View.GONE);
                viewHolder.binding.productImage.setVisibility(View.GONE);
            }
        }
        viewHolder.binding.productImage.setOnClickListener(v -> context.startActivity(new Intent(context, SeeAllProduct.class)
                .putExtra("cat_id","")
                .putExtra("subCatId","")
                .putExtra("type","brand")
                .putExtra("brand_id",isSpecialModel.id+"")
        ));
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        OtherItemBinding binding;
        public ViewHolder(@NonNull OtherItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
