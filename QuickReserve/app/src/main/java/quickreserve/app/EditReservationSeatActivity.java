package quickreserve.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class EditReservationSeatActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reservation_seat);
        final Context context = this;

        Intent intent = getIntent();
        final int ID = intent.getIntExtra("ID", -1);
        if (ID == -1) {

            finish();
        }

        final MySQLiteHelper reservationManager = new MySQLiteHelper(this);
        Reservation reservation = reservationManager.getReservation(ID);

        int date = reservation.getDate();
        List<Workspace> availableWorkspaces = reservationManager.getOpenWorkspaces(date, 800, 1700);
        //should possibly make a workspace list adapter instead
        ArrayList<String> workspaceNames = new ArrayList<String>();

        for (Workspace w: availableWorkspaces){
            workspaceNames.add(w.getName());
        }

        final ListView seatList = (ListView) findViewById(R.id.editReservationSeatList);
        final ImageView seatImage = (ImageView) findViewById(R.id.editReservationSeatImage);
        Button submitButton = (Button) findViewById(R.id.editReservationSeatButton);
        final TextView dateText = (TextView) findViewById(R.id.editReservationSeatText);
        int selectedItem = -1;

        dateText.setText("Available workspaces for " + TimeParser.parseDate(date) + " during the time slot " + TimeParser.parseTime(700, 1700));

        //to use in listener
        final List<Workspace> workspaces = availableWorkspaces;
        ListAdapter seatListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, workspaceNames);
        seatList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        seatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                int sector = workspaces.get(position).getSector();
                seatList.setItemChecked(position, true);
                if (sector == 1) {
                    seatImage.setImageResource(R.drawable.section_a);
                } else if (sector == 2) {
                    seatImage.setImageResource(R.drawable.section_b);
                } else {
                    seatImage.setImageResource(R.drawable.section_c);
                }
            }
        });

        seatList.setAdapter(seatListAdapter);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int checkedPosition = seatList.getCheckedItemPosition();
                    ReservationController controller = new ReservationController(context);
                    int result = controller.editReservationSeat(ID, workspaces.get(checkedPosition).getName());
                    //Toast.makeText(EditReservationSeatActivity.this, checkedPosition + " " + selectedWorkspace.getName(), Toast.LENGTH_SHORT).show();
                    if (result == 0) {
                        Toast.makeText(EditReservationSeatActivity.this, "Unknown adding error", Toast.LENGTH_SHORT).show();
                    } else if (result == 1) {
                        Toast.makeText(EditReservationSeatActivity.this, "Scheduling conflict", Toast.LENGTH_SHORT).show();
                    } else if (result == 2) {
                        Toast.makeText(EditReservationSeatActivity.this, "Seat Edited successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(EditReservationSeatActivity.this, "Unkown deleting error", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception E) {
                    Toast.makeText(EditReservationSeatActivity.this, "Please select a seat", Toast.LENGTH_SHORT).show();

                }
            }
        });

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
