/*
 * AddSubscriptionActivity
 *
 * Feb 2, 2018
 */

package com.example.m_qui.msquigle_subbook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

/**
 * @author Matthew Quigley
 * @see Subscription
 * @see MainActivity
 */
public class AddSubscriptionActivity extends AppCompatActivity {
    /*
     * Add/Edit screen of the app
     * Allows for editing of new or old subscriptions
     * Is linked to a given subscription if focus != -1
     * Enforces correct formatting for subscriptions
     * Simple design where error checking is held in the save button
     * Issues: Improper formatting in landscape mode, need to implement
     * scroller correctly
     */
    private static final String FILENAME = "subs.sav";

    private EditText nameText;
    private EditText dateText;
    private EditText chargeText;
    private EditText commentText;
    private ArrayList<Subscription> subList;
    private int focus;
    private Subscription focusSub;
    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subscription);
        loadFromFile();

        //Get subscription focus
        Intent intent = getIntent();
        focus = Integer.valueOf(intent.getStringExtra(MainActivity.EXTRA_MESSAGE));

        nameText = findViewById(R.id.entry_name);
        dateText = findViewById(R.id.entry_date);
        chargeText = findViewById(R.id.entry_charge);
        commentText = findViewById(R.id.entry_comment);

        Button saveButton = findViewById(R.id.save);
        Button delButton = findViewById(R.id.del_button);

        if(!(focus == -1) && focus < subList.size()) {
            //Update text initial values if editing a subscription
            focusSub = subList.get(focus);
            nameText.setText(focusSub.getName());
            dateText.setText(format.format(focusSub.getDate()));
            chargeText.setText(String.format(Locale.CANADA,"%.2f", focusSub.getCharge()));
            commentText.setText(focusSub.getComment());
        }

        saveButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                /*
                 * Gathers all data from the four fields
                 * and goes through each and checks
                 * for improper formatting
                 * Name: Must not be empty
                 * Date: Must be yyyy-MM-dd
                 * Charge: Must be valid monetary amount
                 * Comment: No restriction
                 */

                setResult(RESULT_OK);

                boolean nameValid = true;
                boolean dateValid = true;
                boolean chargeValid = true;

                String name = nameText.getText().toString();
                String rawDate = dateText.getText().toString();
                Date date = new Date();
                String chargeString = chargeText.getText().toString();
                double charge = 0;
                String comment = commentText.getText().toString();

                if(name.isEmpty()) {
                    //Check for name validity
                    nameValid = false;
                }

                if(!rawDate.isEmpty() && rawDate.length() == 10) {
                    //Date has to be correct length
                    format.setLenient(false);
                    try {
                        //Has to parse as established by format
                        date = format.parse(rawDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        dateValid = false;
                    }
                } else {
                    dateValid = false;
                }

                if((!chargeString.isEmpty())
                    && ((!chargeString.contains(".")) || (chargeString.indexOf(".") + 3 >= chargeString.length()))) {
                    //Charge can't be empty and must not contain a . or no more than 2 decimal places

                    charge = Double.parseDouble(chargeString);
                } else {
                    chargeValid = false;
                }

                if(dateValid && chargeValid && nameValid) {
                    //All are valid so create new subscription

                    Subscription newSub = new Subscription(name, date, charge, comment);

                    if (focus == -1) {
                        //Not focused on a given subscription
                        subList.add(newSub);
                    } else {
                        subList.set(focus, newSub);
                    }

                    saveInFile();

                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);

                } else {
                    //Determine which sections are invalid and create a message

                    String errMsg = "";
                    if(!nameValid) {
                        errMsg = errMsg + "Please enter a valid name\n";
                    }
                    if(!dateValid) {
                        errMsg = errMsg + "Please enter a valid date: yyyy-MM-dd\n";
                    }
                    if(!chargeValid) {
                        errMsg = errMsg + "Please enter a valid cost\n";
                    }
                    errMsg = errMsg.trim();

                    Toast toast = Toast.makeText(getApplicationContext(), errMsg, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Deletes the given subscription or clears current values

                if(!(focus == -1)) {

                    subList.remove(focus);
                    saveInFile();

                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    nameText.setText("");
                    dateText.setText("");
                    chargeText.setText("");
                    commentText.setText("");
                }
            }
        });
    }

    private void loadFromFile() {
        //Load a given JSON file

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
        //Save SubList to a JSON file

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
