package app.univers7.ultra_instinct;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Alex on 28/03/2018.
 */

public class CustomListAdapter extends ArrayAdapter {

    //to reference the Activity
    private final Activity context;

    //to store the list of matches

    private final String[] player1Array;
    private final String[] player2Array;
    private final String[] dateArray;
    private final String[] descriptionArray;
    private final int[] statusArray;


    public CustomListAdapter(Activity context, String[] player1ArrayParam, String[] player2ArrayParam, String[] dateArrayParam, String[] descriptionArrayParam, int[] statusArrayParam){

        super(context,R.layout.allmatches_listview_row, descriptionArrayParam);

        this.context=context;
        this.player1Array = player1ArrayParam;
        this.player2Array = player2ArrayParam;
        this.dateArray = dateArrayParam;
        this.descriptionArray = descriptionArrayParam;
        this.statusArray = statusArrayParam;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.allmatches_listview_row, null,true);

        //this code gets references to objects in the listview_row.xml file
        ImageView statusImageField = (ImageView) rowView.findViewById(R.id.ListView_Matches_Status);
        TextView playersTextField = (TextView) rowView.findViewById(R.id.ListView_Matches_Players);
        TextView dateTextField = (TextView) rowView.findViewById(R.id.ListView_Matches_Date);
        TextView descriptionTextField = (TextView) rowView.findViewById(R.id.ListView_Matches_Description);

        //this code sets the values of the objects to values from the arrays
        if(statusArray[position] == 0)
        {
            statusImageField.setImageResource(R.drawable.ic_fight);
        }
        else
        {
            statusImageField.setImageResource(R.drawable.ic_fight_finish);
        }

        playersTextField.setText(player1Array[position] + " VS " + player2Array[position]);
        dateTextField.setText(dateArray[position]);
        descriptionTextField.setText(descriptionArray[position]);

        return rowView;

    };
}
