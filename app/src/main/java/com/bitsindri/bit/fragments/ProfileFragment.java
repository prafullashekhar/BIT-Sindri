package com.bitsindri.bit.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bitsindri.bit.R;

public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }
    private LinearLayout socialMediaContainer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        socialMediaContainer = view.findViewById(R.id.social_media_container);
        socialMediaContainer.bringToFront();
        return view;
    }
}