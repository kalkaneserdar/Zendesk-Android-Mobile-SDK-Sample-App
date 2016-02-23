package com.sample.sampleapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Button;

import com.zendesk.logger.Logger;
import com.zendesk.sdk.feedback.ZendeskFeedbackConfiguration;
import com.zendesk.sdk.feedback.impl.BaseZendeskFeedbackConfiguration;
import com.zendesk.sdk.feedback.ui.ContactZendeskActivity;
import com.zendesk.sdk.model.access.AnonymousIdentity;
import com.zendesk.sdk.model.access.Identity;
import com.zendesk.sdk.network.impl.ZendeskConfig;
import com.zendesk.sdk.rating.ui.RateMyAppDialog;
import com.zendesk.sdk.support.SupportActivity;
import com.zendesk.util.StringUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Enable Logging for Message Base mobile SDK
        Logger.setLoggable(true);

        //Reference of Contact Resources
        Button HelpCenterButton = (Button)(findViewById(R.id.HelpCenterButton));
        Button ContactUsButton = (Button)(findViewById(R.id.ContactUsButton));
        Button RateMyAppButton = (Button)(findViewById(R.id.RateMyAppButton));

        //Initializing mobile SDK for Message Base Sample Application
        ZendeskConfig.INSTANCE.init(this, "https://messagebase.zendesk.com", "41001d9be97186cdc323bb10c750e53a237c2a01c4e59355", "mobile_sdk_client_8c58512b6fd95599a694");

        //Setting User Identity
        Identity user;
        user = new AnonymousIdentity.Builder()
                .withEmailIdentifier("kalkaneserdar@gmail.com")
                .withNameIdentifier("Serdar Kalkan")
                .build();
        ZendeskConfig.INSTANCE.setIdentity(user);

        //Displaying Help Center (aka Knowledge Base)
        HelpCenterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SupportActivity.Builder builder = new SupportActivity.Builder();
                builder.listCategories().show(MainActivity.this);
            }
        });

        //Setting Contact Configuration and Contact Us screen
        ZendeskConfig.INSTANCE.setContactConfiguration(new BaseZendeskFeedbackConfiguration() {
            @Override
            public String getRequestSubject() {
                return "Customer Feedback!";
            }
        });

        ContactUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactUsIntent = new Intent(MainActivity.this, ContactZendeskActivity.class);
                startActivity(contactUsIntent);
            }
        });

        //Displaying Rate My App
        final BaseZendeskFeedbackConfiguration RateMyAppConfiguration = new BaseZendeskFeedbackConfiguration() {
            @Override
            public String getRequestSubject() {
                return "How is this app";
            }
        };

        RateMyAppButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                RateMyAppDialog rateMyAppDialog = new RateMyAppDialog.Builder(MainActivity.this)
                        .withAndroidStoreRatingButton()
                        .withSendFeedbackButton(RateMyAppConfiguration)
                        .withDontRemindMeAgainButton()
                        .build();

                rateMyAppDialog.show(MainActivity.this);
                }
            });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
