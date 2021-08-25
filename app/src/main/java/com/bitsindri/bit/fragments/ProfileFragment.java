package com.bitsindri.bit.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.bitsindri.bit.R;
import com.bitsindri.bit.ViewModel.ProfileSharedPreferencesViewModel;
import com.bitsindri.bit.databinding.FragmentProfileBinding;
import com.bitsindri.bit.models.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private LinearLayout socialMediaContainer;
    private CircleImageView profileImage;
    private ProfileSharedPreferencesViewModel viewModel;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(ProfileSharedPreferencesViewModel.class);

        User user = viewModel.getUser().getValue();

        // assign everything with user model here
        binding.profileUserName.setText(user.getName());
        binding.profileUserBranch.setText(user.getBranch());
        binding.profileUserSession.setText(user.getBatch());

        binding.profileAbout.setText(user.getAbout());
        binding.profileEmail.setText(user.getEmail());
        binding.profileRollNumber.setText(user.getRollNo());
        binding.profileRegNumber.setText(user.getRegNo());
        binding.profileDob.setText(user.getDob());
        binding.profileClub.setText(user.getClub());


        binding.socialMediaContainer.bringToFront();
        binding.profileImage.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.pop_up));
        

        return binding.getRoot();
    }
}