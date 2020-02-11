package shoppingpager.wingstud.shopinpager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import shoppingpager.wingstud.shopinpager.R;
import shoppingpager.wingstud.shopinpager.databinding.AvailablePlaceBinding;
import shoppingpager.wingstud.shopinpager.model.AvailablePlaceModel;

import java.util.ArrayList;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {

    ArrayList<AvailablePlaceModel> arrayList;
    Context context;


    public PlaceAdapter(Context context, ArrayList<AvailablePlaceModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AvailablePlaceBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.available_place, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
//        return arrayList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AvailablePlaceBinding binding;

        public ViewHolder(@NonNull AvailablePlaceBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

    }
}
