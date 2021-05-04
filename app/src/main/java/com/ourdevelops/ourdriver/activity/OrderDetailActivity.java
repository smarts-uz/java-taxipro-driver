package com.ourdevelops.ourdriver.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.ornolfr.ratingview.RatingView;
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
import com.ourdevelops.ourdriver.constants.BaseApp;
import com.ourdevelops.ourdriver.constants.Constants;
import com.ourdevelops.ourdriver.gmap.directions.Directions;
import com.ourdevelops.ourdriver.gmap.directions.Route;
import com.ourdevelops.ourdriver.item.ItemPesananItem;
import com.ourdevelops.ourdriver.utils.api.MapDirectionAPI;
import com.ourdevelops.ourdriver.utils.api.ServiceGenerator;
import com.ourdevelops.ourdriver.utils.api.service.DriverService;
import com.ourdevelops.ourdriver.json.DetailRequestJson;
import com.ourdevelops.ourdriver.json.DetailTransResponseJson;
import com.ourdevelops.ourdriver.models.PelangganModel;
import com.ourdevelops.ourdriver.models.TransaksiModel;
import com.ourdevelops.ourdriver.models.User;
import com.ourdevelops.ourdriver.utils.Log;
import com.ourdevelops.ourdriver.utils.Utility;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.bottom_sheet)
    LinearLayout bottomsheet;
    @BindView(R.id.layanan)
    TextView layanan;
    @BindView(R.id.layanandes)
    TextView layanandesk;
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
    @BindView(R.id.namamerchant)
    TextView namamerchant;
    @BindView(R.id.order)
    Button orderButton;
    @BindView(R.id.rlprogress)
    RelativeLayout rlprogress;
    @BindView(R.id.textprogress)
    TextView textprogress;
    @BindView(R.id.phonenumber)
    ImageView phone;
    @BindView(R.id.chat)
    ImageView chat;
    @BindView(R.id.llchatmerchant)
    LinearLayout llchatmerchant;
    @BindView(R.id.lldestination)
    LinearLayout lldestination;
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

    @BindView(R.id.scroller)
    NestedScrollView scrollView;

    @BindView(R.id.back_btn)
    ImageView backbutton;
    @BindView(R.id.status)
    LinearLayout llrating;
    @BindView(R.id.ratingView)
    RatingView ratingView;
    @BindView(R.id.llbutton)
    LinearLayout llbutton;

    @BindView(R.id.cost)
    TextView cost;
    @BindView(R.id.deliveryfee)
    TextView deliveryfee;
    @BindView(R.id.orderdetail)
    LinearLayout llorderdetail;
    @BindView(R.id.merchantdetail)
    LinearLayout llmerchantdetail;
    @BindView(R.id.merchantinfo)
    LinearLayout llmerchantinfo;
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

    @BindView(R.id.merchantnear)
    RecyclerView rvmerchantnear;

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
    String idtrans, idpelanggan, response, fitur;
    ItemPesananItem itemPesananItem;
    TextView totaltext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        ButterKnife.bind(this);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomsheet);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setPeekHeight(300);
        llrating.setVisibility(View.VISIBLE);
        backbutton.setVisibility(View.VISIBLE);
        llbutton.setVisibility(View.GONE);
        llchat.setVisibility(View.GONE);
        llchatmerchant.setVisibility(View.GONE);
        totaltext = findViewById(R.id.totaltext);
        shimmerload();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rvmerchantnear.setHasFixedSize(true);
        rvmerchantnear.setNestedScrollingEnabled(false);
        rvmerchantnear.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        Intent intent = getIntent();
        idpelanggan = intent.getStringExtra("id_pelanggan");
        idtrans = intent.getStringExtra("id_transaksi");
        response = intent.getStringExtra("response");

        if (response.equals("2")) {
            llchat.setVisibility(View.VISIBLE);
            layanandesk.setText(getString(R.string.notification_accept));
        } else if (response.equals("3")) {
            llchat.setVisibility(View.VISIBLE);
            isCancelable = false;
            orderButton.setVisibility(View.GONE);
            layanandesk.setText(getString(R.string.notification_start));
        } else if (response.equals("4")) {
            scrollView.setPadding(0,0,0,10);
            isCancelable = false;
            llchat.setVisibility(View.GONE);
            orderButton.setVisibility(View.GONE);
            layanandesk.setText(getString(R.string.notification_finish));
        } else if (response.equals("5")) {
            scrollView.setPadding(0,0,0,10);
            isCancelable = false;
            llchat.setVisibility(View.GONE);
            orderButton.setVisibility(View.GONE);
            layanandesk.setText(getString(R.string.notification_cancel));
        }
    }

    private void getData(final String idtrans, final String idpelanggan) {
        User loginUser = BaseApp.getInstance(this).getLoginUser();
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
                    PelangganModel pelanggan = responsedata.body().getPelanggan().get(0);
                    pickUpLatLng = new LatLng(transaksi.getStartLatitude(), transaksi.getStartLongitude());
                    destinationLatLng = new LatLng(transaksi.getEndLatitude(), transaksi.getEndLongitude());

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
                        Utility.currencyTXT(deliveryfee, String.valueOf(transaksi.getHarga()), OrderDetailActivity.this);
                        Utility.currencyTXT(cost, String.valueOf(transaksi.getTotal_biaya()), OrderDetailActivity.this);
                        namamerchant.setText(transaksi.getNama_merchant());

                        itemPesananItem = new ItemPesananItem(OrderDetailActivity.this, responsedata.body().getItem(), R.layout.item_pesanan);
                        rvmerchantnear.setAdapter(itemPesananItem);
                    } else if (fitur.equalsIgnoreCase("5")) {
                        requestRoute();
                        lldetailsend.setVisibility(View.VISIBLE);
                        produk.setText(transaksi.getNamaBarang());
                        sendername.setText(transaksi.namaPengirim);
                        receivername.setText(transaksi.namaPenerima);

                        senderphone.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OrderDetailActivity.this, R.style.DialogStyle);
                                alertDialogBuilder.setTitle("Call Driver");
                                alertDialogBuilder.setMessage("You want to call " + transaksi.getNamaPengirim() + "(" + transaksi.teleponPengirim + ")?");
                                alertDialogBuilder.setPositiveButton("yes",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                if (ActivityCompat.checkSelfPermission(OrderDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                    ActivityCompat.requestPermissions(OrderDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_CALL);
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
                                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OrderDetailActivity.this, R.style.DialogStyle);
                                alertDialogBuilder.setTitle("Call Driver");
                                alertDialogBuilder.setMessage("You want to call " + transaksi.getNamaPenerima() + "(" + transaksi.teleponPenerima + ")?");
                                alertDialogBuilder.setPositiveButton("yes",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                if (ActivityCompat.checkSelfPermission(OrderDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                                    ActivityCompat.requestPermissions(OrderDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION_CALL);
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
        rlprogress.setVisibility(View.GONE);
        pickUpLatLng = new LatLng(request.getStartLatitude(), request.getStartLongitude());
        destinationLatLng = new LatLng(request.getEndLatitude(), request.getEndLongitude());

        Picasso.with(this)
                .load(Constants.IMAGESUSER + pelanggan.getFoto())
                .placeholder(R.drawable.image_placeholder)
                .into(foto);

        if (request.isPakaiWallet()) {
            totaltext.setText("Total (Wallet)");
        } else {
            totaltext.setText("Total (Cash)");
        }

        if (!request.getRate().isEmpty()) {
            ratingView.setRating(Float.parseFloat(request.getRate()));
        }

        layanan.setText(pelanggan.getFullnama());
        pickUpText.setText(request.getAlamatAsal());
        destinationText.setText(request.getAlamatTujuan());
        if (request.getOrderFitur().equalsIgnoreCase("10") || request.getOrderFitur().equalsIgnoreCase("11")
                || request.getOrderFitur().equalsIgnoreCase("12") || request.getOrderFitur().equalsIgnoreCase("13")) {
            double totalbiaya = Double.parseDouble(request.getTotal_biaya());
            Utility.currencyTXT(priceText, String.valueOf(request.getHarga()+ totalbiaya), this);
        } else {
            Utility.currencyTXT(priceText, String.valueOf(request.getHarga()), this);
        }

    }

    private void updateLastLocation(boolean move) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_LOCATION);
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
                final long distance = MapDirectionAPI.getDistance(OrderDetailActivity.this, json);
                final String time = MapDirectionAPI.getTimeDistance(OrderDetailActivity.this, json);
                if (distance >= 0) {
                    runOnUiThread(new Runnable() {
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
        Directions directions = new Directions(this);
        try {
            List<Route> routes = directions.parse(json);

            if (directionLine != null) directionLine.remove();
            if (routes.size() > 0) {
                directionLine = gMap.addPolyline((new PolylineOptions())
                        .addAll(routes.get(0).getOverviewPolyLine())
                        .color(ContextCompat.getColor(this, R.color.colorgradient))
                        .width(8));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onConnected(@androidx.annotation.Nullable Bundle bundle) {

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
                            this, R.raw.style_json));

            if (!success) {
                android.util.Log.e("", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            android.util.Log.e("", "Can't find style. Error: ", e);
        }
        isMapReady = true;
        getData(idtrans, idpelanggan);

    }
}
