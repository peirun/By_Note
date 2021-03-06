package fr.utt.peirun_zhongping.by_note;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NoteFragment.OnFragmentInteractionListener {

    private int userId;
    private String userName;
    private String userEmail;
    // User Session Manager Class
    private UserSessionManager session;
    private TextView textName;
    private TextView textEail;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private String folder_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);

            }

        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
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

        // get user's name and email
        String userName = (String) user.get(UserSessionManager.USER_NAME);
        getSupportActionBar().setTitle(userName);
        String userEmail = (String) user.get(UserSessionManager.USER_MAIL);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        textName = (TextView) headerView.findViewById(R.id.textName);
        textName.setText(userName);
        textEail = (TextView) headerView.findViewById(R.id.textEmail);
        textEail.setText(userEmail);
        navigationView.setNavigationItemSelectedListener(this);
        toolbar.setTitle("Note");
        folder_name = "Note";
        Fragment noteFragment = new NoteFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.ly_content, noteFragment).commit();
    }

    // back press function
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // implement menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // implement item selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent = new Intent(MainActivity.this, AddContent.class);
        intent.putExtra("userid", userId);
        //noinspection SimplifiableIfStatement
        if (id == R.id.content_add) {
            intent.putExtra("flag", "content");
            intent.putExtra(NoteDB.FOLDER_NAME, folder_name);
            startActivity(intent);
            return true;
        }

        if(id == R.id.photo_add){
            intent.putExtra("flag", "photo");
            intent.putExtra(NoteDB.FOLDER_NAME, folder_name);
            startActivity(intent);
            return true;
        }

        if(id == R.id.vedio_add){
            intent.putExtra("flag", "video");
            intent.putExtra(NoteDB.FOLDER_NAME, folder_name);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // navigation item
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment noteFragment = new NoteFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();

        if (id == R.id.nav_note) {
            bundle.putString("params", "Note");
            // set MyFragment Arguments
            noteFragment.setArguments(bundle);
            transaction.replace(R.id.ly_content, noteFragment).commit();
            folder_name = "Note";
            // Handle the camera action
        } else if (id == R.id.nav_personal) {
            bundle.putString("params", "Personal");
            // set MyFragment Arguments
            noteFragment.setArguments(bundle);
            transaction.replace(R.id.ly_content, noteFragment).commit();
            getSupportActionBar().setTitle("Personal");
            folder_name = "Personal";
        } else if (id == R.id.nav_work) {
            bundle.putString("params", "Work");
            // set MyFragment Arguments
            noteFragment.setArguments(bundle);
            transaction.replace(R.id.ly_content, noteFragment).commit();
            getSupportActionBar().setTitle("Work");
            folder_name = "Work";
        } else if (id == R.id.nav_study) {
            bundle.putString("params", "Study");
            // set MyFragment Arguments
            noteFragment.setArguments(bundle);
            transaction.replace(R.id.ly_content, noteFragment).commit();
            getSupportActionBar().setTitle("Study");
            folder_name = "Study";
        } else if (id == R.id.nav_star) {
            bundle.putString("params", "Starred");
            // set MyFragment Arguments
            noteFragment.setArguments(bundle);
            transaction.replace(R.id.ly_content, noteFragment).commit();
            getSupportActionBar().setTitle("Starred");
            folder_name = "Note";
        } else if (id == R.id.nav_deconnection) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
