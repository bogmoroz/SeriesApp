package com.example.hva.seriesapp;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String PREF_DATA = "pref_data";
    private GridView gridView;
    private DataSource dataSource;
    private ArrayAdapter<Series> seriesArrayAdapter;
    private CustomAdapter adapter;
    List<GridItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gridView = (GridView) findViewById(R.id.main_grid);
        TextView emptyView = (TextView) findViewById(R.id.main_grid_empty);
        gridView.setEmptyView(emptyView);

          dataSource = new DataSource(this);
          List<Series> series = dataSource.getAllWatchedSeries();
          adapter = new CustomAdapter(series, this);
          gridView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        boolean isDatabaseFilled = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(PREF_DATA, false);

        if (!isDatabaseFilled) {

            long seriesId = dataSource.createSeries("Game of thrones", R.drawable.game_of_thrones);
            Series newSeries = dataSource.getSeries(seriesId);
            series.add(newSeries);

            seriesId = dataSource.createSeries("Breaking Bad", R.drawable.breaking_bad);
            newSeries = dataSource.getSeries(seriesId);
            series.add(newSeries);

            seriesId = dataSource.createSeries("Fargo", R.drawable.fargo);
            newSeries = dataSource.getSeries(seriesId);
            series.add(newSeries);


            //adapter.notifyDataSetChanged();
            isDatabaseFilled = true;
        }

        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putBoolean(PREF_DATA, isDatabaseFilled)
                .apply();


        registerForContextMenu(gridView);

        

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, AddSeriesActivity.class);
                startActivityForResult(intent, 1);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        dataSource = new DataSource(this);
        List<Series> series = dataSource.getAllWatchedSeries();
        adapter = new CustomAdapter(series, this);
        gridView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

    }
}
