package app.univers7.ultra_instinct;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomListAdapterPlayers extends ArrayAdapter{

    //to reference the Activity
    private final Activity context;

    //to store the list of matches
    private final String[] nameArray;

    public CustomListAdapterPlayers(Activity context, String[] nameArrayParam){

        super(context,R.layout.allmatches_listview_row , nameArrayParam);

        this.context=context;
        this.nameArray = nameArrayParam;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.allplayers_listview_row, null,true);

        //this code gets references to objects in the listview_row.xml file
        TextView infoTextField = (TextView) rowView.findViewById(R.id.Listview_textView_playername);

        //this code sets the values of the objects to values from the arrays
        infoTextField.setText(nameArray[position]);

        return rowView;

    };
}
