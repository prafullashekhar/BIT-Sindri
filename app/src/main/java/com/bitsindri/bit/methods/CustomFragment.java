package com.bitsindri.bit.methods;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public abstract class CustomFragment {
    public CustomFragment() {
    }

    public CustomFragment(int layoutId) {
        this.layoutId = layoutId;
    }

    private Context context;
    protected ViewGroup viewGroup;
    protected LayoutInflater inflater;
    private int layoutId;
    protected Fragment parent;

    protected View onCreateView(ViewGroup viewGroup){
        View view = inflater.inflate(layoutId,viewGroup);
        return view;
    }
    protected void onViewCreated(View view){

    }

    public void startCustomFragment(ViewGroup viewGroup,LayoutInflater inflater,Fragment parent){
        this.context = viewGroup.getContext();
        this.viewGroup = viewGroup;
        this.inflater = inflater;
        this.parent = parent;
        viewGroup.removeAllViewsInLayout();
        onViewCreated(onCreateView(viewGroup));
    }

    public Context getContext() {
        return context;
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

}
