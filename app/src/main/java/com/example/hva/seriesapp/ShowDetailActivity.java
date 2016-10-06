package com.example.hva.seriesapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ShowDetailActivity extends AppCompatActivity {

    private TextView showTitle;
    private TextView showDescription;
    private EditText editDescription;
    private Button updateButton;
    private DataSource dataSource;
    private Series series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);


        showTitle = (TextView) findViewById(R.id.details_show_title);
        showDescription = (TextView) findViewById(R.id.details_show_description);
        editDescription = (EditText) findViewById(R.id.details_show_edit_text);
        updateButton = (Button) findViewById(R.id.button_update_description);

        dataSource = new DataSource(this);
        final long seriesId = getIntent().getLongExtra(MainActivity.SERIES_ID, -1);
        series = dataSource.getWatchedSeries(seriesId);

        showTitle.setText(series.getTitle());
        showDescription.setText(series.getDescription());

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                series.setDescription(editDescription.getText().toString());
                showDescription.setText(editDescription.getText().toString());
                dataSource.updateWatchedSeries(series);
                Toast.makeText(ShowDetailActivity.this, "Description updated", Toast.LENGTH_SHORT).show();

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
