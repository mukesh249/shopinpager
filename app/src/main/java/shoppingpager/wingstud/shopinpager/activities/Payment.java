package shoppingpager.wingstud.shopinpager.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.jhonnyx2012.horizontalpicker.DatePickerListener;
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker;
import shoppingpager.wingstud.shopinpager.R;
import shoppingpager.wingstud.shopinpager.adapter.CheckoutAdapter;
import shoppingpager.wingstud.shopinpager.adapter.TimeSlotAdapter;
import shoppingpager.wingstud.shopinpager.api.JsonDeserializer;
import shoppingpager.wingstud.shopinpager.api.RequestCode;
import shoppingpager.wingstud.shopinpager.api.WebCompleteTask;
import shoppingpager.wingstud.shopinpager.api.WebTask;
import shoppingpager.wingstud.shopinpager.api.WebUrls;
import shoppingpager.wingstud.shopinpager.common.Constrants;
import shoppingpager.wingstud.shopinpager.common.SharedPrefManager;
import shoppingpager.wingstud.shopinpager.databinding.ActivityPaymentBinding;
import shoppingpager.wingstud.shopinpager.model.CheckOutModel;
import shoppingpager.wingstud.shopinpager.model.GenerateCheckSumResponse;
import shoppingpager.wingstud.shopinpager.model.PlaceOrderCodModel;
import shoppingpager.wingstud.shopinpager.utils.Utils;

import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@SuppressLint("SimpleDateFormat")
public class Payment extends AppCompatActivity implements WebCompleteTask, DatePickerListener {

    ActivityPaymentBinding paymentBinding;
    List<CheckOutModel.AddressList> addressLists = new ArrayList<>();
    List<CheckOutModel.DateTimeSlot> timeSlotList = new ArrayList<>();
    private CheckoutAdapter checkoutAdapter;
    private TimeSlotAdapter timeSlotAdapter;
    HorizontalPicker picker;
    double wallet_amt = 0;
    double pretotal = 0;
    double final_deli_ch = 0;
    public static String delivery_type = "";
    private String delivery_date = "",current_date="";
    private static Payment mInstance;
    private String payment_mode = "";
    private String expressTime = "";

    private String orderId, orderNo, method = "";

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paymentBinding = DataBindingUtil.setContentView(this, R.layout.activity_payment);

        mInstance = this;

        /**
         * Preload payment resources
         */
//        Checkout.preload(getApplicationContext());

        paymentBinding.headlyaout.searchIcon.setVisibility(View.GONE);
        paymentBinding.headlyaout.cartRL.setVisibility(View.GONE);
        paymentBinding.headlyaout.productCatName.setText(getResources().getString(R.string.payment));

        paymentBinding.headlyaout.backBtn.setOnClickListener(view -> finish());
        paymentBinding.userNameTv.setText(SharedPrefManager.getUserName(Constrants.UserName));
        paymentBinding.addRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        paymentBinding.addRecyclerView.setNestedScrollingEnabled(false);
        checkoutAdapter = new CheckoutAdapter(this, addressLists);
        paymentBinding.addRecyclerView.setAdapter(checkoutAdapter);
        checkoutAdapter.notifyDataSetChanged();

        paymentBinding.timeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        paymentBinding.timeRecyclerView.setNestedScrollingEnabled(false);
        timeSlotAdapter = new TimeSlotAdapter(this, timeSlotList);
        paymentBinding.timeRecyclerView.setAdapter(timeSlotAdapter);
        timeSlotAdapter.notifyDataSetChanged();

        paymentBinding.razorpayRadioBtn.setOnClickListener(view -> {
            paymentBinding.razorpayRadioBtn.setChecked(true);
            paymentBinding.codRadioBtn.setChecked(false);
            payment_mode = "online";
            paymentBinding.paynowBtn.setText(getResources().getString(R.string.pay_now));
        });

        paymentBinding.codRadioBtn.setOnClickListener(view -> {
            paymentBinding.codRadioBtn.setChecked(true);
            paymentBinding.razorpayRadioBtn.setChecked(false);
            payment_mode = "cod";
            paymentBinding.paynowBtn.setText(getResources().getString(R.string.place_order));
        });

        paymentBinding.paynowBtn.setOnClickListener(view -> {
            if (!Utils.checkEmptyNull(payment_mode)) {
                Utils.Toast(this, "Please Select Payment Method.");
            }
//            else if (!Utils.checkEmptyNull(delivery_date)) {
//                Utils.Toast(this, "Please Select Date.");
//            } else if (delivery_type.equals(getResources().getString(R.string.standard))
//                    &&!Utils.checkEmptyNull(TimeSlotAdapter.seleted_Time)) {
//                Utils.Toast(this, "Please Select Time Slot.");
//            }
            else {
//                if (paymentBinding.razorpayRadioBtn.isChecked())
//                    startPayment(orderId, orderNo);
//                if (paymentBinding.codRadioBtn.isChecked())
                    orderPlace();
            }
        });

        paymentBinding.changeAddressTv.setOnClickListener(view -> {
            startActivity(new Intent(Payment.this, AddNewAddress.class));
        });

        // find the picker
        picker = (HorizontalPicker) findViewById(R.id.datePicker);

        // initialize it and attach a listener
        picker.setListener(this)
                .setDays(6)
                .setOffset(0)
                .showTodayButton(false)
                .setTodayDateBackgroundColor(getResources().getColor(R.color.colorPrimary))
                .init();
//        picker.setDate(new DateTime().plusDays(0));

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        delivery_date = dateFormat.format(calendar.getTime());
        Log.i("first_date",delivery_date);
        delivery_type = getResources().getString(R.string.standard);

//        init();
        paymentBinding.express.setOnClickListener(view -> {
            delivery_type = getResources().getString(R.string.express);

            paymentBinding.express.setBackground(getResources().getDrawable(R.drawable.greyborder));
            paymentBinding.express.setTextColor(getResources().getColor(R.color.colorPrimary));
            paymentBinding.standardTv.setTextColor(getResources().getColor(R.color.black));
            paymentBinding.standardTv.setBackground(null);
//            paymentBinding.TimeLL.setVisibility(View.GONE);
            paymentBinding.expressViewTv.setVisibility(View.VISIBLE);
            paymentBinding.datePicker.setVisibility(View.GONE);
            deliveryAmt(CheckoutAdapter.seleted_address_id);
        });

        paymentBinding.standardTv.setOnClickListener(view -> {
            delivery_type = getResources().getString(R.string.standard);
            paymentBinding.standardTv.setBackground(getResources().getDrawable(R.drawable.greyborder));
            paymentBinding.standardTv.setTextColor(getResources().getColor(R.color.colorPrimary));
            paymentBinding.express.setTextColor(getResources().getColor(R.color.black));
            paymentBinding.express.setBackground(null);
//            paymentBinding.TimeLL.setVisibility(View.VISIBLE);
            paymentBinding.expressViewTv.setVisibility(View.GONE);
            paymentBinding.datePicker.setVisibility(View.VISIBLE);
            deliveryAmt(CheckoutAdapter.seleted_address_id);
        });

        paymentBinding.checkboxWallet.setOnCheckedChangeListener((buttonView, isChecked) -> {
            PayableAmount(isChecked);
        });
        userCheckOut();
    }

    public static boolean crtdate = true;
    @Override
    public void onDateSelected(DateTime dateSelected) {
        // log it for demo
        Log.i("HorizontalPicker", "Selected date is " + dateSelected.toString());
        try {
            delivery_date = Utils.formatDateFromDateString(
                    "yyyy-MM-dd'T'HH:mm:ss.SSS",
                    "dd-MM-yyyy",
                    dateSelected.toString());
            if (current_date.equals(delivery_date)){
                crtdate = true;
            }else {
                crtdate = false;
            }
//            TimeSlotAdapter.checkedPosition = -1;
//            TimeSlotAdapter.lastChecked = null;
//
//            for (int i =0;i<timeSlotList.size();i++){
//                timeSlotList.get(i).isChecked=false;
//            }
            TimeSlotAdapter.seleted_Time = "";
            timeSlotAdapter = new TimeSlotAdapter(this, timeSlotList);
            paymentBinding.timeRecyclerView.setAdapter(timeSlotAdapter);
//            timeSlotAdapter.notifyDataSetChanged();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.i("select_date", "Selected date is " + delivery_date);
    }

    public static Payment getInstance() {
        return mInstance;
    }

    public void userCheckOut() {
        HashMap objectNew = new HashMap();
        objectNew.put("pincode", SharedPrefManager.getPinCode(Constrants.PinCode));
        objectNew.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));

        Log.i("UserCheckout_obj", objectNew + "");
        new WebTask(this, WebUrls.BASE_URL + WebUrls.UserCheckout,
                objectNew, Payment.this, RequestCode.CODE_UserCheckout, 5);

    }

    public void deliveryAmt(String adr_id) {
        HashMap objectNew = new HashMap();
        objectNew.put("address_id", adr_id);
        objectNew.put("delivery_type", delivery_type.toLowerCase());
        objectNew.put("amount", pretotal+"");
        Log.i("GetDeliveryAmount_obj", objectNew + "");
        new WebTask(this, WebUrls.BASE_URL + WebUrls.GetDeliveryAmount,
                objectNew, Payment.this, RequestCode.CODE_GetDeliveryAmount, 5);
    }

    @Override
    protected void onStart() {
        super.onStart();
        paymentBinding.matrialProgress.setVisibility(View.GONE);
        paymentBinding.paynowBtn.setVisibility(View.VISIBLE);
        paymentBinding.scrollView.setEnabled(true);
        userCheckOut();
    }

    public void deliveryChargeRefresh(){
        final_deli_ch = 0;
        paymentBinding.deliveryChargeTv.setText(String.format("₹%.0f", final_deli_ch));
    }
    @SuppressLint("DefaultLocale")
    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_UserCheckout == taskcode) {
            Log.i("UserCheckout_res", response);
            addressLists.clear();
            timeSlotList.clear();
            CheckOutModel checkOutModel = JsonDeserializer.deserializeJson(response, CheckOutModel.class);
            if (checkOutModel.expressStatus == 1){
                paymentBinding.express.setVisibility(View.VISIBLE);
            }else {
                paymentBinding.express.setVisibility(View.GONE);
            }
            addressLists.addAll(checkOutModel.data.addressList);
            timeSlotList.addAll(checkOutModel.data.dateTimeSlot);
            checkoutAdapter.notifyDataSetChanged();
            timeSlotAdapter = new TimeSlotAdapter(this, timeSlotList);
            paymentBinding.timeRecyclerView.setAdapter(timeSlotAdapter);
            timeSlotAdapter.notifyDataSetChanged();
            paymentBinding.expressViewTv.setText(String.format("%s", checkOutModel.data.expressTimeString));
            expressTime = checkOutModel.data.expressTime;
            wallet_amt = checkOutModel.data.walletAmount;
            paymentBinding.checkboxWallet.setText(String.format("Use ShopinPager Wallet(₹%.0f)", wallet_amt));
            if (getIntent().getExtras() != null) {
                paymentBinding.walletLL.setVisibility(View.GONE);
                paymentBinding.walletV.setVisibility(View.GONE);
                pretotal = getIntent().getExtras().getDouble("amt", 0);
                paymentBinding.totalPayableTv.setText(String.format("₹%.0f", pretotal));
                PayableAmount(paymentBinding.checkboxWallet.isChecked());
            }
//            paymentBinding.scrollView.setVisibility(View.VISIBLE);
        }
        if (RequestCode.CODE_GetDeliveryAmount == taskcode) {
            Log.i("GetDeliveryAmount_res", response);

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
                if (jsonObject.optInt("status_code") == 1) {
                    final_deli_ch = jsonObject.optInt("delivery_charge");
                    paymentBinding.deliveryChargeTv.setText(String.format("₹%.0f", final_deli_ch));

                    if (getIntent().getExtras() != null) {
                        paymentBinding.walletLL.setVisibility(View.GONE);
                        paymentBinding.walletV.setVisibility(View.GONE);
                        pretotal = getIntent().getExtras().getDouble("amt", 0);
                        paymentBinding.totalPayableTv.setText(String.format("₹%.0f", pretotal));
                        PayableAmount(paymentBinding.checkboxWallet.isChecked());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (RequestCode.CODE_UserPlaceOrder == taskcode) {
            Log.i("UserPlaceOrder_res", response);

            PlaceOrderCodModel placeOrderCodModel = JsonDeserializer.deserializeJson(response, PlaceOrderCodModel.class);
            if (placeOrderCodModel.statusCode == 1) {
                paymentBinding.matrialProgress.setVisibility(View.GONE);
                paymentBinding.paynowBtn.setVisibility(View.GONE);
                paymentBinding.scrollView.setEnabled(false);

                orderId = placeOrderCodModel.data.id + "";
                orderNo = placeOrderCodModel.data.orderId + "";

                if (paymentBinding.codRadioBtn.isChecked()) {
                    Intent intent = new Intent(Payment.this, Success.class);
                    intent.putExtra("order_id", orderId);
                    intent.putExtra("order_no", orderNo);
                    intent .putExtra("method", "cod");
                    startActivity(intent);
                }
                if (paymentBinding.razorpayRadioBtn.isChecked()) {
                    generateChuckSum();
                }
            } else {
                Utils.Toast(this, placeOrderCodModel.message);
            }
        }
        //THg\/JJO\/jHE/jezz/z6nrNycWIu21BruuHZavW8Y5WG8rmXqJ5cF3orZxMqGHjBCRhcupBr4zhhO2pW6m5kSLzBS5kiExAaCqrU7cbCOiOQ=
        //THg/JJO/jHE/jezz/z6nrNycWIu21BruuHZavW8Y5WG8rmXqJ5cF3orZxMqGHjBCRhcupBr4zhhO2pW6m5kSLzBS5kiExAaCqrU7cbCOiOQ=
        if (RequestCode.CODE_generateChuckSum == taskcode) {
            Log.i("generateChuckSum_res", response);

            GenerateCheckSumResponse generateCheckSumResponse = JsonDeserializer.deserializeJson(response, GenerateCheckSumResponse.class);
            if (generateCheckSumResponse.paytSTATUS.equals("1")) {
                makePaytm(generateCheckSumResponse.oRDERID,generateCheckSumResponse.cHECKSUMHASH);
            }
//            else {
//                Utils.Toast(this, placeOrderCodModel.message);
//            }
        }
    }

    double netAmt = 0, wltDrwAmt = 0;

    @SuppressLint("DefaultLocale")
    public void PayableAmount(boolean isChecked) {
        double totAmt = 0;
        double wltSubAmt = 0;
        if (isChecked) {
            paymentBinding.walletLL.setVisibility(View.VISIBLE);
            paymentBinding.walletV.setVisibility(View.VISIBLE);
            if ((pretotal + final_deli_ch) > wallet_amt) {
                wltSubAmt = wallet_amt;
            } else {
                wltSubAmt = (pretotal + final_deli_ch);
            }
            totAmt = (pretotal + final_deli_ch) - wltSubAmt;
            paymentBinding.walletAmountTv.setText(String.format("-Rs.%.2f", wltSubAmt));
//                paymentBinding.checkboxWallet.setText(String.format("Use Grocito Wallet(Rs. %.2f)", totAmt));

        } else {
            paymentBinding.walletLL.setVisibility(View.GONE);
            paymentBinding.walletV.setVisibility(View.GONE);
            totAmt = (pretotal + final_deli_ch);
        }
        netAmt = totAmt;
        wltDrwAmt = wltSubAmt;
        if (totAmt == 0) {
            paymentBinding.razorpayRadioBtn.setVisibility(View.GONE);
        } else {
            paymentBinding.razorpayRadioBtn.setVisibility(View.VISIBLE);
        }
        paymentBinding.amountTv.setText(String.format("%.2f", totAmt));
    }

    public void orderPlace() {
        paymentBinding.matrialProgress.setVisibility(View.VISIBLE);
        paymentBinding.paynowBtn.setVisibility(View.GONE);
        paymentBinding.scrollView.setEnabled(false);
        paymentBinding.scrollView.setAlpha(0.5f);
        HashMap objectNew = new HashMap();
        objectNew.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
        objectNew.put("address_id", CheckoutAdapter.seleted_address_id);
        objectNew.put("pincode", SharedPrefManager.getPinCode(Constrants.PinCode));
        objectNew.put("net_amount", "100");
        objectNew.put("sgst_amount", "10");
        objectNew.put("payment_mode", payment_mode);
        objectNew.put("delivery_date", delivery_date);
        objectNew.put("delivery_time", TimeSlotAdapter.seleted_Time);
        objectNew.put("delivery_type", delivery_type.toLowerCase());
        objectNew.put("express_time", expressTime);
        objectNew.put("withdraw_wallet_amount", wltDrwAmt + "");
        objectNew.put("delivery_charge", final_deli_ch + "");
        Log.i("orderPlace_obj", objectNew + "");

        new WebTask(this, WebUrls.BASE_URL + WebUrls.UserPlaceOrder, objectNew, Payment.this, RequestCode.CODE_UserPlaceOrder, 5);

    }
    public void generateChuckSum(){
        double pay = Double.parseDouble(paymentBinding.amountTv.getText().toString());
        HashMap objectNew = new HashMap();
        objectNew.put("ORDER_ID",orderId);
        objectNew.put("TXN_AMOUNT",pay+"");
        objectNew.put("CUST_ID",SharedPrefManager.getUserID(Constrants.UserId));

        Log.i("generateChuckSum_obj", objectNew + "");
        new WebTask(this, "https://seller.cartlay.com/paytm001/generateChecksum.php", objectNew, Payment.this, RequestCode.CODE_generateChuckSum, 5);
    }

    private void makePaytm(String orderNumber,String paytmChecksum) {
        double amount = Double.parseDouble(paymentBinding.amountTv.getText().toString());

        //getting paytm service
        PaytmPGService Service = PaytmPGService.getProductionService();

        //use this when using for production
        //PaytmPGService Service = PaytmPGService.getProductionService();
//        PaytmPGService Service = PaytmPGService.getStagingService("https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=" + orderNumber);
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("MID", Constrants.P_MID);//Provided by Paytm
        paramMap.put("ORDER_ID", orderNumber);//unique OrderId for every request
        paramMap.put("CUST_ID", SharedPrefManager.getUserID(Constrants.UserId));// unique customer identifier
        paramMap.put("INDUSTRY_TYPE_ID", Constrants.P_INDUSTRY_TYPE_ID);//Provided by Paytm
        paramMap.put("CHANNEL_ID", Constrants.CHANNEL_ID);//Provided by Paytm
        paramMap.put("TXN_AMOUNT", amount+""); // transaction amount
        paramMap.put("WEBSITE", Constrants.P_WEBSITE);////Provided by Paytm
        //   paramMap.put("CALLBACK_URL", "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=" + orderNumber); //Provided by Paytm
        paramMap.put("CALLBACK_URL", "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID=" + orderNumber); //Provided by Paytm
        paramMap.put("CHECKSUMHASH", paytmChecksum+"");

        Log.i("Asdfsafas",paramMap+"");
        PaytmOrder Order = new PaytmOrder(paramMap);
        Service.initialize(Order, null);
        Service.startPaymentTransaction(this, true, true,
                new PaytmPaymentTransactionCallback() {
                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {
                        Toast.makeText(Payment.this, "someUIErrorOccurred", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onTransactionResponse(Bundle inResponse) {
                        Log.e("", "Payment Transaction is successful " + inResponse);
                        //  Bundle[{STATUS=PENDING, CHECKSUMHASH=Z4pqsucs67oVnSkdO47+tTi0Sex3SZ6sXEgEzlpXKdR9dxTIR78Wy57CCjcvRAZ1eeAf4Ksm7xfHCTVslzDbG5hwoT+H0DazIeH+Rl0rSXs=, BANKNAME=STATE BANK OF INDIA, ORDERID=213, TXNAMOUNT=270.00, TXNDATE=2018-12-03 10:58:11.0, MID=Ajissu50373323764671, TXNID=20181203111212800110168704300053372, RESPCODE=402, PAYMENTMODE=DC, BANKTXNID=, CURRENCY=INR, RESPMSG=Abondoned Transaction.}]

                        if (inResponse != null) {
                            Log.e("", "Payment Transaction is successful not null " + inResponse);
                            String statUs = inResponse.getString("STATUS");
                            String txnId = inResponse.getString("TXNID");
//                            if (statUs.equalsIgnoreCase("TXN_SUCCESS")) {
//                                placeOrderStatusChange(txnId);
//                            } else {
//                                orderUnSuccess();
//                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();

                        }


                    }

                    @Override
                    public void networkNotAvailable() { // If network is not
                        Toast.makeText(Payment.this, "networkNotAvailable", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {
                        Toast.makeText(Payment.this, "clientAuthenticationFailed", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode,
                                                      String inErrorMessage, String inFailingUrl) {
                        Toast.makeText(Payment.this, "onErrorLoadingWebPage", Toast.LENGTH_SHORT).show();

                    }

                    // had to be added: NOTE
                    @Override
                    public void onBackPressedCancelTransaction() {
                        Toast.makeText(getApplicationContext(), "Back pressed. Transaction cancelled", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                        Log.e("", "onTransactionCancel: " + inErrorMessage);
                        Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
                    }
                });
    }

}
