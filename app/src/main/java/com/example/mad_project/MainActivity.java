package com.example.mad_project;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    EditText user, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Build.VERSION.SDK_INT >= 23){
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
        user = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);
        //String s=getString(R.string.AdminUsername);
        sharedPreferences = getSharedPreferences("credentials", Activity.MODE_PRIVATE);
        String userName = sharedPreferences.getString("username", "");
        String password = sharedPreferences.getString("password", "");
        checkCredentials(userName, password);
    }

    private void checkCredentials(String uname, String pswd) {
//        Toast.makeText(this, String.format("UserName: %s, Password: %s", uname, pswd), Toast.LENGTH_SHORT).show();
        if(uname.equals(getString(R.string.AdminUsername)) && pswd.equals(getString(R.string.AdminPassword))) {
            Toast.makeText(this, String.format("Starting adminact UserName: %s, Password: %s", uname, pswd), Toast.LENGTH_SHORT).show();
            Intent i1 = new Intent(this, AdminActivity.class);
            switchActivity(i1);
        } else if(uname.equals(getString(R.string.OperatorsUsername))&&pswd.equals(getString(R.string.OperatorsPassword))) {
            Intent i3 = new Intent(this, OperatorActivity.class);
            switchActivity(i3);
        }
    }

    private void switchActivity(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        ActivityCompat.finishAffinity(this);
    }

    public void login(View view) {
        String uname = user.getText().toString();
        String pswd = pass.getText().toString();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        Intent i = new Intent();
        boolean correct = false;
        if(uname.equals(getString(R.string.AdminUsername)) && pswd.equals(getString(R.string.AdminPassword))) {
            i = new Intent(this, AdminActivity.class);
            correct = true;
        } else if(uname.equals(getString(R.string.OperatorsUsername))&&pswd.equals(getString(R.string.OperatorsPassword))) {
            i = new Intent(this, OperatorActivity.class);
            correct = true;
        } else{
            Toast toast = Toast.makeText(this,"Wrong Username/Password",Toast.LENGTH_SHORT);
            toast.show();
            correct = false;
        }
        if (correct) {
            editor.putString("username", uname);
            editor.putString("password", pswd);
            editor.apply();
            switchActivity(i);
        }
    }
}
