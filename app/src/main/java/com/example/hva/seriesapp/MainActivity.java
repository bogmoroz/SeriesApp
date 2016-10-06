package com.example.hva.seriesapp;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
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
    public static final String SERIES_ID = "seriesId";
    private GridView gridView;
    private DataSource dataSource;
    private ArrayAdapter<Series> seriesArrayAdapter;
    private CustomAdapter adapter;
    private List<Series> series;


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
        series = dataSource.getAllWatchedSeries();
        adapter = new CustomAdapter(series, this);
        gridView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        boolean isDatabaseFilled = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(PREF_DATA, false);

        if (!isDatabaseFilled) {

            long seriesId = dataSource.createSeries("Game of Thrones", R.drawable.game_of_thrones, "All men must die!");
            Series newSeries = dataSource.getSeries(seriesId);
            series.add(newSeries);

            seriesId = dataSource.createSeries("Breaking Bad", R.drawable.breaking_bad, "Meth and stuff");
            newSeries = dataSource.getSeries(seriesId);
            series.add(newSeries);

            seriesId = dataSource.createSeries("Fargo", R.drawable.fargo, "What if you are right and they are wrong?");
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


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Series clickedSeries = (Series) gridView.getItemAtPosition(position);
                long seriesId = clickedSeries.getId();
                Intent detailsIntent = new Intent(MainActivity.this, ShowDetailActivity.class);

                detailsIntent.putExtra(SERIES_ID, seriesId);
                startActivity(detailsIntent);

            }
        });


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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        getMenuInflater().inflate(R.menu.context_menu, menu);
        MenuItem deleteButton = menu.findItem(R.id.context_menu_delete_item);
        deleteButton.setTitle("Delete show and all your notes?");

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo itemInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getItemId() == R.id.context_menu_delete_item) {


            Series seriesToDelete = adapter.getItem(itemInfo.position);
            dataSource.deleteWatchedSeries(seriesToDelete);

            dataSource = new DataSource(this);
            List<Series> series = dataSource.getAllWatchedSeries();
            CustomAdapter newAdapter = new CustomAdapter(series, this);
            gridView.setAdapter(newAdapter);

//            finish();
//            startActivity(getIntent());
            return true;

        }

//        gridView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();

        return super.onContextItemSelected(item);
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
