package app.univers7.ultra_instinct;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

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

public class EditMatchActivity extends AppCompatActivity implements EditMatchGestionFragment.OnFragmentInteractionListener, EditMatchScoreFragment.OnFragmentInteractionListener{

    int matchID, matchStatus;
    String player1Name, player2Name;

    int scoringID, roundNumber, scoreP1R1, scoreP1R2, scoreP1R3, scoreP1R4, scoreP1R5, scoreP2R1, scoreP2R2, scoreP2R3, scoreP2R4, scoreP2R5;
    int statsID, p1_punches, p1_bk, p1_sbk, p1_hk, p1_shk, p2_punches, p2_bk, p2_sbk, p2_hk, p2_shk, p1_fautes, p2_fautes;

    EditMatchScoreFragment editMatchScoreFragment;
    EditMatchGestionFragment editMatchGestionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_match);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras(); // on récupère les données transférées par l'intent de la précédente activité
        if(extras!=null)
        {
            matchID = extras.getInt("id");
            player1Name = extras.getString("player1");
            player2Name = extras.getString("player2");
            matchStatus = extras.getInt("status");
        }

        editMatchScoreFragment = new EditMatchScoreFragment();
        editMatchGestionFragment = new EditMatchGestionFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.fl_score, editMatchScoreFragment).commitNow();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_gestion, editMatchGestionFragment).commitNow();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
        getMatchScoringAsync.execute(matchID);

    }

    public void initializeScoring(String json) throws JSONException
    {
        JSONArray jsonArray = new JSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            scoringID = obj.getInt("id");
            roundNumber = obj.getInt("round_number");
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

        editMatchScoreFragment.initializeScore(player1Name, player2Name, roundNumber, scoreP1R1, scoreP1R2, scoreP1R3, scoreP1R4, scoreP1R5, scoreP2R1, scoreP2R2, scoreP2R3, scoreP2R4, scoreP2R5);
        editMatchGestionFragment.setNextRound(roundNumber);
        editMatchGestionFragment.setPlayersName(player1Name, player2Name);
        editMatchScoreFragment.updateScore(roundNumber, "p1", scoreP1R1+scoreP1R2+scoreP1R3+scoreP1R4+scoreP1R5);
        editMatchScoreFragment.updateScore(roundNumber, "p2", scoreP2R1+scoreP2R2+scoreP2R3+scoreP2R4+scoreP2R5);
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
        getMatchStatsAsync.execute(matchID);

    }

    public void initializeStats(String json) throws  JSONException
    {
        JSONArray jsonArray = new JSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            statsID = obj.getInt("id");

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
    }

    @Override
    public void gestionButtonClicked(String button_name)
    {

        if(roundNumber == 1)
        {
            switch(button_name)
            {
                case "edit_j1_bk":
                    p1_bk += 1;
                    scoreP1R1 += 1;
                    editMatchScoreFragment.updateScore(1, "p1", scoreP1R1);
                    break;
                case "edit_j1_sbk":
                    p1_sbk += 1;
                    scoreP1R1 += 3;
                    editMatchScoreFragment.updateScore(1, "p1", scoreP1R1);
                    break;
                case "edit_j1_hk":
                    p1_hk += 1;
                    scoreP1R1 += 3;
                    editMatchScoreFragment.updateScore(1, "p1", scoreP1R1);
                    break;
                case "edit_j1_shk":
                    p1_shk += 1;
                    scoreP1R1 += 4;
                    editMatchScoreFragment.updateScore(1, "p1", scoreP1R1);
                    break;
                case "edit_j1_punch":
                    p1_punches += 1;
                    scoreP1R1 += 1;
                    editMatchScoreFragment.updateScore(1, "p1", scoreP1R1);
                    break;
                case "edit_j1_KO":
                    endMatch(player1Name, player2Name);
                    //TODO : ALLER METTRE A JOUR LA TABLE DU JOUEUR1 POUR LUI RAJOUTER UN KO ET UNE VICTOIRE
                    //TODO : METTRE A JOUR LE MATCH POUR CHANGER SON STATUT ET SON NUMERO DE ROUND
                    //TODO : METTRE A JOUR LES SCORES ET LES STATISTIQUES
                    break;
                case "edit_j1_faute":
                    p1_fautes += 1;
                    scoreP1R1 -= 1;
                    editMatchScoreFragment.updateScore(1, "p1", scoreP1R1);
                    break;
                case "edit_j2_bk":
                    p2_bk += 1;
                    scoreP2R1 += 1;
                    editMatchScoreFragment.updateScore(1, "p2", scoreP2R1);
                    break;
                case "edit_j2_sbk":
                    p2_sbk += 1;
                    scoreP2R1 += 3;
                    editMatchScoreFragment.updateScore(1, "p2", scoreP2R1);
                    break;
                case "edit_j2_hk":
                    p2_hk += 1;
                    scoreP2R1 += 3;
                    editMatchScoreFragment.updateScore(1, "p2", scoreP2R1);
                    break;
                case "edit_j2_shk":
                    p2_shk += 1;
                    scoreP2R1 += 4;
                    editMatchScoreFragment.updateScore(1, "p2", scoreP2R1);
                    break;
                case "edit_j2_punch":
                    p2_punches += 1;
                    scoreP2R1 += 1;
                    editMatchScoreFragment.updateScore(1, "p2", scoreP2R1);
                    break;
                case "edit_j2_KO":
                    endMatch(player2Name, player1Name);
                    //TODO : ALLER METTRE A JOUR LA TABLE DU JOUEUR1 POUR LUI RAJOUTER UN KO ET UNE VICTOIRE
                    //TODO : METTRE A JOUR LE MATCH POUR CHANGER SON STATUT ET SON NUMERO DE ROUND
                    //TODO : METTRE A JOUR LES SCORES ET LES STATISTIQUES
                    break;
                case "edit_j2_faute":
                    p2_fautes += 1;
                    scoreP2R1 -= 1;
                    editMatchScoreFragment.updateScore(1, "p2", scoreP2R1);
                    break;
            }
        }
        else if(roundNumber == 2)
        {
            switch(button_name)
            {
                case "edit_j1_bk":
                    p1_bk += 1;
                    scoreP1R2 += 1;
                    editMatchScoreFragment.updateScore(2, "p1", scoreP1R1+scoreP1R2);
                    break;
                case "edit_j1_sbk":
                    p1_sbk += 1;
                    scoreP1R2 += 3;
                    editMatchScoreFragment.updateScore(2, "p1", scoreP1R1+scoreP1R2);
                    break;
                case "edit_j1_hk":
                    p1_hk += 1;
                    scoreP1R2 += 3;
                    editMatchScoreFragment.updateScore(2, "p1", scoreP1R1+scoreP1R2);
                    break;
                case "edit_j1_shk":
                    p1_shk += 1;
                    scoreP1R2 += 4;
                    editMatchScoreFragment.updateScore(2, "p1", scoreP1R1+scoreP1R2);
                    break;
                case "edit_j1_punch":
                    p1_punches += 1;
                    scoreP1R2 += 1;
                    editMatchScoreFragment.updateScore(2, "p1", scoreP1R1+scoreP1R2);
                    break;
                case "edit_j1_KO":
                    endMatch(player1Name, player2Name);
                    //TODO : ALLER METTRE A JOUR LA TABLE DU JOUEUR1 POUR LUI RAJOUTER UN KO ET UNE VICTOIRE
                    //TODO : METTRE A JOUR LE MATCH POUR CHANGER SON STATUT ET SON NUMERO DE ROUND
                    //TODO : METTRE A JOUR LES SCORES ET LES STATISTIQUES
                    break;
                case "edit_j1_faute":
                    p1_fautes += 1;
                    scoreP1R2 -= 1;
                    editMatchScoreFragment.updateScore(2, "p1", scoreP1R1+scoreP1R2);
                    break;
                case "edit_j2_bk":
                    p2_bk += 1;
                    scoreP2R2 += 1;
                    editMatchScoreFragment.updateScore(2, "p2", scoreP2R1+scoreP2R2);
                    break;
                case "edit_j2_sbk":
                    p2_sbk += 1;
                    scoreP2R2 += 3;
                    editMatchScoreFragment.updateScore(2, "p2", scoreP2R1+scoreP2R2);
                    break;
                case "edit_j2_hk":
                    p2_hk += 1;
                    scoreP2R2 += 3;
                    editMatchScoreFragment.updateScore(2, "p2", scoreP2R1+scoreP2R2);
                    break;
                case "edit_j2_shk":
                    p2_shk += 1;
                    scoreP2R2 += 4;
                    editMatchScoreFragment.updateScore(2, "p2", scoreP2R1+scoreP2R2);
                    break;
                case "edit_j2_punch":
                    p2_punches += 1;
                    scoreP2R2 += 1;
                    editMatchScoreFragment.updateScore(2, "p2", scoreP2R1+scoreP2R2);
                    break;
                case "edit_j2_KO":
                    endMatch(player2Name, player1Name);
                    //TODO : ALLER METTRE A JOUR LA TABLE DU JOUEUR1 POUR LUI RAJOUTER UN KO ET UNE VICTOIRE
                    //TODO : METTRE A JOUR LE MATCH POUR CHANGER SON STATUT ET SON NUMERO DE ROUND
                    //TODO : METTRE A JOUR LES SCORES ET LES STATISTIQUES
                    break;
                case "edit_j2_faute":
                    p2_fautes += 1;
                    scoreP2R2 -= 1;
                    editMatchScoreFragment.updateScore(2, "p2", scoreP2R1+scoreP2R2);
                    break;
            }
        }
        else if(roundNumber == 3)
        {
            switch(button_name)
            {
                case "edit_j1_bk":
                    p1_bk += 1;
                    scoreP1R3 += 1;
                    editMatchScoreFragment.updateScore(3, "p1", scoreP1R1+scoreP1R2+scoreP1R3);
                    break;
                case "edit_j1_sbk":
                    p1_sbk += 1;
                    scoreP1R3 += 3;
                    editMatchScoreFragment.updateScore(3, "p1", scoreP1R1+scoreP1R2+scoreP1R3);
                    break;
                case "edit_j1_hk":
                    p1_hk += 1;
                    scoreP1R3 += 3;
                    editMatchScoreFragment.updateScore(3, "p1", scoreP1R1+scoreP1R2+scoreP1R3);
                    break;
                case "edit_j1_shk":
                    p1_shk += 1;
                    scoreP1R3 += 4;
                    editMatchScoreFragment.updateScore(3, "p1", scoreP1R1+scoreP1R2+scoreP1R3);
                    break;
                case "edit_j1_punch":
                    p1_punches += 1;
                    scoreP1R3 += 1;
                    editMatchScoreFragment.updateScore(3, "p1", scoreP1R1+scoreP1R2+scoreP1R3);
                    break;
                case "edit_j1_KO":
                    endMatch(player1Name, player2Name);
                    //TODO : ALLER METTRE A JOUR LA TABLE DU JOUEUR1 POUR LUI RAJOUTER UN KO ET UNE VICTOIRE
                    //TODO : METTRE A JOUR LE MATCH POUR CHANGER SON STATUT ET SON NUMERO DE ROUND
                    //TODO : METTRE A JOUR LES SCORES ET LES STATISTIQUES
                    break;
                case "edit_j1_faute":
                    p1_fautes += 1;
                    scoreP1R3 -= 1;
                    editMatchScoreFragment.updateScore(3, "p1", scoreP1R1+scoreP1R2+scoreP1R3);
                    break;
                case "edit_j2_bk":
                    p2_bk += 1;
                    scoreP2R3 += 1;
                    editMatchScoreFragment.updateScore(3, "p2", scoreP2R1+scoreP2R2+scoreP2R3);
                    break;
                case "edit_j2_sbk":
                    p2_sbk += 1;
                    scoreP2R3 += 3;
                    editMatchScoreFragment.updateScore(3, "p2", scoreP2R1+scoreP2R2+scoreP2R3);
                    break;
                case "edit_j2_hk":
                    p2_hk += 1;
                    scoreP2R3 += 3;
                    editMatchScoreFragment.updateScore(3, "p2", scoreP2R1+scoreP2R2+scoreP2R3);
                    break;
                case "edit_j2_shk":
                    p2_shk += 1;
                    scoreP2R3 += 4;
                    editMatchScoreFragment.updateScore(3, "p2", scoreP2R1+scoreP2R2+scoreP2R3);
                    break;
                case "edit_j2_punch":
                    p2_punches += 1;
                    scoreP2R3 += 1;
                    editMatchScoreFragment.updateScore(3, "p2", scoreP2R1+scoreP2R2+scoreP2R3);
                    break;
                case "edit_j2_KO":
                    endMatch(player2Name, player1Name);
                    //TODO : ALLER METTRE A JOUR LA TABLE DU JOUEUR1 POUR LUI RAJOUTER UN KO ET UNE VICTOIRE
                    //TODO : METTRE A JOUR LE MATCH POUR CHANGER SON STATUT ET SON NUMERO DE ROUND
                    //TODO : METTRE A JOUR LES SCORES ET LES STATISTIQUES
                    break;
                case "edit_j2_faute":
                    p2_fautes += 1;
                    scoreP2R3 -= 1;
                    editMatchScoreFragment.updateScore(3, "p2", scoreP2R1+scoreP2R2+scoreP2R3);
                    break;
            }
        }
        else if(roundNumber == 4)
        {
            switch(button_name)
            {
                case "edit_j1_bk":
                    p1_bk += 1;
                    scoreP1R4 += 1;
                    editMatchScoreFragment.updateScore(4, "p1", scoreP1R1+scoreP1R2+scoreP1R3+scoreP1R4);
                    if( ( (scoreP1R1+scoreP1R2+scoreP1R3+scoreP1R4) - (scoreP2R1+scoreP2R2+scoreP2R3+scoreP2R4) ) >= 12)
                    {
                        endMatch(player1Name, player2Name);
                    }
                    break;
                case "edit_j1_sbk":
                    p1_sbk += 1;
                    scoreP1R4 += 3;
                    editMatchScoreFragment.updateScore(4, "p1", scoreP1R1+scoreP1R2+scoreP1R3+scoreP1R4);
                    if( ( (scoreP1R1+scoreP1R2+scoreP1R3+scoreP1R4) - (scoreP2R1+scoreP2R2+scoreP2R3+scoreP2R4) ) >= 12)
                    {
                        endMatch(player1Name, player2Name);
                    }
                    break;
                case "edit_j1_hk":
                    p1_hk += 1;
                    scoreP1R4 += 3;
                    editMatchScoreFragment.updateScore(4, "p1", scoreP1R1+scoreP1R2+scoreP1R3+scoreP1R4);
                    if( ( (scoreP1R1+scoreP1R2+scoreP1R3+scoreP1R4) - (scoreP2R1+scoreP2R2+scoreP2R3+scoreP2R4) ) >= 12)
                    {
                        endMatch(player1Name, player2Name);
                    }
                    break;
                case "edit_j1_shk":
                    p1_shk += 1;
                    scoreP1R4 += 4;
                    editMatchScoreFragment.updateScore(4, "p1", scoreP1R1+scoreP1R2+scoreP1R3+scoreP1R4);
                    if( ( (scoreP1R1+scoreP1R2+scoreP1R3+scoreP1R4) - (scoreP2R1+scoreP2R2+scoreP2R3+scoreP2R4) ) >= 12)
                    {
                        endMatch(player1Name, player2Name);
                    }
                    break;
                case "edit_j1_punch":
                    p1_punches += 1;
                    scoreP1R4 += 1;
                    editMatchScoreFragment.updateScore(4, "p1", scoreP1R1+scoreP1R2+scoreP1R3+scoreP1R4);
                    if( ( (scoreP1R1+scoreP1R2+scoreP1R3+scoreP1R4) - (scoreP2R1+scoreP2R2+scoreP2R3+scoreP2R4) ) >= 12)
                    {
                        endMatch(player1Name, player2Name);
                    }
                    break;
                case "edit_j1_KO":
                    endMatch(player1Name, player2Name);
                    //TODO : ALLER METTRE A JOUR LA TABLE DU JOUEUR1 POUR LUI RAJOUTER UN KO ET UNE VICTOIRE
                    //TODO : METTRE A JOUR LE MATCH POUR CHANGER SON STATUT ET SON NUMERO DE ROUND
                    //TODO : METTRE A JOUR LES SCORES ET LES STATISTIQUES
                    break;
                case "edit_j1_faute":
                    p1_fautes += 1;
                    scoreP1R4 -= 1;
                    editMatchScoreFragment.updateScore(4, "p1", scoreP1R1+scoreP1R2+scoreP1R3+scoreP1R4);
                    if( ( (scoreP1R1+scoreP1R2+scoreP1R3+scoreP1R4) - (scoreP2R1+scoreP2R2+scoreP2R3+scoreP2R4) ) >= 12)
                    {
                        endMatch(player1Name, player2Name);
                    }
                    break;
                case "edit_j2_bk":
                    p2_bk += 1;
                    scoreP2R4 += 1;
                    editMatchScoreFragment.updateScore(4, "p2", scoreP2R1+scoreP2R2+scoreP2R3+scoreP2R4);
                    if( ( (scoreP2R1+scoreP2R2+scoreP2R3+scoreP2R4) - (scoreP1R1+scoreP1R2+scoreP1R3+scoreP1R4) ) >= 12)
                    {
                        endMatch(player2Name, player1Name);
                    }
                    break;
                case "edit_j2_sbk":
                    p2_sbk += 1;
                    scoreP2R4 += 3;
                    editMatchScoreFragment.updateScore(4, "p2", scoreP2R1+scoreP2R2+scoreP2R3+scoreP2R4);
                    if( ( (scoreP2R1+scoreP2R2+scoreP2R3+scoreP2R4) - (scoreP1R1+scoreP1R2+scoreP1R3+scoreP1R4) ) >= 12)
                    {
                        endMatch(player2Name, player1Name);
                    }
                    break;
                case "edit_j2_hk":
                    p2_hk += 1;
                    scoreP2R4 += 3;
                    editMatchScoreFragment.updateScore(4, "p2", scoreP2R1+scoreP2R2+scoreP2R3+scoreP2R4);
                    if( ( (scoreP2R1+scoreP2R2+scoreP2R3+scoreP2R4) - (scoreP1R1+scoreP1R2+scoreP1R3+scoreP1R4) ) >= 12)
                    {
                        endMatch(player2Name, player1Name);
                    }
                    break;
                case "edit_j2_shk":
                    p2_shk += 1;
                    scoreP2R4 += 4;
                    editMatchScoreFragment.updateScore(4, "p2", scoreP2R1+scoreP2R2+scoreP2R3+scoreP2R4);
                    if( ( (scoreP2R1+scoreP2R2+scoreP2R3+scoreP2R4) - (scoreP1R1+scoreP1R2+scoreP1R3+scoreP1R4) ) >= 12)
                    {
                        endMatch(player2Name, player1Name);
                    }
                    break;
                case "edit_j2_punch":
                    p2_punches += 1;
                    scoreP2R4 += 1;
                    editMatchScoreFragment.updateScore(4, "p2", scoreP2R1+scoreP2R2+scoreP2R3+scoreP2R4);
                    if( ( (scoreP2R1+scoreP2R2+scoreP2R3+scoreP2R4) - (scoreP1R1+scoreP1R2+scoreP1R3+scoreP1R4) ) >= 12)
                    {
                        endMatch(player2Name, player1Name);
                    }
                    break;
                case "edit_j2_KO":
                    endMatch(player2Name, player1Name);
                    //TODO : ALLER METTRE A JOUR LA TABLE DU JOUEUR1 POUR LUI RAJOUTER UN KO ET UNE VICTOIRE
                    //TODO : METTRE A JOUR LE MATCH POUR CHANGER SON STATUT ET SON NUMERO DE ROUND
                    //TODO : METTRE A JOUR LES SCORES ET LES STATISTIQUES
                    break;
                case "edit_j2_faute":
                    p2_fautes += 1;
                    scoreP2R4 -= 1;
                    editMatchScoreFragment.updateScore(4, "p2", scoreP2R1+scoreP2R2+scoreP2R3+scoreP2R4);
                    if( ( (scoreP2R1+scoreP2R2+scoreP2R3+scoreP2R4) - (scoreP1R1+scoreP1R2+scoreP1R3+scoreP1R4) ) >= 12)
                    {
                        endMatch(player2Name, player1Name);
                    }
                    break;
            }
        }
        else if(roundNumber == 5)
        {
            switch(button_name)
            {
                case "edit_j1_bk":
                    p1_bk += 1;
                    scoreP1R5 += 1;
                    editMatchScoreFragment.updateScore(5, "p1", scoreP1R1+scoreP1R2+scoreP1R3+scoreP1R4+scoreP1R5);
                    endMatch(player1Name, player2Name);
                    break;
                case "edit_j1_sbk":
                    p1_sbk += 1;
                    scoreP1R5 += 3;
                    editMatchScoreFragment.updateScore(5, "p1", scoreP1R1+scoreP1R2+scoreP1R3+scoreP1R4+scoreP1R5);
                    endMatch(player1Name, player2Name);
                    break;
                case "edit_j1_hk":
                    p1_hk += 1;
                    scoreP1R5 += 3;
                    editMatchScoreFragment.updateScore(5, "p1", scoreP1R1+scoreP1R2+scoreP1R3+scoreP1R4+scoreP1R5);
                    endMatch(player1Name, player2Name);
                    break;
                case "edit_j1_shk":
                    p1_shk += 1;
                    scoreP1R5 += 4;
                    editMatchScoreFragment.updateScore(5, "p1", scoreP1R1+scoreP1R2+scoreP1R3+scoreP1R4+scoreP1R5);
                    endMatch(player1Name, player2Name);
                    break;
                case "edit_j1_punch":
                    p1_punches += 1;
                    scoreP1R5 += 1;
                    editMatchScoreFragment.updateScore(5, "p1", scoreP1R1+scoreP1R2+scoreP1R3+scoreP1R4+scoreP1R5);
                    endMatch(player1Name, player2Name);
                    break;
                case "edit_j1_KO":
                    endMatch(player1Name, player2Name);
                    //TODO : ALLER METTRE A JOUR LA TABLE DU JOUEUR1 POUR LUI RAJOUTER UN KO ET UNE VICTOIRE
                    //TODO : METTRE A JOUR LE MATCH POUR CHANGER SON STATUT ET SON NUMERO DE ROUND
                    //TODO : METTRE A JOUR LES SCORES ET LES STATISTIQUES
                    break;
                case "edit_j1_faute":
                    p1_fautes += 1;
                    scoreP1R5 -= 1;
                    editMatchScoreFragment.updateScore(5, "p1", scoreP1R1+scoreP1R2+scoreP1R3+scoreP1R4+scoreP1R5);
                    endMatch(player2Name, player1Name);
                    break;
                case "edit_j2_bk":
                    p2_bk += 1;
                    scoreP2R5 += 1;
                    editMatchScoreFragment.updateScore(5, "p2", scoreP2R1+scoreP2R2+scoreP2R3+scoreP2R4+scoreP2R5);
                    endMatch(player2Name, player1Name);
                    break;
                case "edit_j2_sbk":
                    p2_sbk += 1;
                    scoreP2R5 += 3;
                    editMatchScoreFragment.updateScore(5, "p2", scoreP2R1+scoreP2R2+scoreP2R3+scoreP2R4+scoreP2R5);
                    endMatch(player2Name, player1Name);
                    break;
                case "edit_j2_hk":
                    p2_hk += 1;
                    scoreP2R5 += 3;
                    editMatchScoreFragment.updateScore(5, "p2", scoreP2R1+scoreP2R2+scoreP2R3+scoreP2R4+scoreP2R5);
                    endMatch(player2Name, player1Name);
                    break;
                case "edit_j2_shk":
                    p2_shk += 1;
                    scoreP2R5 += 4;
                    editMatchScoreFragment.updateScore(5, "p2", scoreP2R1+scoreP2R2+scoreP2R3+scoreP2R4+scoreP2R5);
                    endMatch(player2Name, player1Name);
                    break;
                case "edit_j2_punch":
                    p2_punches += 1;
                    scoreP2R5 += 1;
                    editMatchScoreFragment.updateScore(5, "p2", scoreP2R1+scoreP2R2+scoreP2R3+scoreP2R4+scoreP2R5);
                    endMatch(player2Name, player1Name);
                    break;
                case "edit_j2_KO":
                    endMatch(player2Name, player1Name);
                    //TODO : ALLER METTRE A JOUR LA TABLE DU JOUEUR1 POUR LUI RAJOUTER UN KO ET UNE VICTOIRE
                    //TODO : METTRE A JOUR LE MATCH POUR CHANGER SON STATUT ET SON NUMERO DE ROUND
                    //TODO : METTRE A JOUR LES SCORES ET LES STATISTIQUES
                    break;
                case "edit_j2_faute":
                    p2_fautes += 1;
                    scoreP2R5 -= 1;
                    editMatchScoreFragment.updateScore(5, "p2", scoreP2R1+scoreP2R2+scoreP2R3+scoreP2R4+scoreP2R5);
                    endMatch(player2Name, player1Name);
                    break;
            }
        }
        if(button_name == "btn_gestion_nextround")
        {
            if(roundNumber < 5)
            {
                if(roundNumber == 3)
                {
                    if( ( (scoreP1R1+scoreP1R2+scoreP1R3+scoreP1R4) - (scoreP2R1+scoreP2R2+scoreP2R3+scoreP2R4) ) >= 12)
                    {
                        endMatch(player1Name, player2Name);
                    }
                    else if( ( (scoreP2R1+scoreP2R2+scoreP2R3+scoreP2R4) - (scoreP1R1+scoreP1R2+scoreP1R3+scoreP1R4) ) >= 12)
                    {
                        endMatch(player2Name, player1Name);
                    }
                }
                if(roundNumber == 4)
                {
                    if(  (scoreP1R1+scoreP1R2+scoreP1R3+scoreP1R4) > (scoreP2R1+scoreP2R2+scoreP2R3+scoreP2R4))
                    {
                        endMatch(player1Name, player2Name);
                    }
                    else if( (scoreP2R1+scoreP2R2+scoreP2R3+scoreP2R4) > (scoreP1R1+scoreP1R2+scoreP1R3+scoreP1R4) )
                    {
                        endMatch(player2Name, player1Name);
                    }
                }
                roundNumber += 1;
                editMatchScoreFragment.updateScore(roundNumber, "p1", scoreP1R1+scoreP1R2+scoreP1R3+scoreP1R4+scoreP1R5);
                editMatchScoreFragment.updateScore(roundNumber, "p2", scoreP2R1+scoreP2R2+scoreP2R3+scoreP2R4+scoreP2R5);
                editMatchGestionFragment.setNextRound(roundNumber);
            }
            else
            {
                if((scoreP1R1+scoreP1R2+scoreP1R3+scoreP1R4+scoreP1R5) > (scoreP2R1+scoreP2R2+scoreP2R3+scoreP2R4+scoreP2R5))
                {
                    endMatch(player1Name, player2Name);
                }
                else if((scoreP1R1+scoreP1R2+scoreP1R3+scoreP1R4+scoreP1R5) < (scoreP2R1+scoreP2R2+scoreP2R3+scoreP2R4+scoreP2R5))
                {
                    endMatch(player2Name, player1Name);
                }
            }

        }
    }


    public void endMatch(String winner, String loser)
    {
        roundNumber = 6;
        editMatchGestionFragment.setNextRound(roundNumber); // On indique que le match est terminé
        if(winner == player1Name)
        {
            editMatchScoreFragment.setWinnerRowColor("p1");
        }
        else
        {
            editMatchScoreFragment.setWinnerRowColor("p2");
        }
    }
}
