package com.bitsindri.bit.Adapter;

import static com.bitsindri.bit.methods.Methods.loadImage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bitsindri.bit.R;
import com.bitsindri.bit.methods.Constants;
import com.bitsindri.bit.methods.Methods;
import com.bitsindri.bit.models.Club;

import java.util.ArrayList;
import java.util.List;

public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.ViewHolder> implements Filterable {
    private ArrayList<Club> clubs;
    private ArrayList<Club> backupClubs;
    private final Context context;
    private int lastPos = -1;
    private boolean onAttach = true;
    private OnItemClickListener listener;

    public ClubAdapter(Context context) {
        this.context = context;
        backupClubs = new ArrayList<>();
        clubs = new ArrayList<>(backupClubs);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_club, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Club club = clubs.get(position);
        holder.clubName.setText(club.getClubName());
        holder.clubDesc.setText(club.getClubDescription());
        loadImage(holder.clubLogo,club.getClubLogoUrl(),R.drawable.ic_icon_bottom_clubs);
        setAnimation(holder.itemView,position);
        holder.visitProfile.setOnClickListener(view ->{
            if(listener != null)listener.onItemClick(club);
        });
    }

    @Override
    public int getItemCount() {
        return clubs.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateClubList(ArrayList<Club> clubs_p) {
        this.clubs.clear();
        this.clubs = clubs_p;
        backupClubs = new ArrayList<>(clubs_p);
        notifyDataSetChanged();
    }

    private void setAnimation(View view, int position) {
        if (position > lastPos) {
            Methods.animateFadeIn(view, onAttach ? position : -1);
            lastPos = position;
        }
    }

    /*------------ implementing search --------------------------------------------------------------*/
    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter = new Filter() {

        // it is running on background thread
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Club> filteredList = new ArrayList<>();

            if(constraint.toString().isEmpty()){
                filteredList.addAll(backupClubs);
            }else{
                Log.e(Constants.msg, "Searching for ");
                for(Club cb : backupClubs){
                    if(cb.getClubName().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredList.add(cb);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clubs.clear();
            clubs.addAll((ArrayList<Club>) results.values);
            notifyDataSetChanged();
        }
    };

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView clubName, clubDesc;
        AppCompatButton visitProfile;
        ImageView clubLogo, clubNotification;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            clubName = itemView.findViewById(R.id.club_name);
            clubDesc = itemView.findViewById(R.id.club_desc);
            visitProfile = itemView.findViewById(R.id.club_visit_profile);
            clubLogo = itemView.findViewById(R.id.club_profile);
            clubNotification = itemView.findViewById(R.id.club_notification);

        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.listener = onItemClickListener;
    }
    public interface OnItemClickListener{
        void onItemClick(Club club);
    }
}
