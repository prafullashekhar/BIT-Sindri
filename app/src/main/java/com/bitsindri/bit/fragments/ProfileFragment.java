package com.bitsindri.bit.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.net.Uri;
import android.widget.Toast;

import com.bitsindri.bit.R;

import com.bitsindri.bit.ViewModel.ProfileSharedPreferencesViewModel;
import com.bitsindri.bit.databinding.FragmentProfileBinding;
import com.bitsindri.bit.methods.Methods;
import com.bitsindri.bit.models.User;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private LinearLayout socialMediaContainer;
    private ProfileSharedPreferencesViewModel viewModel;
    private BottomSheetBehavior mBehaviour;
    private BottomSheetDialog mBottomSheetDialog;
    private View bottom_sheet;

    public ProfileFragment() {
        // Required empty public constructor
    }

    private int REQUEST_CODE = 18;
    private CircleImageView normalProfileImage;
    private int shortAnimationDuration = 400;
    private CoordinatorLayout profileFragContainer;
    private View mediumProfileViewer;
    private View fullSizeProfileViewer;
    private ImageView mediumExpandedImage;
    private ImageView fullSizeImage;
    private TextView headerUserName;
    private ImageView showProfileEditContainer;
    private View profileEditContainer;
    private User currentUser;
    private LinearLayout selectImageFromProfile;
    private LinearLayout selectImageFromCamera;
    private LinearLayout showProfilePic;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(ProfileSharedPreferencesViewModel.class);

        // assign everything with user model here
        currentUser = viewModel.getUser().getValue();
        assert currentUser != null;
        bottom_sheet = binding.bottomSheet;
        try {
            mBehaviour = BottomSheetBehavior.from(binding.bottomSheet);
        } catch (Exception e) {
            Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        initialiseProfileViews(currentUser);
        try {
            Picasso.get().load(currentUser.getProfilePic()).placeholder(R.drawable.ic_icon_user).into(binding.profileImage);
        } catch (Exception e) {
            Toast.makeText(getContext(), "" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }

        viewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                initialiseProfileViews(user);
            }
        });
        progressBar = binding.progressBar;
        socialMediaContainer = binding.socialMediaContainer;
        socialMediaContainer.bringToFront();
        showProfileEditContainer = binding.editProfileIcon;

        /* normal profile image show when fragment is started and very first time appear */
        normalProfileImage = binding.profileImage;
        normalProfileImage.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.pop_up));
        headerUserName = binding.profileUserName;
        profileFragContainer = binding.profileContainer;

        /* medium profile viewer show the profile pic on half of the screen */
        mediumProfileViewer = binding.profileViewerContainer.getRoot();
        mediumExpandedImage = mediumProfileViewer.findViewById(R.id.expanded_profile_image);
        if (currentUser.getProfilePic().equals(""))
            mediumExpandedImage.setImageDrawable(normalProfileImage.getDrawable());
        else
            Picasso.get().load(currentUser.getProfilePic()).into(mediumExpandedImage);

        TextView userNameInMediumProfileViewer = mediumProfileViewer.findViewById(R.id.expanded_user_name);
        userNameInMediumProfileViewer.setText(headerUserName.getText());

        /* full size profile show the profile pic on full screen and also get the edit button
         * for resetting the profile picture.
         */
        fullSizeProfileViewer = binding.fullSizeProfileViewer.getRoot();
        fullSizeImage = fullSizeProfileViewer.findViewById(R.id.full_profile_image);
        if (currentUser.getProfilePic().equals(""))
            fullSizeImage.setImageDrawable(normalProfileImage.getDrawable());
        else Picasso.get().load(currentUser.getProfilePic()).into(fullSizeImage);
        TextView userNameInFullSizeProfileViewer = fullSizeProfileViewer.findViewById(R.id.full_size_user_name);
        userNameInFullSizeProfileViewer.setText(headerUserName.getText());

        /* Initialising the profile edit Container */
        profileEditContainer = binding.profileEditContainer.getRoot();
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
//            mediumProfileViewer.setVisibility(View.INVISIBLE);
            showBottomSheetDialog();
//            fullSizeProfileViewer.setVisibility(View.VISIBLE);
//            fullSizeProfileViewer.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));
        });

        /* When user clicked when image is on full size the full size profile will get closed */
        fullSizeProfileViewer.setOnClickListener(v -> {
            /* Methods.closeView() methods simply hide the current view
             * This methods takes the view which has to be hide and the context
             * in second parameter it takes the root view of the current view.
             */
            Methods.closeView(v, normalProfileImage, getContext());
        });

        showProfileEditContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Methods.showtoToggle(v, profileEditContainer, profileFragContainer);
            }
        });

        ImageView cancelProfileEditButton = profileEditContainer.findViewById(R.id.canel_profile_edit);
        cancelProfileEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileEditContainer.performClick();
            }
        });
        AppCompatButton saveChanges = profileEditContainer.findViewById(R.id.saveChanges);
        /* the imageview button editprofile pic will give user to set profile image or reset the profile pic */
        ImageView editProfilePic = fullSizeProfileViewer.findViewById(R.id.edit_profile_image);
        editProfilePic.setOnClickListener(v -> setProfilePic());
        initialiseEditProfile();

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEditProfile();
            }
        });
        binding.profileCodechef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(v);
            }
        });
        binding.profileLinkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(v);
            }
        });
        binding.profileFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(v);
            }
        });
        binding.profileInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(v);
            }
        });
        binding.profileGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(v);
            }
        });
        binding.profileCodeforces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl(v);
            }
        });
        return binding.getRoot();
    }

    private void openUrl(View v) {
        String url = v.getContentDescription().toString();
        if (url.equals(""))
            Toast.makeText(getContext(), v.getTag().toString() + " is empty", Toast.LENGTH_SHORT).show();
        else {
            if (!url.startsWith("https://")) url = "https://" + url;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            try {
                getContext().startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initialiseProfileViews(User currentUser) {
        binding.profileUserName.setText(currentUser.getName());
        binding.profileUserBranch.setText(currentUser.getBranch());
        String batch = currentUser.getBatch();
        String session = batch + "-" + String.valueOf(Integer.parseInt(batch.substring(2)) + 4);
        binding.profileUserSession.setText(session);
        binding.profileAbout.setText(currentUser.getAbout());
        binding.profileEmail.setText(currentUser.getEmail());
        binding.profileRollNumber.setText(currentUser.getRollNo());
        binding.profileRegNumber.setText(currentUser.getRegNo());
        binding.profileDob.setText(currentUser.getDob());
        binding.profileClub.setText(currentUser.getClub());
        binding.profileCodechef.setContentDescription(currentUser.getCodechefUrl());
        binding.profileLinkedin.setContentDescription(currentUser.getLinkedInUrl());
        binding.profileFacebook.setContentDescription(currentUser.getFacebookUrl());
        binding.profileInstagram.setContentDescription(currentUser.getInstaUrl());
        binding.profileGithub.setContentDescription(currentUser.getGithubUrl());
        binding.profileCodeforces.setContentDescription(currentUser.getCodefrocesUrl());
    }

    EditText about, name, dob, club, codeChef, linkedIn, faceBook, instagram, github, codeForces;

    private void initialiseEditProfile() {
        about = profileEditContainer.findViewById(R.id.edit_profile_about);
        name = profileEditContainer.findViewById(R.id.edit_profile_name);
        dob = profileEditContainer.findViewById(R.id.edit_profile_dob);
        club = profileEditContainer.findViewById(R.id.edit_profile_club);
        codeChef = profileEditContainer.findViewById(R.id.edit_codechef_link);
        linkedIn = profileEditContainer.findViewById(R.id.edit_linkedin_link);
        faceBook = profileEditContainer.findViewById(R.id.edit_facebook_link);
        instagram = profileEditContainer.findViewById(R.id.edit_instagram_link);
        github = profileEditContainer.findViewById(R.id.edit_github_link);
        codeForces = profileEditContainer.findViewById(R.id.edit_codeforces_link);

        name.setText(binding.profileUserName.getText());
        club.setText(binding.profileClub.getText());
        dob.setText(binding.profileDob.getText());
        about.setText(binding.profileAbout.getText());
        codeChef.setText(binding.profileCodechef.getContentDescription().toString());
        linkedIn.setText(binding.profileLinkedin.getContentDescription().toString());
        faceBook.setText(binding.profileFacebook.getContentDescription().toString());
        instagram.setText(binding.profileInstagram.getContentDescription().toString());
        github.setText(binding.profileGithub.getContentDescription().toString());
        codeForces.setText(binding.profileCodeforces.getContentDescription().toString());
    }


    private void saveEditProfile() {
        String abouts, names, dobs, clubs, codechefs, linkedins, facebooks, instagrams, githubs, codeforcess;


        abouts = about.getText().toString();
        assert abouts != null;
        names = name.getText().toString();
        assert names != null;
        dobs = dob.getText().toString();
        assert dobs != null;
        clubs = club.getText().toString();
        assert clubs != null;
        codechefs = codeChef.getText().toString();
        linkedins = linkedIn.getText().toString();
        facebooks = faceBook.getText().toString();
        instagrams = instagram.getText().toString();
        githubs = github.getText().toString();
        codeforcess = codeForces.getText().toString();
        currentUser.setAbout(abouts);
        currentUser.setName(names);
        currentUser.setDob(dobs);
        currentUser.setClub(clubs);
        currentUser.setCodechefUrl(codechefs);
        currentUser.setLinkedInUrl(linkedins);
        currentUser.setFacebookUrl(facebooks);
        currentUser.setInstaUrl(instagrams);
        currentUser.setGithubUrl(githubs);
        currentUser.setCodefrocesUrl(codeforcess);
        viewModel.updateUser(currentUser);
        initialiseProfileViews(currentUser);
        Methods.closeView(profileEditContainer, showProfileEditContainer, getContext());
    }

    private void setProfilePic(boolean isGallery) {
        if (isGallery) {
            ImagePicker.with(this)
                    .crop()
                    .maxResultSize(512, 512)
                    .compress(400)
                    .galleryOnly()
                    .start();
        } else {
            ImagePicker.with(this)
                    .crop()
                    .maxResultSize(512, 512)
                    .compress(400)
                    .cameraOnly()
                    .start();
        }
    }

    private void setProfilePic() {
        ImagePicker.with(this)
                .crop()
                .maxResultSize(512, 512)
                .compress(400)
                .start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mBottomSheetDialog.dismiss();
        mediumProfileViewer.setVisibility(View.INVISIBLE);
        fullSizeProfileViewer.setVisibility(View.VISIBLE);
        Uri imageToBeUpload = data.getData();
        viewModel.uploadProfilePicInStorage(imageToBeUpload, fullSizeImage,progressBar);
        normalProfileImage.setImageURI(imageToBeUpload);
        mediumExpandedImage.setImageURI(imageToBeUpload);
        fullSizeImage.setImageURI(imageToBeUpload);
        fullSizeProfileViewer.setClickable(true);
    }

    private void showBottomSheetDialog() {
        if (mBehaviour.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        final View view = getLayoutInflater().inflate(R.layout.sheet_list, null);
        mBottomSheetDialog = new BottomSheetDialog(getContext());
        mBottomSheetDialog.setContentView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        mBottomSheetDialog.show();
        selectImageFromProfile = view.findViewById(R.id.setProfileFromGallery);
        selectImageFromCamera = view.findViewById(R.id.setProfileFromCamera);
        showProfilePic = view.findViewById(R.id.showProfile);
        selectImageFromProfile.setOnClickListener(v -> {
            setProfilePic(true);
        });
        selectImageFromCamera.setOnClickListener(v->{
            setProfilePic(false);
        });
        showProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediumProfileViewer.setVisibility(View.INVISIBLE);
                fullSizeProfileViewer.setVisibility(View.VISIBLE);
                fullSizeProfileViewer.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));
                mBottomSheetDialog.dismiss();
            }
        });

        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBottomSheetDialog = null;
            }
        });
    }
}