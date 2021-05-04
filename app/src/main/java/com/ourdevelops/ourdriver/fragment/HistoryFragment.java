package com.ourdevelops.ourdriver.fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.ourdevelops.ourdriver.R;
import com.ourdevelops.ourdriver.constants.BaseApp;
import com.ourdevelops.ourdriver.item.HistoryItem;
import com.ourdevelops.ourdriver.json.AllTransResponseJson;
import com.ourdevelops.ourdriver.json.DetailRequestJson;
import com.ourdevelops.ourdriver.models.User;
import com.ourdevelops.ourdriver.utils.api.ServiceGenerator;
import com.ourdevelops.ourdriver.utils.api.service.DriverService;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HistoryFragment extends Fragment {


    View getView;
    Context context;
    ShimmerFrameLayout shimmer;
    RecyclerView recycle;
    HistoryItem historyItem;
    RelativeLayout rlnodata;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getView = inflater.inflate(R.layout.fragment_recycle, container, false);
        context = getContext();
        shimmer = getView.findViewById(R.id.shimmerwallet);
        recycle = getView.findViewById(R.id.inboxlist);
        rlnodata = getView.findViewById(R.id.rlnodata);

        recycle.setHasFixedSize(true);
        recycle.setLayoutManager(new GridLayoutManager(context, 1));

        return getView;
    }

    private void shimmershow() {
        recycle.setVisibility(View.GONE);
        shimmer.setVisibility(View.VISIBLE);
        shimmer.startShimmerAnimation();
    }

    private void shimmertutup() {

        recycle.setVisibility(View.VISIBLE);
        shimmer.setVisibility(View.GONE);
        shimmer.stopShimmerAnimation();
    }

    private void getdatatrans() {
        shimmershow();
        User loginUser = BaseApp.getInstance(context).getLoginUser();
        DriverService driverService = ServiceGenerator.createService(
                DriverService.class, loginUser.getNoTelepon(), loginUser.getPassword());
        DetailRequestJson param = new DetailRequestJson();
        param.setId(loginUser.getId());
        driverService.history(param).enqueue(new Callback<AllTransResponseJson>() {
            @Override
            public void onResponse(Call<AllTransResponseJson> call, Response<AllTransResponseJson> response) {
                if (response.isSuccessful()) {
                    shimmertutup();
                    historyItem = new HistoryItem(context, response.body().getData(), R.layout.item_order);
                    recycle.setAdapter(historyItem);
                    if (response.body().getData().isEmpty()) {
                        recycle.setVisibility(View.GONE);
                        rlnodata.setVisibility(View.VISIBLE);
                    } else {
                        recycle.setVisibility(View.VISIBLE);
                        rlnodata.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<AllTransResponseJson> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getdatatrans();
    }
}
