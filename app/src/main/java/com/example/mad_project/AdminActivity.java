package com.example.mad_project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class AdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer2;
    android.support.v7.widget.Toolbar toolbar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        toolbar2=findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);

        drawer2=findViewById(R.id.drawer_layout);
        NavigationView navigationView=findViewById(R.id.nav_view2);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer2,toolbar2,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer2.addDrawerListener(toggle);
        toggle.syncState();
    }

    public void logout() {
        Intent i1=new Intent(this,MainActivity.class);
        startActivity(i1);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){

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
}
