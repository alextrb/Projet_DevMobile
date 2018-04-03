package app.univers7.ultra_instinct;

import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class PlayersDetailsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String name;
    String origin;
    String description;
    int victories;
    int loses;
    int kos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players_details);
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

        TextView textViewName = (TextView) findViewById(R.id.TextViewPlayerName);
        TextView textViewOrigin = (TextView) findViewById(R.id.TextViewPlayerOrigin);
        TextView textViewDescription = (TextView) findViewById(R.id.TextViewPlayerDescription);
        TextView textViewVictories = (TextView) findViewById(R.id.TextViewPlayerVictories);
        TextView textViewLoses = (TextView) findViewById(R.id.TextViewPlayerLoses);
        TextView textViewKos = (TextView) findViewById(R.id.TextViewPlayerKos);

        Bundle extras = getIntent().getExtras(); // on récupère les données transférées par l'intent de la précédente activité
        if(extras!=null)
        {
            name = extras.getString("name");
            origin = extras.getString("origin");
            description = extras.getString("description");
            victories = extras.getInt("victories");
            loses = extras.getInt("loses");
            kos = extras.getInt("kos");
        }
        textViewName.setText(name);
        textViewOrigin.setText("Origine : " + origin);
        textViewDescription.setText(description);
        textViewVictories.setText("V : " + String.valueOf(victories));
        textViewLoses.setText("D : " + String.valueOf(loses));
        textViewKos.setText("KO : " + String.valueOf(kos));
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
}
