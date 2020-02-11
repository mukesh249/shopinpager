package shoppingpager.wingstud.shopinpager.activities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.JsonObject;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import shoppingpager.wingstud.shopinpager.R;
import shoppingpager.wingstud.shopinpager.api.RequestCode;
import shoppingpager.wingstud.shopinpager.api.WebCompleteTask;
import shoppingpager.wingstud.shopinpager.api.WebTask;
import shoppingpager.wingstud.shopinpager.api.WebUrls;
import shoppingpager.wingstud.shopinpager.common.Constrants;
import shoppingpager.wingstud.shopinpager.common.SharedPrefManager;
import shoppingpager.wingstud.shopinpager.contacts.Contact;
import shoppingpager.wingstud.shopinpager.contacts.Contacts;
import shoppingpager.wingstud.shopinpager.utils.DataObject;

import static android.Manifest.permission.READ_CONTACTS;

public class IntroActivity extends AppCompatActivity {

    private static final String TAG = "IntroActivity";
    @BindView(R.id.next)
    TextView Next;

    @BindView(R.id.view_pager)
    ViewPager vp;

    @BindView(R.id.dots_indicator)
    WormDotsIndicator dotsIndicator;

    CustomPageAdapter myvpAdapter;
    private final int REQUEST_PERMISSION = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        ButterKnife.bind(this, this);


        SharedPrefManager.setIntro(Constrants.IsIntro, true);
        if (isContectPermissionGranted()) {
//            getContacts();
            getContactList();
        } else {
            requestPermission();
        }
        Next.setOnClickListener(v -> {
            if (Next.getText().toString().compareTo(getResources().getString(R.string.let_s_start)) == 0) {
                Intent intent = new Intent(IntroActivity.this, Login.class);
                startActivity(intent);
            } else {
                vp.setCurrentItem(myvpAdapter.getItem(+1), true);
            }
        });
        vp.addOnPageChangeListener(viewPagerPageChangeListener);

        List<DataObject> getData = dataSource();
        myvpAdapter = new CustomPageAdapter(IntroActivity.this, getData);
        vp.setAdapter(myvpAdapter);
        dotsIndicator.setViewPager(vp);

    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            nextButtonBehaviour(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    @SuppressWarnings("PointlessBooleanExpression")
    private void nextButtonBehaviour(final int position) {
//        boolean hasPermissionToGrant = fragment.hasNeededPermissionsToGrant();
        if (myvpAdapter.isLastSlide(position)) {
            Next.setText(getResources().getString(R.string.let_s_start));
        } else {
            Next.setText(getResources().getString(R.string.next));
            Next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Next.getText().toString().compareTo(getResources().getString(R.string.let_s_start)) == 0) {
                        Intent intent = new Intent(IntroActivity.this, Login.class);
                        startActivity(intent);
                    } else {
                        vp.setCurrentItem(myvpAdapter.getItem(+1), true);
                    }
                }
            });
        }
    }

    private List<DataObject> dataSource() {
        List<DataObject> data = new ArrayList<DataObject>();
        data.add(new DataObject(R.drawable.intro1, getResources().getString(R.string.supermarket_in_n_your_mobile)));
        data.add(new DataObject(R.drawable.intro2, getResources().getString(R.string.shopin_provided_door_step_delivery)));
        data.add(new DataObject(R.drawable.intro3, getResources().getString(R.string.easy_payment_option_to_pay)));
        return data;
    }

    public class CustomPageAdapter extends PagerAdapter {
        private Context context;
        private List<DataObject> dataObjectList;
        private LayoutInflater layoutInflater;

        public CustomPageAdapter(Context context, List<DataObject> dataObjectList) {
            this.context = context;
            this.layoutInflater = (LayoutInflater) this.context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
            this.dataObjectList = dataObjectList;
        }

        private int getItem(int i) {
            return vp.getCurrentItem() + i;
        }

        @Override
        public int getCount() {
            return dataObjectList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = this.layoutInflater.inflate(R.layout.intro1, container, false);
            ImageView displayImage = (ImageView) view.findViewById(R.id.imageView);
            TextView imageText = (TextView) view.findViewById(R.id.textView1);
            displayImage.setImageResource(this.dataObjectList.get(position).getImageId());
            imageText.setText(this.dataObjectList.get(position).getImageName());
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        public boolean isLastSlide(int position) {
            return position == getCount() - 1;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public boolean isContectPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
            ) {
                return true;
            } else {
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation

            return true;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(IntroActivity.this,
                READ_CONTACTS)) {
            Log.e(TAG, "shouldShowRequestPermissionRationale:   READ_CONTACTS");
        } else {
            ActivityCompat.requestPermissions(IntroActivity.this, new String[]{READ_CONTACTS}, REQUEST_PERMISSION);
            Log.e(TAG, "requestPermissions:   READ_CONTACTS");
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    getContacts();
                    getContactList();
                } else {
                    Toast.makeText(IntroActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
//                    finish();
                }
                return;
            }

        }
    }

    private void getContacts() {
        JSONObject mainJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        List<Contact> contacts = Contacts.getQuery().find();
        for (Contact contact : contacts) {
            JSONObject innerJson = new JSONObject();
            try {
                try {
                    innerJson.put("DisplayName", contact.getDisplayName());
                    innerJson.put("PhoneNumber", contact.getPhoneNumbers().get(0).getNumber());
                } catch (IndexOutOfBoundsException e) {
                    innerJson.put("PhoneNumber", "");
                }
                jsonArray.put(innerJson);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


        try {
            mainJson.put("contact", jsonArray);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SharedPrefManager.putSP(Constrants.SP_CONTACTS, mainJson.toString());
        Log.e(TAG, "Contact Number " + mainJson.toString());
    }

    private void getContactList() {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            JSONArray contactArray = new JSONArray();
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    JSONObject jsonObject = new JSONObject();
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));

                        try {
                            jsonObject.put("name", name);
                            jsonObject.put("phone_number", phoneNo);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        Log.i(TAG, "Name: " + name);
                        Log.i(TAG, "Phone Number: " + phoneNo);
                    }
                    contactArray.put(jsonObject);
                    pCur.close();
                }
            }
            Log.i(TAG, "Contact_array: " + contactArray);
            SharedPrefManager.putSP(Constrants.SP_CONTACTS, contactArray.toString());

        }
        if (cur != null) {
            cur.close();
        }
    }

//    public void contactSubmit(JSONArray userContact){
//        HashMap objectNew = new HashMap();
//        objectNew.put("user_contact",userContact+"");
//        objectNew.put("user_id",SharedPrefManager.getUserID(Constrants.UserId));
//        Log.i(TAG, "Contact_array: " + objectNew);
//        new WebTask(IntroActivity.this, WebUrls.BASE_URL+WebUrls.getUserContact,objectNew,IntroActivity.this,
//                RequestCode.CODE_contactSubmit,5);
//    }
//    @Override
//    public void onComplete(String response, int taskcode) {
//
//        if (RequestCode.CODE_contactSubmit==taskcode){
//            Log.i(TAG, "Contact_res: " + response);
//
//            try {
//                JSONObject jsonObject = new JSONObject(response);
//                if (jsonObject.optInt("status_code")==1){
//                    Log.i(TAG, "success_res: " + jsonObject.optInt("message"));
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
