package com.example.mad_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText user,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);
        //String s=getString(R.string.AdminUsername);

    }

    public void login(View view) {

        String uname=user.getText().toString();
        String pswd=pass.getText().toString();

        //String s=getString(R.string.AdminUsername);
        //s.toString();

        if(uname.equals(getString(R.string.AdminUsername)) && pswd.equals(getString(R.string.AdminPassword))) {
            Intent i1 = new Intent(this, AdminActivity.class);
            startActivity(i1);
        }



        else if(uname.equals(getString(R.string.QualityUsername))&&pswd.equals(getString(R.string.QualityPassword))) {
            Intent i2 = new Intent(this, QualityActivity.class);
            startActivity(i2);
        }



        else if(uname.equals(getString(R.string.OperatorsUsername))&&pswd.equals(getString(R.string.OperatorsPassword))) {
            Intent i3 = new Intent(this, OperatorActivity.class);
            startActivity(i3);
        }
        else{
            Toast toast=Toast.makeText(this,"Wrong Username/Password",Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
