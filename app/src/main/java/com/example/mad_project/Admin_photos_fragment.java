package com.example.mad_project;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Admin_photos_fragment extends Fragment {
    private FirebaseFirestore db;
    FirebaseStorage storage;
    List<Map<String, ImageView>> imageList = new ArrayList<Map<String, ImageView>>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_photos, container, false);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        String name = new SimpleDateFormat("yyyyMMdd").format(new Date());
        StorageReference storageRef = storage.getReference();
        StorageReference folderRef = storageRef.child(name);
        folderRef.getStorage();


        GridView gridview = (GridView) view.findViewById(R.id.photo_grid);
        SimpleAdapter adapter;
//        gridview.setAdapter(adapter);
        return view;
    }
}
