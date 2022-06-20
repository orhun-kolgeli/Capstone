package com.orhunkolgeli.capstone;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("haljS0n1MTmYEfhdjaII0FE7rTZ9pBtUmFYqhN5W")
                .clientKey("Jd2qrNUKX3l8Db42eXNFQVd0095fUVRWlk6NoKWH")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
