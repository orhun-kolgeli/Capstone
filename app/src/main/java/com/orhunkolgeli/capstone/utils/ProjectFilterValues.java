package com.orhunkolgeli.capstone.utils;

import android.util.Log;
import android.widget.Toast;

import com.orhunkolgeli.capstone.LoginActivity;
import com.orhunkolgeli.capstone.Project;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

public class ProjectFilterValues {
    public static final String CREATED_AT = "createdAt";
    public static final String UPDATED_AT = "updatedAt";
    public static final String APPLICANT_COUNT = "applicantCount";
    public static final String ANDROID = "Android";
    public static final String IOS = "iOS";
    public static final String WEB = "Web";
    public static final String LOCATION = "location";
    public static final String TYPE = "type";
    public static final String DESCRIPTION = "description";
    public static final int CREATED_AT_DESCENDING = 0;
    public static final int UPDATED_AT_DESCENDING = 1;
    public static final int APPLICANT_COUNT_ASCENDING = 2;
    public static final int APPLICANT_COUNT_DESCENDING = 3;
    public static final int MILES = 0;
    public static final int KILOMETERS = 1;
    public static final int DEFAULT_DISTANCE = 3000;
    private int sortBy;
    private boolean isAndroidChecked;
    private boolean isiOSChecked;
    private boolean isWebChecked;
    private int distance;
    private int distanceUnit;

    private HashMap<String, Boolean> keywords;

    public ProjectFilterValues() {
        // Set default values
        this.sortBy = CREATED_AT_DESCENDING;
        this.isAndroidChecked = true;
        this.isiOSChecked = true;
        this.isWebChecked = true;
        this.distance = DEFAULT_DISTANCE;
        this.distanceUnit = MILES;
        this.keywords = new HashMap<>();
    }

    public void addKeyword(String keyword) {
        this.keywords.putIfAbsent(keyword, true);
    }

    public void removeKeyword(String keyword) {
        this.keywords.remove(keyword);
    }

    public HashMap<String, Boolean> getKeywords() {
        return keywords;
    }

    public int getDistanceUnit() {
        return distanceUnit;
    }

    public ProjectFilterValues setDistanceUnit(int distanceUnit) {
        this.distanceUnit = distanceUnit;
        return this;
    }

    public int getDistance() {
        return distance;
    }

    public ProjectFilterValues setDistance(int distance) {
        this.distance = distance;
        return this;
    }

    public ProjectFilterValues setDistance(String distanceString) {
        if (!distanceString.isEmpty()) {
            int distance = Integer.parseInt(distanceString);
            setDistance(distance);
        }
        return this;
    }

    public int getSortBy() {return sortBy;}

    public ProjectFilterValues setSortBy(int sortBy) {
        this.sortBy = sortBy;
        return this;
    }

    public boolean isAndroidChecked() {
        return isAndroidChecked;
    }

    public ProjectFilterValues setAndroidChecked(boolean androidChecked) {
        this.isAndroidChecked = androidChecked;
        return this;
    }

    public boolean isiOSChecked() {
        return isiOSChecked;
    }

    public ProjectFilterValues setIsiOSChecked(boolean isiOSChecked) {
        this.isiOSChecked = isiOSChecked;
        return this;
    }

    public boolean isWebChecked() {
        return isWebChecked;
    }

    public ProjectFilterValues setWebChecked(boolean webChecked) {
        isWebChecked = webChecked;
        return this;
    }

    public void addKeywordFilterToQuery(ParseQuery<Project> query) {
        for (String keyword : getKeywords().keySet()) {
            query.whereContains(DESCRIPTION, keyword);
        }

    }

    public void addTypeFilterToQuery(ParseQuery<Project> query) {
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
        query.whereContainedIn(TYPE, selectedProjectTypes);
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

    public void addDistanceFilterToQuery(ParseQuery<Project> query) {
        ParseGeoPoint userLocation = ParseUser.getCurrentUser().getParseGeoPoint(LOCATION);
        if (getDistanceUnit() == MILES) {
            query.whereWithinMiles(LOCATION, userLocation, getDistance());
        } else {
            query.whereWithinKilometers(LOCATION, userLocation, getDistance());
        }
    }


}
