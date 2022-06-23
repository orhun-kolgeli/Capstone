package com.orhunkolgeli.capstone;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Register parse models
        ParseObject.registerSubclass(Project.class);
        ParseObject.registerSubclass(Developer.class);

        // Initialization
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("haljS0n1MTmYEfhdjaII0FE7rTZ9pBtUmFYqhN5W")
                .clientKey("Jd2qrNUKX3l8Db42eXNFQVd0095fUVRWlk6NoKWH")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
