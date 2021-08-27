package com.bitsindri.bit.methods;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;

import com.bitsindri.bit.R;
import com.bitsindri.bit.fragments.FragmentClickListener;

public class Methods {

    /*
     call to launch progress dialog
     ------how to use ------
     private ProgressDialog progressDialog;
     progressDialog = Methods.launchProgressDialog(progressDialog, LoginActivity.this);
     */

    public static ProgressDialog launchProgressDialog(ProgressDialog progressDialog, Context context){
        // launching progress bar
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_bar);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        return progressDialog;
    }

    public static void closeView(View view,View root, Context context){
        view.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_out));
        view.postOnAnimation(new Runnable() {
            @Override
            public void run() {
                root.setAlpha(1f);
                view.setVisibility(View.GONE);
            }
        });
    }
    private static Animator currentAnimator = null;
    private static int shortAnimationDuration = 400;

    public static void showtoToggle(View from, View to, View container , Context context) {
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }
        FragmentClickListener stateListener = ((FragmentClickListener) context);
        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        from.getGlobalVisibleRect(startBounds);
        container.getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        from.setAlpha(0f);
        to.setVisibility(View.VISIBLE);
        stateListener.setProfileFragmentState(to);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        to.setPivotX(0f);
        to.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(to, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(to, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(to, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(to,
                        View.SCALE_Y, startScale, 1f));
        set.setDuration(shortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                currentAnimator = null;
            }
        });
        set.start();
        currentAnimator = set;
        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentAnimator != null) {
                    currentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(to, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(to,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(to,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(to,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(shortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        from.setAlpha(1f);
                        to.setVisibility(View.GONE);
                        currentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        from.setAlpha(1f);
                        to.setVisibility(View.GONE);
                        currentAnimator = null;
                    }
                });
                stateListener.setProfileFragmentState(null);
                set.start();
                currentAnimator = set;
            }
        });
    }

}
