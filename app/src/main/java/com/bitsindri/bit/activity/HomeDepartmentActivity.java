package com.bitsindri.bit.activity;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bitsindri.bit.R;
import com.bitsindri.bit.databinding.ActivityDepartmentBinding;
import com.bitsindri.bit.department_fagments.ChemicalFragment;
import com.bitsindri.bit.department_fagments.CivilFragment;
import com.bitsindri.bit.department_fagments.CseFagment;
import com.bitsindri.bit.department_fagments.EceFragment;
import com.bitsindri.bit.department_fagments.ElectricalFagment;
import com.bitsindri.bit.department_fagments.ItFragment;
import com.bitsindri.bit.department_fagments.MechanicalFragment;
import com.bitsindri.bit.department_fagments.MetallurgyFragment;
import com.bitsindri.bit.department_fagments.MiningFragment;
import com.bitsindri.bit.department_fagments.ProductionFragment;
import com.google.android.material.navigation.NavigationView;


public class HomeDepartmentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityDepartmentBinding binding;

    private boolean isDrawerOpened = false;
    private String[] screenTitles;

    Toolbar toolbar;
    Fragment fragment;
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDepartmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        screenTitles = getResources().getStringArray(R.array.full_name_branch_list);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDrawerOpened) {
                    closeDrawer();
                } else {
                    binding.drawerLeftMenu.setVisibility(View.VISIBLE);
                    binding.departmentMainContainer.setAnimation(AnimationUtils.loadAnimation(HomeDepartmentActivity.this, R.anim.left_to_right));
                    binding.drawerLeftMenu.setAnimation(AnimationUtils.loadAnimation(HomeDepartmentActivity.this, R.anim.left_to_right_menu));
                    isDrawerOpened = true;
                }

            }
        });
        binding.drawerLeftMenu.setNavigationItemSelectedListener(this);
        fragment = new MechanicalFragment();
        showFragment(fragment);
        binding.departmentMainContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDrawerOpened) closeDrawer();
            }
        });
    }

    private void showFragment(Fragment fragment) {
        toolbar.setTitle(screenTitles[position]);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public void closeDrawer() {
        binding.departmentMainContainer.setAnimation(AnimationUtils.loadAnimation(HomeDepartmentActivity.this, R.anim.right_to_left));
        binding.drawerLeftMenu.setAnimation(AnimationUtils.loadAnimation(HomeDepartmentActivity.this, R.anim.right_to_left_menu));
        isDrawerOpened = false;
        binding.drawerLeftMenu.setVisibility(View.GONE);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mechanical:
                fragment = new MechanicalFragment();
                position = 0;
                break;
            case R.id.electrical:
                fragment = new ElectricalFagment();
                position = 1;
                break;
            case R.id.computer_science:
                fragment = new CseFagment();
                position = 2;
                break;
            case R.id.information_tech:
                fragment = new ItFragment();
                position = 3;
                break;
            case R.id.ECE:
                fragment = new EceFragment();
                position = 4;
                break;
            case R.id.production:
                fragment = new ProductionFragment();
                position = 5;
                break;
            case R.id.metallurgy:
                fragment = new MetallurgyFragment();
                position = 6;
                break;
            case R.id.chemical:
                fragment = new ChemicalFragment();
                position = 7;
                break;
            case R.id.civil:
                fragment = new CivilFragment();
                position = 8;
                break;
            case R.id.mining:
                fragment = new MiningFragment();
                position = 9;
                break;
            default:
                new MechanicalFragment();
        }
        showFragment(fragment);
        closeDrawer();
        return true;
    }
}