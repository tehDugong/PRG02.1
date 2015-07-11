package org.cs160.prg021;

import android.content.BroadcastReceiver;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import android.content.Intent;
import android.view.View;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.File;


public class MobileActivity extends ActionBarActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "YUCCHiHQ3emlegiAi4E3K21dI";
    private static final String TWITTER_SECRET = "nIuD8qIXDPb4nyYG7CSsgLzDXo6ZGrQk85m0xtElMx79JTaZU0";

    private TwitterLoginButton loginButton;
    private boolean loggedIn = false;


    private static final String TAG = "MobileActivity";
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig), new TweetComposer());
        setContentView(R.layout.activity_mobile);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        Log.d(TAG, "onConnected: " + connectionHint);
                        // Now you can use the Data Layer API
                    }
                    @Override
                    public void onConnectionSuspended(int cause) {
                        Log.d(TAG, "onConnectionSuspended: " + cause);
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.d(TAG, "onConnectionFailed: " + result);
                        if (result.getErrorCode() == ConnectionResult.API_UNAVAILABLE){
                            // Wearable API is unavailable
                            Log.d(TAG, "Wearable API is unavailable");
                        }
                    }
                })
                        // Request access only to the Wearable API
                .addApi(Wearable.API)
                .build();

        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                loggedIn = true;

            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                loggedIn = false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
        tweet();
    }

    private void tweet(){
        Uri uri = Uri.parse("android.resource://org.cs160.prg021/drawable/excited");

        TweetComposer.Builder builder = new TweetComposer.Builder(this)
                .text("#cs160excited")
                .image(uri);
        builder.show();

        switchInterface();
    }

    private void switchInterface (){
        if (loggedIn){
            findViewById(R.id.twitter_login_button).setVisibility(View.GONE);
            findViewById(R.id.success).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.twitter_login_button).setVisibility(View.VISIBLE);
            findViewById(R.id.success).setVisibility(View.GONE);
        }
    }

    protected void onStart(){
        super.onStart();
        // needs resolving error, will NOT gracefully fail right now!
        mGoogleApiClient.connect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mobile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
