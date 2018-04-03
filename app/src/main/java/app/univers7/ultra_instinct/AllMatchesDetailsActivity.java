package app.univers7.ultra_instinct;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AllMatchesDetailsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    int id;
    String player1;
    String player2;
    Double latitude;
    Double longitude;
    String date;
    String description;
    int status;

    String image_ids[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_matches_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView textViewID = (TextView) findViewById(R.id.TextViewMatchID);
        TextView textViewPlayer1 = (TextView) findViewById(R.id.TextViewMatchPlayer1);
        TextView textViewPlayer2 = (TextView) findViewById(R.id.TextViewMatchPlayer2);
        TextView textViewLatitude = (TextView) findViewById(R.id.TextViewMatchLatitude);
        TextView textViewLongitude = (TextView) findViewById(R.id.TextViewMatchLongitude);
        TextView textViewDate = (TextView) findViewById(R.id.TextViewMatchDate);
        TextView textViewDescription = (TextView) findViewById(R.id.TextViewMatchDescription);
        TextView textViewStatus = (TextView) findViewById(R.id.TextViewMatchStatus);

        Bundle extras = getIntent().getExtras(); // on récupère les données transférées par l'intent de la précédente activité
        if(extras!=null)
        {
            id = extras.getInt("id");
            player1 = extras.getString("player1");
            player2 = extras.getString("player2");
            latitude = extras.getDouble("latitude");
            longitude = extras.getDouble("longitude");
            date = extras.getString("date");
            description = extras.getString("description"); // on récupère l'opération précédente
            status = extras.getInt("status");
        }

        textViewID.setText(String.valueOf(id));
        textViewPlayer1.setText(player1);
        textViewPlayer2.setText(player2);
        textViewLatitude.setText(String.valueOf(latitude));
        textViewLongitude.setText(String.valueOf(longitude));
        textViewDate.setText(date);
        textViewDescription.setText(description);
        textViewStatus.setText(String.valueOf(status));

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_matches) {
            Intent intent = new Intent(this, AllMatchesActivity.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_recent_matches) {
            Intent intent = new Intent(this, PreviousMatchesActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_players) {
            Intent intent = new Intent(this, PlayersActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void matchButtonsManager(View view)
    {
        switch(view.getId())
        {
            case R.id.btn_editMatch:
                Intent intent_editmatch = new Intent(AllMatchesDetailsActivity.this, EditMatchActivity.class);
                intent_editmatch.putExtra("id", id);
                intent_editmatch.putExtra("player1", player1);
                intent_editmatch.putExtra("player2", player2);
                intent_editmatch.putExtra("status", status);
                startActivity(intent_editmatch);
                break;

            case R.id.btn_stats:
                Intent intent_stats = new Intent(AllMatchesDetailsActivity.this, StatsActivity.class);
                intent_stats.putExtra("id", id);
                intent_stats.putExtra("player1", player1);
                intent_stats.putExtra("player2", player2);
                startActivity(intent_stats);
                break;

            case R.id.btn_pictures:
                Intent intent_photos = new Intent(AllMatchesDetailsActivity.this, TakePhotoActivity.class);
                intent_photos.putExtra("id", id);
                startActivity(intent_photos);
                break;
        }
    }
}
