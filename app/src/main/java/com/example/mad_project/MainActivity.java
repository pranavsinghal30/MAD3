package com.example.mad_project;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    EditText user, pass;
    private FirebaseFirestore db;
    private String p = null, type = null;
    boolean correctCredentials = false;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
        user = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);
        db = FirebaseFirestore.getInstance();
        context = this;
        //String s=getString(R.string.AdminUsername);
        sharedPreferences = getSharedPreferences("credentials", Activity.MODE_PRIVATE);
        String userName = sharedPreferences.getString("username", null);
        String password = sharedPreferences.getString("password", null);
        if (userName != null && password != null) {
//            Toast.makeText(this, "already logged in", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "sharedPref", Toast.LENGTH_SHORT).show();
            checkCredentials(userName, password);
//            switchActivity();
        }
    }

    private Intent nextActivity () {
        Intent i = null;
        switch (type) {
            case "Operator":
                i = new Intent(this, OperatorActivity.class);
                break;
            case "Administrator":
            case "Admin":
                i = new Intent(this, AdminActivity.class);
                break;
            default:
                break;
        }
        return i;
    }

    private void checkCredentials(String uname, String pswd) {
        p = null;
        type = null;
        final String password = pswd, username = uname;
        Toast.makeText(this, String.format("check cred %s, %s", uname, pswd), Toast.LENGTH_SHORT).show();
        db.collection("users").document(uname)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                                p = (String) document.get("password");
                                type = (String) document.get("type");
                                System.out.println(String.format(".......P: %s, type: %s.....", p, type));
                                if (p.equals(password)) {
                                    Intent intent = nextActivity();
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("username", username);
                                    editor.putString("password", p);
                                    editor.apply();
                                    Toast.makeText(context, String.format("U: %s, P: %s", username, p), Toast.LENGTH_SHORT).show();
                                    switchActivity(intent);
                                } else {
                                    Toast.makeText(context, String.format("wrong password P: %s", p), Toast.LENGTH_SHORT).show();
                                }
                        } else {
                            Toast.makeText(context, "task unsuccessful", Toast.LENGTH_SHORT).show();
                            System.out.println(String.format("task unsuccessful", p, type));
                        }
                    }
                });
//        if (p != null) {
//            Toast.makeText(this, String.format("checkCred: %s, %s", p, type), Toast.LENGTH_SHORT).show();
//            return p.equals((pswd));
//        }
//        Toast.makeText(this, String.format("checkCred: wrong"), Toast.LENGTH_SHORT).show();
//
//        return false;
////
//        Toast.makeText(this, String.format("UserName: %s, Password: %s", uname, pswd), Toast.LENGTH_SHORT).show();
//        if(uname.equals(getString(R.string.AdminUsername)) && pswd.equals(getString(R.string.AdminPassword))) {
//            Toast.makeText(this, String.format("Starting adminact UserName: %s, Password: %s", uname, pswd), Toast.LENGTH_SHORT).show();
//            Intent i1 = new Intent(this, AdminActivity.class);
//            switchActivity(i1);
//        } else if(uname.equals(getString(R.string.OperatorsUsername))&&pswd.equals(getString(R.string.OperatorsPassword))) {
//            Intent i3 = new Intent(this, OperatorActivity.class);
//            switchActivity(i3);
//        }
    }


    private void switchActivity() {
        Intent intent = null;
        switch (type) {
            case "Operator":
                intent = new Intent(this, OperatorActivity.class);
                break;
            case "Admin":
                intent = new Intent(this, AdminActivity.class);
                break;
            default:
                break;
        }
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            ActivityCompat.finishAffinity(this);
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
        boolean valid = false;
        checkCredentials(uname, pswd);
//        if(uname.equals(getString(R.string.AdminUsername)) && pswd.equals(getString(R.string.AdminPassword))) {
//            i = new Intent(this, AdminActivity.class);
//            correct = true;
//        } else if(uname.equals(getString(R.string.OperatorsUsername))&&pswd.equals(getString(R.string.OperatorsPassword))) {
//            i = new Intent(this, OperatorActivity.class);
//            correct = true;
//        } else{
//            Toast toast = Toast.makeText(this,"Wrong Username/Password",Toast.LENGTH_SHORT);
//            toast.show();
//            correct = false;
//        }
//        if (valid) {
//            editor.putString("username", uname);
//            editor.putString("password", pswd);
//            editor.apply();
//            switchActivity();
//        } else {
//            Toast toast = Toast.makeText(this,"Wrong Username/Password",Toast.LENGTH_SHORT);
//            toast.show();
//        }
    }
}
