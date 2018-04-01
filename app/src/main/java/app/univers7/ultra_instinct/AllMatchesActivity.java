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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AllMatchesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    int[] matchesID;
    String[] matchesPlayer1;
    String[] matchesPlayer2;
    Double[] matchesLatitude;
    Double[] matchesLongitude;
    String[] matchesDate;
    String[] matchesDescription;
    int[] matchesStatus;

    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_matches);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_match_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllMatchesActivity.this, AddMatchActivity.class);
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

        listView = (ListView) findViewById(R.id.allMatchesListViewId);

        getAllMatches("http://ultra-instinct-ece.000webhostapp.com/getMatchesList.php");

        //CustomListAdapter whatever = new CustomListAdapter(this, infoArray);

        //listView.setAdapter(whatever);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.all_matches, menu);
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

    private void getAllMatches(final String urlWebService) {

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
        matchesID = new int[jsonArray.length()];
        matchesPlayer1 = new String[jsonArray.length()];
        matchesPlayer2 = new String[jsonArray.length()];
        matchesLatitude = new Double[jsonArray.length()];
        matchesLongitude = new Double[jsonArray.length()];
        matchesDate = new String[jsonArray.length()];
        matchesDescription = new String[jsonArray.length()];
        matchesStatus = new int[jsonArray.length()];

        SimpleDateFormat comingDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat trueDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            matchesID[i] = obj.getInt("id");
            matchesPlayer1[i] = obj.getString("player1_name");
            matchesPlayer2[i] = obj.getString("player2_name");
            matchesLatitude[i] = obj.getDouble("latitude");
            matchesLongitude[i] = obj.getDouble("longitude");
            try{
                Date d = comingDateFormat.parse(obj.getString("date"));
                matchesDate[i] = trueDateFormat.format(d);
            }catch(Exception e){
                e.printStackTrace();
            }
            matchesDescription[i] = obj.getString("description");
            matchesStatus[i] = obj.getInt("status");
        }
        CustomListAdapter whatever = new CustomListAdapter(this, matchesDescription);
        listView.setAdapter(whatever);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
                Intent intent = new Intent(AllMatchesActivity.this, AllMatchesDetailsActivity.class);
                intent.putExtra("id", matchesID[position]);
                intent.putExtra("player1", matchesPlayer1[position]);
                intent.putExtra("player2", matchesPlayer2[position]);
                intent.putExtra("latitude", matchesLatitude[position]);
                intent.putExtra("longitude", matchesLongitude[position]);
                intent.putExtra("date", matchesDate[position]);
                intent.putExtra("description", matchesDescription[position]);
                intent.putExtra("status", matchesStatus[position]);
                startActivity(intent);
            }
        });

    }
}
