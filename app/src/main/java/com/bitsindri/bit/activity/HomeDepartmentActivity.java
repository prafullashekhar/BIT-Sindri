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

    private NavigationView departmentDrawer;
    private LinearLayout mainContainer;
    private boolean isFirst = true;
    private boolean isDrawerOpened = false;
    private String[] screenTitles;

    Toolbar toolbar;
    Fragment fragment;
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        departmentDrawer = findViewById(R.id.drawer_left_menu);
        mainContainer = findViewById(R.id.department_main_container);
        screenTitles = getResources().getStringArray(R.array.full_name_branch_list);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDrawerOpened) {
                    closeDrawer();
                } else {
                    departmentDrawer.setVisibility(View.VISIBLE);
                    mainContainer.setAnimation(AnimationUtils.loadAnimation(HomeDepartmentActivity.this, R.anim.left_to_right));
                    departmentDrawer.setAnimation(AnimationUtils.loadAnimation(HomeDepartmentActivity.this, R.anim.left_to_right_menu));
                    isDrawerOpened = true;
                }

            }
        });
        departmentDrawer.setNavigationItemSelectedListener(this);
        fragment = new MechanicalFragment();
        showFragment(fragment);
        mainContainer.setOnClickListener(new View.OnClickListener() {
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
        mainContainer.setAnimation(AnimationUtils.loadAnimation(HomeDepartmentActivity.this, R.anim.right_to_left));
        departmentDrawer.setAnimation(AnimationUtils.loadAnimation(HomeDepartmentActivity.this, R.anim.right_to_left_menu));
        isDrawerOpened = false;
        departmentDrawer.setVisibility(View.GONE);
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