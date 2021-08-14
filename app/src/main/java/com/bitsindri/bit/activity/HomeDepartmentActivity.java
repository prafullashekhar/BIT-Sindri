package com.bitsindri.bit.activity;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bitsindri.bit.R;
import com.bitsindri.bit.custommenu.DrawerAdapter;
import com.bitsindri.bit.custommenu.DrawerItem;
import com.bitsindri.bit.custommenu.SimpleItem;
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


import java.util.Arrays;

public class HomeDepartmentActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {
    private static final int POS_ME = 0;
    private static final int POS_EE = 1;
    private static final int POS_CSE = 2;
    private static final int POS_IT = 3;
    private static final int POS_ECE = 4;
    private static final int POS_PRODUCTION = 5;
    private static final int POS_METALLURGY = 6;
    private static final int POS_CHEMICAL = 7;
    private static final int POS_CIVIL = 8;
    private static final int POS_MINING = 9;

    private String[] screenTitles;
    private Drawable[] screenIcons;
    private View departmentDrawer;
    private LinearLayout mainContainer;
    private boolean isFirst = true;
    private boolean isDrawerOpened=false;
    private ImageView navHeaderImage;

    Toolbar toolbar;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        departmentDrawer = findViewById(R.id.drawer_left_menu);
        mainContainer = findViewById(R.id.department_main_container);
        navHeaderImage=departmentDrawer.findViewById(R.id.menu_header_Image);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDrawerOpened) {
                    closeDrawer();
                } else {
                    departmentDrawer.setVisibility(View.VISIBLE);
                    mainContainer.setAnimation(AnimationUtils.loadAnimation(HomeDepartmentActivity.this, R.anim.left_to_right));
                    departmentDrawer.setAnimation(AnimationUtils.loadAnimation(HomeDepartmentActivity.this, R.anim.left_to_right));
                    isDrawerOpened=true;
                }

            }
        });
        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();
        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_ME).setChecked(true),
                createItemFor(POS_EE),
                createItemFor(POS_CSE),
                createItemFor(POS_IT),
                createItemFor(POS_ECE),
                createItemFor(POS_PRODUCTION),
                createItemFor(POS_METALLURGY),
                createItemFor(POS_CHEMICAL),
                createItemFor(POS_CIVIL),
                createItemFor(POS_MINING)));
        adapter.setListener(this);

        RecyclerView menuList = findViewById(R.id.menu_list);
        menuList.setNestedScrollingEnabled(false);
        menuList.setLayoutManager(new LinearLayoutManager(this));
        menuList.setAdapter(adapter);

        adapter.setSelected(POS_ME);
        toolbar.setTitle(screenTitles[POS_ME]);
        fragment = new MechanicalFragment();
        showFragment(fragment);
        mainContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDrawerOpened)closeDrawer();
            }
        });
    }

    @Override
    public void onItemSelected(int position) {

        toolbar.setTitle(screenTitles[position]);
        switch (position) {
            case POS_ME:
                fragment = new MechanicalFragment();
                break;

            case POS_EE:
                fragment = new ElectricalFagment();
                break;

            case POS_CSE:
                fragment = new CseFagment();
                break;

            case POS_IT:
                fragment = new ItFragment();
                break;

            case POS_ECE:
                fragment = new EceFragment();
                break;

            case POS_PRODUCTION:
                fragment = new ProductionFragment();
                break;

            case POS_METALLURGY:
                fragment = new MetallurgyFragment();
                break;

            case POS_CHEMICAL:
                fragment = new ChemicalFragment();
                break;

            case POS_CIVIL:
                fragment = new CivilFragment();
                break;

            case POS_MINING:
                fragment = new MiningFragment();
                break;

            default:
                break;
        }
        if (!isFirst)
            closeDrawer();
        isFirst = false;
        showFragment(fragment);
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public void closeDrawer() {
        mainContainer.setAnimation(AnimationUtils.loadAnimation(HomeDepartmentActivity.this, R.anim.right_to_left));
        departmentDrawer.setAnimation(AnimationUtils.loadAnimation(HomeDepartmentActivity.this,R.anim.right_to_left_menu));
        isDrawerOpened=false;
        departmentDrawer.setVisibility(View.GONE);
    }

    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.light_text_color))
                .withTextTint(color(R.color.light_text_color))
                .withSelectedIconTint(color(R.color.app_theme))
                .withSelectedTextTint(color(R.color.app_theme));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.full_name_branch_list);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.branch_icon);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }
}