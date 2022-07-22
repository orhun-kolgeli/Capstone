package com.orhunkolgeli.capstone;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestHeaders;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.orhunkolgeli.capstone.databinding.FragmentDeveloperDetailBinding;
import com.orhunkolgeli.capstone.models.Developer;
import com.orhunkolgeli.capstone.models.Project;
import com.orhunkolgeli.capstone.utils.CustomTextView;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;

public class DeveloperDetailFragment extends Fragment {
    public static final String BASE_URL = "https://api.github.com/users/%s/repos";
    public static final String TAG = "DeveloperDetailFragment";
    public static final int MAX_REPOS_DISPLAYED = 20;
    public static final String POSITION = "position";
    public static final String PROJECT = "project";
    public static final String DEVELOPER = "developer";
    public static final String DEVELOPER_CATEGORY = "developerCategory";

    public enum DeveloperCategory {
        DEVELOPER, APPLICANT
    }
    private FragmentDeveloperDetailBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDeveloperDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DeveloperCategory developerCategory = (DeveloperCategory) getArguments().get(DEVELOPER_CATEGORY);
        Developer developer = getArguments().getParcelable(DEVELOPER);
        binding.tvBio2.setText(developer.getBio());
        binding.tvFullName2.setText(developer.getFullName());
        binding.tvDevInitials2.setText(developer.getFullName().substring(0,1));
        binding.tvSkills2.setText(developer.getSkills());
        binding.tvGitHub2.setText(developer.getGitHub());
        if (developerCategory == DeveloperCategory.DEVELOPER) {
            binding.btnInvite.setVisibility(View.VISIBLE);
            binding.btnInvite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendNotificationToDeveloper(developer);
                }
            });
        } else if (developerCategory == DeveloperCategory.APPLICANT) {
            ((OrganizationActivity) requireActivity()).getSupportActionBar().setTitle(R.string.review_application);
            binding.btnExtendOffer.setVisibility(View.VISIBLE);
            binding.btnRemove.setVisibility(View.VISIBLE);
            setApplicantOnClickListeners(developer);
        }
        getGitHubRepos(developer.getGitHub());
        binding.webViewRepo.setOnPinchToZoomListener(binding);
    }

    private void setApplicantOnClickListeners(Developer developer) {
        binding.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove item from the recyclerView
                int position = getArguments().getInt(POSITION);
                ApplicantAdapter.removeCallback.onActionRemove(position);
                // Navigate back to the review screen
                NavHostFragment.findNavController(DeveloperDetailFragment.this).popBackStack();
                // Remove the applicant from database
                ParseUser.getCurrentUser().getParseObject(PROJECT).fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if (object != null) {
                            Project project = (Project) object;
                            project.removeApplicant(developer);
                        }
                    }
                });
            }
        });

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
                String repoName = jsonObject.getString("name");
                String language = jsonObject.getString("language");
                if (language.equals("null")) {
                    continue;
                }
                String html_url = jsonObject.getString("html_url");
                // Create a new TextView to put into linearLayoutRepos
                CustomTextView tvRepo = new CustomTextView(requireContext());
                tvRepo.setText(String.format("%s\n·\n%s", repoName, language));
                setOnLongPressListener(tvRepo, html_url);
                setWebViewLayout();
                // Put the TextView into the LinearLayout
                binding.linearLayoutRepos.addView(tvRepo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        binding.pbLoadingRepos.setVisibility(View.GONE);
    }

    private void setWebViewLayout() {
        binding.ivCloseWebView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.clWebView.setVisibility(View.GONE);
            }
        });
    }

    private void sendNotificationToDeveloper(@NonNull Developer developer) {
        ParseUser developerUser = developer.getUser();
        String developer_user_id = developerUser.getObjectId();
        Log.i(TAG, "Sending notification to the user with the following id: " + developer_user_id);
        // Make a query where the user is the developer being invited
        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        userQuery.whereEqualTo("objectId", developer_user_id);

        // Find devices associated with this user
        ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
        pushQuery.whereMatchesQuery("user", userQuery);

        // Send push notification to the developer
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery);
        push.setMessage(ParseUser.getCurrentUser().getString("name") +
                " would like you to consider their project!");
        push.sendInBackground();
    }

    private void setOnLongPressListener(CustomTextView tvRepo, String html_url) {
        // Set onLongPress listener
        tvRepo.setOnTouchListener(new View.OnTouchListener() {
            private final GestureDetector gestureDetector = new GestureDetector(getContext(),
                    new GestureDetector.SimpleOnGestureListener() {
                @Override
                public void onLongPress(MotionEvent e) {
                    binding.clWebView.setVisibility(View.VISIBLE);
                    binding.webViewRepo.loadUrl(html_url);
                    binding.webViewRepo.bringToFront();
                    super.onLongPress(e);
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    binding.clWebView.setVisibility(View.GONE);
                    return super.onSingleTapUp(e);
                }
            });
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.performClick();
                gestureDetector.onTouchEvent(event);
                return false;
            }
        });
    }
}