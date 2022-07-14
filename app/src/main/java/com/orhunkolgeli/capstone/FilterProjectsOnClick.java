package com.orhunkolgeli.capstone;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;

public class FilterProjectsOnClick {

    private Context context;
    private View dialogView;
    private Spinner spinner;
    private CheckBox checkBoxAndroid;
    private CheckBox checkBoxiOS;
    private CheckBox checkBoxWeb;
    private EditText etDistance;
    private ProjectFilterValues projectFilterValues;


    public FilterProjectsOnClick(Context context) {
        this.context = context;
        this.projectFilterValues = new ProjectFilterValues();
    }

    public FilterProjectsOnClick getReferences(LayoutInflater layoutInflater) {
        dialogView = layoutInflater.inflate(R.layout.dialog_filter, null);
        // Get references to filter dialog's view objects
        spinner = dialogView.findViewById(R.id.spinnerSortProjects);
        checkBoxAndroid = dialogView.findViewById(R.id.checkBoxAndroid);
        checkBoxiOS = dialogView.findViewById(R.id.checkBoxiOS);
        checkBoxWeb = dialogView.findViewById(R.id.checkBoxWeb);
        etDistance = dialogView.findViewById(R.id.etDistance);
        return this;
    }

    public FilterProjectsOnClick populateDialog() {
        // Populate the dialog's fields with previous (or default) selection
        spinner.setSelection(projectFilterValues.getSortBy());
        checkBoxAndroid.setChecked(projectFilterValues.isAndroidChecked());
        checkBoxiOS.setChecked(projectFilterValues.isiOSChecked());
        checkBoxWeb.setChecked(projectFilterValues.isWebChecked());
        etDistance.setText(String.valueOf(projectFilterValues.getDistance()));
        return this;
    }

    public FilterProjectsOnClick showDialog(ProjectFilterListener projectFilterListener) {
        // Create and show the dialog
        new AlertDialog.Builder(context)
                .setTitle(R.string.filter_projects)
                .setIcon(R.drawable.icon)
                .setView(dialogView)
                .setPositiveButton(R.string.filter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        projectFilterValues
                                .setSortBy(spinner.getSelectedItemPosition())
                                .setAndroidChecked(checkBoxAndroid.isChecked())
                                .setIsiOSChecked(checkBoxiOS.isChecked())
                                .setWebChecked(checkBoxWeb.isChecked())
                                .setDistance(etDistance.getText().toString());
                        // Communicate the new filter values to ProjectSearchFragment
                        if (projectFilterListener != null) {
                            projectFilterListener.onActionFilterProjects(projectFilterValues);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
        return this;
    }


}
