package com.orhunkolgeli.capstone;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.Size;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestHeaders;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.orhunkolgeli.capstone.databinding.FragmentDeveloperDetailBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

import okhttp3.Headers;

public class DeveloperDetailFragment extends Fragment {
    public static final String BASE_URL = "https://api.github.com/users/%s/repos";
    public static final String TAG = "DeveloperDetailFragment";
    public static final int MAX_REPOS_DISPLAYED = 20;

    private FragmentDeveloperDetailBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDeveloperDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Developer developer = getArguments().getParcelable("developer");
        binding.tvBio2.setText(developer.getBio());
        String full_name = developer.getUser().getString("name");
        binding.tvFullName2.setText(full_name);
        binding.tvDevInitials2.setText(full_name.substring(0,1).toUpperCase());
        binding.tvSkills2.setText(developer.getSkills());
        binding.tvGitHub2.setText(developer.getGitHub());
        binding.btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                String email = developer.getUser().getString("emailAddress");
                intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {email} );
                intent.putExtra(intent.EXTRA_SUBJECT, "Invitation to Interview");
                intent.putExtra(android.content.Intent.EXTRA_TEXT,
                        "Hi " + full_name + ",\n\n");
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    getActivity().startActivity(Intent.createChooser(intent, "Send Email using:"));
                }
                else {
                    Toast.makeText(getActivity(), "You don't have any email apps.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getGitHubRepos(developer.getGitHub());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getGitHubRepos(String github_username) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        RequestHeaders headers = new RequestHeaders();
        headers.put("Authorization", requireContext().getString(R.string.github_access_token));
        client.get(String.format(BASE_URL, github_username), headers, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONArray repos = json.jsonArray;
                createLinearLayoutFromJsonArray(repos);
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure: " + throwable.toString());
            }
        });
    }

    private void createLinearLayoutFromJsonArray(JSONArray repos) {
        int length = Math.min(MAX_REPOS_DISPLAYED, repos.length());
        for (int i = 0; i < length; i++) {
            try {
                JSONObject jsonObject = repos.getJSONObject(i);
                // Extract strings from the json object
                String repo_name = jsonObject.getString("name");
                String language = jsonObject.getString("language");
                String html_url = jsonObject.getString("html_url");
                // Create a new TextView to put into linearLayoutRepos
                TextView tvRepo = new TextView(getContext());
                tvRepo.setText(String.format("%s\n·\n%s", repo_name, language));
                // Style the TextView
                tvRepo.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvRepo.setBackground(ResourcesCompat.getDrawable(getResources(),
                        R.drawable.rounded_corners, null));
                tvRepo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                tvRepo.setTextColor(getResources().getColor(R.color.white, null));
                // Take user to the repository on GitHub on click
                tvRepo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(html_url));
                        startActivity(i);
                    }
                });
                // Put the TextView into the LinearLayout
                binding.linearLayoutRepos.addView(tvRepo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}