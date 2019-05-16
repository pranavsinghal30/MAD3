package com.example.mad_project;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AdminActivity extends ActivityParent implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer2;
    android.support.v7.widget.Toolbar toolbar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        boolean correct = checkCredentials();
        if (!correct) {
            Toast.makeText(this, "Logging out, wrong cred", Toast.LENGTH_SHORT).show();
            logout();
        }

        toolbar2 = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);

        drawer2=findViewById(R.id.drawer_layout);
        NavigationView navigationView=findViewById(R.id.nav_view2);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView name = (TextView) header.findViewById(R.id.username_header);
        if (name != null)
            name.setText(getSharedPreferences("credentials", Activity.MODE_PRIVATE).getString("username", "Admin"));

        if(savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container2, new Admin_daily_fragment(), "default");//.commit();
            ft.commit();
            navigationView.setCheckedItem(R.id.nav_daily_admin);
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer2, toolbar2, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer2.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        getSupportFragmentManager().popBackStack("top", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        switch(menuItem.getItemId()){
            case R.id.nav_daily_admin:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, new Admin_daily_fragment(), "top").commit();
                break;

            case R.id.nav_task_admin:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, new Admin_addTask_fragment(), "top").addToBackStack("top").commit();
                break;

            case R.id.nav_productivity_admin:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, new productivity(), "top").addToBackStack("top").commit();
                break;

            case R.id.nav_adduser_admin:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, new adduser(), "top").addToBackStack("top").commit();
                break;

            case R.id.nav_analysis_admin:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, new Admin_analysis_fragment(), "top").addToBackStack("top").commit();
                break;

            case R.id.nav_back:
                logout();
                break;


        }
        drawer2.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer2.isDrawerOpen(GravityCompat.START)) {
            drawer2.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    protected boolean checkCredentials() {
        SharedPreferences sharedPreferences = getSharedPreferences("credentials", Activity.MODE_PRIVATE);
        String uname = sharedPreferences.getString("username", "");
        String pswd = sharedPreferences.getString("password", "");
//        Toast.makeText(this, String.format("Check UserName: %s, Password: %s", uname, pswd), Toast.LENGTH_SHORT).show();
        if (uname.equals(getString(R.string.AdminUsername)) && pswd.equals(getString(R.string.AdminPassword))) {
            return true;
        }
        return false;
    }

}
