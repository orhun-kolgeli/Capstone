package com.orhunkolgeli.capstone;

import android.widget.Toast;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ParseClassName("Project")
public class Project extends ParseObject {
    public static final String KEY_TYPE = "type";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_USER = "user";
    public static final String KEY_APPLICANTS = "applicants";
    public static final String KEY_APPLICANT_COUNT = "applicantCount";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_IMAGE_DESCRIPTION = "imageDescription";

    public String getType() {
        return getString(KEY_TYPE);
    }

    public void setType(String type) {
        put(KEY_TYPE, type);
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public ParseRelation<Developer> getApplicants() {
        return getRelation(KEY_APPLICANTS);
    }

    public void addApplicant(Developer developer) {
        // Get a reference to the pool of applicants
        ParseRelation<Developer> relation = this.getApplicants();
        // Atomically increment applicant count
        increment(KEY_APPLICANT_COUNT);
        // Add the developer to the pool of applicants and save
        relation.add(developer);
        saveInBackground();
    }

    public int getApplicantCount() {
        return (int) getNumber(KEY_APPLICANT_COUNT);
    }

    public void removeApplicant(Developer developer) {
        // Get a reference to the pool of applicants
        ParseRelation<Developer> relation = this.getApplicants();
        // Atomically decrement applicant count
        increment(KEY_APPLICANT_COUNT, -1);
        // Remove the developer from database and save
        relation.remove(developer);
        saveInBackground();
    }

    public void setLocation (ParseGeoPoint geoPoint) {
        put(KEY_LOCATION, geoPoint);
    }

    public String getImageDescription() {
        return getString(KEY_IMAGE_DESCRIPTION);
    }

    public void setImageDescription(String imageDescription) {
        put(KEY_IMAGE_DESCRIPTION, imageDescription);
    }

}
