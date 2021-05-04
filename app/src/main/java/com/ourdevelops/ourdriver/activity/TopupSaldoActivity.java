package com.ourdevelops.ourdriver.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.ourdevelops.ourdriver.R;
import com.ourdevelops.ourdriver.constants.BaseApp;
import com.ourdevelops.ourdriver.constants.Constants;
import com.ourdevelops.ourdriver.json.ResponseJson;
import com.ourdevelops.ourdriver.json.WithdrawRequestJson;
import com.ourdevelops.ourdriver.json.fcm.FCMMessage;
import com.ourdevelops.ourdriver.models.Notif;
import com.ourdevelops.ourdriver.models.User;
import com.ourdevelops.ourdriver.utils.SettingPreference;
import com.ourdevelops.ourdriver.utils.Utility;
import com.ourdevelops.ourdriver.utils.api.FCMHelper;
import com.ourdevelops.ourdriver.utils.api.ServiceGenerator;
import com.ourdevelops.ourdriver.utils.api.service.DriverService;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class TopupSaldoActivity extends AppCompatActivity {

    EditText nominal;
    ImageView text1, text2, text3, text4;
    RelativeLayout rlnotif, rlprogress;
    TextView textnotif;
    String disableback;
    LinearLayout banktransfer, creditcard, paypal;
    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    private View bottom_sheet;

    ImageView backBtn;
    public static final int PAYPAL_REQUEST_CODE = 123;
    private static PayPalConfiguration configpaypal;
    SettingPreference sp;
    private String paymentAmount;


    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup);
        bottom_sheet = findViewById(R.id.bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottom_sheet);
        sp = new SettingPreference(this);
        configpaypal = new PayPalConfiguration();
        if (sp.getSetting()[12].equals("1")) {
            configpaypal.environment(PayPalConfiguration.ENVIRONMENT_SANDBOX);
        } else {
            configpaypal.environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION);
        }
        configpaypal.clientId(sp.getSetting()[9]);

        Intent intent = new Intent(this, PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configpaypal);

        startService(intent);

        nominal = findViewById(R.id.saldo);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);
        rlnotif = findViewById(R.id.rlnotif);
        textnotif = findViewById(R.id.textnotif);
        rlprogress = findViewById(R.id.rlprogress);
        backBtn = findViewById(R.id.back_btn);
        banktransfer = findViewById(R.id.banktransfer);
        creditcard = findViewById(R.id.creditcard);
        paypal = findViewById(R.id.paypal);



        nominal.addTextChangedListener(Utility.currencyTW(nominal,this));

        if (sp.getSetting()[10].equals("1")) {
            paypal.setVisibility(View.VISIBLE);
        } else {
            paypal.setVisibility(View.GONE);
        }

        if (sp.getSetting()[11].equals("1")) {
            creditcard.setVisibility(View.VISIBLE);
        } else {
            creditcard.setVisibility(View.GONE);
        }

        banktransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nominal.getText().toString().isEmpty()) {
                    sheetlist();
                } else {
                    notif("nominal cant be empty!");
                }
            }
        });

        paypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nominal.getText().toString().isEmpty()) {
                    getPaypal();
                } else {
                    notif("nominal cant be empty!");
                }
            }
        });

        creditcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nominal.getText().toString().isEmpty()) {
                    Intent i = new Intent(TopupSaldoActivity.this, CreditcardActivity.class);
                    i.putExtra("price", convertAngka(nominal.getText().toString()));
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                } else {
                    notif("nominal cant be empty!");
                }
            }
        });

        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nominal.setText("1000");
            }
        });

        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nominal.setText("2000");
            }
        });

        text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nominal.setText("5000");
            }
        });

        text4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nominal.setText("10000");
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        disableback = "false";
    }


    public void notif(String text) {
        rlnotif.setVisibility(View.VISIBLE);
        textnotif.setText(text);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                rlnotif.setVisibility(View.GONE);
            }
        }, 3000);
    }


    @Override
    public void onBackPressed() {
        if (disableback.equals("true")) {
            return;
        } else {
            finish();
        }
    }

    public String convertAngka(String value) {
        String newValue = (((((value + "")
                .replaceAll("Rp", ""))
                .replaceAll(" ", ""))
                .replaceAll(",", ""))
                .replaceAll("[$.]", ""));
        return newValue;
    }

    private void sheetlist() {
        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        final View mDialog = getLayoutInflater().inflate(R.layout.sheet_list, null);
        ///////////
        mBottomSheetDialog = new BottomSheetDialog(this);
        mBottomSheetDialog.setContentView(mDialog);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        mBottomSheetDialog.show();
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBottomSheetDialog = null;
            }
        });
    }

    private void getPaypal() {
        Double Amount = Double.valueOf(convertAngka(nominal.getText().toString().replace(sp.getSetting()[4],"")));
        DecimalFormat formatter = new DecimalFormat("#,#########,##");
        paymentAmount = formatter.format(Amount);
        PayPalPayment payment = new PayPalPayment(new BigDecimal(paymentAmount),
                sp.getSetting()[13], "Topup "+getString(R.string.app_name),
                PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configpaypal);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.i("payment", paymentDetails);
                        submit();

                    } catch (JSONException e) {
                        Log.e("payment", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("payment", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("payment", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

    private void submit() {
        progressshow();
        paymentAmount = nominal.getText().toString();
        final User user = BaseApp.getInstance(this).getLoginUser();
        WithdrawRequestJson request = new WithdrawRequestJson();
        request.setId(user.getId());
        request.setBank("paypal");
        request.setName(user.getFullnama());
        request.setAmount(convertAngka(paymentAmount.replace(sp.getSetting()[4],"")));
        request.setCard("1234");
        request.setNotelepon(user.getNoTelepon());
        request.setEmail(user.getEmail());

        DriverService service = ServiceGenerator.createService(DriverService.class, user.getNoTelepon(), user.getPassword());
        service.topuppaypal(request).enqueue(new Callback<ResponseJson>() {
            @Override
            public void onResponse(Call<ResponseJson> call, Response<ResponseJson> response) {
                progresshide();
                if (response.isSuccessful()) {
                    if (response.body().getMessage().equalsIgnoreCase("success")) {
                        Intent i = new Intent(TopupSaldoActivity.this,MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);

                        Notif notif = new Notif();
                        notif.title = "Topup";
                        notif.message = "topup has been successful";
                        sendNotif(user.getToken(), notif);

                    } else {
                        notif("error, please check your account data!");
                    }
                } else {
                    notif("error!");
                }
            }

            @Override
            public void onFailure(Call<ResponseJson> call, Throwable t) {
                progresshide();
                t.printStackTrace();
                notif("error");
            }
        });
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
