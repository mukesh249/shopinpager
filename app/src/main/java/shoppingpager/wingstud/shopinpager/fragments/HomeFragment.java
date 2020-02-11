package shoppingpager.wingstud.shopinpager.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import shoppingpager.wingstud.shopinpager.R;
import shoppingpager.wingstud.shopinpager.activities.HomeScreen;
import shoppingpager.wingstud.shopinpager.activities.ProductDetail;
import shoppingpager.wingstud.shopinpager.activities.SeeAllProduct;
import shoppingpager.wingstud.shopinpager.adapter.CateHomeAdapter;
import shoppingpager.wingstud.shopinpager.adapter.CategoryHomeAdapter;
import shoppingpager.wingstud.shopinpager.adapter.ImageViewPagerAdapter;
import shoppingpager.wingstud.shopinpager.adapter.OfferHomeAdapter;
import shoppingpager.wingstud.shopinpager.adapter.OtherAdapter;
import shoppingpager.wingstud.shopinpager.adapter.SliderPagerAdapter;
import shoppingpager.wingstud.shopinpager.adapter.SunProductAdapter;
import shoppingpager.wingstud.shopinpager.api.JsonDeserializer;
import shoppingpager.wingstud.shopinpager.api.RequestCode;
import shoppingpager.wingstud.shopinpager.api.WebCompleteTask;
import shoppingpager.wingstud.shopinpager.api.WebTask;
import shoppingpager.wingstud.shopinpager.api.WebUrls;
import shoppingpager.wingstud.shopinpager.common.Constrants;
import shoppingpager.wingstud.shopinpager.common.MyApplication;
import shoppingpager.wingstud.shopinpager.common.SharedPrefManager;
import shoppingpager.wingstud.shopinpager.databinding.FragmentHomeBinding;
import shoppingpager.wingstud.shopinpager.model.HomeGsonModel;
import shoppingpager.wingstud.shopinpager.model.ImageModel;
import shoppingpager.wingstud.shopinpager.utils.Utils;
import shoppingpager.wingstud.shopinpager.viewmodel.HomeViewModel;

public class HomeFragment extends Fragment implements WebCompleteTask {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    //--------------Category---------------------------
    CategoryHomeAdapter categoryHomeAdapter;
    List<HomeGsonModel.CatDatum> catDatumArrayList = new ArrayList<>();


    //------------Bottom Image ------------------------
    OtherAdapter otherAdapter;
    FragmentHomeBinding binding;
    ArrayList<HomeGsonModel.FooterBanner> specialArrayList = new ArrayList<>();
    ArrayList<HomeGsonModel.FooterBanner> brandArrayList = new ArrayList<>();

    //--------------------Slider-----------------------
    SliderPagerAdapter sliderPagerAdapter;
    ArrayList<HomeGsonModel.SliderList> sliderListArrayList = new ArrayList<>();
    ArrayList<HomeGsonModel.SliderList> firstSliderArrayList = new ArrayList<>();
    ArrayList<HomeGsonModel.SliderList> secondSliderArrayList = new ArrayList<>();
    ImageViewPagerAdapter imageViewPagerAdapter;
    ArrayList<ImageModel> imageModelArrayList;


    //--------------Category---------------------------
    CateHomeAdapter cateHomeAdapter;
    OfferHomeAdapter offerHomeAdapter;
    List<HomeGsonModel.FirstBanner> firstBannerArrayList = new ArrayList<>();
    List<HomeGsonModel.FirstBanner> secondBannerArrayList = new ArrayList<>();


    //---------------Best Selling Products-------------
    List<HomeGsonModel.MonthlyEssentialsProduct> bestSellingProducts = new ArrayList<>();
    SunProductAdapter sunProductAdapter;

    List<HomeGsonModel.MonthlyEssentialsProduct> newProductArrayList = new ArrayList<>();
    List<HomeGsonModel.MonthlyEssentialsProduct> monthlyEssentialsProductArrayList = new ArrayList<>();
    List<HomeGsonModel.MonthlyEssentialsProduct> weatherProductArrayList = new ArrayList<>();
    List<HomeGsonModel.MonthlyEssentialsProduct> savingProductArrayList = new ArrayList<>();
    List<HomeGsonModel.MonthlyEssentialsProduct> todayOfferProductArrayList = new ArrayList<>();

    private HomeViewModel homeViewModel;
    int noticount = 0;
    private int size;
    Timer timer;

    Runnable runnable;
    Handler handler;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);

        homeViewModel = ViewModelProviders.of(getActivity()).get(HomeViewModel.class);

        ProductDetail.product_detail = false;
        //----------------Slider Image----------------------
        imageModelArrayList = new ArrayList<>();
        imageViewPagerAdapter = new ImageViewPagerAdapter(getActivity(), imageModelArrayList);
        binding.viewPager.setAdapter(imageViewPagerAdapter);
        binding.dotsIndicator.setViewPager(binding.viewPager);

        //-------------------------------------isSpecial-----------------

        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
//        manager.setSpanSizeLookup(
//                new GridLayoutManager.SpanSizeLookup() {
//                    @Override
//                    public int getSpanSize(int position) {
//                        return (position % 3 == 0 ? 2 : 1);
//                    }
//                }
//        );
        binding.isSpecialRecyclerView.setLayoutManager(manager);
        MyApplication.RecyclerView(binding.isSpecialRecyclerView);

        binding.brandsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false));
        MyApplication.RecyclerView(binding.brandsRecyclerView);

        binding.bestRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        MyApplication.RecyclerView(binding.bestRecyclerView);

        binding.shopbyCatRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false));
        MyApplication.RecyclerView(binding.shopbyCatRecyclerView);

        binding.todayRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        MyApplication.RecyclerView(binding.todayRecyclerView);

        binding.newRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        MyApplication.RecyclerView(binding.newRecyclerView);

        binding.weathRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        MyApplication.RecyclerView(binding.weathRecyclerView);

        binding.savingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        MyApplication.RecyclerView(binding.savingRecyclerView);

        binding.monthRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        MyApplication.RecyclerView(binding.monthRecyclerView);

        binding.firstBannerRv.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false));
        MyApplication.RecyclerView(binding.firstBannerRv);

        binding.secondBannerRv.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false));
        MyApplication.RecyclerView(binding.secondBannerRv);

        HomeData();
        binding.seeAll.setVisibility(View.GONE);
        binding.seeAllbest.setOnClickListener(v -> startActivity(new Intent(getActivity(), SeeAllProduct.class)
                .putExtra("type", "is_best_selling")
        ));
        binding.seeAlltoday.setOnClickListener(v -> startActivity(new Intent(getActivity(), SeeAllProduct.class)
                .putExtra("type", "is_today_offer")
        ));
        binding.seeAllNew.setOnClickListener(v -> startActivity(new Intent(getActivity(), SeeAllProduct.class)
                .putExtra("type", "new")
        ));
        binding.seeAllmonth.setOnClickListener(v -> startActivity(new Intent(getActivity(), SeeAllProduct.class)
                .putExtra("type", "monthly_essentials")
        ));
        binding.seeAllsaving.setOnClickListener(v -> startActivity(new Intent(getActivity(), SeeAllProduct.class)
                .putExtra("type", "saving_pack")
        ));
        binding.seeAllweath.setOnClickListener(v -> startActivity(new Intent(getActivity(), SeeAllProduct.class)
                .putExtra("type", "weather_special")
        ));

        binding.seeAll.setVisibility(View.GONE);
        binding.seeAll.setOnClickListener(v -> {
            if (brandArrayList.size() > 9) {
                if (size == 9) {
                    size = brandArrayList.size();
                    CateSize();
                } else {
                    size = 9;
                    CateSize();
                }
            } else {
                size = brandArrayList.size();
            }
        });

        return binding.getRoot();
    }


    @Override
    public void onStart() {
        super.onStart();
        if (sliderListArrayList.size() > 0) {
//            sliderTime();
            timer = new Timer();
            timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
        }
        HomeScreen.getInstance().Notif(noticount);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (timer != null)
            timer.cancel();
        if (runnable != null)
            handler.removeCallbacks(runnable);
    }

    int i=0;
//    public void sliderTime() {
//        try {
//            new Handler().postDelayed(() -> {
//
////                if(i <= sliderPagerAdapter.getCount()-1)
////                {
////                    binding.viewPager.setCurrentItem(i, true);
////                    handler.postDelayed(TopChartAnimation, 100);
////                    i++;
////                }
//                //Your process to do
//                if (binding.viewPager.getCurrentItem() < sliderListArrayList.size() - 1) {
//                    binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1);
//                } else {
//                    binding.viewPager.setCurrentItem(0);
//                }
//
//                if (binding.firstviewPager.getCurrentItem() < firstSliderArrayList.size() - 1) {
//                    binding.firstviewPager.setCurrentItem(binding.firstviewPager.getCurrentItem() + 1);
//                } else {
//                    binding.firstviewPager.setCurrentItem(0);
//                }
//
//                if (binding.secondviewPager.getCurrentItem() < secondSliderArrayList.size() - 1) {
//                    binding.secondviewPager.setCurrentItem(binding.secondviewPager.getCurrentItem() + 1);
//                } else {
//                    binding.secondviewPager.setCurrentItem(0);
//                }
//            }, 5000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    private class SliderTimer extends TimerTask {
        @Override
        public void run() {
            Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
                if (binding.viewPager.getCurrentItem() < sliderListArrayList.size() - 1) {
                    binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1);
                } else {
                    binding.viewPager.setCurrentItem(0);
                }
                if (binding.firstviewPager.getCurrentItem() < firstSliderArrayList.size() - 1) {
                    binding.firstviewPager.setCurrentItem(binding.firstviewPager.getCurrentItem() + 1);
                } else {
                    binding.firstviewPager.setCurrentItem(0);
                }

                if (binding.secondviewPager.getCurrentItem() < secondSliderArrayList.size() - 1) {
                    binding.secondviewPager.setCurrentItem(binding.secondviewPager.getCurrentItem() + 1);
                } else {
                    binding.secondviewPager.setCurrentItem(0);
                }
            });
        }
    }

    public void CateSize() {
        otherAdapter = new OtherAdapter(getActivity(), brandArrayList, size);
        binding.brandsRecyclerView.setAdapter(otherAdapter);
        otherAdapter.notifyDataSetChanged();
    }

    public void HomeData() {
        binding.nestScrollview.setVisibility(View.GONE);
//        Utils.ProgressShow(getActivity(), binding.materialProgress, binding.nestScrollview);
        HashMap objectNew = new HashMap();
        objectNew.put("pincode", SharedPrefManager.getPinCode(Constrants.PinCode));
        objectNew.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
        new WebTask(getActivity(), WebUrls.BASE_URL + WebUrls.HomeApi, objectNew, HomeFragment.this, RequestCode.CODE_HomeData, 1);
    }

    @Override
    public void onComplete(String response, int taskcode) {

        if (RequestCode.CODE_HomeData == taskcode) {

            HomeGsonModel homeGsonModel = JsonDeserializer.deserializeJson(response, HomeGsonModel.class);

            Log.i("Home_Data_Res", response);
            if (homeGsonModel.statusCode == 1) {

                HomeScreen.getInstance().Notif(homeGsonModel.data.userNotifyCount);
                //-------------------------------------Category home-----------------
                catDatumArrayList.clear();
                if (!homeGsonModel.data.catData.isEmpty()) {
                    catDatumArrayList.addAll(homeGsonModel.data.catData);
                    categoryHomeAdapter = new CategoryHomeAdapter(getActivity(), catDatumArrayList);
                    binding.shopbyCatRecyclerView.setAdapter(categoryHomeAdapter);
                    categoryHomeAdapter.notifyDataSetChanged();
                }

                firstBannerArrayList.clear();
                if (!homeGsonModel.data.firstBanner.isEmpty()) {
                    firstBannerArrayList.addAll(homeGsonModel.data.firstBanner);
                    offerHomeAdapter = new OfferHomeAdapter(getActivity(), firstBannerArrayList, homeGsonModel.data.firstBanner.size());
                    binding.firstBannerRv.setAdapter(offerHomeAdapter);
                }

                secondBannerArrayList.clear();
                if (!homeGsonModel.data.secondBanner.isEmpty()) {
                    secondBannerArrayList.addAll(homeGsonModel.data.secondBanner);
                    offerHomeAdapter = new OfferHomeAdapter(getActivity(), secondBannerArrayList, homeGsonModel.data.firstBanner.size());
                    binding.secondBannerRv.setAdapter(offerHomeAdapter);
                }

                //----------------Sun offer----------------------------
                bestSellingProducts.clear();
                if (!homeGsonModel.data.bestSellingProduct.isEmpty()) {
                    bestSellingProducts.addAll(homeGsonModel.data.bestSellingProduct);
                    sunProductAdapter = new SunProductAdapter(getActivity(), bestSellingProducts);
                    binding.bestRecyclerView.setAdapter(sunProductAdapter);
//                        sunProductAdapter.notifyDataSetChanged();
                }

                //---------------- isExcl offer----------------------------
                newProductArrayList.clear();
                if (!homeGsonModel.data.newProduct.isEmpty()) {
                    newProductArrayList.addAll(homeGsonModel.data.newProduct);
                    sunProductAdapter = new SunProductAdapter(getActivity(), newProductArrayList);
                    binding.newRecyclerView.setAdapter(sunProductAdapter);
//                        sunProductAdapter.notifyDataSetChanged();
                }
                //---------------- new offer----------------------------
                monthlyEssentialsProductArrayList.clear();
                if (!homeGsonModel.data.monthlyEssentialsProduct.isEmpty()) {
                    monthlyEssentialsProductArrayList.addAll(homeGsonModel.data.monthlyEssentialsProduct);
                    sunProductAdapter = new SunProductAdapter(getActivity(), monthlyEssentialsProductArrayList);
                    binding.monthRecyclerView.setAdapter(sunProductAdapter);
//                        sunProductAdapter.notifyDataSetChanged();
                }

                todayOfferProductArrayList.clear();
                if (!homeGsonModel.data.todayOfferProduct.isEmpty()) {
                    todayOfferProductArrayList.addAll(homeGsonModel.data.todayOfferProduct);
                    sunProductAdapter = new SunProductAdapter(getActivity(), todayOfferProductArrayList);
                    binding.todayRecyclerView.setAdapter(sunProductAdapter);
//                        sunProductAdapter.notifyDataSetChanged();
                }

                savingProductArrayList.clear();
                if (!homeGsonModel.data.savingProduct.isEmpty()) {
                    savingProductArrayList.addAll(homeGsonModel.data.savingProduct);
                    sunProductAdapter = new SunProductAdapter(getActivity(), savingProductArrayList);
                    binding.savingRecyclerView.setAdapter(sunProductAdapter);
//                        sunProductAdapter.notifyDataSetChanged();
                }

                weatherProductArrayList.clear();
                if (!homeGsonModel.data.weatherProduct.isEmpty()) {
                    weatherProductArrayList.addAll(homeGsonModel.data.weatherProduct);
                    sunProductAdapter = new SunProductAdapter(getActivity(), weatherProductArrayList);
                    binding.weathRecyclerView.setAdapter(sunProductAdapter);
//                        sunProductAdapter.notifyDataSetChanged();
                }

                //-------------------------------------Home Slider-----------------
                sliderListArrayList.clear();
                if (!homeGsonModel.data.sliderList.isEmpty()) {
                    sliderListArrayList.addAll(homeGsonModel.data.sliderList);
                    sliderPagerAdapter = new SliderPagerAdapter(getActivity(), sliderListArrayList);
                    binding.viewPager.setAdapter(sliderPagerAdapter);
                    binding.dotsIndicator.setViewPager(binding.viewPager);

                    if (timer==null) {
                        timer = new Timer();
                        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
                    }
                }
                firstSliderArrayList.clear();
                if (!homeGsonModel.data.firstSlider.isEmpty()) {
                    firstSliderArrayList.addAll(homeGsonModel.data.firstSlider);
                    sliderPagerAdapter = new SliderPagerAdapter(getActivity(), firstSliderArrayList);
                    binding.firstviewPager.setAdapter(sliderPagerAdapter);
                    binding.dotsIndicatorFirst.setViewPager(binding.firstviewPager);

                    if (timer==null) {
                        timer = new Timer();
                        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
                    }
                }

                secondSliderArrayList.clear();
                if (!homeGsonModel.data.secondSlider.isEmpty()) {
                    secondSliderArrayList.addAll(homeGsonModel.data.secondSlider);
                    sliderPagerAdapter = new SliderPagerAdapter(getActivity(), secondSliderArrayList);
                    binding.secondviewPager.setAdapter(sliderPagerAdapter);
                    binding.dotsIndicatorSecond.setViewPager(binding.secondviewPager);

                    if (timer==null) {
                        timer = new Timer();
                        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
                    }
                }


                if (getActivity() != null)
                    Utils.setImage(getActivity(), binding.bottomBanner, WebUrls.BASE_URL + WebUrls.Banner_Image_URL +
                            homeGsonModel.data.isSpecial.images);

                specialArrayList.clear();
                if (!homeGsonModel.data.footerBanner.isEmpty()) {
//                    binding.isSpecialHeadTv.setText(homeGsonModel.data.isSpecial.name);
                    specialArrayList.addAll(homeGsonModel.data.footerBanner);
                    otherAdapter = new OtherAdapter(getActivity(), specialArrayList, specialArrayList.size());
                    binding.isSpecialRecyclerView.setAdapter(otherAdapter);
                    otherAdapter.notifyDataSetChanged();
                }
                brandArrayList.clear();
                if (!homeGsonModel.data.catData.isEmpty()) {
                    brandArrayList.addAll(homeGsonModel.data.brand);
                    if (brandArrayList.size() > 9) {
                        size = 9;
                        binding.seeAll.setVisibility(View.VISIBLE);
                    } else {
                        size = brandArrayList.size();
                        binding.seeAll.setVisibility(View.GONE);
                    }
                    CateSize();
                } else {
                    binding.seeAll.setVisibility(View.GONE);
                }
//                Utils.ProgressHide(getActivity(), binding.materialProgress, binding.nestScrollview);
                binding.nestScrollview.setVisibility(View.VISIBLE);

            }
        }

    }
}
