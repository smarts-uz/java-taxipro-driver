package com.ourdevelops.ourdriver.utils.api.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ourdevelops.ourdriver.R;
import com.ourdevelops.ourdriver.activity.ChatActivity;
import com.ourdevelops.ourdriver.activity.MainActivity;
import com.ourdevelops.ourdriver.activity.NewOrderActivity;
import com.ourdevelops.ourdriver.activity.SplashActivity;
import com.ourdevelops.ourdriver.constants.BaseApp;
import com.ourdevelops.ourdriver.models.User;
import com.ourdevelops.ourdriver.utils.SettingPreference;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.ourdevelops.ourdriver.json.fcm.FCMType.CHAT;
import static com.ourdevelops.ourdriver.json.fcm.FCMType.ORDER;
import static com.ourdevelops.ourdriver.json.fcm.FCMType.OTHER;
import static com.ourdevelops.ourdriver.json.fcm.FCMType.OTHER2;

/**
 * Created by Ourdevelops Team on 10/13/2019.
 */

public class MessagingService extends FirebaseMessagingService {
    Intent intent;
    public static final String BROADCAST_ACTION = "com.ourdevelops.ourdriver";
    public static final String BROADCAST_ORDER = "order";
    Intent intentOrder;

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
        intentOrder = new Intent(BROADCAST_ORDER);

    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData() != null) {
            messageHandler(remoteMessage);
        }
    }

    private void messageHandler(RemoteMessage remoteMessage) {
        SettingPreference sp = new SettingPreference(this);
        User user = BaseApp.getInstance(this).getLoginUser();
        int code = Integer.parseInt(remoteMessage.getData().get("type"));
        switch (code) {
            case ORDER:
                Log.d("", "FCM Data Message: " + remoteMessage.getData());
                String resp = remoteMessage.getData().get("response");
                if (resp == null) {
                    if (sp.getSetting()[1].equals("Unlimited")) {
                        if (sp.getSetting()[2].equals("ON") && sp.getSetting()[3].equals("OFF") && user != null) {
                            notification(remoteMessage);
                        }
                    } else {
                        double uangbelanja = Double.valueOf(sp.getSetting()[1]);
                        double harga = Double.parseDouble(remoteMessage.getData().get("harga"));
                        if (uangbelanja > harga && sp.getSetting()[2].equals("ON") && sp.getSetting()[3].equals("OFF") && user != null) {
                            notification(remoteMessage);
                        }
                    }
                } else {
                    playSound();
                    intentCancel();
                }
                break;
            case OTHER:
                if (user != null) {
                    otherHandler(remoteMessage);
                }
                break;
            case OTHER2:
                otherHandler2(remoteMessage);
                break;

            case CHAT:
                if (user != null) {
                    Log.e("", "FCM Data Message: " + remoteMessage.getData());
                    chat(remoteMessage);
                }
                break;


        }
    }

    private void intentCancel() {
        Intent toMain = new Intent(getBaseContext(), MainActivity.class);
        toMain.addFlags(FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toMain);
    }

    private void otherHandler2(RemoteMessage remoteMessage) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_001");
        Intent intent1 = new Intent(getApplicationContext(), SplashActivity.class);
        intent1.addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pIntent1 = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent1, 0);
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(remoteMessage.getData().get("title"));
        bigTextStyle.bigText(remoteMessage.getData().get("message"));

        mBuilder.setContentIntent(pIntent1);
        mBuilder.setSmallIcon(R.drawable.logo);
        mBuilder.setContentTitle(remoteMessage.getData().get("title"));
        mBuilder.setContentText(remoteMessage.getData().get("message"));
        mBuilder.setStyle(bigTextStyle);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "customer";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel customer",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        notificationManager.notify(0, mBuilder.build());
    }

    private void otherHandler(RemoteMessage remoteMessage) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_001");
        Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
        intent1.addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pIntent1 = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent1, 0);
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(remoteMessage.getData().get("title"));
        bigTextStyle.bigText(remoteMessage.getData().get("message"));

        mBuilder.setContentIntent(pIntent1);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(remoteMessage.getData().get("title"));
        mBuilder.setContentText(remoteMessage.getData().get("message"));
        mBuilder.setStyle(bigTextStyle);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "customer";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel customer",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        notificationManager.notify(0, mBuilder.build());
    }

    private void chat(RemoteMessage remoteMessage) {

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "notify_001");
        Intent intent1 = new Intent(getApplicationContext(), ChatActivity.class);
        intent1.addFlags(FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent1.putExtra("senderid", remoteMessage.getData().get("receiverid"));
        intent1.putExtra("receiverid", remoteMessage.getData().get("senderid"));
        intent1.putExtra("name", remoteMessage.getData().get("name"));
        intent1.putExtra("tokendriver", remoteMessage.getData().get("tokendriver"));
        intent1.putExtra("tokenku", remoteMessage.getData().get("tokenuser"));
        intent1.putExtra("pic", remoteMessage.getData().get("pic"));
        PendingIntent pIntent1 = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent1, 0);
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(remoteMessage.getData().get("name"));
        bigTextStyle.bigText(remoteMessage.getData().get("message"));

        mBuilder.setContentIntent(pIntent1);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(remoteMessage.getData().get("name"));
        mBuilder.setContentText(remoteMessage.getData().get("message"));
        mBuilder.setStyle(bigTextStyle);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "customer";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel customer",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        notificationManager.notify(0, mBuilder.build());
    }

    private void notification(RemoteMessage remoteMessage) {
        Intent toOrder = new Intent(getBaseContext(), NewOrderActivity.class);
        toOrder.putExtra("id_transaksi", remoteMessage.getData().get("id_transaksi"));
        toOrder.putExtra("icon", remoteMessage.getData().get("icon"));
        toOrder.putExtra("layanan", remoteMessage.getData().get("layanan"));
        toOrder.putExtra("layanandesc", remoteMessage.getData().get("layanandesc"));
        toOrder.putExtra("alamat_asal", remoteMessage.getData().get("alamat_asal"));
        toOrder.putExtra("alamat_tujuan", remoteMessage.getData().get("alamat_tujuan"));
        toOrder.putExtra("estimasi_time", remoteMessage.getData().get("estimasi_time"));
        toOrder.putExtra("harga", remoteMessage.getData().get("harga"));
        toOrder.putExtra("biaya", remoteMessage.getData().get("biaya"));
        toOrder.putExtra("distance", remoteMessage.getData().get("distance"));
        toOrder.putExtra("pakai_wallet", remoteMessage.getData().get("pakai_wallet"));
        toOrder.putExtra("reg_id", remoteMessage.getData().get("reg_id_pelanggan"));
        toOrder.putExtra("order_fitur", remoteMessage.getData().get("order_fitur"));
        toOrder.putExtra("token_merchant", remoteMessage.getData().get("token_merchant"));
        toOrder.putExtra("id_pelanggan", remoteMessage.getData().get("id_pelanggan"));
        toOrder.putExtra("id_trans_merchant", remoteMessage.getData().get("id_trans_merchant"));
        toOrder.putExtra("waktu_order", remoteMessage.getData().get("waktu_order"));
        toOrder.addFlags(FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toOrder);
    }

    private void playSound() {
        MediaPlayer BG = MediaPlayer.create(getBaseContext(), R.raw.notification);
        BG.setLooping(false);
        BG.setVolume(100, 100);
        BG.start();

        Vibrator v = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
        v.vibrate(2000);
    }


}
