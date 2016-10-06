package com.example.hva.seriesapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.util.List;

public class AddSeriesActivity extends AppCompatActivity {

    private EditText editText;
    private Button searchButton;
    private GridView gridView;
    private CustomAdapter adapter;
    private CustomAdapter searchAdapter;
    private List<Series> seriesList;
    private DataSource dataSource;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_series);

        editText = (EditText) findViewById(R.id.browse_shows);
        searchButton = (Button) findViewById(R.id.button_search);
        gridView = (GridView) findViewById(R.id.full_database);

        dataSource = new DataSource(this);
        seriesList = dataSource.getAllSeries();
        adapter = new CustomAdapter(seriesList, this);
        gridView.setAdapter(adapter);



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(AddSeriesActivity.this, "Show added", Toast.LENGTH_SHORT).show();

                Series series = (Series) parent.getItemAtPosition(position);
                dataSource.createWatchedSeries(series.getTitle(), series.getImageSource(), series.getDescription());




            }
        });


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(AddSeriesActivity.this, "You clicked the search button", Toast.LENGTH_SHORT).show();

                seriesList = dataSource.searchSeries(editText.getText().toString());

                System.out.println(seriesList);

                searchAdapter = new CustomAdapter(seriesList, AddSeriesActivity.this);
                gridView.setAdapter(searchAdapter);
                searchAdapter.notifyDataSetChanged();

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

}
