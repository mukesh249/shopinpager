package shoppingpager.wingstud.shopinpager.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SocailLoginResponse {

    public class Data {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("reff_code")
        @Expose
        public String reffCode;
        @SerializedName("ref_by")
        @Expose
        public String refBy;
        @SerializedName("username")
        @Expose
        public String username;
        @SerializedName("unique_code")
        @Expose
        public String uniqueCode;
        @SerializedName("agent_id")
        @Expose
        public String agentId;
        @SerializedName("category_id")
        @Expose
        public String categoryId;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("password")
        @Expose
        public String password;
        @SerializedName("simple_pass")
        @Expose
        public String simplePass;
        @SerializedName("otp")
        @Expose
        public String otp;
        @SerializedName("mobile")
        @Expose
        public String mobile;
        @SerializedName("merchant_count")
        @Expose
        public String merchantCount;
        @SerializedName("is_email_varifried")
        @Expose
        public String isEmailVarifried;
        @SerializedName("is_otp_varified")
        @Expose
        public String isOtpVarified;
        @SerializedName("role_id")
        @Expose
        public Integer roleId;
        @SerializedName("activated")
        @Expose
        public Integer activated;
        @SerializedName("is_available")
        @Expose
        public Integer isAvailable;
        @SerializedName("banned")
        @Expose
        public Integer banned;
        @SerializedName("verify_status")
        @Expose
        public String verifyStatus;
        @SerializedName("ban_reason")
        @Expose
        public String banReason;
        @SerializedName("new_password_key")
        @Expose
        public String newPasswordKey;
        @SerializedName("reset_password_token")
        @Expose
        public String resetPasswordToken;
        @SerializedName("new_email")
        @Expose
        public String newEmail;
        @SerializedName("new_email_key")
        @Expose
        public String newEmailKey;
        @SerializedName("device_token")
        @Expose
        public String deviceToken;
        @SerializedName("device_type")
        @Expose
        public String deviceType;
        @SerializedName("ip_address")
        @Expose
        public String ipAddress;
        @SerializedName("contact_details")
        @Expose
        public String contactDetails;
        @SerializedName("transaction_id")
        @Expose
        public String transactionId;
        @SerializedName("order_id")
        @Expose
        public String orderId;
        @SerializedName("last_login")
        @Expose
        public String lastLogin;
        @SerializedName("social_id")
        @Expose
        public String socialId;
        @SerializedName("rider_lat")
        @Expose
        public String riderLat;
        @SerializedName("rider_long")
        @Expose
        public String riderLong;
        @SerializedName("last_ip")
        @Expose
        public String lastIp;
        @SerializedName("login_time")
        @Expose
        public String loginTime;
        @SerializedName("login_type")
        @Expose
        public String loginType;
        @SerializedName("is_active")
        @Expose
        public String isActive;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;
        @SerializedName("deleted")
        @Expose
        public String deleted;
        @SerializedName("new_password_requested")
        @Expose
        public String newPasswordRequested;
        @SerializedName("is_login")
        @Expose
        public Integer isLogin;
        @SerializedName("is_clear_notification_date")
        @Expose
        public String isClearNotificationDate;
        @SerializedName("user_contacts")
        @Expose
        public String userContacts;
        @SerializedName("user_kyc")
        @Expose
        public UserKyc userKyc;

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
        @SerializedName("error_message")
        @Expose
        public String errorMessage;
        
    public class UserKyc {

        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("user_id")
        @Expose
        public Integer userId;
        @SerializedName("state_id")
        @Expose
        public String stateId;
        @SerializedName("country_id")
        @Expose
        public String countryId;
        @SerializedName("city_id")
        @Expose
        public String cityId;
        @SerializedName("f_name")
        @Expose
        public String fName;
        @SerializedName("l_name")
        @Expose
        public String lName;
        @SerializedName("dob")
        @Expose
        public String dob;
        @SerializedName("address")
        @Expose
        public String address;
        @SerializedName("pincode")
        @Expose
        public String pincode;
        @SerializedName("delivery_pincode")
        @Expose
        public String deliveryPincode;
        @SerializedName("gender")
        @Expose
        public String gender;
        @SerializedName("cin_number")
        @Expose
        public String cinNumber;
        @SerializedName("cin_image")
        @Expose
        public String cinImage;
        @SerializedName("aadhar_number")
        @Expose
        public String aadharNumber;
        @SerializedName("tan_number")
        @Expose
        public String tanNumber;
        @SerializedName("aadhar_image")
        @Expose
        public String aadharImage;
        @SerializedName("pan_number")
        @Expose
        public String panNumber;
        @SerializedName("gst_number")
        @Expose
        public String gstNumber;
        @SerializedName("pan_image")
        @Expose
        public String panImage;
        @SerializedName("seller_image")
        @Expose
        public String sellerImage;
        @SerializedName("cancel_cheque")
        @Expose
        public String cancelCheque;
        @SerializedName("account_number")
        @Expose
        public String accountNumber;
        @SerializedName("bank_name")
        @Expose
        public String bankName;
        @SerializedName("ifsc_code")
        @Expose
        public String ifscCode;
        @SerializedName("account_holder_name")
        @Expose
        public String accountHolderName;
        @SerializedName("alternate_mobile_no")
        @Expose
        public String alternateMobileNo;
        @SerializedName("food_license_no")
        @Expose
        public String foodLicenseNo;
        @SerializedName("business_reg_no")
        @Expose
        public String businessRegNo;
        @SerializedName("address_1")
        @Expose
        public String address1;
        @SerializedName("warehouse_id")
        @Expose
        public Integer warehouseId;
        @SerializedName("address_2")
        @Expose
        public String address2;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;
        @SerializedName("is_delete")
        @Expose
        public Integer isDelete;
        @SerializedName("profile_image")
        @Expose
        public String profileImage;
        @SerializedName("driving_licence_image")
        @Expose
        public String drivingLicenceImage;
        @SerializedName("tc")
        @Expose
        public Integer tc;
        @SerializedName("signature")
        @Expose
        public String signature;
        @SerializedName("cartlay_commission")
        @Expose
        public Integer cartlayCommission;
        @SerializedName("payment_plan")
        @Expose
        public Integer paymentPlan;
        @SerializedName("business_name")
        @Expose
        public String businessName;
        @SerializedName("business_address")
        @Expose
        public String businessAddress;
        @SerializedName("latitude")
        @Expose
        public String latitude;
        @SerializedName("longitude")
        @Expose
        public String longitude;

    }
}
