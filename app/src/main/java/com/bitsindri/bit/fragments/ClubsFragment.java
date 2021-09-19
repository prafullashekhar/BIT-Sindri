package com.bitsindri.bit.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bitsindri.bit.Adapter.ClubAdapter;
import com.bitsindri.bit.R;
import com.bitsindri.bit.ViewModel.ProfileSharedPreferencesViewModel;
import com.bitsindri.bit.databinding.FragmentClubsBinding;
import com.bitsindri.bit.methods.Constants;
import com.bitsindri.bit.models.Club;

import java.util.ArrayList;

public class ClubsFragment extends Fragment implements Toolbar.OnMenuItemClickListener{

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

        binding = FragmentClubsBinding.inflate(inflater, container, false);
        allClub = new ArrayList<>();
        adapter = new ClubAdapter(getContext());
        binding.rvAllClub.setAdapter(adapter);
        binding.rvAllClub.setLayoutManager(new LinearLayoutManager(requireContext()));
        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())).get(ProfileSharedPreferencesViewModel.class);
        viewModel.clubs.observe(getViewLifecycleOwner(),result ->{
            try {
                switch (result.getStatus()){
                    case LOADING:
                        binding.progressBar.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        binding.progressBar.setVisibility(View.GONE);
                        adapter.updateClubList((ArrayList<Club>) result.getData());
                }
            }
            catch (Exception e){
                Log.e("Nipun",e.getMessage());
            }
        });
        binding.clubToolbar.setOnMenuItemClickListener(this);
        return binding.getRoot();
    }

    // implementing search in toolbar -------------------------------------------------------------------
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.club_search) {
            SearchView searchView = (SearchView) item.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.getFilter().filter(newText);
                    return false;
                }
            });
        }
        return false;
    }
}