package com.bitsindri.bit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.bitsindri.bit.ViewModel.ProfileSharedPreferencesViewModel;
import com.bitsindri.bit.databinding.ActivityMainBinding;
import com.bitsindri.bit.fragments.ClubsFragment;
import com.bitsindri.bit.fragments.HomeFragment;
import com.bitsindri.bit.fragments.NotificationsFragment;
import com.bitsindri.bit.fragments.ProfileFragment;
import com.bitsindri.bit.fragments.FragmentClickListener;
import com.bitsindri.bit.fragments.SearchFragment;
import com.bitsindri.bit.methods.Constants;
import com.bitsindri.bit.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements FragmentClickListener {

    ActivityMainBinding binding;
    private Fragment fragment;
    public static ProfileSharedPreferencesViewModel viewModel;
    private View profileState = null;

    @Override
    public void onBackPressed() {
        if(binding.bottomNavigation.getSelectedItemId()==R.id.nav_home){
            finishAffinity();
        }
        else if(profileState != null){
            profileState.performClick();
            profileState = null;
        }
        else {
            binding.bottomNavigation.setVisibility(View.VISIBLE);
            binding.bottomNavigation.setSelectedItemId(R.id.nav_home);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // getting intent from custom tab
//        if(getIntent().getData() != null){
//            Log.e(Constants.msg, "HELLO HELLO");
//            String url = getIntent().getDataString();
//            Toast.makeText(MainActivity.this, url, Toast.LENGTH_SHORT).show();
//        }

                // initiating view model for all the fragments associated with main activity
        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(ProfileSharedPreferencesViewModel.class);
        User user  = viewModel.getUser().getValue();

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

        binding.bottomNavigation.setSelectedItemId(R.id.nav_home);

    }

    @Override
    public void setProfileFragmentState(View view) {
        this.profileState = view;
        if(profileState!=null){
            binding.bottomNavigation.setVisibility(View.GONE);
        }
        else binding.bottomNavigation.setVisibility(View.VISIBLE);
    }

    @Override
    public void setFragment(int id) {
        binding.bottomNavigation.setSelectedItemId(id);
    }

    @Override
    public void setScrollListener(int scrollY, int oldScrollY ) {
        if(scrollY - oldScrollY > 0 )binding.bottomNavigation.setVisibility(View.GONE);
        else binding.bottomNavigation.setVisibility(View.VISIBLE);
    }


}