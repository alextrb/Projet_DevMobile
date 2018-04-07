package app.univers7.ultra_instinct;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
import java.util.ArrayList;
import java.util.List;

public class AddMatchActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private Spinner spinner_p1, spinner_p2;
    private EditText et_description;
    private Double match_latitude;
    private Double match_longitude;
    private Button btn_addmatch;

    private SupportMapFragment mapFragment;

    private static GoogleMap mMap;
    private Marker match_marker;

    String[] listPlayers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_match);

        spinner_p1 = (Spinner) findViewById(R.id.spinner_p1);
        spinner_p2 = (Spinner) findViewById(R.id.spinner_p2);
        et_description = (EditText) findViewById(R.id.add_match_description);
        btn_addmatch = (Button) findViewById(R.id.add_match);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    public void addMatch(View view) {
        if (et_description.getText().toString().matches("") || match_marker!=null)
        {
            Toast.makeText(AddMatchActivity.this, "Veuillez donner toutes les informations", Toast.LENGTH_LONG).show();
        }
        else
        {
            if(String.valueOf(spinner_p1.getSelectedItem()).equals(String.valueOf(spinner_p2.getSelectedItem())))
            {
                Toast.makeText(AddMatchActivity.this, "Veuillez choisir des joueurs différents", Toast.LENGTH_LONG).show();
            }
            else
            {
                class InsertDataAsync extends AsyncTask<String, Void, String> {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                    }

                    @Override
                    protected String doInBackground(String... strings) {
                        URL url;
                        HttpURLConnection conn;
                        try {

                            url = new URL("http://ultra-instinct-ece.000webhostapp.com/addMatch.php");

                        } catch (MalformedURLException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            return "exception";
                        }
                        try {
                            // Setup HttpURLConnection class to send and receive data from php and mysql
                            conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("POST");

                            // setDoInput and setDoOutput method depict handling of both send and receive
                            conn.setDoInput(true);
                            conn.setDoOutput(true);

                            // Append parameters to URL
                            Uri.Builder builder = new Uri.Builder()
                                    .appendQueryParameter("player1", strings[0])
                                    .appendQueryParameter("player2", strings[1])
                                    .appendQueryParameter("latitude", strings[2])
                                    .appendQueryParameter("longitude", strings[3])
                                    .appendQueryParameter("description", strings[4]);
                            String query = builder.build().getEncodedQuery();

                            // Open connection for sending data
                            OutputStream os = conn.getOutputStream();
                            BufferedWriter writer = new BufferedWriter(
                                    new OutputStreamWriter(os, "UTF-8"));
                            writer.write(query);
                            writer.flush();
                            writer.close();
                            os.close();
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
                        if(result.equalsIgnoreCase("true"))
                        {
                            Intent intent = new Intent(AddMatchActivity.this, AllMatchesActivity.class);
                            startActivity(intent);
                            AddMatchActivity.this.finish();

                        }else if (result.equalsIgnoreCase("false")){

                            // If username and password does not match display a error message
                            Toast.makeText(AddMatchActivity.this, "Erreur avec l'insertion dans la BDD", Toast.LENGTH_LONG).show();

                        } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                            Toast.makeText(AddMatchActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

                        }
                    }

                }
                InsertDataAsync insertDataAsync = new InsertDataAsync();
                insertDataAsync.execute(String.valueOf(spinner_p1.getSelectedItem()), String.valueOf(spinner_p2.getSelectedItem()), String.valueOf(match_latitude), String.valueOf(match_longitude), et_description.getText().toString());
            }

        }

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
                    loadIntoSpinners(s);
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

    private void loadIntoSpinners(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);

        listPlayers = new String[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            listPlayers[i] = obj.getString("name");
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listPlayers);
        spinner_p1.setAdapter(dataAdapter);
        spinner_p2.setAdapter(dataAdapter);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);

        LatLng match_LatLng = new LatLng(48.8520514, 2.2861233999999513);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(match_LatLng, 6));
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if(latLng != null) {

            if(match_marker != null)
            {
                match_marker.remove();
            }

            match_marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Emplacement du match").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            match_latitude = latLng.latitude;
            match_longitude = latLng.longitude;

        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        getAllPlayers("http://ultra-instinct-ece.000webhostapp.com/getPlayersList.php");
    }
}
