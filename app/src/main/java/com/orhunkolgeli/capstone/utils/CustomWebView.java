package com.orhunkolgeli.capstone.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.orhunkolgeli.capstone.databinding.FragmentDeveloperDetailBinding;

public class CustomWebView extends WebView {

    public static final int MARGIN = 16;
    public static final int NO_MARGIN = 0;
    public static final int DURATION = 400;

    public CustomWebView(@NonNull Context context) {
        super(context);
    }

    public CustomWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public void setOnPinchToZoomListener(FragmentDeveloperDetailBinding binding) {
        this.setOnTouchListener(new View.OnTouchListener() {
            private final ScaleGestureDetector gestureDetector = new ScaleGestureDetector(
                    getContext(), new ScaleGestureDetector.OnScaleGestureListener() {
                @Override
                public boolean onScale(ScaleGestureDetector detector) {
                    double scaleFactor = detector.getScaleFactor();
                    if (scaleFactor > 1) { // User zooms out
                        ConstraintSet constraintSet = new ConstraintSet();
                        constraintSet.clone(binding.clDeveloperDetail);
                        // Go back to the original size (covers roughly half of the screen)
                        constraintSet.connect(binding.clWebView.getId(), ConstraintSet.BOTTOM,
                                binding.clDeveloperDetail.getId(),ConstraintSet.BOTTOM, MARGIN);
                        TransitionManager.beginDelayedTransition(
                                binding.clDeveloperDetail, createTransition());
                        constraintSet.applyTo(binding.clDeveloperDetail);
                    } else { // User zooms in
                        ConstraintSet constraintSet = new ConstraintSet();
                        constraintSet.clone(binding.clDeveloperDetail);
                        // Go fullscreen
                        constraintSet.connect(binding.clWebView.getId(),ConstraintSet.BOTTOM,
                                binding.scrollViewRepos.getId(),ConstraintSet.TOP, NO_MARGIN);
                        TransitionManager.beginDelayedTransition(
                                binding.clDeveloperDetail, createTransition());
                        constraintSet.applyTo(binding.clDeveloperDetail);
                    }
                    return true;
                }

                @Override
                public boolean onScaleBegin(ScaleGestureDetector detector) {
                    return true;
                }

                @Override
                public void onScaleEnd(ScaleGestureDetector detector) {}
            });
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.performClick();
                gestureDetector.onTouchEvent(event);
                return false;
            }
        });
    }

    private AutoTransition createTransition() {
        AutoTransition transition = new AutoTransition();
        transition.setDuration(DURATION)
                .setInterpolator(new AccelerateDecelerateInterpolator());
        return transition;
    }
}
