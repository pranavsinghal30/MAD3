package com.example.mad_project;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.support.constraint.Constraints.TAG;

public class StockFragment extends Fragment implements DialogInterface.OnClickListener {
    Spinner item;
    ToggleButton inout;
    Calendar myCalendar = Calendar.getInstance();
    String dateFormat = "dd.MM.yyyy";
    DatePickerDialog.OnDateSetListener date;
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.GERMAN);
    EditText qty, company, billno, editDate;
    Context context;
    private FirebaseFirestore db;
    ValueEventListener valueEventListener;
    Timestamp firebasedate;
    String companys, billnos, items, date1s, inouts;
    Long quantity;
    stock stocks ;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock2, container, false);
        editDate = (EditText) view.findViewById(R.id.sdate);
        Button buttonSubmit = (Button) view.findViewById(R.id.buttonSubmit);
        item = (Spinner) view.findViewById(R.id.spinner);
        company = (EditText) view.findViewById(R.id.company);
        inout = (ToggleButton) view.findViewById(R.id.toggleButton);
        billno = (EditText) view.findViewById(R.id.bill);
        qty = (EditText) view.findViewById(R.id.editquantity);
        db = FirebaseFirestore.getInstance();


        context = view.getContext();
        Toast.makeText(context, "hello", Toast.LENGTH_LONG).show();

        System.out.println("hello world");
        //gets a list of items and populates the spinner
        db.collection("stock").document("itemlist")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            {

                                String s = document.getData().toString();
                                String [] lis = s.split(",?.=");
                                List<String> list = new ArrayList<String>(Arrays.asList(lis));
                                list.remove("{");
                                lis = list.toArray(new String[0]);
                                ArrayAdapter <String> arr = new ArrayAdapter<String>(context,R.layout.support_simple_spinner_dropdown_item,lis);
                                item.setAdapter(arr);
                                Log.d(TAG,s);
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        //Date picker
        long currentdate = System.currentTimeMillis();
        String dateString = sdf.format(currentdate);
        editDate.setText(dateString);

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

        //submit the info to firestore
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"in submit",Toast.LENGTH_LONG).show();

                //Make sure all the fields are filled with values
                if (TextUtils.isEmpty(editDate.getText().toString()) ||
                        TextUtils.isEmpty(item.getSelectedItem().toString()) ||
                        TextUtils.isEmpty(billno.getText().toString())) {
                    Toast.makeText(context, "All fields are mandatory.", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    DocumentReference newentry = db.collection("stock").document();
                    if(newentry != null) {
                        Toast.makeText(context, "refernce obtained", Toast.LENGTH_LONG).show();
                        companys = company.getText().toString();
                        billnos = billno.getText().toString();
                        quantity = Long.parseLong(qty.getText().toString());
                        inouts = inout.getText().toString();
                        items = item.getSelectedItem().toString();
                        firebasedate = new Timestamp(myCalendar.getTime());
                        stocks = new stock(companys, billnos, quantity, items, firebasedate, inouts);
                        Toast.makeText(context, "object created", Toast.LENGTH_LONG).show();
                        newentry.set(stocks);
                        Toast.makeText(context, "added entry", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context,"error connecting",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        return view;
    }

    private void updateDate() {
        editDate.setText(sdf.format(myCalendar.getTime()));
//        date1s = (sdf.format(myCalendar.getTime()));
    }

    private void submit(View v) {



    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}



