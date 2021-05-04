package com.ourdevelops.ourdriver.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ourdevelops.ourdriver.R;
import com.ourdevelops.ourdriver.constants.BaseApp;
import com.ourdevelops.ourdriver.constants.Constants;
import com.ourdevelops.ourdriver.json.AcceptRequestJson;
import com.ourdevelops.ourdriver.json.AcceptResponseJson;
import com.ourdevelops.ourdriver.utils.api.FCMHelper;
import com.ourdevelops.ourdriver.utils.api.ServiceGenerator;
import com.ourdevelops.ourdriver.utils.api.service.DriverService;
import com.ourdevelops.ourdriver.json.fcm.FCMMessage;
import com.ourdevelops.ourdriver.models.OrderFCM;
import com.ourdevelops.ourdriver.models.User;
import com.ourdevelops.ourdriver.utils.SettingPreference;
import com.ourdevelops.ourdriver.utils.Utility;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class NewOrderActivity extends AppCompatActivity {
    @BindView(R.id.layanan)
    TextView layanantext;
    @BindView(R.id.layanandes)
    TextView layanandesctext;
    @BindView(R.id.pickUpText)
    TextView pickuptext;
    @BindView(R.id.destinationText)
    TextView destinationtext;
    @BindView(R.id.fitur)
    TextView estimatetext;
    @BindView(R.id.distance)
    TextView distancetext;
    @BindView(R.id.cost)
    TextView costtext;
    @BindView(R.id.price)
    TextView pricetext;
    @BindView(R.id.totaltext)
    TextView totaltext;
    @BindView(R.id.image)
    ImageView icon;
    @BindView(R.id.timer)
    TextView timer;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.distancetext)
    TextView distancetextes;
    @BindView(R.id.costtext)
    TextView costtextes;
    @BindView(R.id.cancel)
    Button cancel;
    @BindView(R.id.order)
    Button order;
    @BindView(R.id.rlprogress)
    RelativeLayout rlprogress;
    @BindView(R.id.lldestination)
    LinearLayout lldestination;
    @BindView(R.id.lldistance)
    LinearLayout lldistance;

    String waktuorder,iconfitur, layanan, layanandesc, alamatasal, alamattujuan, estimasitime, hargatotal, cost, distance, idtrans, regid, orderfitur,tokenmerchant,idpelanggan,idtransmerchant;
    String wallett;
    MediaPlayer BG;
    Vibrator v;
    SettingPreference sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        ButterKnife.bind(this);
        removeNotif();
        setScreenOnFlags();
        sp = new SettingPreference(this);
        sp.updateNotif("ON");
        Intent intent = getIntent();
        iconfitur = intent.getStringExtra("icon");
        layanan = intent.getStringExtra("layanan");
        layanandesc = intent.getStringExtra("layanandesc");
        alamatasal = intent.getStringExtra("alamat_asal");
        alamattujuan = intent.getStringExtra("alamat_tujuan");
        estimasitime = intent.getStringExtra("estimasi_time");
        hargatotal = intent.getStringExtra("harga");
        cost = intent.getStringExtra("biaya");
        distance = intent.getStringExtra("distance");
        idtrans = intent.getStringExtra("id_transaksi");
        regid = intent.getStringExtra("reg_id");
        wallett = intent.getStringExtra("pakai_wallet");
        orderfitur = intent.getStringExtra("order_fitur");
        tokenmerchant = intent.getStringExtra("token_merchant");
        idpelanggan = intent.getStringExtra("id_pelanggan");
        idtransmerchant = intent.getStringExtra("id_trans_merchant");
        waktuorder = intent.getStringExtra("waktu_order");
        playSound();
        if (orderfitur.equalsIgnoreCase("6")) {
            lldestination.setVisibility(View.GONE);
            lldistance.setVisibility(View.GONE);

        }
        if (orderfitur.equalsIgnoreCase("10") || orderfitur.equalsIgnoreCase("11")
                || orderfitur.equalsIgnoreCase("12") || orderfitur.equalsIgnoreCase("13")) {

            estimatetext.setText(estimasitime);
            time.setText("Merchant");
            distancetextes.setText("Delivery Fee");
            costtextes.setText("Order Cost");
            Utility.currencyTXT(distancetext, distance, this);
            Utility.currencyTXT(costtext, cost, this);
        } else {

            estimatetext.setText(estimasitime);
            distancetext.setText(distance);
            costtext.setText(cost);
        }
        layanantext.setText(layanan);
        layanandesctext.setText(layanandesc);
        pickuptext.setText(alamatasal);
        destinationtext.setText(alamattujuan);
        Utility.currencyTXT(pricetext, hargatotal, this);
        if (wallett.equalsIgnoreCase("true")) {
            totaltext.setText("Total (WALLET)");
        } else {
            totaltext.setText("Total (CASH)");
        }


        Picasso.with(this)
                .load(Constants.IMAGESFITUR + iconfitur)
                .placeholder(R.drawable.logo)
                .resize(100, 100)
                .into(icon);

        timerplay.start();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BG.isPlaying()) {
                    BG.stop();
                    v.cancel();
                }
                timerplay.cancel();
                Intent toOrder = new Intent(NewOrderActivity.this, MainActivity.class);
                toOrder.addFlags(FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(toOrder);

            }
        });

        if (new SettingPreference(this).getSetting()[0].equals("OFF")) {
            order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getaccept();

                }
            });
        } else {
            getaccept();
        }


    }

    CountDownTimer timerplay = new CountDownTimer(20000, 1000) {

        public void onTick(long millisUntilFinished) {
            timer.setText("" + millisUntilFinished / 1000);
        }


        public void onFinish() {
            if (BG.isPlaying()) {
                BG.stop();
                v.cancel();
            }
            timer.setText("0");
            Intent toOrder = new Intent(NewOrderActivity.this, MainActivity.class);
            toOrder.addFlags(FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(toOrder);
        }
    }.start();


    private void playSound() {
        v = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        long[] pattern = {0, 100, 700};
        v.vibrate(pattern, 0);

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            BG = MediaPlayer.create(getBaseContext(), notification);
        } catch (Exception e) {
            e.printStackTrace();
        }

        BG.setLooping(true);
        BG.setVolume(100, 100);
        BG.start();
    }

    @Override
    public void onBackPressed() {
        return;
    }

    private void getaccept() {
        if (BG.isPlaying()) {
            BG.stop();
            v.cancel();
        }
        timerplay.cancel();
        rlprogress.setVisibility(View.VISIBLE);
        final User loginUser = BaseApp.getInstance(this).getLoginUser();
        DriverService userService = ServiceGenerator.createService(
                DriverService.class, loginUser.getNoTelepon(), loginUser.getPassword());
        AcceptRequestJson param = new AcceptRequestJson();
        param.setId(loginUser.getId());
        param.setIdtrans(idtrans);
        userService.accept(param).enqueue(new Callback<AcceptResponseJson>() {
            @Override
            public void onResponse(Call<AcceptResponseJson> call, final Response<AcceptResponseJson> response) {
                if (response.isSuccessful()) {
                    sp.updateNotif("OFF");
                    if (response.body().getMessage().equalsIgnoreCase("berhasil")) {
                        rlprogress.setVisibility(View.GONE);
                        Intent i = new Intent(NewOrderActivity.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        OrderFCM orderfcm = new OrderFCM();
                        orderfcm.id_driver = loginUser.getId();
                        orderfcm.id_transaksi = idtrans;
                        orderfcm.response = "2";
                        if (orderfitur.equalsIgnoreCase("10") || orderfitur.equalsIgnoreCase("11")
                                || orderfitur.equalsIgnoreCase("12") || orderfitur.equalsIgnoreCase("13")) {
                            orderfcm.desc = "the driver is buying an order";
                            orderfcm.id_pelanggan = idpelanggan;
                            orderfcm.invoice = "INV-"+idtrans+idtransmerchant;
                            orderfcm.ordertime = waktuorder;
                            sendMessageToDriver(tokenmerchant, orderfcm);
                        } else {
                            orderfcm.desc = getString(R.string.notification_start);
                        }
                        sendMessageToDriver(regid, orderfcm);
                    } else {
                        sp.updateNotif("OFF");
                        rlprogress.setVisibility(View.GONE);
                        Intent i = new Intent(NewOrderActivity.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        Toast.makeText(NewOrderActivity.this, "Order is no longer available!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AcceptResponseJson> call, Throwable t) {
                Toast.makeText(NewOrderActivity.this, "Error Connection!", Toast.LENGTH_SHORT).show();
                rlprogress.setVisibility(View.GONE);
                sp.updateNotif("OFF");
                rlprogress.setVisibility(View.GONE);
                Intent i = new Intent(NewOrderActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            }
        });
    }

    private void sendMessageToDriver(final String regIDTujuan, final OrderFCM response) {

        final FCMMessage message = new FCMMessage();
        message.setTo(regIDTujuan);
        message.setData(response);

        FCMHelper.sendMessage(Constants.FCM_KEY, message).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                Log.e("REQUEST TO DRIVER", message.getData().toString());
            }

            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void removeNotif() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }

    private void setScreenOnFlags() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            keyguardManager.requestDismissKeyguard(this, null);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        }
    }
}
