package com.orhunkolgeli.capstone;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Random;

@ParseClassName("Developer")
public class Developer extends ParseObject {
    public static final String KEY_BIO = "bio";
    public static final String KEY_GITHUB = "github";
    public static final String KEY_SKILLS = "skills";
    public static final String KEY_USER = "user";
    public static final String KEY_FULL_NAME = "fullName";

    public String getBio() {
        return getString(KEY_BIO);
    }

    public void setBio(String bio) {
        put(KEY_BIO, bio);
    }

    public String getGitHub() {
        return getString(KEY_GITHUB);
    }

    public void setGitHub(String github) {
        put(KEY_GITHUB, github);
    }

    public String getSkills() {
        return getString(KEY_SKILLS);
    }

    public void setSkills(String skills) {
        put(KEY_SKILLS, skills);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public void setFullName(String fullName) {
        put(KEY_FULL_NAME, fullName);
    }

    public String getFullName() {
        return getString(KEY_FULL_NAME);
    }
}
