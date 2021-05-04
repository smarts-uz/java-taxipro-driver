package com.ourdevelops.ourdriver.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.ourdevelops.ourdriver.R;
import com.ourdevelops.ourdriver.activity.ChatActivity;
import com.ourdevelops.ourdriver.activity.MainActivity;
import com.ourdevelops.ourdriver.activity.OrderDetailActivity;
import com.ourdevelops.ourdriver.constants.BaseApp;
import com.ourdevelops.ourdriver.constants.Constants;
import com.ourdevelops.ourdriver.gmap.directions.Directions;
import com.ourdevelops.ourdriver.gmap.directions.Route;
import com.ourdevelops.ourdriver.item.ItemPesananItem;
import com.ourdevelops.ourdriver.json.AcceptRequestJson;
import com.ourdevelops.ourdriver.json.AcceptResponseJson;
import com.ourdevelops.ourdriver.json.ResponseJson;
import com.ourdevelops.ourdriver.json.VerifyRequestJson;
import com.ourdevelops.ourdriver.utils.api.FCMHelper;
import com.ourdevelops.ourdriver.utils.api.MapDirectionAPI;
import com.ourdevelops.ourdriver.utils.api.ServiceGenerator;
import com.ourdevelops.ourdriver.utils.api.service.DriverService;
import com.ourdevelops.ourdriver.json.DetailRequestJson;
import com.ourdevelops.ourdriver.json.DetailTransResponseJson;
import com.ourdevelops.ourdriver.json.fcm.FCMMessage;
import com.ourdevelops.ourdriver.models.OrderFCM;
import com.ourdevelops.ourdriver.models.PelangganModel;
import com.ourdevelops.ourdriver.models.TransaksiModel;
import com.ourdevelops.ourdriver.models.User;
import com.ourdevelops.ourdriver.utils.Log;
import com.ourdevelops.ourdriver.utils.Utility;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class OrderFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "Home";
    View getView;
    Context context;
    private GoogleMap gMap;
    private boolean isMapReady = false;
    private Location lastKnownLocation;
    private static final int REQUEST_PERMISSION_LOCATION = 991;
    private static final int REQUEST_PERMISSION_CALL = 992;
    private GoogleApiClient googleApiClient;
    private LatLng pickUpLatLng;
    private LatLng destinationLatLng;
    private Polyline directionLine;
    private Marker pickUpMarker;
    private Marker destinationMarker;
    private boolean isCancelable = true;
    String idtrans, idpelanggan, response, fitur, onsubmit;

    @BindView(R.id.bottom_sheet)
    LinearLayout bottomsheet;
    @BindView(R.id.layanan)
    TextView layanan;
    @BindView(R.id.layanandes)
    TextView layanandesk;
    @BindView(R.id.verifycation)
    TextView verify;
    @BindView(R.id.namamerchant)
    TextView namamerchant;
    @BindView(R.id.llchat)
    LinearLayout llchat;
    @BindView(R.id.background)
    CircleImageView foto;
    @BindView(R.id.pickUpText)
    TextView pickUpText;
    @BindView(R.id.destinationText)
    TextView destinationText;
    @BindView(R.id.fitur)
    TextView fiturtext;
    @BindView(R.id.distance)
    TextView distanceText;
    @BindView(R.id.price)
    TextView priceText;
    @BindView(R.id.rlprogress)
    RelativeLayout rlprogress;
    @BindView(R.id.textprogress)
    TextView textprogress;
    @BindView(R.id.cost)
    TextView cost;
    @BindView(R.id.deliveryfee)
    TextView deliveryfee;
    @BindView(R.id.phonenumber)
    ImageView phone;

    @BindView(R.id.chat)
    ImageView chat;
    @BindView(R.id.phonemerchant)
    ImageView phonemerchant;
    @BindView(R.id.chatmerchant)
    ImageView chatmerchant;
    @BindView(R.id.lldestination)
    LinearLayout lldestination;
    @BindView(R.id.orderdetail)
    LinearLayout llorderdetail;
    @BindView(R.id.lldistance)
    LinearLayout lldistance;
    @BindView(R.id.senddetail)
    LinearLayout lldetailsend;
    @BindView(R.id.produk)
    TextView produk;
    @BindView(R.id.sendername)
    TextView sendername;
    @BindView(R.id.receivername)
    TextView receivername;
    @BindView(R.id.senderphone)
    Button senderphone;
    @BindView(R.id.receiverphone)
    Button receiverphone;
    @BindView(R.id.shimmerlayanan)
    ShimmerFrameLayout shimmerlayanan;
    @BindView(R.id.shimmerpickup)
    ShimmerFrameLayout shimmerpickup;
    @BindView(R.id.shimmerdestination)
    ShimmerFrameLayout shimmerdestination;
    @BindView(R.id.shimmerfitur)
    ShimmerFrameLayout shimmerfitur;
    @BindView(R.id.shimmerdistance)
    ShimmerFrameLayout shimmerdistance;
    @BindView(R.id.shimmerprice)
    ShimmerFrameLayout shimmerprice;
    @BindView(R.id.order)
    Button submit;
    @BindView(R.id.merchantdetail)
    LinearLayout llmerchantdetail;
    @BindView(R.id.merchantinfo)
    LinearLayout llmerchantinfo;
    @BindView(R.id.llbutton)
    LinearLayout llbutton;
    @BindView(R.id.merchantnear)
    RecyclerView rvmerchantnear;
    ItemPesananItem itemPesananItem;
    TextView totaltext;

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getView = inflater.inflate(R.layout.activity_detail_order, container, false);
        context = getContext();
        ButterKnife.bind(this, getView);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomsheet);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        totaltext = getView.findViewById(R.id.totaltext);

        Bundle bundle = getArguments();
        if (bundle != null) {
            idpelanggan = bundle.getString("id_pelanggan");
            idtrans = bundle.getString("id_transaksi");
            response = bundle.getString("response");
        }
        shimmerload();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


        if (response.equals("2")) {
            onsubmit = "2";
            llchat.setVisibility(View.VISIBLE);
        } else if (response.equals("3")) {
            onsubmit = "3";
            llchat.setVisibility(View.VISIBLE);
            isCancelable = false;
            submit.setVisibility(View.VISIBLE);
            verify.setVisibility(View.GONE);
            submit.setText("finish");
        } else if (response.equals("4")) {
            isCancelable = false;
            llchat.setVisibility(View.GONE);
            submit.setVisibility(View.GONE);
            layanandesk.setText(getString(R.string.notification_finish));
        } else if (response.equals("5")) {
            isCancelable = false;
            llchat.setVisibility(View.GONE);
            layanandesk.setText(getString(R.string.notification_cancel));
        }
        rvmerchantnear.setHasFixedSize(true);
        rvmerchantnear.setNestedScrollingEnabled(false);
        rvmerchantnear.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        rlprogress.setVisibility(View.GONE);
        textprogress.setText(getString(R.string.waiting_pleaseWait));
        return getView;
    }

    private void getData(final String idtrans, final String idpelanggan) {
        User loginUser = BaseApp.getInstance(context).getLoginUser();
        DriverService service = ServiceGenerator.createService(DriverService.class, loginUser.getEmail(), loginUser.getPassword());
        DetailRequestJson param = new DetailRequestJson();
        param.setId(idtrans);
        param.setIdPelanggan(idpelanggan);
        service.detailtrans(param).enqueue(new Callback<DetailTransResponseJson>() {
            @Override
            public void onResponse(Call<DetailTransResponseJson> call, Response<DetailTransResponseJson> responsedata) {
                if (responsedata.isSuccessful()) {
                    shimmertutup();
                    Log.e("", String.valueOf(responsedata.body().getData().get(0)));
                    final TransaksiModel transaksi = responsedata.body().getData().get(0);
                    final PelangganModel pelanggan = responsedata.body().getPelanggan().get(0);

                    if (transaksi.isPakaiWallet()) {
                        totaltext.setText("Total (Wallet)");
                    } else {
                        totaltext.setText("Total (Cash)");
                    }

                    if (onsubmit.equals("2")) {
                        if (transaksi.getOrderFitur().equalsIgnoreCase("10") || transaksi.getOrderFitur().equalsIgnoreCase("11")
                                || transaksi.getOrderFitur().equalsIgnoreCase("12") || transaksi.getOrderFitur().equalsIgnoreCase("13")) {
                            layanandesk.setText("Go buy orders");
                            submit.setText("deliver orders");
                            verify.setVisibility(View.VISIBLE);
                        } else {
                            layanandesk.setText(getString(R.string.notification_accept));
                        }
                        submit.setVisibility(View.VISIBLE);
                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (transaksi.getOrderFitur().equalsIgnoreCase("10") || transaksi.getOrderFitur().equalsIgnoreCase("11")
                                        || transaksi.getOrderFitur().equalsIgnoreCase("12") || transaksi.getOrderFitur().equalsIgnoreCase("13")) {

                                    if (verify.getText().toString().isEmpty()) {
                                        Toast.makeText(context, "Please enter verify code!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        SimpleDateFormat timeFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
                                        String finalDate = timeFormat.format(transaksi.getWaktuOrder());
                                        rlprogress.setVisibility(View.VISIBLE);
                                        verify(verify.getText().toString(), pelanggan, transaksi.getToken_merchant(), transaksi.idtransmerchant, finalDate);
                                    }
                                } else {
                                    start(pelanggan, transaksi.getToken_merchant(), transaksi.idtransmerchant, String.valueOf(transaksi.getWaktuOrder()));
                                }

                            }
                        });
                    } else if (onsubmit.equals("3")) {
                        if (transaksi.getOrderFitur().equalsIgnoreCase("10") || transaksi.getOrderFitur().equalsIgnoreCase("11")
                                || transaksi.getOrderFitur().equalsIgnoreCase("12") || transaksi.getOrderFitur().equalsIgnoreCase("13")) {
                            layanandesk.setText("deliver orders");
                        } else {
                            layanandesk.setText(getString(R.string.notification_start));
                        }

                        verify.setVisibility(View.GONE);
                        submit.setText("Finish");
                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish(pelanggan, transaksi.token_merchant);
                            }
                        });
                    }

                    fitur = transaksi.getOrderFitur();

                    if (fitur.equalsIgnoreCase("6")) {
                        lldestination.setVisibility(View.GONE);
                        lldistance.setVisibility(View.GONE);
                        fiturtext.setText(transaksi.getEstimasi());
                    } else if (fitur.equalsIgnoreCase("10") || fitur.equalsIgnoreCase("11")
                            || fitur.equalsIgnoreCase("12") || fitur.equalsIgnoreCase("13")) {
                        llorderdetail.setVisibility(View.VISIBLE);
                        llmerchantdetail.setVisibility(View.VISIBLE);
                        llmerchantinfo.setVisibility(View.VISIBLE);
                        Utility.currencyTXT(deliveryfee, String.valueOf(transaksi.getHarga()), context);
                        Utility.currencyTXT(cost, String.valueOf(transaksi.getTotal_biaya()), context);
                        namamerchant.setText(transaksi.getNama_merchant());
                        itemPesananItem = new ItemPesananItem(context, responsedata.body().getItem(), R.layout.item_pesanan);
                        rvmerchantnear.setAdapter(itemPesananItem);

                        phonemerchant.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.DialogStyle);
                                alertDialogBuilder.setTitle("Call Customer");
                                alertDialogBuilder.setMessage("You want to call Merchant (+" + transaksi.getTeleponmerchant() + ")?");
                                alertDialogBuilder.setPositiveButton("yes",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_CALL);
                                                    return;
                                                }

                                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                                callIntent.setData(Uri.parse("tel:+" + transaksi.getTeleponmerchant()));
                                                startActivity(callIntent);
                                            }
                                        });

                                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();


                            }
                        });

                        chatmerchant.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, ChatActivity.class);
                                intent.putExtra("senderid", loginUser.getId());
                                intent.putExtra("receiverid", transaksi.getId_merchant());
                                intent.putExtra("tokendriver", loginUser.getToken());
                                intent.putExtra("tokenku", transaksi.getToken_merchant());
                                intent.putExtra("name", transaksi.getNama_merchant());
                                intent.putExtra("pic", Constants.IMAGESMERCHANT + transaksi.getFoto_merchant());
                                startActivity(intent);
                            }
                        });

                    } else if (fitur.equalsIgnoreCase("5")) {
                        requestRoute();
                        lldetailsend.setVisibility(View.VISIBLE);
                        produk.setText(transaksi.getNamaBarang());
                        sendername.setText(transaksi.namaPengirim);
                        receivername.setText(transaksi.namaPenerima);

                        senderphone.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.DialogStyle);
                                alertDialogBuilder.setTitle("Call Driver");
                                alertDialogBuilder.setMessage("You want to call " + transaksi.getNamaPengirim() + "(" + transaksi.teleponPengirim + ")?");
                                alertDialogBuilder.setPositiveButton("yes",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_CALL);
                                                    return;
                                                }

                                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                                callIntent.setData(Uri.parse("tel:" + transaksi.teleponPengirim));
                                                startActivity(callIntent);
                                            }
                                        });

                                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();


                            }
                        });

                        receiverphone.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.DialogStyle);
                                alertDialogBuilder.setTitle("Call Driver");
                                alertDialogBuilder.setMessage("You want to call " + transaksi.getNamaPenerima() + "(" + transaksi.teleponPenerima + ")?");
                                alertDialogBuilder.setPositiveButton("yes",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_CALL);
                                                    return;
                                                }

                                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                                callIntent.setData(Uri.parse("tel:" + transaksi.teleponPenerima));
                                                startActivity(callIntent);
                                            }
                                        });

                                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();


                            }
                        });

                    }
                    pickUpLatLng = new LatLng(transaksi.getStartLatitude(), transaksi.getStartLongitude());
                    destinationLatLng = new LatLng(transaksi.getEndLatitude(), transaksi.getEndLongitude());
                    if (pickUpMarker != null) pickUpMarker.remove();
                    pickUpMarker = gMap.addMarker(new MarkerOptions()
                            .position(pickUpLatLng)
                            .title("Pick Up")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pickup)));


                    if (destinationMarker != null) destinationMarker.remove();
                    destinationMarker = gMap.addMarker(new MarkerOptions()
                            .position(destinationLatLng)
                            .title("Destination")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.destination)));
                    updateLastLocation(true);
                    parsedata(transaksi, pelanggan);


                }
            }

            @Override
            public void onFailure(retrofit2.Call<DetailTransResponseJson> call, Throwable t) {

            }
        });


    }

    private void shimmerload() {
        shimmerlayanan.startShimmerAnimation();
        shimmerpickup.startShimmerAnimation();
        shimmerdestination.startShimmerAnimation();
        shimmerfitur.startShimmerAnimation();
        shimmerdistance.startShimmerAnimation();
        shimmerprice.startShimmerAnimation();

        layanan.setVisibility(View.GONE);
        layanandesk.setVisibility(View.GONE);
        pickUpText.setVisibility(View.GONE);
        destinationText.setVisibility(View.GONE);
        fiturtext.setVisibility(View.GONE);
        priceText.setVisibility(View.GONE);
    }

    private void shimmertutup() {
        shimmerlayanan.stopShimmerAnimation();
        shimmerpickup.stopShimmerAnimation();
        shimmerdestination.stopShimmerAnimation();
        shimmerfitur.stopShimmerAnimation();
        shimmerdistance.stopShimmerAnimation();
        shimmerprice.stopShimmerAnimation();

        shimmerlayanan.setVisibility(View.GONE);
        shimmerpickup.setVisibility(View.GONE);
        shimmerdestination.setVisibility(View.GONE);
        shimmerfitur.setVisibility(View.GONE);
        shimmerdistance.setVisibility(View.GONE);
        shimmerprice.setVisibility(View.GONE);

        layanan.setVisibility(View.VISIBLE);
        layanandesk.setVisibility(View.VISIBLE);
        pickUpText.setVisibility(View.VISIBLE);
        destinationText.setVisibility(View.VISIBLE);
        distanceText.setVisibility(View.VISIBLE);
        fiturtext.setVisibility(View.VISIBLE);
        priceText.setVisibility(View.VISIBLE);
    }

    private void parsedata(TransaksiModel request, final PelangganModel pelanggan) {
        requestRoute();
        final User loginUser = BaseApp.getInstance(context).getLoginUser();
        rlprogress.setVisibility(View.GONE);
        pickUpLatLng = new LatLng(request.getStartLatitude(), request.getStartLongitude());
        destinationLatLng = new LatLng(request.getEndLatitude(), request.getEndLongitude());

        Picasso.with(context)
                .load(Constants.IMAGESUSER + pelanggan.getFoto())
                .placeholder(R.drawable.image_placeholder)
                .into(foto);


        layanan.setText(pelanggan.getFullnama());
        pickUpText.setText(request.getAlamatAsal());
        destinationText.setText(request.getAlamatTujuan());
        if (request.getOrderFitur().equalsIgnoreCase("10") || request.getOrderFitur().equalsIgnoreCase("11")
                || request.getOrderFitur().equalsIgnoreCase("12") || request.getOrderFitur().equalsIgnoreCase("13")) {
            double totalbiaya = Double.parseDouble(request.getTotal_biaya());
            Utility.currencyTXT(priceText, String.valueOf(request.getHarga() + totalbiaya), context);
        } else {
            Utility.currencyTXT(priceText, String.valueOf(request.getHarga()), context);
        }

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.DialogStyle);
                alertDialogBuilder.setTitle("Call Customer");
                alertDialogBuilder.setMessage("You want to call Costumer (+" + pelanggan.getNoTelepon() + ")?");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_CALL);
                                    return;
                                }

                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:+" + pelanggan.getNoTelepon()));
                                startActivity(callIntent);
                            }
                        });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("senderid", loginUser.getId());
                intent.putExtra("receiverid", pelanggan.getId());
                intent.putExtra("tokendriver", loginUser.getToken());
                intent.putExtra("tokenku", pelanggan.getToken());
                intent.putExtra("name", pelanggan.getFullnama());
                intent.putExtra("pic", Constants.IMAGESUSER + pelanggan.getFoto());
                startActivity(intent);
            }
        });
    }

    private void updateLastLocation(boolean move) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_LOCATION);
            return;
        }
        lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);
        gMap.setMyLocationEnabled(true);

        if (pickUpLatLng != null) {
            if (move) {
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        pickUpLatLng, 15f)
                );

                gMap.animateCamera(CameraUpdateFactory.zoomTo(15f));
            }
        }
    }

    private okhttp3.Callback updateRouteCallback = new okhttp3.Callback() {
        @Override
        public void onFailure(okhttp3.Call call, IOException e) {

        }

        @Override
        public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
            if (response.isSuccessful()) {
                final String json = response.body().string();
                final long distance = MapDirectionAPI.getDistance(context, json);
                final String time = MapDirectionAPI.getTimeDistance(context, json);
                if (distance >= 0) {
                    if (getActivity() == null)
                        return;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateLineDestination(json);
                            float km = ((float) (distance)) / 1000f;
                            String format = String.format(Locale.US, "%.1f", km);
                            distanceText.setText(format);
                            fiturtext.setText(time);
                        }
                    });
                }
            }
        }
    };

    private void requestRoute() {
        if (pickUpLatLng != null && destinationLatLng != null) {
            MapDirectionAPI.getDirection(pickUpLatLng, destinationLatLng).enqueue(updateRouteCallback);
        }
    }

    private void updateLineDestination(String json) {
        Directions directions = new Directions(context);
        try {
            List<Route> routes = directions.parse(json);

            if (directionLine != null) directionLine.remove();
            if (routes.size() > 0) {
                directionLine = gMap.addPolyline((new PolylineOptions())
                        .addAll(routes.get(0).getOverviewPolyLine())
                        .color(ContextCompat.getColor(context, R.color.colorgradient))
                        .width(8));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.getUiSettings().setMyLocationButtonEnabled(true);
        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            context, R.raw.style_json));

            if (!success) {
                android.util.Log.e("", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            android.util.Log.e("", "Can't find style. Error: ", e);
        }
        isMapReady = true;
        getData(idtrans, idpelanggan);
    }

    private void start(final PelangganModel pelanggan, String tokenmerchant, String idtransmerchant, String waktuorder) {
        rlprogress.setVisibility(View.VISIBLE);
        final User loginUser = BaseApp.getInstance(context).getLoginUser();
        DriverService userService = ServiceGenerator.createService(
                DriverService.class, loginUser.getNoTelepon(), loginUser.getPassword());
        AcceptRequestJson param = new AcceptRequestJson();
        param.setId(loginUser.getId());
        param.setIdtrans(idtrans);
        userService.startrequest(param).enqueue(new Callback<AcceptResponseJson>() {
            @Override
            public void onResponse(Call<AcceptResponseJson> call, final Response<AcceptResponseJson> response) {
                if (response.isSuccessful()) {

                    if (response.body().getMessage().equalsIgnoreCase("berhasil")) {
                        rlprogress.setVisibility(View.GONE);
                        onsubmit = "3";
                        getData(idtrans, idpelanggan);
                        OrderFCM orderfcm = new OrderFCM();
                        orderfcm.id_driver = loginUser.getId();
                        orderfcm.id_transaksi = idtrans;
                        orderfcm.response = "3";
                        if (fitur.equalsIgnoreCase("10") || fitur.equalsIgnoreCase("11")
                                || fitur.equalsIgnoreCase("12") || fitur.equalsIgnoreCase("13")) {
                            orderfcm.id_pelanggan = idpelanggan;
                            orderfcm.invoice = "INV-" + idtrans + idtransmerchant;
                            orderfcm.ordertime = waktuorder;
                            orderfcm.desc = "driver delivers the order";
                            sendMessageToDriver(tokenmerchant, orderfcm);
                        } else {
                            orderfcm.desc = getString(R.string.notification_start);
                        }
                        sendMessageToDriver(pelanggan.getToken(), orderfcm);
                    } else {
                        rlprogress.setVisibility(View.GONE);
                        Intent i = new Intent(context, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        Toast.makeText(context, "Order is no longer available!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AcceptResponseJson> call, Throwable t) {
                Toast.makeText(context, "Error Connection!", Toast.LENGTH_SHORT).show();
                rlprogress.setVisibility(View.GONE);
            }
        });
    }

    private void verify(String verificode, final PelangganModel pelanggan, String tokenmerchant, String idtransmerchant, String waktuorder) {
        rlprogress.setVisibility(View.VISIBLE);
        final User loginUser = BaseApp.getInstance(context).getLoginUser();
        DriverService userService = ServiceGenerator.createService(
                DriverService.class, loginUser.getNoTelepon(), loginUser.getPassword());
        VerifyRequestJson param = new VerifyRequestJson();
        param.setId(loginUser.getNoTelepon());
        param.setIdtrans(idtrans);
        param.setVerifycode(verificode);
        userService.verifycode(param).enqueue(new Callback<ResponseJson>() {
            @Override
            public void onResponse(Call<ResponseJson> call, final Response<ResponseJson> response) {
                if (response.isSuccessful()) {

                    if (response.body().getMessage().equalsIgnoreCase("success")) {

                        start(pelanggan, tokenmerchant, idtransmerchant, waktuorder);
                    } else {
                        rlprogress.setVisibility(View.GONE);
                        Toast.makeText(context, "verifycode not correct!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseJson> call, Throwable t) {
                Toast.makeText(context, "Error Connection!", Toast.LENGTH_SHORT).show();
                rlprogress.setVisibility(View.GONE);
            }
        });
    }

    private void finish(final PelangganModel pelanggan, String tokenmerchant) {
        rlprogress.setVisibility(View.VISIBLE);
        final User loginUser = BaseApp.getInstance(context).getLoginUser();
        DriverService userService = ServiceGenerator.createService(
                DriverService.class, loginUser.getNoTelepon(), loginUser.getPassword());
        AcceptRequestJson param = new AcceptRequestJson();
        param.setId(loginUser.getId());
        param.setIdtrans(idtrans);
        userService.finishrequest(param).enqueue(new Callback<AcceptResponseJson>() {
            @Override
            public void onResponse(Call<AcceptResponseJson> call, final Response<AcceptResponseJson> response) {
                if (response.isSuccessful()) {

                    if (response.body().getMessage().equalsIgnoreCase("berhasil")) {
                        rlprogress.setVisibility(View.GONE);
                        Intent i = new Intent(context, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        OrderFCM orderfcm = new OrderFCM();
                        orderfcm.id_driver = loginUser.getId();
                        orderfcm.id_transaksi = idtrans;
                        orderfcm.response = "4";
                        orderfcm.desc = getString(R.string.notification_finish);
                        if (fitur.equalsIgnoreCase("10") || fitur.equalsIgnoreCase("11")
                                || fitur.equalsIgnoreCase("12") || fitur.equalsIgnoreCase("13")) {
                            sendMessageToDriver(tokenmerchant, orderfcm);
                        } else {
                            orderfcm.desc = getString(R.string.notification_start);
                        }

                        sendMessageToDriver(pelanggan.getToken(), orderfcm);
                    } else {
                        rlprogress.setVisibility(View.GONE);
                        Intent i = new Intent(context, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        Toast.makeText(context, "Order is no longer available!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AcceptResponseJson> call, Throwable t) {
                Toast.makeText(context, "Error Connection!", Toast.LENGTH_SHORT).show();
                rlprogress.setVisibility(View.GONE);
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
                android.util.Log.e("REQUEST TO DRIVER", message.getData().toString());
            }

            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }
}
