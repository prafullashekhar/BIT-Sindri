package com.bitsindri.bit.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.bitsindri.bit.ViewModel.ProfileSharedPreferencesViewModel;
import com.bitsindri.bit.databinding.FragmentHomeBinding;
import com.bitsindri.bit.models.SlidingImgUrl;
import com.bitsindri.bit.models.User;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.bitsindri.bit.activity.HomeDepartmentActivity;
import com.squareup.picasso.Picasso;

import java.util.List;


public class HomeFragment extends Fragment
{
    private FragmentHomeBinding binding;

    private ImgUrlViewModel imgUrlViewModel;
    private User currentUser;
    SliderAdapter sliderAdapter;
    private ProfileSharedPreferencesViewModel viewModel;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    CardView departments;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // initiating view model
        imgUrlViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(ImgUrlViewModel.class);
        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(ProfileSharedPreferencesViewModel.class);
        // assign everything with user model here
        currentUser = viewModel.getUser().getValue();
        assert currentUser != null;

        binding.userNameHomeFragment.setText(currentUser.getName());
        if(!currentUser.getProfilePic().equals("")){
            Picasso.get().load(currentUser.getProfilePic()).placeholder(R.drawable.test_pic).into(binding.homeProfileImage);
        }
        // sliding image list
        binding.imageSlider.setAutoCycle(true);
        binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.SLIDE);
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);

        sliderAdapter = new SliderAdapter(getContext(), imgUrlViewModel.getAllImgUrl().getValue());
        binding.imageSlider.setSliderAdapter(sliderAdapter);

        imgUrlViewModel.getAllImgUrl().observe(getViewLifecycleOwner(), new Observer<List<SlidingImgUrl>>() {
            @Override
            public void onChanged(List<SlidingImgUrl> list) {
                sliderAdapter.renewItems(list);
            }
        });

      
        // Inflate the layout for this fragment
        binding.homeDepartments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(), HomeDepartmentActivity.class));
            }
        });
        return binding.getRoot();

    }

}