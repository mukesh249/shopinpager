package shoppingpager.wingstud.shopinpager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import shoppingpager.wingstud.shopinpager.R;
import shoppingpager.wingstud.shopinpager.databinding.FaqItemBinding;
import shoppingpager.wingstud.shopinpager.model.UserPagesModel;
import shoppingpager.wingstud.shopinpager.utils.Utils;

import java.util.List;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.ViewHolder> {

    List<UserPagesModel.FaqList> arrayList;

    Context context;

    public FaqAdapter(Context context, List<UserPagesModel.FaqList> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        FaqItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext())
                ,R.layout.faq_item,viewGroup,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        UserPagesModel.FaqList datum = arrayList.get(i);
        viewHolder.binding.quesTv.setText(Utils.FirstLatterCap(String.format("Qus.  %s",datum.title)));
        viewHolder.binding.ansTv.setText(Utils.FirstLatterCap(String.format("Ans.  %s",datum.description)));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        FaqItemBinding binding;
        public ViewHolder(@NonNull FaqItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
