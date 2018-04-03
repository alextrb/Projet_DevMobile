package app.univers7.ultra_instinct;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class AllMatchesDetailsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    int id;
    String player1;
    String player2;
    Double latitude;
    Double longitude;
    String date;
    String description;
    int status;

    String image_ids[];

    SupportMapFragment mapFragment;

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

        TextView textViewPlayers = (TextView) findViewById(R.id.TextViewMatchPlayers);
        TextView textViewAddress = (TextView) findViewById(R.id.TextViewMatchAddress);
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

        textViewPlayers.setText(player1 + " VS " + player2);
        textViewAddress.setText(getGeocoding(latitude, longitude));
        textViewDate.setText(date);
        textViewDescription.setText(description);
        textViewStatus.setText(String.valueOf(status));

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getGeocoding(latitude, longitude);

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng match_LatLng = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(match_LatLng)
                .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(match_LatLng, 11));

    }

    public String getGeocoding(Double latitude, Double longitude)
    {
        Geocoder geocoder;
        List<Address> addresses;
        String address = " ";
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }
}
