package app.univers7.ultra_instinct;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class AllMatchesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, LocationListener{

    int[] matchesID;
    String[] matchesPlayer1;
    String[] matchesPlayer2;
    Double[] matchesLatitude;
    Double[] matchesLongitude;
    String[] matchesDate;
    String[] matchesDescription;
    int[] matchesStatus;

    ListView listView;
    ScrollView v;

    SupportMapFragment mapFragment;
    GoogleMap mMap;

    private Marker currentMarker;

    private LocationManager locationManager;
    private String provider;

    String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    private int USER_LOCATION_REQUESTCODE = 1;

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

        v = (ScrollView) findViewById(R.id.scrollView);





        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER)){
            provider = locationManager.NETWORK_PROVIDER;
        }
        else if(locationManager.isProviderEnabled(locationManager.GPS_PROVIDER)){
            provider = locationManager.GPS_PROVIDER;
        }
        else{
            Toast toast = Toast.makeText(this,"Please activate either Cellular or Wifi", Toast.LENGTH_LONG);
            toast.show();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {

            mapFragment.getMapAsync(this);
        }
        else // Si on ne les as pas, on les demande et ça appelle ensuite "onRequestPermissionsResult"
        {
            ActivityCompat.requestPermissions(this, permissions, USER_LOCATION_REQUESTCODE);
        }

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
                    for(int i=0; i<matchesLatitude.length; i++)
                    {
                        LatLng match_LatLng = new LatLng(matchesLatitude[i],matchesLongitude[i]);
                        mMap.addMarker(new MarkerOptions().position(match_LatLng).title(matchesDescription[i]).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(match_LatLng));
                    }

                    v.scrollTo(0,0);
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
        CustomListAdapter whatever = new CustomListAdapter(this, matchesPlayer1, matchesPlayer2, matchesDate, matchesDescription, matchesStatus);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        findCurrentLocation();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(requestCode == USER_LOCATION_REQUESTCODE)
        {
            // Si on a bien reçu les permissions de l'utilisateur
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
            {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    mapFragment.getMapAsync(this);

                }
            }
        }
    }

    public void findCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            Location location = locationManager.getLastKnownLocation(provider);

            if(location != null) {
                Double lat = location.getLatitude();
                Double lng = location.getLongitude();

                if(currentMarker != null)
                {
                    currentMarker.remove();
                }

                LatLng position = new LatLng(lat, lng);


                currentMarker = mMap.addMarker(new MarkerOptions().position(position).title("Ma position").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            }
        }
        else // Si on ne les as pas, on les demande et ça appelle ensuite "onRequestPermissionsResult"
        {
            Toast toast = Toast.makeText(this, "Veuillez autoriser la localisation", Toast.LENGTH_LONG);
            toast.show();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        findCurrentLocation();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        getAllMatches("http://ultra-instinct-ece.000webhostapp.com/getMatchesList.php");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
        }
        else // Si on ne les as pas, on les demande et ça appelle ensuite "onRequestPermissionsResult"
        {
            ActivityCompat.requestPermissions(this, permissions, USER_LOCATION_REQUESTCODE);
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {

        }
        locationManager.removeUpdates(this);
    }
}
