package com.bitsindri.bit.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitsindri.bit.R;

import com.bitsindri.bit.Adapter.SliderAdapter;
import com.bitsindri.bit.ViewModel.ImgUrlViewModel;
import com.bitsindri.bit.models.SlidingImgUrl;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.bitsindri.bit.activity.HomeDepartmentActivity;

import java.util.List;


public class HomeFragment extends Fragment
{
    private ImgUrlViewModel imgUrlViewModel;
    SliderView sliderView;
    SliderAdapter sliderAdapter;

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

        // initiating view model
        imgUrlViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(ImgUrlViewModel.class);

        // sliding image list
        sliderView = view.findViewById(R.id.image_slider);
        sliderView.setAutoCycle(true);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.SLIDE);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);

        sliderAdapter = new SliderAdapter(getContext(), imgUrlViewModel.getAllImgUrl().getValue());
        sliderView.setSliderAdapter(sliderAdapter);

        imgUrlViewModel.getAllImgUrl().observe(getViewLifecycleOwner(), new Observer<List<SlidingImgUrl>>() {
            @Override
            public void onChanged(List<SlidingImgUrl> list) {
                sliderAdapter.renewItems(list);
            }
        });

      
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