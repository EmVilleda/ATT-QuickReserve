package quickreserve.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by at892q on 6/12/2014.
 */
public class MyReservationAdapter extends ArrayAdapter<Reservation>{


    private Context context;

    public MyReservationAdapter(Context context, int textViewResourceId, List<Reservation> items) {
        super(context, textViewResourceId, items);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.my_reservation_row_layout, null);
        }

        Reservation reservation = (Reservation)getItem(position);




            TextView dateView = (TextView) view.findViewById(R.id.myReservationRowDate);
            TextView timeView = (TextView) view.findViewById(R.id.myReservationRowTime);
            TextView seatView = (TextView) view.findViewById(R.id.myReservationRowSeat);


        seatView.setText(reservation.getWorkspaceID() + "");
        dateView.setText(TimeParser.parseDate(reservation.getDate()));
        timeView.setText(TimeParser.parseTime(reservation.getStartTime(), reservation.getEndTime()));


        return view;
    }
}
