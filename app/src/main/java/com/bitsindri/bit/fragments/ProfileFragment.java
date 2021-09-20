package com.bitsindri.bit.fragments;

import static com.bitsindri.bit.methods.Methods.loadImage;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

import com.bitsindri.bit.BroadcastReceiver.GetUrlBroadcastReceiver;
import com.bitsindri.bit.R;

import com.bitsindri.bit.ViewModel.ProfileSharedPreferencesViewModel;
import com.bitsindri.bit.activity.AuthenticationActivity;
import com.bitsindri.bit.databinding.FragmentProfileBinding;
import com.bitsindri.bit.methods.Constants;
import com.bitsindri.bit.methods.Methods;
import com.bitsindri.bit.models.User;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.sdsmdg.tastytoast.TastyToast;

import java.lang.invoke.ConstantCallSite;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment implements Toolbar.OnMenuItemClickListener {

    private final int REQUEST_CODE = 18;

    // View Groups Variables
    private FragmentProfileBinding binding;
    private CoordinatorLayout profileFragContainer;
    private LinearLayout selectImageFromProfile;
    private LinearLayout selectImageFromCamera;
    private LinearLayout showProfilePic;
    private View mediumProfileViewer;
    private View fullSizeProfileViewer;
    private View profileEditContainer;
    private View bottom_sheet;

    // View Variables
    private CircleImageView normalProfileImage;
    private ImageView mediumExpandedImage;
    private ImageView fullSizeImage;
    private ProgressBar progressBar;
    private TextView headerUserName;

    // Bottom Sheet Variables
    private ProfileSharedPreferencesViewModel viewModel;
    private BottomSheetBehavior mBehaviour;
    private BottomSheetDialog mBottomSheetDialog;

    // Basic variables
    private final int shortAnimationDuration = 400;
    private User currentUser;

    // Declaring the variable handling the on back button
    private FragmentClickListener stateListener;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        stateListener = ((FragmentClickListener) getContext());

        /* -------- This section Initialising the view model  -------------- */

        // initialising view model for getting user data
        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().
                        getApplication())).get(ProfileSharedPreferencesViewModel.class);

        // assign everything with user model here
        currentUser = viewModel.user.getValue().getData();
        assert currentUser != null;
        viewModel.user.observe(getViewLifecycleOwner(),result ->{
            switch (result.getStatus()){
                case LOADING:
                    binding.progressBar.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    binding.progressBar.setVisibility(View.GONE);
                    initialiseProfileViews(result.getData());
                    break;
            }
        });

        /* ----------------- This section initialising the profile edit button -----------------*/

        /* After clicking the showProfileEditContainer profile edit container will appear
         * Initialising the profile edit Container */
        profileEditContainer = binding.profileEditContainer.getRoot();
        binding.profileToolbar.setOnMenuItemClickListener(this);

        /* Profile edit container contains a cancel button in next line of code
         * we initialise the cancel edit button
         */
        ImageView cancelProfileEditButton = profileEditContainer.findViewById(R.id.canel_profile_edit);
        cancelProfileEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When user clicked on cancel button profile edit container will get closed.
                profileEditContainer.performClick();
            }
        });

        /* Initialising the save changes button */
        AppCompatButton saveChanges = profileEditContainer.findViewById(R.id.saveChanges);
        /* the imageview button editprofile pic will give user to set profile image or reset the profile pic */
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEditProfile();
            }
        });
        ImageView saveButton = profileEditContainer.findViewById(R.id.save_profile_changes);
        saveButton.setOnClickListener(v -> {
            saveEditProfile();
        });

        /* ---------------------- This section initialising the profile picture -------------- */


        // Initialising progress bar
        progressBar = binding.progressBar;

        // Bringing Social media container on front
        binding.socialMediaContainer.bringToFront();

        /* normal profile image show when fragment is started and very first time appear */
        normalProfileImage = binding.profileImage;
        normalProfileImage.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.pop_up));
        headerUserName = binding.profileUserName;
        profileFragContainer = binding.profileContainer;
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
            Methods.showtoToggle(v, mediumProfileViewer, profileFragContainer, getContext());
        });

        /* medium profile viewer show the profile pic on half of the screen */
        mediumProfileViewer = binding.profileViewerContainer.getRoot();
        mediumExpandedImage = mediumProfileViewer.findViewById(R.id.expanded_profile_image);
        /* When user clicked the image view when medium profile image is opened
         * profile image will zoom to full size and get the profile image edit
         * button option.
         */
        // Bottom Sheet initialising
        bottom_sheet = binding.bottomSheet;
        mBehaviour = BottomSheetBehavior.from(binding.bottomSheet);
        mediumExpandedImage.setOnClickListener(v -> {
            /* When user click on the image which opened in half size
             * it will prompt a bottom sheet which give 3 option 1st is show profile,
             * 2nd is set profile from gallery and 3rd is set profile from camera
             */
            showBottomSheetDialog();
        });

        /* Setting the name of user when image is in zoom mode */
        TextView userNameInMediumProfileViewer = mediumProfileViewer.findViewById(R.id.expanded_user_name);
        userNameInMediumProfileViewer.setText(headerUserName.getText());

        /* full size profile show the profile pic on full screen and also get the edit button
         * for resetting the profile picture.
         */
        fullSizeProfileViewer = binding.fullSizeProfileViewer.getRoot();
        fullSizeImage = fullSizeProfileViewer.findViewById(R.id.full_profile_image);
        loadImage(fullSizeImage, currentUser.getProfilePic(),R.drawable.ic_icon_user);
        /* When user clicked when image is on full size the full size profile will get closed */
        fullSizeProfileViewer.setOnClickListener(v -> {
            /* Methods.closeView() methods simply hide the current view
             * This methods takes the view which has to be hide and the context
             * in second parameter it takes the root view of the current view.
             */
            Methods.closeView(v, normalProfileImage, getContext());
            stateListener.setProfileFragmentState(null);
        });
        TextView userNameInFullSizeProfileViewer = fullSizeProfileViewer.findViewById(R.id.full_size_user_name);
        userNameInFullSizeProfileViewer.setText(headerUserName.getText());
        ImageView editProfilePic = fullSizeProfileViewer.findViewById(R.id.edit_profile_image);
        editProfilePic.setOnClickListener(v -> setProfilePic());


        /* --------------- Handle  ClickListener on various social media icon --------------- */

        binding.profileCodechef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCustomTab(v);
            }
        });
        binding.profileLinkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCustomTab(v);
            }
        });
        binding.profileFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCustomTab(v);
            }
        });
        binding.profileInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCustomTab(v);
            }
        });
        binding.profileGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCustomTab(v);
            }
        });
        binding.profileCodeforces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCustomTab(v);
            }
        });

        /*----------------- Handle On Scroll of profile -------------------------------------*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    // We take the last son in the scrollview
                    stateListener.setScrollListener(scrollY, oldScrollY);
                }
            });
        }


        /*-------------------------- Settings -------------------------------------------------------------------------*/
        binding.profileLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.clearLoginInfo();
                TastyToast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT, TastyToast.DEFAULT);
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), AuthenticationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        return binding.getRoot();
    }

    private void openCustomTab(View v) {
        String url = v.getContentDescription().toString();

        Intent intent = new Intent(getContext(), GetUrlBroadcastReceiver.class);
        intent.putExtra(Intent.EXTRA_SUBJECT, "url is ");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (url.equals("")) {
            switch (v.getTag().toString()) {
                case "Codechef link":
                    url += "https://www.codechef.com/ratings/all?order=asc&sortBy=global_rank";
                    break;
                case "LinkedIn link":
                    url += "https://www.linkedin.com";
                    break;
                case "Facebook link":
                    url += "https://www.social-searcher.com/facebook-search/";
                    break;
                case "Instagram link":
                    url += "https://www.instagram.com/";
                    break;
                case "Github link":
                    url += "https://github.com/";
                    break;

                default:
                    url += "https://google.com";
            }
        }

        if (!url.startsWith("https://")) url = "https://" + url;
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.addMenuItem("Set it as your " + v.getTag().toString(), pendingIntent);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(getContext(), Uri.parse(url));

    }


    private void initialiseProfileViews(User currentUser) {
        /* This method is used for initialising the various variable
         * and set the initial value of profile fragment
         */
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
        loadImage(binding.profileImage, currentUser.getProfilePic(),R.drawable.ic_icon_user);
        loadImage(mediumExpandedImage, currentUser.getProfilePic(),R.drawable.ic_icon_user);
        TextView mediumProfileText = mediumProfileViewer.findViewById(R.id.expanded_user_name),
                fullSizeProfileText = fullSizeProfileViewer.findViewById(R.id.full_size_user_name);
        mediumProfileText.setText(currentUser.getName());
        fullSizeProfileText.setText(currentUser.getName());

    }

    EditText about, name, dob, club, codeChef, linkedIn, faceBook, instagram, github, codeForces;

    private void initialiseEditProfile() {
        /* This method is used for initialising the view
         * inside the edit button and set their initial value
         */
        about = profileEditContainer.findViewById(R.id.edit_profile_about);
        about.scrollTo(0, about.getBottom());
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
        /* This method is used for saving the value after
         * clicking the save changes button inside the profile edit section.
         */

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
        Methods.closeView(profileEditContainer, binding.profileToolbar, getContext());
    }

    private void setProfilePic(boolean isGallery) {
        /* This method is used for setting the profile pic */
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
        stateListener.setProfileFragmentState(fullSizeProfileViewer);
        assert data != null;
        Uri imageToBeUpload = data.getData();
        viewModel.uploadProfilePicInStorage(imageToBeUpload, fullSizeImage);
        fullSizeImage.setImageURI(imageToBeUpload);
        fullSizeProfileViewer.setClickable(true);
    }

    private void showBottomSheetDialog() {
        /* This method is used for initialising the bottom sheet for selection
         * of profile upload option.
         */
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
        selectImageFromCamera.setOnClickListener(v -> {
            setProfilePic(false);
        });
        showProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediumProfileViewer.setVisibility(View.INVISIBLE);
                fullSizeProfileViewer.setVisibility(View.VISIBLE);
                stateListener.setProfileFragmentState(fullSizeProfileViewer);
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

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit_profile_icon:{
                try {
                    Methods.showtoToggle(binding.profileToolbar, profileEditContainer, profileFragContainer, getContext());
                    initialiseEditProfile();
                }
                catch (Exception e){
                    Log.e("Nipun",e.getMessage().toString());
                }
                return false;
            }
        }
        return false;
    }
}