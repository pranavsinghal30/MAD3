package com.example.mad_project;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.os.Build.VERSION_CODES.P;

public class OperatorActivity extends ActivityParent implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    android.support.v7.widget.Toolbar toolbar;
    private Chronometer chronometer;
    private boolean running;
    private long pauseOffset;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator);
        boolean correct = checkCredentials();
        if (!correct) {
            logout();
        }
        // chronometer = (Chronometer) findViewById(R.id.chronometer);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer=findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, new DailyFragment(), "default");//.commit();
//            ft.addToBackStack("default");
            ft.commit();
            navigationView.setCheckedItem(R.id.nav_daily);
        }
        /*TextView tv_date=(TextView)findViewById(R.id.text_view_date);
        String date_n = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
        tv_date.setText(date_n);

        Calendar cal=Calendar.getInstance();
        String currDate= DateFormat.getDateInstance().format(cal.getTime());

        TextView tvd=findViewById(R.id.text_view_date);
        tvd.setText(currDate);

        String dt="2019-04-04";
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar c=Calendar.getInstance();
        try{
            c.setTime(sdf.parse(dt));
        }
        catch (ParseException e){
            e.printStackTrace();
        }
        c.add(Calendar.DATE,40);
        SimpleDateFormat sdf1=new SimpleDateFormat("MM-dd,yyyy");
        String output=sdf1.format(c.getTime());
        TextView tv_date=(TextView)findViewById(R.id.text_view_date);
        tv_date.setText(output);*/
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        getSupportFragmentManager().popBackStack("top", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        switch(menuItem.getItemId()){
            case R.id.nav_daily:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DailyFragment(), "top").commit();
                break;

            case R.id.nav_processing:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProcessingFragment(), "top").addToBackStack("top").commit();
                break;

            case R.id.nav_fumigation:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FumigationFragment(), "top").addToBackStack("top").commit();
                break;

            case R.id.nav_stock:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new StockFragment(), "top").addToBackStack("top").commit();
                break;

            case R.id.nav_back:
                logout();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    public void start_chronometer(View view){
//        if(!running){
//            chronometer.setBase(SystemClock.elapsedRealtime()-pauseOffset);
//            chronometer.start();
//            running=true;
//        }
//
//    }
//
//    public void pause_chronometer(View view){
//        if(running){
//            chronometer.stop();
//            pauseOffset=SystemClock.elapsedRealtime()-chronometer.getBase();
//            running=false;
//        }
//
//    }
//
//    public void reset_chronometer(View view){
//        chronometer.setBase(SystemClock.elapsedRealtime());
//        pauseOffset=0;
//
//    }
    protected boolean checkCredentials() {
        SharedPreferences sharedPreferences = getSharedPreferences("credentials", Activity.MODE_PRIVATE);
        String uname = sharedPreferences.getString("username", "");
        String pswd = sharedPreferences.getString("password", "");
//        Toast.makeText(this, String.format("Check UserName: %s, Password: %s", uname, pswd), Toast.LENGTH_SHORT).show();
        if (uname.equals(getString(R.string.OperatorsUsername)) && pswd.equals(getString(R.string.OperatorsPassword))) {
            return true;
        }
        return false;
    }
/*
    public void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("credentials", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("username");
        editor.remove("password");
        editor.apply();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        ActivityCompat.finishAffinity(this);
    }
   */
}
