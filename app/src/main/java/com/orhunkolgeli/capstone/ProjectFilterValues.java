package com.orhunkolgeli.capstone;

import android.content.res.Resources;

import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class ProjectFilterValues {
    public static final String CREATED_AT = "createdAt";
    public static final String UPDATED_AT = "updatedAt";
    public static final String APPLICANT_COUNT = "applicantCount";
    public static final String ANDROID = "Android";
    public static final String IOS = "iOS";
    public static final String WEB = "Web";
    public static final int CREATED_AT_DESCENDING = 0;
    public static final int UPDATED_AT_DESCENDING = 1;
    public static final int APPLICANT_COUNT_ASCENDING = 2;
    public static final int APPLICANT_COUNT_DESCENDING = 3;
    private int sortBy;
    private boolean isAndroidChecked;
    private boolean isiOSChecked;
    private boolean isWebChecked;

    public ProjectFilterValues() {
        // Set default values
        this.sortBy = 0;
        this.isAndroidChecked = true;
        this.isiOSChecked = true;
        this.isWebChecked = true;
    }

    public int getSortBy() {return sortBy;}

    public void setSortBy(int sortBy) {this.sortBy = sortBy;}

    public boolean isAndroidChecked() {
        return isAndroidChecked;
    }

    public void setAndroidChecked(boolean androidChecked) {
        isAndroidChecked = androidChecked;
    }

    public boolean isiOSChecked() {
        return isiOSChecked;
    }

    public void setIsiOSChecked(boolean isiOSChecked) {
        this.isiOSChecked = isiOSChecked;
    }

    public boolean isWebChecked() {
        return isWebChecked;
    }

    public void setWebChecked(boolean webChecked) {
        isWebChecked = webChecked;
    }

    public List<String> selectedProjectTypes() {
        List<String> selectedProjectTypes = new ArrayList<>();
        if (isAndroidChecked()) {
            selectedProjectTypes.add(ANDROID);
        }
        if (isiOSChecked()) {
            selectedProjectTypes.add(IOS);
        }
        if (isWebChecked()) {
            selectedProjectTypes.add(WEB);
        }
        return selectedProjectTypes;
    }

    public void addSortingToQuery(ParseQuery<Project> query) {
        if (getSortBy() == CREATED_AT_DESCENDING) {
            query.addDescendingOrder(CREATED_AT);
        } else if (getSortBy() == UPDATED_AT_DESCENDING) {
            query.addDescendingOrder(UPDATED_AT);
        } else if (getSortBy() == APPLICANT_COUNT_ASCENDING) {
            query.addAscendingOrder(APPLICANT_COUNT);
        } else if (getSortBy() == APPLICANT_COUNT_DESCENDING) {
            query.addDescendingOrder(APPLICANT_COUNT);
        }
    }


}
