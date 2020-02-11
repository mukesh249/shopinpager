package shoppingpager.wingstud.shopinpager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import shoppingpager.wingstud.shopinpager.R;
import shoppingpager.wingstud.shopinpager.adapter.NotificationAdapter;
import shoppingpager.wingstud.shopinpager.api.JsonDeserializer;
import shoppingpager.wingstud.shopinpager.api.RequestCode;
import shoppingpager.wingstud.shopinpager.api.WebCompleteTask;
import shoppingpager.wingstud.shopinpager.api.WebTask;
import shoppingpager.wingstud.shopinpager.api.WebUrls;
import shoppingpager.wingstud.shopinpager.common.Constrants;
import shoppingpager.wingstud.shopinpager.common.SharedPrefManager;
import shoppingpager.wingstud.shopinpager.databinding.ActivityNotificationBinding;
import shoppingpager.wingstud.shopinpager.model.NotificationModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Notification extends AppCompatActivity implements WebCompleteTask {

    private ActivityNotificationBinding notificationBinding;
    private NotificationAdapter notificationAdapter;
    private List<NotificationModel.Notifydatum> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        notificationBinding = DataBindingUtil.setContentView(this,R.layout.activity_notification);
        notificationBinding.headlyaout.searchIcon.setVisibility(View.GONE);
        notificationBinding.headlyaout.cartRL.setVisibility(View.GONE);
        notificationBinding.headlyaout.productCatName.setText(getResources().getString(R.string.notification));

        notificationBinding.headlyaout.backBtn.setOnClickListener(view -> finish());

        notificationBinding.headlyaout.cartIcon.setOnClickListener(view -> startActivity(new Intent(Notification.this,Cart.class)));

        notificationBinding.notificationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notifaction();
    }

    public void notifaction(){
       notificationBinding.matrialProgress.setVisibility(View.VISIBLE);
        HashMap objectNew = new HashMap();
        objectNew.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
        new WebTask(this, WebUrls.BASE_URL+WebUrls.GetUserNotification,objectNew,Notification.this, RequestCode.CODE_GetUserNotification,5);
    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_GetUserNotification == taskcode){
            NotificationModel model = JsonDeserializer.deserializeJson(response,NotificationModel.class);
            if (model.statusCode==1){
                list.clear();
                if (model.notifydata.isEmpty()) {
                    notificationBinding.emptyList.imageEmpty.setImageDrawable(getDrawable(R.drawable.envelope));
                    notificationBinding.emptyList.titleEmpty.setText("No Notification");
                    notificationBinding.emptyList.desEmpty.setText("we something came to new. we will let you know.");
                    notificationBinding.emptyListLL.setVisibility(View.VISIBLE);
                    notificationBinding.notificationRecyclerView.setVisibility(View.GONE);
                }else {
                    notificationBinding.emptyListLL.setVisibility(View.GONE);
                    notificationBinding.notificationRecyclerView.setVisibility(View.VISIBLE);
                    list.addAll(model.notifydata);
                    notificationAdapter = new NotificationAdapter(Notification.this, list);
                    notificationBinding.notificationRecyclerView.setAdapter(notificationAdapter);
                    notificationAdapter.notifyDataSetChanged();
                }
            }
            notificationBinding.matrialProgress.setVisibility(View.GONE);
        }
    }
}
