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
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

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

public class RegisterActivity extends AppCompatActivity {

    EditText et_name;
    EditText et_origin;
    EditText et_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        et_name = (EditText) findViewById(R.id.register_name);
        et_origin = (EditText) findViewById(R.id.register_origin);
        et_description = (EditText) findViewById(R.id.register_description);

        et_name.setText("");
        et_origin.setText("");
        et_description.setText("");

    }

    public void registerPlayer(View view) {
        if (et_name.getText().toString().matches("") || et_origin.getText().toString().matches("") || et_description.getText().toString().matches(""))
        {
            Toast.makeText(RegisterActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_LONG).show();
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

                        url = new URL("http://ultra-instinct-ece.000webhostapp.com/addPlayer.php");

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
                                .appendQueryParameter("name", strings[0])
                                .appendQueryParameter("origin", strings[1])
                                .appendQueryParameter("description", strings[2]);
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
                /* Here launching another activity when login successful. If you persist login state
                use sharedPreferences of Android. and logout button to clear sharedPreferences.
                 */
                        Intent intent = new Intent(RegisterActivity.this, PlayersActivity.class);
                        startActivity(intent);
                        RegisterActivity.this.finish();

                    }else if (result.equalsIgnoreCase("false")){

                        // If username and password does not match display a error message
                        Toast.makeText(RegisterActivity.this, "Erreur avec l'insertion dans la BDD", Toast.LENGTH_LONG).show();

                    } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                        Toast.makeText(RegisterActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

                    }
                }

            }
            InsertDataAsync insertDataAsync = new InsertDataAsync();
            insertDataAsync.execute(et_name.getText().toString(), et_origin.getText().toString(), et_description.getText().toString());
        }

    }
    }

