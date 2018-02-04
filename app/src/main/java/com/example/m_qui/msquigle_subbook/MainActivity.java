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

public class MainActivity extends AppCompatActivity {

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

                gotoAddActivity(view, i);
            }
        });

        Button addButton = (Button) findViewById(R.id.add);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoAddActivity(view, -1);
            }
        });

    }

    public void onStart() {

        super.onStart();

        loadFromFile();
        double total = 0;
        for(int i = 0; i < subList.size();i++) {
            total = total + subList.get(i).getCharge();
        }

        String costDisplay = "Total Monthly Cost: $" + String.format(Locale.CANADA,"%.2f", total);
        costView.setText(costDisplay);

        adapter = new ArrayAdapter<Subscription>(this, R.layout.sub_layout, subList);

        subListView.setAdapter(adapter);


    }

    public void onResume() {
        super.onResume();

        adapter.notifyDataSetChanged();


    }

    public void gotoAddActivity(View view, int i) {

        Intent intent = new Intent(this, AddSubscriptionActivity.class);
        String sending = String.valueOf(i);
        intent.putExtra(EXTRA_MESSAGE, sending);
        startActivity(intent);

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
