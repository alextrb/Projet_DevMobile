package app.univers7.ultra_instinct;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

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

public class StatsActivity extends AppCompatActivity {

    private TextView tv_p1name, tv_p1m1, tv_p1m2, tv_p1m3, tv_p1m4, tv_p1m5, tv_p1bk, tv_p1sbk, tv_p1hk, tv_p1shk, tv_p1fautes, tv_p1punches;
    private TextView tv_p2name, tv_p2m1, tv_p2m2, tv_p2m3, tv_p2m4, tv_p2m5, tv_p2bk, tv_p2sbk, tv_p2hk, tv_p2shk, tv_p2fautes, tv_p2punches;

    int match_id;
    private String p1name, p2name;
    int scoreP1R1, scoreP1R2, scoreP1R3, scoreP1R4, scoreP1R5, scoreP2R1, scoreP2R2, scoreP2R3, scoreP2R4, scoreP2R5;
    int p1_punches, p1_bk, p1_sbk, p1_hk, p1_shk, p2_punches, p2_bk, p2_sbk, p2_hk, p2_shk, p1_fautes, p2_fautes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        tv_p1name = (TextView) findViewById(R.id.p1_name);
        tv_p1m1 = (TextView) findViewById(R.id.p1_m1);
        tv_p1m2 = (TextView) findViewById(R.id.p1_m2);
        tv_p1m3 = (TextView) findViewById(R.id.p1_m3);
        tv_p1m4 = (TextView) findViewById(R.id.p1_m4);
        tv_p1m5 = (TextView) findViewById(R.id.p1_m5);
        tv_p1bk = (TextView) findViewById(R.id.p1_bk);
        tv_p1sbk = (TextView) findViewById(R.id.p1_sbk);
        tv_p1hk = (TextView) findViewById(R.id.p1_hk);
        tv_p1shk = (TextView) findViewById(R.id.p1_shk);
        tv_p1fautes = (TextView) findViewById(R.id.p1_fautes);
        tv_p1punches = (TextView) findViewById(R.id.p1_punches);
        tv_p2name = (TextView) findViewById(R.id.p2_name);
        tv_p2m1 = (TextView) findViewById(R.id.p2_m1);
        tv_p2m2 = (TextView) findViewById(R.id.p2_m2);
        tv_p2m3 = (TextView) findViewById(R.id.p2_m3);
        tv_p2m4 = (TextView) findViewById(R.id.p2_m4);
        tv_p2m5 = (TextView) findViewById(R.id.p2_m5);
        tv_p2bk = (TextView) findViewById(R.id.p2_bk);
        tv_p2sbk = (TextView) findViewById(R.id.p2_sbk);
        tv_p2hk = (TextView) findViewById(R.id.p2_hk);
        tv_p2shk = (TextView) findViewById(R.id.p2_shk);
        tv_p2fautes = (TextView) findViewById(R.id.p2_fautes);
        tv_p2punches = (TextView) findViewById(R.id.p2_punches);

        Bundle extras = getIntent().getExtras(); // on récupère les données transférées par l'intent de la précédente activité
        if(extras!=null)
        {
            match_id = extras.getInt("id");
            p1name = extras.getString("player1");
            p2name = extras.getString("player2");
        }

        tv_p1name.setText(p1name);
        tv_p2name.setText(p2name);
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        getMatchScoring();
        getMatchStats();
    }


    public void getMatchScoring()
    {
        class GetMatchScoringAsync extends AsyncTask<Integer, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Integer... integers) {
                URL url;
                HttpURLConnection conn;
                try {

                    url = new URL("http://ultra-instinct-ece.000webhostapp.com/getMatchScoring.php");

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
                            .appendQueryParameter("match_id", integers[0].toString());
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
                try {
                    initializeScoring(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        GetMatchScoringAsync getMatchScoringAsync = new GetMatchScoringAsync();
        getMatchScoringAsync.execute(match_id);

    }

    public void initializeScoring(String json) throws JSONException
    {
        JSONArray jsonArray = new JSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            scoreP1R1 = obj.getInt("round1_player1");
            scoreP1R2 = obj.getInt("round2_player1");
            scoreP1R3 = obj.getInt("round3_player1");
            scoreP1R4 = obj.getInt("round4_player1");
            scoreP1R5 = obj.getInt("round5_player1");
            scoreP2R1 = obj.getInt("round1_player2");
            scoreP2R2 = obj.getInt("round2_player2");
            scoreP2R3 = obj.getInt("round3_player2");
            scoreP2R4 = obj.getInt("round4_player2");
            scoreP2R5 = obj.getInt("round5_player2");
        }

        tv_p1m1.setText(String.valueOf(scoreP1R1));
        tv_p1m2.setText(String.valueOf(scoreP1R2));
        tv_p1m3.setText(String.valueOf(scoreP1R3));
        tv_p1m4.setText(String.valueOf(scoreP1R4));
        tv_p1m5.setText(String.valueOf(scoreP1R5));
        tv_p2m1.setText(String.valueOf(scoreP1R1));
        tv_p2m2.setText(String.valueOf(scoreP2R2));
        tv_p2m3.setText(String.valueOf(scoreP2R3));
        tv_p2m4.setText(String.valueOf(scoreP2R4));
        tv_p2m5.setText(String.valueOf(scoreP2R5));
    }

    public void getMatchStats()
    {
        class GetMatchStatsAsync extends AsyncTask<Integer, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Integer... integers) {
                URL url;
                HttpURLConnection conn;
                try {

                    url = new URL("http://ultra-instinct-ece.000webhostapp.com/getMatchStats.php");

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
                            .appendQueryParameter("match_id", integers[0].toString());
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
                try {
                    initializeStats(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        GetMatchStatsAsync getMatchStatsAsync = new GetMatchStatsAsync();
        getMatchStatsAsync.execute(match_id);

    }

    public void initializeStats(String json) throws  JSONException
    {
        JSONArray jsonArray = new JSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);

            p1_bk = obj.getInt("p1_body_simple_kicks");
            p1_sbk = obj.getInt("p1_body_spinning_kicks");
            p1_hk = obj.getInt("p1_head_simple_kicks");
            p1_shk = obj.getInt("p1_head_spinning_kicks");
            p1_punches = obj.getInt("p1_punches");
            p1_fautes = obj.getInt("p1_fouls");

            p2_bk = obj.getInt("p2_body_simple_kicks");
            p2_sbk = obj.getInt("p2_body_spinning_kicks");
            p2_hk = obj.getInt("p2_head_simple_kicks");
            p2_shk = obj.getInt("p2_head_spinning_kicks");
            p2_punches = obj.getInt("p2_punches");
            p2_fautes = obj.getInt("p2_fouls");

        }

        tv_p1bk.setText(String.valueOf(p1_bk));
        tv_p1sbk.setText(String.valueOf(p1_sbk));
        tv_p1hk.setText(String.valueOf(p1_hk));
        tv_p1shk.setText(String.valueOf(p1_shk));
        tv_p1punches.setText(String.valueOf(p1_punches));
        tv_p1fautes.setText(String.valueOf(p1_fautes));

        tv_p2bk.setText(String.valueOf(p2_bk));
        tv_p2sbk.setText(String.valueOf(p2_sbk));
        tv_p2hk.setText(String.valueOf(p2_hk));
        tv_p2shk.setText(String.valueOf(p2_shk));
        tv_p2punches.setText(String.valueOf(p2_punches));
        tv_p2fautes.setText(String.valueOf(p2_fautes));
    }
}
