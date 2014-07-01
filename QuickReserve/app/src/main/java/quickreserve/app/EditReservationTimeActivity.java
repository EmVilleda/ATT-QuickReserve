package quickreserve.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class EditReservationTimeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reservation_time);

        final Context context = this;
        Intent intent = getIntent();
        final int ID = intent.getIntExtra("ID", -1);
        if (ID == -1) {

            finish();
        }

        final MySQLiteHelper reservationManager = new MySQLiteHelper(this);
        Reservation reservation = reservationManager.getReservation(ID);


        int date = reservation.getDate();
        int start_time = reservation.getStartTime();
        int end_time = reservation.getEndTime();
        String seat = reservation.getWorkspaceID();
        //remove when sqlhelper is updated
        List<Reservation> allReservations = reservationManager.getAllReservations();
        ArrayList<Reservation> seatReservations = new ArrayList<Reservation>();
        for(Reservation r: allReservations){
            //ignores the reservation being edited
            if (r.getWorkspaceID().equals(seat) && r.getID()!=ID){
                seatReservations.add(r);
            }
        }
        final ArrayList<Reservation> finalReservations = seatReservations;



        final TextView dateSelected = (TextView) findViewById(R.id.editReservationTimeDateText);
        final TextView startTimeSelected = (TextView) findViewById(R.id.editReservationTimeStartTimeText);
        final TextView endTimeSelected = (TextView) findViewById(R.id.editReservationTimeEndTimeText);

        dateSelected.setText(TimeParser.parseDateFormat(date));
        startTimeSelected.setText(TimeParser.parseTime(start_time));
        endTimeSelected.setText(TimeParser.parseTime(end_time));

        updateList(seatReservations);

    }

    public void updateList(ArrayList<Reservation> reservations){
        TextView dateSelected = (TextView) findViewById(R.id.editReservationTimeDateText);
        final int date = TimeParser.parseDate(dateSelected.getText().toString());
        Toast.makeText(EditReservationTimeActivity.this, date + "",Toast.LENGTH_SHORT).show();
        ListView reservationListView = (ListView) findViewById(R.id.editReservationTimeList);
        ArrayList<Reservation> reservationList = new ArrayList<Reservation>();
        for (Reservation r: reservations){
            if(r.getDate() == date){
                reservationList.add(r);
            }
        }
        if(reservationList.size()==0){
        }
        else{
            MyReservationAdapter adapter = new MyReservationAdapter(this, R.layout.my_reservation_row_layout, reservationList);
            Log.e("test", reservationList.toString());
            reservationListView.setAdapter(adapter);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_reservation_time, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
