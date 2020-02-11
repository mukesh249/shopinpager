package shoppingpager.wingstud.shopinpager.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import shoppingpager.wingstud.shopinpager.R;
import shoppingpager.wingstud.shopinpager.databinding.CustomerRevRatingItemBinding;
import shoppingpager.wingstud.shopinpager.model.ProductDetialModel;

import java.util.List;

public class CustomerRevRatingAdapter extends RecyclerView.Adapter<CustomerRevRatingAdapter.ViewHolder> {

    List<ProductDetialModel.RatingDatum> arrayList;
    Context context;

    public CustomerRevRatingAdapter(Context context, List<ProductDetialModel.RatingDatum> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CustomerRevRatingItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(viewGroup.getContext()),
                        R.layout.customer_rev_rating_item,viewGroup,false);
        return new ViewHolder(binding);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ProductDetialModel.RatingDatum ratingDatum = arrayList.get(i);

        viewHolder.binding.customerNameTv.setText(ratingDatum.user.username);
        viewHolder.binding.customerDesTv.setText(ratingDatum.message);
        viewHolder.binding.ratingBar.setRating(Float.parseFloat(String.format("%.1f",(double)ratingDatum.rating)));
        viewHolder.binding.customerDateTv.setText(ratingDatum.createdAt);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CustomerRevRatingItemBinding binding;
        public ViewHolder(@NonNull CustomerRevRatingItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;

//            binding.getRoot().setOnClickListener(view -> context.startActivity(new Intent(context, ProductDetail.class)
//                    .putExtra("product_slug",arrayList.get(getAdapterPosition()).slug)));
        }
    }
}
