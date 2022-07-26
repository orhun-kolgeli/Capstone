package com.orhunkolgeli.capstone.utils;

import android.content.Context;
import android.content.Intent;

import com.orhunkolgeli.capstone.LoginActivity;
import com.orhunkolgeli.capstone.OrganizationActivity;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

public class CustomParsePushBroadcastReceiver extends ParsePushBroadcastReceiver {

    public static final String DEVELOPER_ID = "developerId";

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        JSONObject data = getPushData(intent);
        // Try to get the developer's ID, if the push notification is coming from a developer
        if (data != null) {
            try {
                String developerId = data.getString(DEVELOPER_ID);
                // Forward this information to the activity that will launch when user opens push
                intent.putExtra(DEVELOPER_ID, developerId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        super.onPushOpen(context, intent);
    }
}
