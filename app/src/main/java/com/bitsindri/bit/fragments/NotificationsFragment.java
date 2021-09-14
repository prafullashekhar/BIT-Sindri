package com.bitsindri.bit.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bitsindri.bit.R;
import com.bitsindri.bit.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    FragmentNotificationsBinding binding;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        initToolbar();

        return binding.getRoot();
    }

    private void initToolbar() {
        binding.toolbarNotification.setNavigationIcon(R.drawable.ic_drawer);
        binding.toolbarNotification.setTitle(null);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.bottom_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Toast.makeText(getContext(), "home", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "To be done", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}