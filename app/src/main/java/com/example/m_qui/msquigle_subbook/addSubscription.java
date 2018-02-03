package com.example.m_qui.msquigle_subbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class addSubscription extends AppCompatActivity {

    Button backButton = (Button) findViewById(R.id.back);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subscription);
        Log.e("bye", "bye");

        backButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                setResult(RESULT_OK);
                setContentView(R.layout.activity_main);
            }
        });
    }
}
