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
//        try {
//
//            binding = FragmentClubsBinding.inflate(inflater, container, false);
//            adapter = new ClubAdapter(getContext());
//            allClub = new ArrayList<>();
//            Club club = new Club();
//            club.setClubName(getContext().getResources().getString(R.string.hncc));
//            club.setClubDescription(getContext().getResources().getString(R.string.hncc_desc));
//            allClub.add(club);
//            adapter.updateClubList(allClub);
////        viewModel = new ViewModelProvider(this,
////                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(ProfileSharedPreferencesViewModel.class);
////        viewModel.getClubs().observe(getViewLifecycleOwner(), new Observer<List<Club>>() {
////            @Override
////            public void onChanged(List<Club> clubs) {
////                allClub = (ArrayList<Club>) clubs;
////                adapter.updateClubList(allClub);
////            }
////        });
//
//            binding.rvAllClub.setAdapter(adapter);
//            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
//            try {
//                binding.rvAllClub.setLayoutManager(linearLayoutManager);
//            } catch (Exception e) {
//                Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//
//            return binding.getRoot();
//        }
//        catch (Exception e){
//            Log.e("Nipun",e.getMessage().toString());
//            return null;
//        }
        View view = inflater.inflate(R.layout.fragment_clubs,container,false);
        allClub = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.rvAllClub);
        Club club = new Club();
        club.setClubName(getContext().getResources().getString(R.string.hncc));
        club.setClubDescription(getContext().getResources().getString(R.string.hncc_desc));
        allClub.add(club);
        ClubAdapter adapter =  new ClubAdapter(getContext(),allClub);
//        adapter.updateClubList(allClub);

        LinearLayoutManager linearLayoutManager;
            linearLayoutManager = new LinearLayoutManager(getContext());
            try {
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(linearLayoutManager);
            }
            catch (Exception e){
                Log.e("Nipun",e.getMessage().toString());
            }
        return view;
    }
}