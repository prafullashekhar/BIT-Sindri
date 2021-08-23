package com.bitsindri.bit.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.bitsindri.bit.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }
    private LinearLayout socialMediaContainer;
    private CircleImageView profileImage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        socialMediaContainer = view.findViewById(R.id.social_media_container);
        socialMediaContainer.bringToFront();
        profileImage = view.findViewById(R.id.profile_image);
        profileImage.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.pop_up));
        return view;
    }
}