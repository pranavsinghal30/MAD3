package com.example.mad_project;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.support.constraint.Constraints.TAG;

public class StockFragment extends Fragment {
    EditText editDate;
    Spinner item, inout;
    Calendar myCalendar = Calendar.getInstance();
    String dateFormat = "dd.MM.yyyy";
    DatePickerDialog.OnDateSetListener date;
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.GERMAN);
    EditText qty, company, billno, sdate;
    Context context;
    private DatabaseReference mDatabase;
    ValueEventListener valueEventListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock, container, false);
        editDate = (EditText) view.findViewById(R.id.sdate);
        Button buttonSubmit = (Button) view.findViewById(R.id.buttonSubmit);
        item = (Spinner) view.findViewById(R.id.spinner);
        company = (EditText) view.findViewById(R.id.company);
        inout = (Spinner) view.findViewById(R.id.spinner2);
        sdate = (EditText) view.findViewById(R.id.sdate);
        billno = (EditText) view.findViewById(R.id.bill);
        qty = (EditText) view.findViewById(R.id.editquantity);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        System.out.println("hello world");
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stock st = dataSnapshot.getValue(stock.class);
                System.out.println(st);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());

            }
        };
        mDatabase.addValueEventListener(valueEventListener);

        //etime = (EditText) findViewById(R.id.etime);
        //stime = (EditText) findViewById(R.id.stime);
        // init - set date to current date
        long currentdate = System.currentTimeMillis();
        String dateString = sdf.format(currentdate);
        editDate.setText(dateString);
        context = view.getContext();

        // set calendar date and update editDate
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                /// TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDate();
            }

        };
        editDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Make sure all the fields are filled with values
                if (TextUtils.isEmpty(sdate.getText().toString()) ||
                        TextUtils.isEmpty(item.getSelectedItem().toString()) ||
                        TextUtils.isEmpty(billno.getText().toString())) {
                    Toast.makeText(context, "All fields are mandatory.", Toast.LENGTH_LONG).show();
                    return;
                }

            }
        });

        return view;
    }

    private void updateDate() {
        editDate.setText(sdf.format(myCalendar.getTime()));
    }




}



