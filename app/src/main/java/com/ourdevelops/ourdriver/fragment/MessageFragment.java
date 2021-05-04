package com.ourdevelops.ourdriver.fragment;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ourdevelops.ourdriver.R;
import com.ourdevelops.ourdriver.activity.ChatActivity;
import com.ourdevelops.ourdriver.constants.Constants;
import com.ourdevelops.ourdriver.constants.Functions;
import com.ourdevelops.ourdriver.item.MessageItem;
import com.ourdevelops.ourdriver.models.MessageModels;

import java.util.ArrayList;
import java.util.Collections;


public class MessageFragment extends Fragment {


    View getView;
    Context context;

    RecyclerView inboxList;

    ArrayList<MessageModels> inboxArraylist;
    ShimmerFrameLayout shimmer;
    DatabaseReference rootRef;

    MessageItem inboxItem;

    boolean isviewCreated = false;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getView = inflater.inflate(R.layout.fragment_recycle, container, false);
        context = getContext();

        rootRef = FirebaseDatabase.getInstance().getReference();

        inboxList = getView.findViewById(R.id.inboxlist);
        shimmer = getView.findViewById(R.id.shimmerwallet);
        inboxArraylist = new ArrayList<>();
        inboxList = (RecyclerView) getView.findViewById(R.id.inboxlist);
        LinearLayoutManager layout = new LinearLayoutManager(context);
        inboxList.setLayoutManager(layout);
        inboxList.setHasFixedSize(false);
        inboxItem = new MessageItem(context, inboxArraylist, new MessageItem.OnItemClickListener() {
            @Override
            public void onItemClick(MessageModels item) {

                if (checkReadStoragepermission()) {
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("senderid", Constants.USERID);
                    intent.putExtra("receiverid", item.getId());
                    intent.putExtra("name", item.getName());
                    intent.putExtra("tokendriver", Constants.TOKEN);
                    intent.putExtra("tokenku", item.getTokenuser());
                    intent.putExtra("pic", item.getPicture());
                    getActivity().startActivity(intent);
                }


            }
        }, new MessageItem.OnLongItemClickListener() {
            @Override
            public void onLongItemClick(MessageModels item) {

            }
        });

        inboxList.setAdapter(inboxItem);


        getView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions.hideSoftKeyboard(getActivity());
            }
        });
        shimmershow();
        isviewCreated = true;
        return getView;
    }

    ValueEventListener valueEventListener;

    Query inboxQuery;

    private void shimmershow() {
        inboxList.setVisibility(View.GONE);
        shimmer.setVisibility(View.VISIBLE);
        shimmer.startShimmerAnimation();
    }

    private void shimmertutup() {

        inboxList.setVisibility(View.VISIBLE);
        shimmer.setVisibility(View.GONE);
        shimmer.stopShimmerAnimation();
    }

    @Override
    public void onStart() {
        super.onStart();
        inboxQuery = rootRef.child("Inbox").child(Constants.USERID).orderByChild("date");
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shimmertutup();
                inboxArraylist.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    MessageModels model = new MessageModels();
                    model.setId(ds.getKey());
                    model.setName(ds.child("name").getValue().toString());
                    model.setMessage(ds.child("msg").getValue().toString());
                    model.setTimestamp(ds.child("date").getValue().toString());
                    model.setStatus(ds.child("status").getValue().toString());
                    model.setPicture(ds.child("pic").getValue().toString());
                    model.setTokendriver(ds.child("tokendriver").getValue().toString());
                    model.setTokenuser(ds.child("tokenuser").getValue().toString());
                    inboxArraylist.add(model);
                }
                Collections.reverse(inboxArraylist);
                inboxItem.notifyDataSetChanged();

                if (inboxArraylist.isEmpty()) {
                    getView.findViewById(R.id.rlnodata).setVisibility(View.VISIBLE);
                } else {
                    getView.findViewById(R.id.rlnodata).setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        };


        inboxQuery.addValueEventListener(valueEventListener);


    }


    @Override
    public void onStop() {
        super.onStop();
        if (inboxQuery != null)
            inboxQuery.removeEventListener(valueEventListener);
    }

    private boolean checkReadStoragepermission() {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            try {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        Constants.permission_Read_data);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
        return false;
    }


}
