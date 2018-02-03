package com.example.m_qui.msquigle_subbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String FILENAME = "subs.sav";


    private ArrayList<Subscription> subList;
    private ArrayAdapter<Subscription> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("hello", "hello");


        Button addButton = (Button) findViewById(R.id.add);


        addButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                setResult(RESULT_OK);
                setContentView(R.layout.activity_add_subscription);
            }
        });

    }
}
