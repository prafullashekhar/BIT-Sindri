package com.bitsindri.bit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.bitsindri.bit.databinding.ActivityMainBinding;
import com.bitsindri.bit.fragments.ClubsFragment;
import com.bitsindri.bit.fragments.HomeFragment;
import com.bitsindri.bit.fragments.NotificationsFragment;
import com.bitsindri.bit.fragments.ProfileFragment;
import com.bitsindri.bit.fragments.FragmentClickListener;
import com.bitsindri.bit.fragments.SearchFragment;
import com.bitsindri.bit.fragments.viewprofile.ClubProfile;
import com.bitsindri.bit.methods.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements FragmentClickListener {

    ActivityMainBinding binding;
    private Fragment fragment;
//    public static ProfileSharedPreferencesViewModel viewModel;
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
        else if(Constants.isInsideFragment){
            binding.bottomNavigation.setSelectedItemId(binding.bottomNavigation.getSelectedItemId());
            Constants.isInsideFragment = false;
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

        // initiating view model for all the fragments associated with main activity -----------------------------------------------
//        viewModel = new ViewModelProvider(this,
//                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(ProfileSharedPreferencesViewModel.class);
//        viewModel.getUser().observe(this, new Observer<User>() {
//            @Override
//            public void onChanged(User user) {
//
//            }
//        });


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
                    Constants.isInsideFragment = false;
                }

                return true;
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.bottom_fragment_container, new HomeFragment()).commit();

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

    @Override
    public void fragmentBackPressedListener() {
        binding.bottomNavigation.setSelectedItemId(binding.bottomNavigation.getSelectedItemId());
        Constants.isInsideFragment = false;
    }
}