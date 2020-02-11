package shoppingpager.wingstud.shopinpager.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import shoppingpager.wingstud.shopinpager.R;
import shoppingpager.wingstud.shopinpager.activities.HomeScreen;
import shoppingpager.wingstud.shopinpager.activities.Login;
import shoppingpager.wingstud.shopinpager.activities.SeeAllProduct;
import shoppingpager.wingstud.shopinpager.activities.Splash;
import shoppingpager.wingstud.shopinpager.common.Constrants;
import shoppingpager.wingstud.shopinpager.common.SharedPrefManager;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String CHANNEL_NAME = "ShopinPager";
    private static final String CHANNEL_DESC = "Firebase Cloud Messaging";
    private static final String TAG = "tag";

    private int numMessages = 0;
//    private FCMData fcmData;
    int count = 0;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN", s);
        SharedPrefManager.setDeviceToken(Constrants.Token,s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            RemoteMessage.Notification notification = remoteMessage.getNotification();
            Map<String, String> data = remoteMessage.getData();
            Log.d("FROM", remoteMessage.getFrom());

            sendNotification(notification, data);
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    private void sendNotification(RemoteMessage.Notification notification, Map<String, String> data) {

        String title = data.get("title");
        String message = data.get("description");
        String image = data.get("image");
        String category_id = data.get("category_id");
        String redirect_type = data.get("redirect_type");
//        String time = data.get("time");
//        String date = data.get("date");
//        String send_type = data.get("send_type");

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.bigPicture(getBitmapFromURL(image)).build();

        Intent intent = null;
        if (SharedPrefManager.isLogin(Constrants.IsLogin)) {
            if (redirect_type.equalsIgnoreCase("category")){
                intent = new Intent(this,SeeAllProduct.class);
                intent.putExtra("cat_id", category_id+"");
                intent.putExtra("subCatId","");
            }else {
                intent = new Intent(this, HomeScreen.class);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
        }else {
            intent = new Intent(this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
        }


        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, getResources().getString(R.string.app_name))
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
                .setStyle(bigPictureStyle)
                .setSmallIcon(R.drawable.logo)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setLights(Color.RED, 1000, 300)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setNumber(++numMessages)
                .setSmallIcon(R.drawable.logo);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    getResources().getString(R.string.app_name), CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription(CHANNEL_DESC);
            channel.setShowBadge(true);
            channel.canShowBadge();
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


            if (notificationSoundUri != null) {
                // Changing Default mode of notification_green
                notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);

                // Creating an Audio Attribute
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build();

                channel.setSound(notificationSoundUri, audioAttributes);
            }

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});

            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }

        assert notificationManager != null;
        notificationManager.notify(0, notificationBuilder.build());

//        if (SharedPrefManager.getCartItemCount(Constrants.CartItemCount)==0){
//            count=1;
//        }else {
//            count = SharedPrefManager.getCartItemCount(Constrants.CartItemCount);
//            count+=1;
//        }
//        Dashboard.getInstance().setCountItem(count);

    }
    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
