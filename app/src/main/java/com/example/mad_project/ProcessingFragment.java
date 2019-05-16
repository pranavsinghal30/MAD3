package com.example.mad_project;

import android.app.DatePickerDialog;
import android.content.Context;
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
import android.widget.Chronometer;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.support.constraint.Constraints.TAG;

public class ProcessingFragment extends Fragment {
    Chronometer chronometer;
    boolean running = false;
    Button submit_button;
    private Calendar myCalendar = Calendar.getInstance();
    String dateFormat = "dd.MM.yyyy";
    DatePickerDialog.OnDateSetListener date;
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.GERMAN);
    private FirebaseFirestore db;
    Spinner itemsSpinner;
    EditText date_edittext, quantity_edittext, rejection_edittext, starttime_edittext, endtime_edittext;
    Context context;
    private long pauseOffset;
    Boolean first;
//    View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_processing,container,false);
        chronometer = (Chronometer) v.findViewById(R.id.chronometer);
        db = FirebaseFirestore.getInstance();
        itemsSpinner = (Spinner) v.findViewById(R.id.itemsSpinner);
        date_edittext = (EditText)  v.findViewById(R.id.date_edittext);
        submit_button = (Button) v.findViewById(R.id.processSubmit_button);
        starttime_edittext = (EditText) v.findViewById(R.id.startTime_edittext);
        endtime_edittext = (EditText) v.findViewById(R.id.endTime_edittext);
        quantity_edittext = (EditText) v.findViewById(R.id.quantity_edittext);
        rejection_edittext = (EditText) v.findViewById(R.id.rejected_edittext);
        context = v.getContext();
        first = true;

        // reading the stock from firebase db
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
                                ArrayAdapter<String> arr = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, lis);
                                itemsSpinner.setAdapter(arr);
                                Log.d(TAG,s);
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        //Date picker.
        long currentdate = System.currentTimeMillis();
        String dateString = sdf.format(currentdate);
        date_edittext.setText(dateString);

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
        date_edittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
//        submit_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View vv) {
//                Toast.makeText(v.getContext(), date_edittext.getText().toString(), Toast.LENGTH_SHORT).show();
//            }
//        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first = false;
                Toast.makeText(context,"in submit",Toast.LENGTH_LONG).show();

                //Make sure all the fields are filled with values
                if (TextUtils.isEmpty(date_edittext.getText().toString()) ||
                        TextUtils.isEmpty(itemsSpinner.getSelectedItem().toString()) ||
                        TextUtils.isEmpty(starttime_edittext.getText().toString()) ||
                        TextUtils.isEmpty(endtime_edittext.getText().toString()) ||
                        TextUtils.isEmpty(quantity_edittext.getText()) ||
                        TextUtils.isEmpty(rejection_edittext.getText())) {
                    Toast.makeText(context, "All fields are mandatory.", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    DocumentReference newentry = db.collection("process").document();
                    if(newentry != null) {
                        Toast.makeText(context, "reference obtained", Toast.LENGTH_LONG).show();
                        Long quantity = Long.parseLong(quantity_edittext.getText().toString());
                        Long rejection = Long.parseLong(rejection_edittext.getText().toString());
                        String item = itemsSpinner.getSelectedItem().toString();
                        String startTime = starttime_edittext.getText().toString();
                        String endTime = endtime_edittext.getText().toString();
                        String dateString = date_edittext.getText().toString();
                        Date date = new Date();
                        Timestamp firebasedate = new Timestamp(myCalendar.getTime());
                        Processing processing = new Processing(item, quantity, rejection, date, startTime, endTime);
                        Toast.makeText(context, "object created", Toast.LENGTH_LONG).show();
                        newentry.set(processing);
                        Toast.makeText(context, "added entry", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context,"error connecting", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        //start_button = (Button) v.findViewById(R.id.button);
        //pause_button = (Button) v.findViewById(R.id.pause_button);
        //reset_button = (Button) v.findViewById(R.id.reset_button);
        //date_text = (TextView) v.findViewById(R.id.text_view_date);


        /*String date_n = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
        date_text.setText(date_n);

        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_chronometer();
            }
        });

        pause_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pause_chronometer();
            }
        });

        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset_chronometer();
            }
        });*/
        db.collection("process").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                DocumentReference docref;
                String item;

                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }


                for (DocumentChange doc : value.getDocumentChanges()) {
                    QueryDocumentSnapshot document = doc.getDocument();
                    Log.d(TAG, doc.getType().toString());
                    switch (doc.getType()) {
                        case ADDED:
                            Log.d(TAG, "New city: " + doc.getDocument().getData());
                            break;
                        case MODIFIED:
                            Log.d(TAG, "Modified city: " + doc.getDocument().getData());
                            break;
                        case REMOVED:
                            Log.d(TAG, "Removed city: " + doc.getDocument().getData());
                            break;
                    }
                    if (document != null && !first) {
                        Log.d(TAG, "New city: " + doc.getDocument() + "inside if");
                        item = doc.getDocument().get("item").toString();
                        item = item.substring(0, item.length() - 2);
                        Log.d(TAG, "incoming" + item);
                        docref = db.collection("inventory").document(item);
                        Log.d(TAG, "docref created" + docref.getId());
                        docref.update("input", FieldValue.increment(-(Integer.parseInt(doc.getDocument().get("quantity").toString()))));
                        docref.update("processing", FieldValue.increment((Integer.parseInt(doc.getDocument().get("quantity").toString()))));
                        docref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });
                        Log.d(TAG, "docref updated");

                    }
                }
            }
        });


        return v;
    }
/*
    public void start_chronometer(){
        if(!running){
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running=true;
        }

    }

    public void pause_chronometer(){
        if(running){
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running=false;
        }

    }

    public void reset_chronometer(){
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }
*/
    private void updateDate() {
        date_edittext.setText(sdf.format(myCalendar.getTime()));
//        date1s = (sdf.format(myCalendar.getTime()));
    }

}