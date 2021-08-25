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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private LinearLayout socialMediaContainer;
    private ProfileSharedPreferencesViewModel viewModel;
    private FirebaseStorage storage;
    private  FirebaseAuth auth;
    private FirebaseFirestore mStore;

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
    private User currentUser;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication())).get(ProfileSharedPreferencesViewModel.class);

        // assign everything with user model here
        currentUser = viewModel.getUser().getValue();
        assert currentUser != null;
        binding.profileUserName.setText(currentUser.getName());
        binding.profileUserBranch.setText(currentUser.getBranch());
        binding.profileUserSession.setText(currentUser.getBatch());

        binding.profileAbout.setText(currentUser.getAbout());
        binding.profileEmail.setText(currentUser.getEmail());
        binding.profileRollNumber.setText(currentUser.getRollNo());
        binding.profileRegNumber.setText(currentUser.getRegNo());
        binding.profileDob.setText(currentUser.getDob());
        binding.profileClub.setText(currentUser.getClub());
        try {
            Picasso.get().load(currentUser.getProfilePic()).placeholder(R.drawable.test_pic).into(binding.profileImage);
        }catch (Exception e){
            Log.e("Nipun",""+e.getMessage());
        }
        viewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                currentUser = user;
            }
        });
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
        if(currentUser.getProfilePic() == null)
        mediumExpandedImage.setImageDrawable(normalProfileImage.getDrawable());
        else {
            try {
                Picasso.get().load(currentUser.getProfilePic()).into(mediumExpandedImage);
            }catch (Exception e){
                Log.e("Nipun",""+e.getMessage());
            }

        }
        TextView userNameInMediumProfileViewer = mediumProfileViewer.findViewById(R.id.expanded_user_name);
        userNameInMediumProfileViewer.setText(headerUserName.getText());

        /* full size profile show the profile pic on full screen and also get the edit button
         * for resetting the profile picture.
         */
        fullSizeProfileViewer = binding.fullSizeProfileViewer.getRoot();
        fullSizeImage = fullSizeProfileViewer.findViewById(R.id.full_profile_image);
        if(currentUser.getProfilePic().equals(""))
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
            final StorageReference reference = storage.getReference().child("profile pictures").child(auth.getUid());
            reference.putFile(imageToBeUpload).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            DocumentReference documentReference = mStore.collection("Users").document(auth.getUid());
                            Map<String, Object> user = new HashMap<>();
                            user.put("ProfilePic", uri.toString());
                            currentUser.setProfilePic(uri.toString());
                            viewModel.updateUser(currentUser);

                            documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    normalProfileImage.setImageURI(imageToBeUpload);
                                    mediumExpandedImage.setImageURI(imageToBeUpload);
                                    fullSizeImage.setImageURI(imageToBeUpload);
                                    Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}