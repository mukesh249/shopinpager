package shoppingpager.wingstud.shopinpager.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GenerateCheckSumResponse {
    @SerializedName("CHECKSUMHASH")
    @Expose
    public String cHECKSUMHASH;
    @SerializedName("ORDER_ID")
    @Expose
    public String oRDERID;
    @SerializedName("payt_STATUS")
    @Expose
    public String paytSTATUS;
}
