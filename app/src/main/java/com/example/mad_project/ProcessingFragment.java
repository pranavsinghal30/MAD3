package com.example.mad_project;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProcessingFragment extends Fragment {
    Chronometer chronometer;
    boolean running = false;
    Button start_button, pause_button, reset_button;
    TextView date_text;
    ProgressBar pb;
    private long pauseOffset;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_processing,container,false);
        chronometer = (Chronometer) v.findViewById(R.id.chronometer);
        start_button = (Button) v.findViewById(R.id.button);
        pause_button = (Button) v.findViewById(R.id.pause_button);
        reset_button = (Button) v.findViewById(R.id.reset_button);
        date_text = (TextView) v.findViewById(R.id.text_view_date);


        String date_n = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
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
        });
        return v;
    }

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
}