package app.univers7.ultra_instinct;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}