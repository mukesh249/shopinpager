package shoppingpager.wingstud.shopinpager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import shoppingpager.wingstud.shopinpager.R;
import shoppingpager.wingstud.shopinpager.databinding.FilterCatItemBinding;
import shoppingpager.wingstud.shopinpager.model.FilterCatModel;
import shoppingpager.wingstud.shopinpager.utils.Utils;

import java.util.List;

public class FilterCatAdapter extends RecyclerView.Adapter<FilterCatAdapter.ViewHolder> {

   List<FilterCatModel.SubCatDatum> arrayList;

    private int checkedPosition = -1;
    public static String cate_id="";

    Context context;

    public FilterCatAdapter(Context context, List<FilterCatModel.SubCatDatum> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        FilterCatItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext())
                ,R.layout.filter_cat_item,viewGroup,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        FilterCatModel.SubCatDatum datum = arrayList.get(i);
        viewHolder.binding.nameTv.setText(Utils.FirstLatterCap(String.format("%s",datum.name)));
//        Glide.with(context).load()

        if (checkedPosition == i) {
            if (datum.categoryId != null)
                cate_id = datum.categoryId.toString();
            viewHolder.binding.llout.setBackground(context.getResources().getDrawable(R.drawable.grey_rounded_radius));
        } else {
            viewHolder.binding.llout.setBackground(null);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.binding.llout.setBackground(context.getResources().getDrawable(R.drawable.grey_rounded_radius));
                if (checkedPosition != i) {
                    notifyItemChanged(checkedPosition);
                    if (datum.categoryId != null)
                        cate_id = datum.categoryId.toString();
                    checkedPosition = i;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        FilterCatItemBinding binding;
        public ViewHolder(@NonNull FilterCatItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
