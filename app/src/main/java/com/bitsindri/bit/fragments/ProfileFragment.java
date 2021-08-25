package com.bitsindri.bit.fragments;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.net.Uri;
import android.widget.Toast;

import com.bitsindri.bit.R;

import com.bitsindri.bit.ViewModel.ProfileSharedPreferencesViewModel;
import com.bitsindri.bit.databinding.FragmentProfileBinding;
import com.bitsindri.bit.methods.Constants;
import com.bitsindri.bit.methods.Methods;
import com.bitsindri.bit.models.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private LinearLayout socialMediaContainer;
    private ProfileSharedPreferencesViewModel viewModel;

    public ProfileFragment() {
        // Required empty public constructor
    }

    private int REQUEST_CODE = 18;
    private CircleImageView normalProfileImage;
    private int shortAnimationDuration = 400;
    private FrameLayout profileFragContainer;
    private View mediumProfileViewer;
    private View fullSizeProfileViewer;
    private Animator currentAnimator;
    private ImageView mediumExpandedImage;
    private ImageView fullSizeImage;
    private TextView headerUserName;
    private ImageView showProfileEditContainer;
    private View profileEditContainer;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
                binding = FragmentProfileBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(ProfileSharedPreferencesViewModel.class);

        // assign everything with user model here

        FrameLayout view = binding.profileContainer;
        User user = viewModel.getUser().getValue();
        assert user != null;
        binding.profileUserName.setText(user.getName());
        binding.profileUserBranch.setText(user.getBranch());
        binding.profileUserSession.setText(user.getBatch());

        binding.profileAbout.setText(user.getAbout());
        binding.profileEmail.setText(user.getEmail());
        binding.profileRollNumber.setText(user.getRollNo());
        binding.profileRegNumber.setText(user.getRegNo());
        binding.profileDob.setText(user.getDob());
        binding.profileClub.setText(user.getClub());

        viewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {

            }
        });
//        socialMediaContainer = view.findViewById(R.id.social_media_container);
        socialMediaContainer = binding.socialMediaContainer;
        socialMediaContainer.bringToFront();
//        showProfileEditContainer = view.findViewById(R.id.edit_profile_icon);
        showProfileEditContainer = binding.editProfileIcon;

        /* normal profile image show when fragment is started and very first time appear */
//        normalProfileImage = view.findViewById(R.id.profile_image);
        normalProfileImage = binding.profileImage;
        normalProfileImage.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.pop_up));
//        headerUserName = view.findViewById(R.id.profile_user_name);
        headerUserName = binding.profileUserName;
//        profileFragContainer = view.findViewById(R.id.profileContainer);
        profileFragContainer = binding.profileContainer;

        /* medium profile viewer show the profile pic on half of the screen */
        mediumProfileViewer = view.findViewById(R.id.profile_viewer_container);
        mediumExpandedImage = mediumProfileViewer.findViewById(R.id.expanded_profile_image);
        mediumExpandedImage.setImageDrawable(normalProfileImage.getDrawable());
        TextView userNameInMediumProfileViewer = mediumProfileViewer.findViewById(R.id.expanded_user_name);
        userNameInMediumProfileViewer.setText(headerUserName.getText());

        /* full size profile show the profile pic on full screen and also get the edit button
         * for resetting the profile picture.
         */
        fullSizeProfileViewer = view.findViewById(R.id.full_size_profile_viewer);
        fullSizeImage = fullSizeProfileViewer.findViewById(R.id.full_profile_image);
        fullSizeImage.setImageDrawable(normalProfileImage.getDrawable());
        TextView userNameInFullSizeProfileViewer = fullSizeProfileViewer.findViewById(R.id.full_size_user_name);
        userNameInFullSizeProfileViewer.setText(headerUserName.getText());

        /* Initialising the profile edit Container */
        profileEditContainer = view.findViewById(R.id.profile_edit_container);
        /* When user click on normal profile image the medium profile image will show
         * medium profile covers only half of the screen.
         * again clicking in the image set the profile pic on full screen .
         */
        normalProfileImage.setOnClickListener(v -> {
            /* Methods.showtoToggle() method simply zoom the view with an zooming animation
             * this methods take 1st argument is a view which indicates the view which has to be zoomed.
             * 2nd method take the view after the 1st view is zoomed their zoomed position will show.
             * 3rd parameter simply takes the container of the activity.
             */
            Methods.showtoToggle(v, mediumProfileViewer, profileFragContainer);
        });

        /* When user clicked the image view when medium profile image is opened
         * profile image will zoom to full size and get the profile image edit
         * button option.
         */
        mediumExpandedImage.setOnClickListener(v -> {
            mediumProfileViewer.setVisibility(View.INVISIBLE);
            normalProfileImage.setAlpha(1f);
            fullSizeProfileViewer.setVisibility(View.VISIBLE);
            fullSizeProfileViewer.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));
        });

        /* When user clicked when image is on full size the full size profile will get closed */
        fullSizeProfileViewer.setOnClickListener(v -> {
            /* Methods.closeView() methods simply hide the current view
             * This methods takes the view which has to be hide and the context
             */
            Methods.closeView(v, getContext());
        });

        showProfileEditContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods.showtoToggle(v,profileEditContainer,profileFragContainer);
            }
        });

        ImageView canceprofileEditButton = profileEditContainer.findViewById(R.id.canel_profile_edit);
        canceprofileEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfileEditContainer.setAlpha(1f);
                Methods.closeView(profileEditContainer,getContext());
            }
        });
        /* the imageview button editprofile pic will give user to set profile image or reset the profile pic */
        ImageView editProfilePic = fullSizeProfileViewer.findViewById(R.id.edit_profile_image);
        editProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProfilePic();
            }
        });
        return binding.getRoot();
    }

    private void setProfilePic() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getData() != null) {
            Uri imageToBeUpload = data.getData();
            normalProfileImage.setImageURI(imageToBeUpload);
            mediumExpandedImage.setImageURI(imageToBeUpload);
            fullSizeImage.setImageURI(imageToBeUpload);
        }
    }

}