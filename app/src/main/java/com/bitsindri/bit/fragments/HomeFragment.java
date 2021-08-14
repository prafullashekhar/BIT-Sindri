package com.bitsindri.bit.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitsindri.bit.MainActivity;
import com.bitsindri.bit.R;

import com.bitsindri.bit.Adapter.SliderAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.bitsindri.bit.activity.HomeDepartmentActivity;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    SliderView sliderView;
    DatabaseReference SliderTopReference;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    CardView departments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);

        // initiating realtime database
        SliderTopReference = FirebaseDatabase.getInstance().getReference().child("SlidingImage").child("HomeTop");

        sliderView =view.findViewById(R.id.image_slider);

        // sliding image list
        List<String> images = new ArrayList<>();
        SliderAdapter sliderAdapter = new SliderAdapter(getContext());

        SliderTopReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    images.clear();
                    for(DataSnapshot dss : snapshot.getChildren()){
                        String imgUrl = dss.getValue(String.class);
                        images.add(imgUrl);
                    }

                    sliderAdapter.renewItems(images);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                sliderAdapter.renewItems(images);
            }
        });

        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setAutoCycle(true);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);

      
        // Inflate the layout for this fragment
        departments=view.findViewById(R.id.home_departments);
        departments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(), HomeDepartmentActivity.class));
            }
        });
        return view;

    }
}