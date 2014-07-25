package quickreserve.app;

/*
*
* */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by at892q on 7/16/2014.
 */
public class MyDrawerRowAdapter extends ArrayAdapter<String> {

    private Context context;

    public MyDrawerRowAdapter(Context context, int textViewResourceId, List<String> items) {
        super(context, textViewResourceId, items);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.my_drawer_row_layout, null);
        }


        String labelName = (String)getItem(position);

        ImageView myIcon = (ImageView) view.findViewById(R.id.myDrawerRowIcon);
        TextView myLabelName = (TextView) view.findViewById(R.id.myDrawerRowLabel);
        myLabelName.setText(labelName);
        int id = R.drawable.ic_action_help;
        if(labelName.equals("Add Reservation")){
            id = R.drawable.ic_action_new_event;
        }
        else if(labelName.equals("My Reservations")){
            id = R.drawable.ic_action_event;
        }
        else if(labelName.equals("View Map")){
            id = R.drawable.ic_action_map;
        }
        else if(labelName.equals("Favorite Seats")){
            id = R.drawable.ic_action_important;
        }
        else if(labelName.equals("Scan QR Code")){
            id = R.drawable.ic_drawer_qr;
        }
        else if(labelName.equals("Find a Friend")){
            id = R.drawable.ic_action_person;
        }
        else if(labelName.equals("About")){
            id = R.drawable.ic_action_about;
        }
        else if(labelName.equals("Help")){
            id = R.drawable.ic_action_help;
        }
        else if(labelName.equals("Logout")){
            id = R.drawable.ic_action_logout;
        }

        myIcon.setImageResource(id);
        return view;
    }
}

