package com.orhunkolgeli.capstone;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Project")
public class Project extends ParseObject {
    public static final String KEY_TYPE = "type";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_DESCRIPTION = "description";

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
}
