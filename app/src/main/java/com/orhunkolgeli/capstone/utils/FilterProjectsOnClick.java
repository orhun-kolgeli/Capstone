package com.orhunkolgeli.capstone.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;

import com.orhunkolgeli.capstone.R;
import com.orhunkolgeli.capstone.interfaces.ProjectFilterListener;

public class FilterProjectsOnClick {

    private Context context;
    private View dialogView;
    private Spinner spinnerSort;
    private CheckBox checkBoxAndroid;
    private CheckBox checkBoxiOS;
    private CheckBox checkBoxWeb;
    private EditText etDistance;
    private Spinner spinnerDistanceUnit;
    private ProjectFilterValues projectFilterValues;


    public FilterProjectsOnClick(Context context) {
        this.context = context;
        this.projectFilterValues = new ProjectFilterValues();
    }

    public FilterProjectsOnClick getReferences(LayoutInflater layoutInflater) {
        dialogView = layoutInflater.inflate(R.layout.dialog_filter, null);
        // Get references to filter dialog's view objects
        spinnerSort = dialogView.findViewById(R.id.spinnerSortProjects);
        checkBoxAndroid = dialogView.findViewById(R.id.checkBoxAndroid);
        checkBoxiOS = dialogView.findViewById(R.id.checkBoxiOS);
        checkBoxWeb = dialogView.findViewById(R.id.checkBoxWeb);
        etDistance = dialogView.findViewById(R.id.etDistance);
        spinnerDistanceUnit = dialogView.findViewById(R.id.spinnerDistanceUnit);
        return this;
    }

    public FilterProjectsOnClick populateDialog() {
        // Populate the dialog's fields with previous (or default) selection
        spinnerSort.setSelection(projectFilterValues.getSortBy());
        checkBoxAndroid.setChecked(projectFilterValues.isAndroidChecked());
        checkBoxiOS.setChecked(projectFilterValues.isiOSChecked());
        checkBoxWeb.setChecked(projectFilterValues.isWebChecked());
        etDistance.setText(String.valueOf(projectFilterValues.getDistance()));
        spinnerDistanceUnit.setSelection(projectFilterValues.getDistanceUnit());
        return this;
    }

    public FilterProjectsOnClick showDialog(ProjectFilterListener projectFilterListener) {
        // Create and show the dialog
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(R.string.filter_projects)
                .setIcon(R.drawable.icon)
                .setView(dialogView)
                .setPositiveButton(R.string.filter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        projectFilterValues
                                .setSortBy(spinnerSort.getSelectedItemPosition())
                                .setAndroidChecked(checkBoxAndroid.isChecked())
                                .setIsiOSChecked(checkBoxiOS.isChecked())
                                .setWebChecked(checkBoxWeb.isChecked())
                                .setDistance(etDistance.getText().toString())
                                .setDistanceUnit(spinnerDistanceUnit.getSelectedItemPosition());
                        // Communicate the new filter values to ProjectSearchFragment
                        if (projectFilterListener != null) {
                            projectFilterListener.onActionFilterProjects(projectFilterValues);
                        }
                    }
                })
                .setNeutralButton(R.string.reset, null)
                .setNegativeButton(R.string.cancel, null)
                .show();
        setResetButton(alertDialog);
        return this;
    }

    private void setResetButton(AlertDialog alertDialog) {
        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                projectFilterValues = new ProjectFilterValues();
                populateDialog();
            }
        });
    }


}
