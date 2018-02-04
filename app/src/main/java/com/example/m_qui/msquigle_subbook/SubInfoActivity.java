package com.example.m_qui.msquigle_subbook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class SubInfoActivity extends AppCompatActivity {

    private static final String FILENAME = "subs.sav";

    private ArrayList<Subscription> subList;
    private Subscription focusSub;
    private TextView nameText;
    private TextView dateText;
    private TextView chargeText;
    private TextView commentText;
    private int focus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_info);
        nameText = (TextView) findViewById(R.id.name_info);
        dateText = (TextView) findViewById(R.id.date_info);
        chargeText = (TextView) findViewById(R.id.charge_info);
        commentText = (TextView) findViewById(R.id.comment_info);

        Button delButton = (Button) findViewById(R.id.del_button);

        Intent intent = getIntent();
        focus = Integer.valueOf(intent.getStringExtra(MainActivity.EXTRA_MESSAGE));
        loadFromFile();
        focusSub = subList.get(focus);
        nameText.setText(focusSub.getName());
        dateText.setText(focusSub.getDate().toString());
        chargeText.setText(String.valueOf(focusSub.getCharge()));
        commentText.setText(focusSub.getComment());

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                subList.remove(focus);
                saveInFile();

                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });


    }

    private void loadFromFile() {

        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            // Taken https://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
            // 2018-01-23
            Type listType = new TypeToken<ArrayList<Subscription>>(){}.getType();
            subList = gson.fromJson(in, listType);

        } catch (FileNotFoundException e) {
            subList = new ArrayList<Subscription>();
            Log.i("No File", "Created New File");
        } catch (IOException e) {
            throw new RuntimeException();
        }

    }

    private void saveInFile() {
        try {

            FileOutputStream fos = openFileOutput(FILENAME,
                    Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(subList, out);
            out.flush();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }
}
