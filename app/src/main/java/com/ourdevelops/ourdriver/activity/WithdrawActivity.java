package com.ourdevelops.ourdriver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ourdevelops.ourdriver.R;
import com.ourdevelops.ourdriver.constants.BaseApp;
import com.ourdevelops.ourdriver.constants.Constants;
import com.ourdevelops.ourdriver.json.ResponseJson;
import com.ourdevelops.ourdriver.json.WithdrawRequestJson;
import com.ourdevelops.ourdriver.json.WithdrawResponseJson;
import com.ourdevelops.ourdriver.json.fcm.FCMMessage;
import com.ourdevelops.ourdriver.models.Notif;
import com.ourdevelops.ourdriver.models.User;
import com.ourdevelops.ourdriver.utils.SettingPreference;
import com.ourdevelops.ourdriver.utils.Utility;
import com.ourdevelops.ourdriver.utils.api.FCMHelper;
import com.ourdevelops.ourdriver.utils.api.ServiceGenerator;
import com.ourdevelops.ourdriver.utils.api.service.DriverService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WithdrawActivity extends AppCompatActivity {

    EditText amount, bank, accnumber, nama;
    Button submit;
    TextView notif;
    ImageView backbtn;
    RelativeLayout rlnotif, rlprogress;
    String disableback;
    SettingPreference sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        sp = new SettingPreference(this);
        disableback = "false";
        amount = findViewById(R.id.amount);
        bank = findViewById(R.id.bank);
        accnumber = findViewById(R.id.accnumber);
        backbtn = findViewById(R.id.back_btn);
        submit = findViewById(R.id.submit);
        rlnotif = findViewById(R.id.rlnotif);
        notif = findViewById(R.id.textnotif);
        rlprogress = findViewById(R.id.rlprogress);
        nama = findViewById(R.id.namanumber);

        amount.addTextChangedListener(Utility.currencyTW(amount,this));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User userLogin = BaseApp.getInstance(WithdrawActivity.this).getLoginUser();
                if (amount.getText().toString().isEmpty()) {
                    notif("please enter amount!");
                } else if (Long.parseLong(amount.getText()
                        .toString()
                        .replace(".", "")
                        .replace(sp.getSetting()[4], "")) > userLogin.getWalletSaldo()) {
                    notif("your balance is not enough!");
                } else if (bank.getText().toString().isEmpty()) {
                    notif("please enter bank!");
                } else if (accnumber.getText().toString().isEmpty()) {
                    notif("please enter Account number!");
                } else {
                    submit();
                }
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void submit() {
        progressshow();
        final User user = BaseApp.getInstance(this).getLoginUser();
        WithdrawRequestJson request = new WithdrawRequestJson();
        request.setId(user.getId());
        request.setBank(bank.getText().toString());
        request.setName(nama.getText().toString());
        request.setAmount(amount.getText().toString().replace(".", "").replace(sp.getSetting()[4], ""));
        request.setCard(accnumber.getText().toString());
        request.setNotelepon(user.getNoTelepon());
        request.setEmail(user.getEmail());

        DriverService service = ServiceGenerator.createService(DriverService.class, user.getNoTelepon(), user.getPassword());
        service.withdraw(request).enqueue(new Callback<WithdrawResponseJson>() {
            @Override
            public void onResponse(Call<WithdrawResponseJson> call, Response<WithdrawResponseJson> response) {
                progresshide();
                if (response.isSuccessful()) {
                    if (response.body().getMessage().equalsIgnoreCase("success")) {
                        Intent intent = new Intent(WithdrawActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();

                        Notif notif = new Notif();
                        notif.title = "Withdraw";
                        notif.message = "Withdrawal requests have been successful, we will send a notification after we have sent funds to your account";
                        sendNotif(user.getToken(), notif);

                    } else {
                        notif("error, please check your account data!");
                    }
                } else {
                    notif("error!");
                }
            }

            @Override
            public void onFailure(Call<WithdrawResponseJson> call, Throwable t) {
                progresshide();
                t.printStackTrace();
                notif("error");
            }
        });
    }

    public void onBackPressed() {
        if (disableback.equals("true")) {
            return;
        } else {
            finish();
        }
    }

    public void notif(String text) {
        rlnotif.setVisibility(View.VISIBLE);
        notif.setText(text);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                rlnotif.setVisibility(View.GONE);
            }
        }, 3000);
    }

    public void progressshow() {
        rlprogress.setVisibility(View.VISIBLE);
        disableback = "true";
    }

    public void progresshide() {
        rlprogress.setVisibility(View.GONE);
        disableback = "false";
    }

    private void sendNotif(final String regIDTujuan, final Notif notif) {

        final FCMMessage message = new FCMMessage();
        message.setTo(regIDTujuan);
        message.setData(notif);

        FCMHelper.sendMessage(Constants.FCM_KEY, message).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
            }

            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }


}
