package com.bitsindri.bit.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bitsindri.bit.R;
import com.bitsindri.bit.models.SlidingImgUrl;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.Holder> {

    private List<SlidingImgUrl> imagesList;

    public SliderAdapter(Context context) {
        imagesList = new ArrayList<>();
        this.imagesList = imagesList;
    }

    public void renewItems(List<SlidingImgUrl> sliderItems) {
        this.imagesList.clear();
        this.imagesList = sliderItems;
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slider_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder viewHolder, int position) {
        Picasso.get().load(imagesList.get(position).getImgUrl()).into(viewHolder.imageView);
    }

    @Override
    public int getCount() {
        return imagesList.size();
    }

    public static class Holder extends SliderViewAdapter.ViewHolder {

        ImageView imageView;

        public Holder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);

        }
    }

}

