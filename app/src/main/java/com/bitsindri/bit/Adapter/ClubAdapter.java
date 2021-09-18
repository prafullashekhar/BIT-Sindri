package com.bitsindri.bit.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bitsindri.bit.R;
import com.bitsindri.bit.methods.Constants;
import com.bitsindri.bit.models.Club;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.ViewHolder> {
    private ArrayList<Club> clubs;
    private Context context;
    public ClubAdapter(Context context,ArrayList<Club> clubs) {
        this.context = context;
        this.clubs = clubs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_club,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            Club club = clubs.get(position);
            holder.clubName.setText(club.getClubName());
            holder.clubDesc.setText(club.getClubDescription());
        }
        catch (Exception e){
            Log.e("Nipun",e.getMessage().toString());
        }
//        try {
//            Picasso.get().load(club.getClubLogoUrl()).placeholder(R.drawable.ic_icon_user).into(holder.clubLogo);
//        }
//        catch (Exception e){
//            Log.e(Constants.msg,e.getMessage().toString());
//        }
    }

    @Override
    public int getItemCount() {
        try {
            return clubs.size();
        }
        catch (Exception e){
            Log.e("Nipun",e.getMessage().toString());
            return 0;
        }
    }

//    public void updateClubList(ArrayList<Club> clubs){
//        this.clubs = clubs;
//    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        TextView clubName,clubDesc;
//        AppCompatButton visitProfile;
        CircleImageView clubLogo,clubNotification;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            clubName = itemView.findViewById(R.id.club_name);
            clubDesc = itemView.findViewById(R.id.club_desc);
//            visitProfile = itemView.findViewById(R.id.club_visit_profile);
//            clubLogo = itemView.findViewById(R.id.club_profile);
//            clubNotification = itemView.findViewById(R.id.club_notification);
        }
    }
}
