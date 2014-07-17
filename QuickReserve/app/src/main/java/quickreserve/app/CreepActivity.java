package quickreserve.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import quickreserve.app.R;

public class CreepActivity extends Activity {

    TextView foundReservation;
    EditText attUIDSelected;
    List<Reservation> reservationList;
    Button searchButton;
    ListView reservationListView;
    MySQLiteHelper reservationManager;
    String att_uid;
    int date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creep);

        att_uid = getIntent().getStringExtra("att_uid");

        foundReservation = (TextView) findViewById(R.id.creepTextView);
        reservationListView = (ListView) findViewById(R.id.creepReservationList);
        attUIDSelected = (EditText) findViewById(R.id.creepIDEditText);
        attUIDSelected.setImeOptions(EditorInfo.IME_ACTION_DONE);
        reservationList = new LinkedList<Reservation>();
        foundReservation.setText("");
        reservationManager = new MySQLiteHelper(this);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        date = calendar.get(Calendar.DAY_OF_MONTH) + ((calendar.get(Calendar.MONTH) + 1) * 100) + (calendar.get(Calendar.YEAR) * 10000);

        searchButton = (Button) findViewById(R.id.creepSearchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateList();
            }
        });

    }

    public void updateList(){
        final String ID = attUIDSelected.getText().toString();
        Toast.makeText(CreepActivity.this, ID, Toast.LENGTH_SHORT).show();
        reservationList = reservationManager.getUserReservations(ID);
        ArrayList<Reservation> reservationListByToday = new ArrayList<Reservation>();
        if(reservationList == null || reservationList.size()==0){
            foundReservation.setText("No Reservations for Today Found");
            reservationList = new LinkedList<Reservation>();
        }
        else{
            foundReservation.setText("Reservations for Today");
            for (Reservation r: reservationList){
                if(r.getDate() == date){
                    reservationListByToday.add(r);
                }
            }
        }
        MyReservationAdapter adapter = new MyReservationAdapter(this, R.layout.my_reservation_row_layout, reservationList);
        Log.e("test", reservationList.toString());
        reservationListView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.creep, menu);
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
