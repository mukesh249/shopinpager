package shoppingpager.wingstud.shopinpager.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

import shoppingpager.wingstud.shopinpager.R;
import shoppingpager.wingstud.shopinpager.activities.ProductDetail;
import shoppingpager.wingstud.shopinpager.activities.SeeAllProduct;
import shoppingpager.wingstud.shopinpager.api.WebUrls;
import shoppingpager.wingstud.shopinpager.databinding.ImageViewItemBinding;
import shoppingpager.wingstud.shopinpager.model.HomeGsonModel;
import shoppingpager.wingstud.shopinpager.utils.Utils;

import java.util.List;

public class SliderPagerAdapter extends PagerAdapter {

    private Context context;
    List<HomeGsonModel.SliderList> imageArray;

    public SliderPagerAdapter(Context context, List<HomeGsonModel.SliderList> imageArray) {
        this.context = context;
        this.imageArray = imageArray;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        ImageViewItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.image_view_item, container, false);

        if (ProductDetail.product_detail){
            binding.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }else {
            binding.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        if (imageArray.get(position).type.equals("slider")) {
            Utils.setImage(context, binding.imageView, WebUrls.BASE_URL + WebUrls.Slider_Image_URL + imageArray.get(position).images);
        }else {
            Utils.setImage(context, binding.imageView, WebUrls.BASE_URL + WebUrls.Banner_Image_URL + imageArray.get(position).images);
        }


        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageArray.get(position).mainCategory!=null){
                    context.startActivity(new Intent(context, SeeAllProduct.class)
                            .putExtra("cat_id", imageArray.get(position).mainCategory.id+"")
                            .putExtra("subCatId","")
                    );
                }
            }
        });

        container.addView(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imageArray.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }
}
