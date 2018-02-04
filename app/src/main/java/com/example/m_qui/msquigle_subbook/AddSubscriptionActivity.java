package com.example.m_qui.msquigle_subbook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.provider.Telephony.Mms.Part.FILENAME;

public class AddSubscriptionActivity extends AppCompatActivity {

    private static final String FILENAME = "subs.sav";

    private EditText nameText;
    private EditText dateText;
    private EditText chargeText;
    private EditText commentText;
    private DatePicker datePicker;
    private ArrayList<Subscription> subList;
    private ArrayAdapter<Subscription> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subscription);
        loadFromFile();

        nameText = (EditText) findViewById(R.id.entry_name);
        dateText = (EditText) findViewById(R.id.entry_date);
        chargeText = (EditText) findViewById(R.id.entry_charge);
        commentText = (EditText) findViewById(R.id.entry_comment);

        Button saveButton = (Button) findViewById(R.id.save);

        saveButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                setResult(RESULT_OK);
                String name = nameText.getText().toString();
                double charge = Double.parseDouble(chargeText.getText().toString());
                String rawDate = dateText.getText().toString();
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                format.setLenient(false);
                Date date = new Date();
                try {
                    date = format.parse(rawDate);
                } catch(ParseException e) {
                    e.printStackTrace();
                    boolean valid = false;
                }
                String comment = commentText.getText().toString();

                Log.i(nameText.getText().toString(), chargeText.getText().toString());
                Log.i(dateText.getText().toString(), commentText.getText().toString());
                Subscription newSub = new Subscription(name, date, charge, comment);
                subList.add(newSub);

                saveInFile();

                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);


            }
        });

        Log.i("created", "created");

        Intent intent = getIntent();
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
