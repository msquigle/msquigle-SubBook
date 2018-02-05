/*
 * MainActivity
 *
 * Feb 2, 2018
 */

package com.example.m_qui.msquigle_subbook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
import java.util.ArrayList;
import java.util.Locale;

/**
 * @author Matthew Quigley
 * @see Subscription
 * @see AddSubscriptionActivity
 */
public class MainActivity extends AppCompatActivity {
    /*
     * Main screen of the app
     * Displays all current subscriptions and
     * provides access to the add/edit page
     * Design: Three areas, a button that leads to
     * the add/edit page. The view of all current subscriptions
     * and finally the total of their costs at the bottom
     * Maintains simplicity and cleanliness to not clutter
     * and do what would be expected. Addition could
     * be popup menu on itemlongclick to delete from there
     * Issue: Sometimes after minor edits the json
     * save file becomes unreadable giving only null
     * object pointers when loading, needing to then
     * wipe the data or change the file.
     */
    public static final String EXTRA_MESSAGE = "com.example.m_qui.msquigle_subbook.MESSAGE";
    private static final String FILENAME = "subs.sav";
    private ListView subListView;
    private TextView costView;

    private ArrayList<Subscription> subList;
    private ArrayAdapter<Subscription> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        subListView = findViewById(R.id.subs);
        costView = findViewById(R.id.cost_summary);

        subListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /*
                 * When a subscription is clicked pass the index and change activities
                 */
                gotoAddActivity(view, i);
            }
        });

        Button addButton = findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                 * Change to add/edit with index -1 to indicate no reference to subscription
                 */
                gotoAddActivity(view, -1);
            }
        });

    }

    public void onStart() {
        /*
         * Initialize listView with subs and set total cost
         */
        super.onStart();
        loadFromFile();

        adapter = new ArrayAdapter<Subscription>(this, R.layout.sub_layout, subList);
        subListView.setAdapter(adapter);

        double total = 0;

        for (int i = 0; i < subList.size(); i++) {
            total = total + subList.get(i).getCharge();
        }

        String costDisplay = "Total Monthly Cost: $" + String.format(Locale.CANADA,"%.2f", total);
        costView.setText(costDisplay);
    }

    public void gotoAddActivity(View view, int i) {
        /*
         * Change to the add/edit activity with the index of the subscription
         */
        Intent intent = new Intent(this, AddSubscriptionActivity.class);
        String sending = String.valueOf(i);
        intent.putExtra(EXTRA_MESSAGE, sending);
        startActivity(intent);

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
}