package com.orhunkolgeli.capstone.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.orhunkolgeli.capstone.R;
import com.orhunkolgeli.capstone.models.User;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

public class LoginViewModel extends ViewModel {
    public enum UserType {
        DEVELOPER, ORGANIZATION
    }
    private final MutableLiveData<User> _userMutableLiveData = new MutableLiveData<>();
    public LiveData<User> userLiveData = _userMutableLiveData;
    public MutableLiveData<Integer> message = new MutableLiveData<>();
    public MutableLiveData<UserType> userType = new MutableLiveData<>();

    public LoginViewModel() {
        super();
        launchIfLoggedIn();
    }

    public MutableLiveData<UserType> getUserType() {
        return this.userType;
    }

    public void updateMessage(int message) {
        this.message.setValue(message);
    }

    public MutableLiveData<Integer> getMessage() {
        return this.message;
    }


    public void updateUser(String username, String password) {
        _userMutableLiveData.setValue(new User()
                .setUsername(username)
                .setPassword(password));
    }

    public void login() {
        // Try to log in the user
        if (userLiveData.getValue() == null) {
            return;
        }
        ParseUser.logInInBackground(
                userLiveData.getValue().getUsername(),
                userLiveData.getValue().getPassword(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    if (userLiveData.getValue().getUsername().isEmpty()) {
                        updateMessage(R.string.enter_username);
                    } else if (userLiveData.getValue().getPassword().isEmpty()) {
                        updateMessage(R.string.enter_password);
                    } else {
                        updateMessage(R.string.invalid_username_password);
                    }
                    return;
                }
                launchIfLoggedIn();
            }
        });
    }

    private void launchIfLoggedIn() {
        // Set user type and associate the current ParseInstallation with the current device
        if (isAuthenticatedDeveloper()) {
            userType.setValue(UserType.DEVELOPER);
            associateDevice();
        } else if (isAuthenticatedOrganization()) {
            userType.setValue(UserType.ORGANIZATION);
            associateDevice();
        }
    }

    private boolean isAuthenticatedDeveloper() {
        // Return true if authenticated developer, otherwise false
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            return false;
        } else {
            return !currentUser.getBoolean("organization");
        }
    }

    private boolean isAuthenticatedOrganization() {
        // Return true if authenticated organization, otherwise false
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            return false;
        } else {
            return currentUser.getBoolean("organization");
        }
    }

    private void associateDevice() {
        // Associate the device with a user
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("user", ParseUser.getCurrentUser());
        installation.saveInBackground();
    }


}
