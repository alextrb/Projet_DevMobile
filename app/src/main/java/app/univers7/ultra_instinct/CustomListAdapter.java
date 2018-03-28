package app.univers7.ultra_instinct;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Alex on 28/03/2018.
 */

public class CustomListAdapter extends ArrayAdapter {

    //to reference the Activity
    private final Activity context;

    //to store the list of matches
    private final String[] infoArray;

    public CustomListAdapter(Activity context, String[] infoArrayParam){

        super(context,R.layout.allmatches_listview_row , infoArrayParam);

        this.context=context;
        this.infoArray = infoArrayParam;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.allmatches_listview_row, null,true);

        //this code gets references to objects in the listview_row.xml file
        TextView infoTextField = (TextView) rowView.findViewById(R.id.Listview_textView);

        //this code sets the values of the objects to values from the arrays
        infoTextField.setText(infoArray[position]);

        return rowView;

    };
}
