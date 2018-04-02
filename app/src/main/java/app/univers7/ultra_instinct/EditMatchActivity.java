package app.univers7.ultra_instinct;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EditMatchActivity extends AppCompatActivity implements EditMatchGestionFragment.OnFragmentInteractionListener{

    EditMatchGestionFragment editMatchGestionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_match);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editMatchGestionFragment = new EditMatchGestionFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.fl_gestion, editMatchGestionFragment).commitNow();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}