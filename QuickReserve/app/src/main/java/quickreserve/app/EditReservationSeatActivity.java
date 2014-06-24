package quickreserve.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class EditReservationSeatActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reservation_seat);

        Intent intent = getIntent();
        final int ID = intent.getIntExtra("ID", -1);
        if (ID == -1) {

            finish();
        }

        final MySQLiteHelper reservationManager = new MySQLiteHelper(this);
        //bad code
        Reservation reservation = new Reservation();
        boolean isFound = false;
        for(Reservation r: reservationManager.getAllReservations()){
            if(r.getID()==ID){
                reservation = r;
                isFound = true;
            }
        }
        if(isFound==false){
            finish();
        }

        int date = reservation.getDate();
        List<Workspace> availableWorkspaces = reservationManager.getOpenWorkspaces(date);
        //should possibly make a workspace list adapter instead
        ArrayList<String> workspaceNames = new ArrayList<String>();

        for (Workspace w: availableWorkspaces){
            workspaceNames.add(w.getName());
        }

        ListView seatList = (ListView) findViewById(R.id.editReservationSeatList);
        ImageView seatImage = (ImageView) findViewById(R.id.editReservationSeatImage);
        Button submitButton = (Button) findViewById(R.id.editReservationSeatButton);

        ListAdapter seatListAdapter = new ArrayAdapter<String>(this, R.id.simpleListItem, workspaceNames);
        seatList.setAdapter(seatListAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_reservation_seat, menu);
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
