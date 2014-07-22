package quickreserve.app;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
public class MyEditReservationAdapter extends ArrayAdapter<Reservation>{


    private Context context;
    private ImageButton icon;

    public MyEditReservationAdapter(Context context, int textViewResourceId, List<Reservation> items) {
        super(context, textViewResourceId, items);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.my_edit_reservation_row_layout, null);
        }

        final Reservation reservation = (Reservation)getItem(position);

        TextView dateView = (TextView) view.findViewById(R.id.myReservationRowDate);
        TextView timeView = (TextView) view.findViewById(R.id.myReservationRowTime);
        TextView seatView = (TextView) view.findViewById(R.id.myReservationRowSeat);
        icon = (ImageButton) view.findViewById(R.id.myEditReservationIcon);


        //icon.setImageResource(R.drawable.ic_action_edit);
        seatView.setText(reservation.getWorkspaceID() + "");
        dateView.setText(TimeParser.parseDate(reservation.getDate()));
        timeView.setText(TimeParser.parseTime(reservation.getStartTime(), reservation.getEndTime()));

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ID = reservation.getID();
                final Intent intent = new Intent(getContext(), ViewReservationActivity.class);
                intent.putExtra("ID", ID);
                intent.putExtra("att_uid", reservation.getAttUid());
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getContext().startActivity(intent);
                    }
                }, 180);

            };


        });

        icon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                icon.setImageResource(R.drawable.ic_action_edit2_highlight);

                return false;
            }
        });

        return view;
    }
}
