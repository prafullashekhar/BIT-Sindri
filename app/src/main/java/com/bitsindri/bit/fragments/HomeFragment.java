package com.bitsindri.bit.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitsindri.bit.MainActivity;
import com.bitsindri.bit.R;

import com.bitsindri.bit.Adapter.SliderAdapter;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.bitsindri.bit.activity.HomeDepartmentActivity;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    SliderView sliderView;

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

        sliderView =view.findViewById(R.id.image_slider);

        List<String> images = new ArrayList<>();
        images.add("https://images.ctfassets.net/hrltx12pl8hq/4plHDVeTkWuFMihxQnzBSb/aea2f06d675c3d710d095306e377382f/shutterstock_554314555_copy.jpg");
        images.add("https://thumbs.dreamstime.com/z/environment-earth-day-hands-trees-growing-seedlings-bokeh-green-background-female-hand-holding-tree-nature-field-gra-130247647.jpg");
        images.add("https://www.oberlo.com/media/1603969791-image-1.jpg?fit=max&fm=webp&w=1824");

        SliderAdapter sliderAdapter = new SliderAdapter(getContext(), images);

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