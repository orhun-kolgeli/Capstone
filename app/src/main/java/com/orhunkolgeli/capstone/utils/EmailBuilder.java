package com.orhunkolgeli.capstone.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.orhunkolgeli.capstone.R;

public class EmailBuilder {
    public static final String TEXT_PLAIN = "text/plain";
    private final Context context;
    private final Intent intent;

    public EmailBuilder(Context context) {
        this.context = context;
        intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType(TEXT_PLAIN);
    }

    public EmailBuilder setEmailAddress(String recipient) {
        intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {recipient} );
        return this;
    }

    public EmailBuilder setSubject(String subject) {
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        return this;
    }

    public EmailBuilder setBody(String text) {
        intent.putExtra(android.content.Intent.EXTRA_TEXT, text);
        return this;
    }

    public void build() {
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(Intent.createChooser(intent,
                    context.getString(R.string.send_email_using)));
        }
        else {
            Toast.makeText(context,
                    context.getString(R.string.no_email_app_found), Toast.LENGTH_SHORT).show();
        }
    }
}
