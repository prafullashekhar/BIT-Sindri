package com.bitsindri.bit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.bitsindri.bit.ViewModel.ProfileSharedPreferencesViewModel;
import com.bitsindri.bit.databinding.ActivityMainBinding;
import com.bitsindri.bit.fragments.ClubsFragment;
import com.bitsindri.bit.fragments.HomeFragment;
import com.bitsindri.bit.fragments.NotificationsFragment;
import com.bitsindri.bit.fragments.ProfileFragment;
import com.bitsindri.bit.fragments.SearchFragment;
import com.bitsindri.bit.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity  {

    ActivityMainBinding binding;
    private Fragment fragment;
    public static ProfileSharedPreferencesViewModel viewModel;

    @Override
    public void onBackPressed() {
        if(binding.bottomNavigation.getSelectedItemId()==R.id.nav_home){
            finishAffinity();
        }
        else {
            binding.bottomNavigation.setSelectedItemId(R.id.nav_home);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // initiating view model for all the fragments associated with main activity
        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(ProfileSharedPreferencesViewModel.class);
        viewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {

            }
        });

        binding.bottomNavigation.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        fragment = new HomeFragment();
                        break;

                    case R.id.nav_search:
                        fragment = new SearchFragment();
                        break;

                    case R.id.nav_clubs:
                        fragment = new ClubsFragment();
                        break;

                    case R.id.nav_notifications:
                        fragment = new NotificationsFragment();
                        break;

                    case R.id.nav_profile:
                        fragment = new ProfileFragment();
                        break;
                }

                if(fragment != null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.bottom_fragment_container, fragment).commit();
                }

                return true;
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.bottom_fragment_container, new HomeFragment()).commit();
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        viewModel = new ViewModelProvider(this,
//                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(ProfileSharedPreferencesViewModel.class);
//    }
}