package app.univers7.ultra_instinct;

import android.content.Intent;
import android.net.Uri;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PreviousMatchesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView listView;

    long[] matchID;
    String[] player1_name;
    String[] player2_name;
    int[] victories;
    int[] loses;
    int[] kos;
    Double[] lat;
    Double[] lng;

    private SQLiteDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_matches);
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

        listView = (ListView) findViewById(R.id.previousMatchesListViewId);


        dataSource = new SQLiteDataSource(this); // On crée l'objet de la classe qui nous permettra d'effectuer des opérations avec la BDD
        dataSource.open(); // On ouvre la connexion avec la BDD

        getMatchsInfo();
       /* dataSource.getPreviousMatches("http://ultra-instinct-ece.000webhostapp.com/getMatchesList.php");
        loadIntoListView(s); */

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
/*
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
*/
    public void getMatchsInfo()
    {
        class GetMatchsInfoAsync extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... voids) {
                URL url;
                HttpURLConnection conn;
                try {

                    url = new URL("http://ultra-instinct-ece.000webhostapp.com/getPreviousMatchsInfos.php");

                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return "exception";
                }
                try {
                    // Setup HttpURLConnection class to send and receive data from php and mysql
                    conn = (HttpURLConnection) url.openConnection();

                    // setDoInput and setDoOutput method depict handling of both send and receive
                    conn.setDoInput(true);
                    conn.setDoOutput(true);

                    conn.connect();

                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                    return "exception";
                }

                try {

                    int response_code = conn.getResponseCode();

                    // Check if successful connection made
                    if (response_code == HttpURLConnection.HTTP_OK) {

                        // Read data sent from server
                        InputStream input = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                        StringBuilder result = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }

                        // Pass data to onPostExecute method
                        return(result.toString());

                    }else{

                        return("unsuccessful");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    return "exception";
                } finally {
                    conn.disconnect();
                }
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                try {
                    initializeMatchsInfos(result);
                    createTable(player1_name, player2_name, victories, loses, kos, lat, lng);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        GetMatchsInfoAsync getMatchStatsAsync = new GetMatchsInfoAsync();
        getMatchStatsAsync.execute();
    }

    public void initializeMatchsInfos(String json) throws  JSONException
    {
        JSONArray jsonArray = new JSONArray(json);
        player1_name = new String[jsonArray.length()];
        player2_name = new String[jsonArray.length()];
        victories = new int[jsonArray.length()];
        loses = new int[jsonArray.length()];
        kos = new int[jsonArray.length()];
        lat = new Double[jsonArray.length()];
        lng = new Double[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);

            player1_name[i] = obj.getString("player1_name");
            player2_name[i] = obj.getString("player2_name");
            victories[i] = obj.getInt("victories");
            loses[i] = obj.getInt("loses");
            kos[i] = obj.getInt("kos");
            lat[i] = obj.getDouble("latitude");
            lng[i] = obj.getDouble("longitude");
        }
    }

    public void createTable (String[] player1_name, String[] player2_name, int[] victories, int[] loses, int[] kos, Double[] lat, Double[] lng){
        dataSource.deleteAllMatchs();
        for(int i = 0; i < player1_name.length; i++){
            dataSource.createMatch(player1_name[i], player2_name[i], victories[i], loses[i], kos[i], lat[i], lng[i]);
        }
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        dataSource.open();
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        dataSource.close();
    }
}
