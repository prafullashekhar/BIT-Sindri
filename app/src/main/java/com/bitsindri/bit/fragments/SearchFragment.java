package com.bitsindri.bit.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitsindri.bit.Adapter.UsersAdapter;
import com.bitsindri.bit.R;
import com.bitsindri.bit.ViewModel.ProfileSharedPreferencesViewModel;
import com.bitsindri.bit.databinding.FragmentSearchBinding;
import com.bitsindri.bit.models.User;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    public SearchFragment() {
        // Required empty public constructor
    }

    private FragmentSearchBinding binding;
    private ProfileSharedPreferencesViewModel viewModel;
    private ArrayList<User> users;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater,container,false);
        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().
                        getApplication())).get(ProfileSharedPreferencesViewModel.class);
        binding.progressBar.setVisibility(View.VISIBLE);
        users = viewModel.getAllUsers(binding.progressBar);
        UsersAdapter adapter = new UsersAdapter(users,getContext());
        binding.userList.setAdapter(adapter);
        binding.userList.setLayoutManager(new LinearLayoutManager(getContext()));
        return binding.getRoot();
    }
}