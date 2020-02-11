package shoppingpager.wingstud.shopinpager.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeGsonModel {


//    public class Brand {
//
//        @SerializedName("id")
//        @Expose
//        public Integer id;
//        @SerializedName("name")
//        @Expose
//        public String name;
//        @SerializedName("images")
//        @Expose
//        public String images;
//        @SerializedName("status")
//        @Expose
//        public Integer status;
//        @SerializedName("created_at")
//        @Expose
//        public String createdAt;
//        @SerializedName("updated_at")
//        @Expose
//        public String updatedAt;
//
//    }

    public class Data {

        @SerializedName("userNotifyCount")
        @Expose
        public Integer userNotifyCount;
        @SerializedName("sliderList")
        @Expose
        public List<SliderList> sliderList = null;
        @SerializedName("firstSlider")
        @Expose
        public List<SliderList> firstSlider = null;
        @SerializedName("secondSlider")
        @Expose
        public List<SliderList> secondSlider = null;
        @SerializedName("thirdSlider")
        @Expose
        public List<ThirdSlider> thirdSlider = null;
        @SerializedName("firstBanner")
        @Expose
        public List<FirstBanner> firstBanner = null;
        @SerializedName("secondBanner")
        @Expose
        public List<FirstBanner> secondBanner = null;
        @SerializedName("footerBanner")
        @Expose
        public List<FooterBanner> footerBanner = null;
        @SerializedName("isSpecial")
        @Expose
        public IsSpecial isSpecial;
        @SerializedName("brand")
        @Expose
        public List<FooterBanner> brand = null;
        @SerializedName("bestSellingProduct")
        @Expose
        public List<MonthlyEssentialsProduct> bestSellingProduct = null;
        @SerializedName("todayOfferProduct")
        @Expose
        public List<MonthlyEssentialsProduct> todayOfferProduct = null;
        @SerializedName("newProduct")
        @Expose
        public List<MonthlyEssentialsProduct> newProduct = null;
        @SerializedName("monthlyEssentialsProduct")
        @Expose
        public List<MonthlyEssentialsProduct> monthlyEssentialsProduct = null;
        @SerializedName("weatherProduct")
        @Expose
        public List<MonthlyEssentialsProduct> weatherProduct = null;
        @SerializedName("savingProduct")
        @Expose
        public List<MonthlyEssentialsProduct> savingProduct = null;
        @SerializedName("catData")
        @Expose
        public List<CatDatum> catData = null;

    }
    public class CatDatum {

        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("slug")
        @Expose
        public String slug;
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("description")
        @Expose
        public String description;
        @SerializedName("position")
        @Expose
        public Integer position;
        @SerializedName("is_home")
        @Expose
        public Integer isHome;
        @SerializedName("is_special")
        @Expose
        public Integer isSpecial;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("banner_img")
        @Expose
        public String bannerImg;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;
        @SerializedName("status")
        @Expose
        public Integer status;

    }

    @SerializedName("status_code")
    @Expose
    public Integer statusCode;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("data")
    @Expose
    public Data data;
    @SerializedName("product_img_url")
    @Expose
    public String productImgUrl;
    @SerializedName("slider_img_url")
    @Expose
    public String sliderImgUrl;
    @SerializedName("banner_img_url")
    @Expose
    public String bannerImgUrl;
    @SerializedName("brand_img_url")
    @Expose
    public String brandImgUrl;

    public class FirstBanner {

        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("title")
        @Expose
        public String title;
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("images")
        @Expose
        public String images;
        @SerializedName("content")
        @Expose
        public String content;
        @SerializedName("link")
        @Expose
        public String link;
        @SerializedName("is_special")
        @Expose
        public Integer isSpecial;
        @SerializedName("status")
        @Expose
        public Integer status;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;
        @SerializedName("main_category")
        @Expose
        public MainCategory mainCategory;

    }
    public class FooterBanner {

        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("title")
        @Expose
        public String title;
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("images")
        @Expose
        public String images;
        @SerializedName("content")
        @Expose
        public String content;
        @SerializedName("link")
        @Expose
        public String link;
        @SerializedName("is_special")
        @Expose
        public Integer isSpecial;
        @SerializedName("status")
        @Expose
        public Integer status;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;
        @SerializedName("main_category")
        @Expose
        public MainCategory mainCategory;

    }

    public class IsSpecial {

        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("title")
        @Expose
        public String title;
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("images")
        @Expose
        public String images;
        @SerializedName("content")
        @Expose
        public String content;
        @SerializedName("link")
        @Expose
        public String link;
        @SerializedName("is_special")
        @Expose
        public Integer isSpecial;
        @SerializedName("status")
        @Expose
        public Integer status;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;
        @SerializedName("main_category")
        @Expose
        public MainCategory mainCategory;

    }

    public class MainCategory {

        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("slug")
        @Expose
        public String slug;
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("description")
        @Expose
        public String description;
        @SerializedName("position")
        @Expose
        public Integer position;
        @SerializedName("is_home")
        @Expose
        public Integer isHome;
        @SerializedName("is_special")
        @Expose
        public Integer isSpecial;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("banner_img")
        @Expose
        public String bannerImg;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;
        @SerializedName("status")
        @Expose
        public Integer status;

    }

    public class MonthlyEssentialsProduct {

        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("user_id")
        @Expose
        public Integer userId;
        @SerializedName("brand_id")
        @Expose
        public Integer brandId;
        @SerializedName("slug")
        @Expose
        public String slug;
        @SerializedName("sku")
        @Expose
        public String sku;
        @SerializedName("category_id")
        @Expose
        public Integer categoryId;
        @SerializedName("category_slug")
        @Expose
        public String categorySlug;
        @SerializedName("sub_category_id")
        @Expose
        public Integer subCategoryId;
        @SerializedName("sub_category_slug")
        @Expose
        public String subCategorySlug;
        @SerializedName("super_sub_category_id")
        @Expose
        public Integer superSubCategoryId;
        @SerializedName("super_sub_category_slug")
        @Expose
        public String superSubCategorySlug;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("commission")
        @Expose
        public String commission;
        @SerializedName("p_gst")
        @Expose
        public String pGst;
        @SerializedName("color")
        @Expose
        public Object color;
        @SerializedName("city_id")
        @Expose
        public Integer cityId;
        @SerializedName("is_recommended")
        @Expose
        public Integer isRecommended;
        @SerializedName("is_today_offer")
        @Expose
        public Integer isTodayOffer;
        @SerializedName("is_best_selling")
        @Expose
        public Integer isBestSelling;
        @SerializedName("monthly_essentials")
        @Expose
        public Integer monthlyEssentials;
        @SerializedName("weather_special")
        @Expose
        public Integer weatherSpecial;
        @SerializedName("saving_pack")
        @Expose
        public Integer savingPack;
        @SerializedName("description")
        @Expose
        public String description;
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;
        @SerializedName("status")
        @Expose
        public Integer status;
        @SerializedName("is_admin_approved")
        @Expose
        public Integer isAdminApproved;
        @SerializedName("stock_status")
        @Expose
        public Integer stockStatus;
        @SerializedName("share_count")
        @Expose
        public Integer shareCount;
        @SerializedName("is_cod")
        @Expose
        public Integer isCod;
        @SerializedName("is_return")
        @Expose
        public Integer isReturn;
        @SerializedName("is_exchange")
        @Expose
        public Integer isExchange;
        @SerializedName("related_product")
        @Expose
        public String relatedProduct;
        @SerializedName("is_featured")
        @Expose
        public Integer isFeatured;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("brand_name")
        @Expose
        public String brandName;
        @SerializedName("offer_name")
        @Expose
        public String offerName;
        @SerializedName("schemeImage")
        @Expose
        public String schemeImage;
        @SerializedName("offer")
        @Expose
        public Integer offer;


    }

    public class SliderList {

        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("title")
        @Expose
        public String title;
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("images")
        @Expose
        public String images;
        @SerializedName("content")
        @Expose
        public String content;
        @SerializedName("link")
        @Expose
        public String link;
        @SerializedName("is_special")
        @Expose
        public Integer isSpecial;
        @SerializedName("status")
        @Expose
        public Integer status;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;
        @SerializedName("main_category")
        @Expose
        public MainCategory mainCategory;

    }

    public class ThirdSlider {

        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("title")
        @Expose
        public String title;
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("images")
        @Expose
        public String images;
        @SerializedName("content")
        @Expose
        public String content;
        @SerializedName("link")
        @Expose
        public String link;
        @SerializedName("is_special")
        @Expose
        public Integer isSpecial;
        @SerializedName("status")
        @Expose
        public Integer status;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;
        @SerializedName("main_category")
        @Expose
        public MainCategory mainCategory;

    }

}
