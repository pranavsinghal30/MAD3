package com.example.mad_project;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class adduser extends Fragment {

    private FirebaseFirestore db;
    Button submit;
    EditText user, pass, repass;
    String uname, pswd, rpswd, types;
    users u;
    Context context;
    Spinner type;
    String[] things={"Operator","Administrator"};


    public adduser() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_adduser, container, false);
        submit=(Button)view.findViewById(R.id.sub);
        user=(EditText)view.findViewById(R.id.username);
        pass=(EditText)view.findViewById(R.id.password);
        repass=(EditText)view.findViewById(R.id.repeat_password);
        type = (Spinner) view.findViewById(R.id.typespinner);
        context = view.getContext();
        db = FirebaseFirestore.getInstance();
        ArrayAdapter<String> arr = new ArrayAdapter<String>(context,R.layout.support_simple_spinner_dropdown_item,things);
        type.setAdapter(arr);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(context,"in submit",Toast.LENGTH_LONG).show();
                //first = false;

                //Make sure all the fields are filled with values
                if (TextUtils.isEmpty(user.getText().toString()) ||
                        TextUtils.isEmpty(pass.getText().toString()) ||
                        TextUtils.isEmpty(type.getSelectedItem().toString())||
                        TextUtils.isEmpty(repass.getText().toString())) {
                    Toast.makeText(context, "All fields are mandatory.", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    DocumentReference newentry = db.collection("users").document(user.getText().toString());
                    if(newentry != null) {
                        Toast.makeText(context, "reference obtained", Toast.LENGTH_LONG).show();
                        uname = user.getText().toString();
                        pswd = pass.getText().toString();
                        rpswd = repass.getText().toString();
                        types = type.getSelectedItem().toString();

                        if(pswd.equals(rpswd)){
                            u = new users(uname, pswd, types);
                        }
                        else{
                            Toast.makeText(context,"wrong username and pass", Toast.LENGTH_SHORT).show();
                        }

                        Toast.makeText(context, "object created", Toast.LENGTH_LONG).show();
                        newentry.set(u);
                        Toast.makeText(context, "added entry", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context,"error connecting",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        return  view;

    }

}
