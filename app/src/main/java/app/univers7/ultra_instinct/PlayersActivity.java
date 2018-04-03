package app.univers7.ultra_instinct;

import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PlayersActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String[] playersName;
    String[] playersOrigin;
    String[] playersDescription;
    int[] playersVictories;
    int[] playersLoses;
    int[] playersKos;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_register);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlayersActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        listView = (ListView) findViewById(R.id.allPlayersListViewId);

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

    private void getAllPlayers(final String urlWebService) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    loadIntoListView(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void loadIntoListView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);

        playersName = new String[jsonArray.length()];
        playersOrigin = new String[jsonArray.length()];
        playersDescription = new String[jsonArray.length()];
        playersVictories = new int[jsonArray.length()];
        playersLoses = new int[jsonArray.length()];
        playersKos = new int[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            playersName[i] = obj.getString("name");
            playersOrigin[i] = obj.getString("origin");
            playersDescription[i] = obj.getString("description");
            playersVictories[i] = obj.getInt("victories");
            playersLoses[i] = obj.getInt("loses");
            playersKos[i] = obj.getInt("kos");
        }
        CustomListAdapterPlayers customListAdapterPlayers = new CustomListAdapterPlayers(this, playersName);
        listView.setAdapter(customListAdapterPlayers);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
                Intent intent = new Intent(PlayersActivity.this, PlayersDetailsActivity.class);
                intent.putExtra("name", playersName[position]);
                intent.putExtra("origin", playersOrigin[position]);
                intent.putExtra("description", playersDescription[position]);
                intent.putExtra("victories", playersVictories[position]);
                intent.putExtra("loses", playersLoses[position]);
                intent.putExtra("kos", playersKos[position]);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        getAllPlayers("http://ultra-instinct-ece.000webhostapp.com/getPlayersList.php");
    }
}
