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

    private static final String TAG = "tag";
    private Button takePictureButton;
    private ImageView imageView;
    String pictureName;
    private Uri file;
    int id;
    String player1;
    String player2;
    Double latitude;
    Double longitude;
    String date;
    String description;
    int status;

    String image_ids[];
          /* "file:///sdcard/Download/MigattenoGokuiPerfectHeroes.png",
            "file:///sdcard/Pictures/Match20180328_231632.jpg"*/

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

        takePictureButton = (Button) findViewById(R.id.button_image);
        imageView = (ImageView) findViewById(R.id.imageview);

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


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            takePictureButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }

        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View imageView) {
                takePicture(imageView);
            }
        });

        /*RecyclerView recyclerView = (RecyclerView) findViewById(R.id.galleryMatchesDetails);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        ArrayList<CellRecyclerView> cells = prepareData();
        AdapterRecyclerView adapterRecyclerView = new AdapterRecyclerView(getApplicationContext(), cells);
        recyclerView.setAdapter(adapterRecyclerView); */

        imageView.setImageBitmap(getImageBitmap("file:///sdcard/Download/MigattenoGokuiPerfectHeroes.png"));

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
        getMenuInflater().inflate(R.menu.all_matches_details, menu);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                takePictureButton.setEnabled(true);
            }
        }
    }

    public void takePicture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        pictureName = getPictureName();
        File imageFile = new File(pictureDirectory,pictureName);
        Uri pictureUri = Uri.fromFile(imageFile);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
        startActivityForResult(intent, 100);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                imageView.setImageBitmap(getImageBitmap("file:///sdcard/Pictures/" + pictureName));
                // Start the async task to load the path to the photo into the database
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

                            url = new URL("http://ultra-instinct-ece.000webhostapp.com/addPhoto.php");


                        } catch (MalformedURLException e) {
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
                                    .appendQueryParameter("match_id", strings[0])
                                    .appendQueryParameter("path", strings[1]);
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
                        }else if (result.equalsIgnoreCase("false")){

                            // If username and password does not match display a error message
                            Toast.makeText(AllMatchesDetailsActivity.this, "Erreur avec l'insertion de la photo dans la BDD", Toast.LENGTH_LONG).show();

                        } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                            Toast.makeText(AllMatchesDetailsActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

                        }
                    }

                }
                InsertDataAsync insertDataAsync = new InsertDataAsync();
                // TODO mettre les données correspondant à la table

                insertDataAsync.execute(Integer.toString(id),"file:///sdcard/Pictures/" + pictureName);
            }
        }
    }

    public String getPictureName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());
        return "Match" + timestamp + ".jpg";
    }

    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e(TAG, "Error getting bitmap", e);
        }
        return bm;
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
                break;
        }
    }

    private ArrayList<CellRecyclerView> prepareData(){
        ArrayList<CellRecyclerView> theImage = new ArrayList<>();
        for (int i = 0; i < image_ids.length; i++){
            CellRecyclerView cell   = new CellRecyclerView();
            cell.setImg(image_ids[i]);
            theImage.add(cell);
        }
        return theImage;
    }

    private void getPicturesOfThisMatch(final String urlWebService, final Integer idMatch){
        class GetPhotoUrls extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    loadIntoImageIds(s);

                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.galleryMatchesDetails);
                    recyclerView.setHasFixedSize(true);

                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
                    recyclerView.setLayoutManager(layoutManager);
                    ArrayList<CellRecyclerView> cells = prepareData();
                    AdapterRecyclerView adapterRecyclerView = new AdapterRecyclerView(getApplicationContext(), cells);
                    recyclerView.setAdapter(adapterRecyclerView);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(String... strings) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");

                    // setDoInput and setDoOutput method depict handling of both send and receive
                    con.setDoInput(true);
                    con.setDoOutput(true);

                    // Append parameters to URL
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("match_id", strings[0]);
                    String query = builder.build().getEncodedQuery();

                    // Open connection for sending data
                    OutputStream os = con.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(query);
                    writer.flush();
                    writer.close();
                    os.close();
                    con.connect();


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
        GetPhotoUrls getPhotoUrls = new GetPhotoUrls();
        getPhotoUrls.execute(Integer.toString(id));
    }

    private void loadIntoImageIds(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);

        image_ids = new String[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            image_ids[i] = jsonObject.getString("path");
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        getPicturesOfThisMatch("http://ultra-instinct-ece.000webhostapp.com/getPhotosMatchesDetails.php", id);
    }
}
