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

        /*Reference of Customer Support UI Resources.
        Each customer support event will be triggered through separate buttons.
         */
        Button HelpCenterButton = (Button)(findViewById(R.id.HelpCenterButton));
        Button ContactUsButton = (Button)(findViewById(R.id.ContactUsButton));
        Button RateMyAppButton = (Button)(findViewById(R.id.RateMyAppButton));

        /*Initializing Zendesk Mobile SDK 1.1.5 for Message Base Sample Application.
        SDK initialization configuration is directly copied from Help Desk Mobil SDK settings screen.
         */
        ZendeskConfig.INSTANCE.init(this, "https://messagebase.zendesk.com", "41001d9be97186cdc323bb10c750e53a237c2a01c4e59355", "mobile_sdk_client_8c58512b6fd95599a694");

        /* Anonymous user authentication will be used. One sample user identity will be set for trial Sample Application.
        User identity will consist of following fields: Email and Name&Surname.
         */
        Identity user;
        user = new AnonymousIdentity.Builder()
                .withEmailIdentifier("kalkaneserdar@gmail.com")
                .withNameIdentifier("Serdar Kalkan")
                .build();
        ZendeskConfig.INSTANCE.setIdentity(user);

        /*Starting a Support Activity which is necessary for ticketing(Contact Us) & self service support(Help Center).
        First the configuration will be set as follows:
        */
        ZendeskConfig.INSTANCE.setContactConfiguration(new BaseZendeskFeedbackConfiguration() {
            @Override
            public String getRequestSubject() {
                return "Support Request";
            }
        });

        /*In order to use self service support (Help Center) functionality, Help Center feature should be enabled on Zendesk Help Desk.
        After enabling Help Center, few sample articles should be published in Help Center.
        Finally, set and display Help Center (aka Knowledge Base)
         */
        HelpCenterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SupportActivity.Builder builder = new SupportActivity.Builder();
                builder.show(MainActivity.this);
            }
        });

        /*Following earlier SupportActivity initialization, a ticketing dialogue should be enabled.
        Initializing and Displaying Contact Us:
         */
        ContactUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
            Intent contactUsIntent = new Intent(MainActivity.this, ContactZendeskActivity.class);
            startActivity(contactUsIntent);
            }
        });

        /*In order to use RateMyApp feedback functionality, RateMyApp settings should be made on Zendesk Help Desk.
        RateMyApp will prompt mobile app users to disclose satisfaction through Store or privately on Zendesk Help Desk.
        Initializing RateMyApp configuration
         */
        final BaseZendeskFeedbackConfiguration RateMyAppConfiguration = new BaseZendeskFeedbackConfiguration() {
            @Override
            public String getRequestSubject() {
                return "How is this app";
            }
        };

        //Displaying RateMyApp Dialogue
        RateMyAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
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
