package com.orhunkolgeli.capstone.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

import com.orhunkolgeli.capstone.R;

public class CustomTextView extends AppCompatTextView {
    public CustomTextView(@NonNull Context context) {
        super(context);
        this.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        this.setBackground(ResourcesCompat.getDrawable(getResources(),
                R.drawable.rounded_corners, null));
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        this.setTextColor(getResources().getColor(R.color.white, null));
        // Add ripple effect to the TextView
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        this.setForeground(context.getDrawable(outValue.resourceId));
        this.setClickable(true);
    }


    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
