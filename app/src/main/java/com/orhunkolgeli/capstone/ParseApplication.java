package com.orhunkolgeli.capstone;

import android.app.Application;
import android.content.Context;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Register parse models
        ParseObject.registerSubclass(Project.class);
        ParseObject.registerSubclass(Developer.class);

        // Initialization
        // Context context = getApplicationContext();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(this.getString(R.string.application_id))
                .clientKey(this.getString(R.string.client_key))
                .server("https://parseapi.back4app.com")
                .build()
        );

        // Right after Parse.initialize() call, create a ParseInstallation
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("GCMSenderId", getString(R.string.sender_id));
        installation.saveInBackground();
    }
}
