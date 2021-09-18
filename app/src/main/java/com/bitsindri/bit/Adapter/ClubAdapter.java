package com.bitsindri.bit.Adapter;

import static com.bitsindri.bit.methods.Methods.loadImage;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bitsindri.bit.R;
import com.bitsindri.bit.methods.Methods;
import com.bitsindri.bit.models.Club;

import java.util.ArrayList;

public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.ViewHolder> {
    private ArrayList<Club> clubs;
    private Context context;
    private int lastPos = -1;
    private boolean onAttach = true;

    public ClubAdapter(Context context) {
        this.context = context;
        clubs = new ArrayList<>();
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
    }



    @Override
    public int getItemCount() {
        return clubs.size();
    }

    public void updateClubList(ArrayList<Club> clubs) {
        this.clubs.clear();
        this.clubs = clubs;
        notifyDataSetChanged();
    }

    private void setAnimation(View view, int position) {
        if (position > lastPos) {
            Methods.animateFadeIn(view, onAttach ? position : -1);
            lastPos = position;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
}
