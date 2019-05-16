package com.example.mad_project;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.support.constraint.Constraints.TAG;

public class StockFragment extends Fragment {
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
    Boolean first;
    FirebaseStorage storage;
    String pathToFile;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock, container, false);
        editDate = (EditText) view.findViewById(R.id.sdate);
        Button buttonSubmit = (Button) view.findViewById(R.id.buttonSubmit);
        item = (Spinner) view.findViewById(R.id.spinner);
        company = (EditText) view.findViewById(R.id.company);
        inout = (ToggleButton) view.findViewById(R.id.toggleButton);
        billno = (EditText) view.findViewById(R.id.bill);
        qty = (EditText) view.findViewById(R.id.editquantity);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        first = true;

        Button takePhoto = (Button) view.findViewById(R.id.buttonPhoto);

        context = view.getContext();
        Toast.makeText(context, "hello", Toast.LENGTH_LONG).show();

        System.out.println("hello world");
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchPictureTakerAction(v.getRootView());
            }

        });
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

//                new DatePickerDialog(context, , Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);
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
                first = false;

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
                        uploadToFirebase(pathToFile);
                    } else {
                        Toast.makeText(context,"error connecting",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        db.collection("stock").addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                    Log.d(TAG,doc.getType().toString());
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
                    if(document!= null && !first) {
                        Log.d(TAG, "New city: " + doc.getDocument().get("inout")+"inside if");
                        if (doc.getDocument().get("inout").toString().equals("OUTGOING")) {
                            item = doc.getDocument().get("item").toString();
                            item = item.substring(0, item.length() - 2);
                            Log.d(TAG,"outgoing"+ item);
                            docref = db.collection("inventory").document(item);
                            docref.update("processing", FieldValue.increment(-(Integer.parseInt(doc.getDocument().get("quantity").toString()))));


                        } else if (doc.getDocument().get("inout").toString().equals("INCOMING")) {
                            item = doc.getDocument().get("item").toString();
                            item = item.substring(0, item.length() - 2);
                            Log.d(TAG,"incoming"+ item);
                            docref = db.collection("inventory").document(item);
                            Log.d(TAG,"docref created"+ docref.getId());
                            docref.update("input", FieldValue.increment((Integer.parseInt(doc.getDocument().get("quantity").toString()))));
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
                            Log.d(TAG,"docref updated");

                        }
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



    private void dispatchPictureTakerAction(View view) {
        Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePic.resolveActivity(view.getContext().getPackageManager()) != null){
            File photoFile=null;
            String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//            photoFile=createPhotoFile();
            File sdCard =
                    Environment.getExternalStorageDirectory();
            File directory = new File(sdCard.getAbsolutePath()
                    + "/Pictures");
            System.out.println(directory.getAbsolutePath());
            try{
                photoFile= File.createTempFile(name,".jpg", directory);
            }
            catch(IOException e){
                Toast.makeText(view.getContext(), "Excep : ", Toast.LENGTH_SHORT).show();
                System.out.println("Excep : "+ e.toString());
                Log.d("mylog","Excep : "+ e.toString());
            }

            if (photoFile != null) {
                pathToFile = photoFile.getAbsolutePath();
                System.out.println(pathToFile);
                Uri photoURI = FileProvider.getUriForFile(view.getContext(),"com.mad.cameraandroid.fileprovider", photoFile);
                takePic.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePic,1);
//                uploadToFirebase(pathToFile);
            }
        }
    }
    private void submit(View v) {



    }

    private void uploadToFirebase(String path){
        String name = new SimpleDateFormat("yyyyMMdd").format(new Date());
        StorageReference storageRef = storage.getReference();
        StorageReference folderRef = storageRef.child(name);
        String imgName = new SimpleDateFormat("HHmmss").format(new Date());
        StorageReference imgRef = folderRef.child(imgName + ".jpg");
        InputStream stream = null;
        try {
            stream = new FileInputStream(new File(path));
            UploadTask uploadTask = imgRef.putStream(stream);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    System.out.println("Error uploading");
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    System.out.println("Done uploading");
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                    // ...
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
