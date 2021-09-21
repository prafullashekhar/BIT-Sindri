package com.bitsindri.bit.fragments.viewprofile;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.bitsindri.bit.fragments.FragmentClickListener;
import com.bitsindri.bit.methods.Methods;

import com.bitsindri.bit.R;
import com.bitsindri.bit.ViewModel.ProfileSharedPreferencesViewModel;
import com.bitsindri.bit.databinding.ClubProfileFragBinding;
import com.bitsindri.bit.methods.CustomFragment;
import com.bitsindri.bit.models.Club;

public class ClubProfile extends CustomFragment {

    private Club club;
    private ClubProfileFragBinding binding;
    private FragmentClickListener listener;

    public ClubProfile(Club club) {
        this.club = club;
    }
    @Override
    protected View onCreateView( ViewGroup viewGroup) {
        binding = ClubProfileFragBinding.inflate(inflater,viewGroup,true);
        binding.getRoot().setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.fade_in));
        listener = (FragmentClickListener)getContext();
        binding.clubName.setText(club.getClubName());
        binding.clubShort.setText(club.getClubDescription());
        binding.clubDesc.setText(club.getClubDescription());
        Methods.loadImage(binding.clubProfilePic,club.getClubLogoUrl(),R.drawable.ic_icon_user);
        binding.clubProfileActionBar.setNavigationOnClickListener(v ->{
            if(listener != null)listener.fragmentBackPressedListener();
        });
        return binding.getRoot();
    }
}
