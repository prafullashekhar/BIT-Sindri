package com.bitsindri.bit.fragments;

import android.view.View;

public interface FragmentClickListener {
    void setProfileFragmentState(View view);
    void setFragment(int id);
    void setScrollListener(int scrollY,int oldScrollY);
}
