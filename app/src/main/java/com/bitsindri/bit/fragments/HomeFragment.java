package com.bitsindri.bit.fragments;

import static com.bitsindri.bit.methods.Methods.loadImage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitsindri.bit.R;

import com.bitsindri.bit.Adapter.SliderAdapter;
import com.bitsindri.bit.ViewModel.ProfileSharedPreferencesViewModel;
import com.bitsindri.bit.databinding.FragmentHomeBinding;
import com.bitsindri.bit.methods.Constants;
import com.bitsindri.bit.models.Club;
import com.bitsindri.bit.models.SlidingImgUrl;
import com.bitsindri.bit.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.bitsindri.bit.activity.HomeDepartmentActivity;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment
{
    private FragmentHomeBinding binding;

    private FirebaseRemoteConfig remoteConfig;
    SliderAdapter sliderAdapter;
    private ProfileSharedPreferencesViewModel viewModel;

    CardView departments;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build();
        remoteConfig.setConfigSettingsAsync(configSettings);
        remoteConfig.setDefaultsAsync(R.xml.sliding_img_url_default);
        remoteConfig.activate();

        // at last checks for updates of sliding pic
        getSlidingImageUrlFromRemote();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        FragmentClickListener listener = ((FragmentClickListener) getContext());

//-------------------------initiating sliding adapter  -----------------------------------------------------------------
        binding.imageSlider.setAutoCycle(true);
        binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.SLIDE);
        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderAdapter = new SliderAdapter(getContext());
        binding.imageSlider.setSliderAdapter(sliderAdapter);

        updateSlidingImageUrlFromFetchedData();

//---------------------- initialising view moder for user ------------------------------------------------------------------------------------
        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(ProfileSharedPreferencesViewModel.class);
        User currentUser = viewModel.getUser().getValue();
        assert currentUser != null;
        assignUserData(currentUser);

        viewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                assignUserData(user);
            }
        });


        // opening profile fragment on click on profile picture in home fragment
        binding.homeProfileImage.setOnClickListener(v -> {
            if(listener!=null)
            listener.setFragment(R.id.nav_profile);
        });

        //------------------ handling click on cards in home fragment --------------------------------------------
        binding.homeDepartments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireContext().startActivity(new Intent(getContext(), HomeDepartmentActivity.class));
            }
        });
        return binding.getRoot();
    }

    // function to assign user data
    private void assignUserData(User currentUser){
        binding.userNameHomeFragment.setText(currentUser.getName());
        loadImage(binding.homeProfileImage,currentUser.getProfilePic());
    }

    // function to get and set url data for config to sliding images
    private void getSlidingImageUrlFromRemote(){

        remoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if(task.isSuccessful()){
                    updateSlidingImageUrlFromFetchedData();
                }
            }
        });
    }

    // function to set the url data from config
    private void updateSlidingImageUrlFromFetchedData(){
        String url = remoteConfig.getString("Home_sliding_image_url");
        List<SlidingImgUrl> mSlidingImgUrlList = new ArrayList<>();

        for(String val: url.split(" ")){
            SlidingImgUrl s = new SlidingImgUrl(val);
            mSlidingImgUrlList.add(s);
        }

        sliderAdapter.renewItems(mSlidingImgUrlList);
    }

}