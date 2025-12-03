package edu.ewubd.calculatorassignment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class History extends AppCompatActivity {

    ListView listView;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listView = findViewById(R.id.listHistory);
        btnBack = findViewById(R.id.btnBack);

        // SharedPreferences  history load
        SharedPreferences sp = getSharedPreferences("calculator_history", MODE_PRIVATE);
        Set<String> set = sp.getStringSet("history_set", new HashSet<String>());
        ArrayList<String> historyList = new ArrayList<>(set);

        // Reverse chronological order
        Collections.sort(historyList, Collections.reverseOrder());

        // ListView show
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, historyList);
        listView.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());
    }
}
