package com.bitsindri.bit.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bitsindri.bit.Adapter.ClubAdapter;
import com.bitsindri.bit.R;
import com.bitsindri.bit.ViewModel.ProfileSharedPreferencesViewModel;
import com.bitsindri.bit.databinding.FragmentClubsBinding;
import com.bitsindri.bit.methods.Constants;
import com.bitsindri.bit.models.Club;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class ClubsFragment extends Fragment {

    private ArrayList<Club> allClub;
    private FragmentClubsBinding binding;
    private ProfileSharedPreferencesViewModel viewModel;
    private ClubAdapter adapter;

    public ClubsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//
        binding = FragmentClubsBinding.inflate(inflater, container, false);
        allClub = new ArrayList<>();
        adapter = new ClubAdapter(getContext());
        binding.rvAllClub.setAdapter(adapter);
        binding.rvAllClub.setLayoutManager(new LinearLayoutManager(requireContext()));
        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(ProfileSharedPreferencesViewModel.class);
        viewModel.getClubs().observe(getViewLifecycleOwner(), clubs -> {
            allClub = (ArrayList<Club>)clubs;
            adapter.updateClubList(allClub);
        });
        return binding.getRoot();
    }

}