package com.bitsindri.bit.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bitsindri.bit.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.Holder> {

    private Context context;
    private List<String> imagesList;

    public SliderAdapter(Context context, List<String> list) {
        this.context = context;
        this.imagesList = list;
    }

    public void renewItems(List<String> sliderItems) {
        this.imagesList = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.imagesList.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(String sliderItemUrl) {
        this.imagesList.add(sliderItemUrl);
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
//        viewHolder.imageView.setImageResource(imagesList.get(position));
        Picasso.get().load(imagesList.get(position)).into(viewHolder.imageView);
    }

    @Override
    public int getCount() {
        return imagesList.size();
    }

    public class Holder extends SliderViewAdapter.ViewHolder {

        ImageView imageView;

        public Holder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);

        }
    }

}

