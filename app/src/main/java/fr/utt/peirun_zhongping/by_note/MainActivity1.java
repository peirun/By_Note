package fr.utt.peirun_zhongping.by_note;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import java.util.HashMap;

public class MainActivity1 extends AppCompatActivity {

    private int userId;
    private String userName;
    // User Session Manager Class
    private UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        // Session class instance
        session = new UserSessionManager(getApplicationContext());

        // Check user login (this is the important point)
        // If User is not logged in , This will redirect user to LoginActivity
        // and finish current activity from activity stack.
        if(session.checkLogin())
            finish();

        // get user data from session
        HashMap<String, Object> user = session.getUserDetails();

        // get id
        int userId = (int) user.get(UserSessionManager.USER_ID);

        // get name
        String userName = (String) user.get(UserSessionManager.USER_NAME);
        myToolbar.setSubtitle(userName);
    }
}
