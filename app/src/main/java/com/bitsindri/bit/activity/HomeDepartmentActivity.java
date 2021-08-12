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

import com.bitsindri.bit.R;
import com.bitsindri.bit.custommenu.DrawerAdapter;
import com.bitsindri.bit.custommenu.DrawerItem;
import com.bitsindri.bit.custommenu.SimpleItem;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;

public class HomeDepartmentActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener{
    private static final int POS_CSE = 0;
    private static final int POS_IT = 1;
    private static final int POS_EE = 2;
    private static final int POS_ME = 3;
    private static final int POS_ECE = 4;

    private String[] screenTitles;
    private Drawable[] screenIcons;

    private SlidingRootNav slidingRootNav;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withDragDistance(40)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();
        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_CSE).setChecked(true),
                createItemFor(POS_IT),
                createItemFor(POS_EE),
                createItemFor(POS_ME),

                createItemFor(POS_ECE)));
        adapter.setListener(this);

        RecyclerView menuList = findViewById(R.id.menu_list);
        menuList.setNestedScrollingEnabled(false);
        menuList.setLayoutManager(new LinearLayoutManager(this));
        menuList.setAdapter(adapter);

        adapter.setSelected(POS_CSE);
        toolbar.setTitle(screenTitles[POS_CSE]);
    }
    @Override
    public void onItemSelected(int position) {
        slidingRootNav.closeMenu();
        toolbar.setTitle(screenTitles[position]);
    }
    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.light_text_color))
                .withTextTint(color(R.color.light_text_color))
                .withSelectedIconTint(color(R.color.app_theme))
                .withSelectedTextTint(color(R.color.app_theme));
    }
    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.branch_list);
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