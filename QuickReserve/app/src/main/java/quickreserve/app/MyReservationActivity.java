package quickreserve.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MyReservationActivity extends ActionBarActivity {
    List<Reservation> reservationList;
    MySQLiteHelper reservationManager = new MySQLiteHelper(this);
    //att_uid will be passed in the intent to this activity and should replace this one
    String att_uid = "ca8681";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reservation);
        updateList();
    }

    @Override
    protected void onResume(){
        super.onResume();
        updateList();
    }

/*
 *Gets the most current reservations and assigns them to the array adapter
 */
    public void updateList(){
        reservationList = reservationManager.getUserReservations(reservationManager.getUser(att_uid));
        ListView reservationView = (ListView) findViewById(R.id.myReservationListView);
        if(reservationList!= null){
            MyReservationAdapter adapter = new MyReservationAdapter(this, R.layout.my_reservation_row_layout, reservationList);
            reservationView.setAdapter(adapter);
            reservationView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    int ID = reservationList.get(position).getID();
                    Intent intent = new Intent(MyReservationActivity.this, ViewReservationActivity.class);
                    intent.putExtra("ID", ID);
                    startActivity(intent);
                }
            });
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_reservation, menu);
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
