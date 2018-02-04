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
    private int focus;
    private Subscription focusSub;
    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subscription);
        loadFromFile();

        Intent intent = getIntent();
        focus = Integer.valueOf(intent.getStringExtra(MainActivity.EXTRA_MESSAGE));

        nameText = (EditText) findViewById(R.id.entry_name);
        dateText = (EditText) findViewById(R.id.entry_date);
        chargeText = (EditText) findViewById(R.id.entry_charge);
        commentText = (EditText) findViewById(R.id.entry_comment);

        Button saveButton = (Button) findViewById(R.id.save);

        if(!(focus == -1)) {
            focusSub = subList.get(focus);
            nameText.setText(focusSub.getName());
            dateText.setText(format.format(focusSub.getDate()));
            chargeText.setText(String.valueOf(focusSub.getCharge()));
            commentText.setText(focusSub.getComment());
        }

        saveButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                boolean nameValid = true;
                boolean dateValid = true;
                boolean chargeValid = true;

                setResult(RESULT_OK);
                String name = nameText.getText().toString();
                if(name.isEmpty()) {
                    nameValid = false;
                }

                String rawDate = dateText.getText().toString();
                Date date = new Date();
                if(!rawDate.isEmpty() && rawDate.length() == 10) {
                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    format.setLenient(false);
                    try {
                        date = format.parse(rawDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        dateValid = false;
                    }
                } else {
                    dateValid = false;
                }

                String chargeString = chargeText.getText().toString();
                double charge = 0;
                if((!chargeString.isEmpty()) && (chargeString.indexOf(".") + 3 >= chargeString.length())) {
                    charge = Double.parseDouble(chargeString);
                }
                else {
                    chargeValid = false;
                }

                String comment = commentText.getText().toString();

                Log.i(nameText.getText().toString(), chargeText.getText().toString());
                Log.i(dateText.getText().toString(), commentText.getText().toString());
                if(dateValid && chargeValid && nameValid) {

                    Subscription newSub = new Subscription(name, date, charge, comment);
                    if(focus == -1) {
                        subList.add(newSub);
                    } else {
                        subList.set(focus, newSub);

                    }

                    saveInFile();

                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                }





            }
        });

        Button delButton = (Button) findViewById(R.id.del_button);

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!(focus == -1)) {

                    subList.remove(focus);
                    saveInFile();

                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                }

                else {
                    nameText.setText("");
                    dateText.setText("");
                    chargeText.setText("");
                    commentText.setText("");
                }
            }
        });


        Log.i("created", "created");

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
