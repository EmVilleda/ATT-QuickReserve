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

    protected int start_time;
    protected int end_time;
    protected int date;
    protected String origin;
    protected int currentSector;
    private static final String ACTIVITY_DRAWER_REF = "";

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

        final boolean timeChange = intent.getBooleanExtra("timeChanged", false);
        final MySQLiteHelper reservationManager = new MySQLiteHelper(this);
        final Reservation reservation = reservationManager.getReservation(ID);

        origin = intent.getStringExtra("origin");

        if (origin.equals("edit")){
            start_time = intent.getIntExtra("start_time", 0);
            end_time = intent.getIntExtra("end_time", 0);
            date = intent.getIntExtra("date", 0);
        }else if (origin.equals("view")){
            date = reservation.getDate();
            start_time = reservation.getStartTime();
            end_time = reservation.getEndTime();
        }
        else{
            finish();
        }

        List<Workspace> availableWorkspaces = reservationManager.getOpenWorkspaces(date, start_time, end_time);
        //should possibly make a workspace list adapter instead
        ArrayList<String> workspaceNames = new ArrayList<String>();

        for (Workspace w: availableWorkspaces){
            workspaceNames.add(w.getName());
        }

        final ListView seatList = (ListView) findViewById(R.id.editReservationSeatList);
        final TouchImageView sectorImage = (TouchImageView) findViewById(R.id.editReservationSeatImage);
        Button submitButton = (Button) findViewById(R.id.editReservationSeatButton);
        final TextView dateText = (TextView) findViewById(R.id.editReservationSeatText);
        int selectedItem = -1;
        currentSector = 1;
        sectorImage.setImageResource(R.drawable.section_a);

        dateText.setText("Available workspaces for " + TimeParser.parseDate(date) + " during the time slot " + TimeParser.parseTime(start_time, end_time));

        //to use in listener
        final List<Workspace> workspaces = availableWorkspaces;
        ListAdapter seatListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, workspaceNames);
        seatList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        seatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                int sector = workspaces.get(position).getSector();
                seatList.setItemChecked(position, true);
                if (sector == 1 && currentSector!=1) {
                    sectorImage.setImageResource(R.drawable.section_a);
                    currentSector = 1;
                    sectorImage.resetZoom();
                } else if (sector == 2 && currentSector!=2) {
                    sectorImage.setImageResource(R.drawable.section_b);
                    currentSector = 2;
                    sectorImage.resetZoom();
                } else if(sector == 3 && currentSector!=3){
                    sectorImage.setImageResource(R.drawable.section_c);
                    currentSector = 3;
                    sectorImage.resetZoom();
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
                    boolean result = false;
                    if (origin.equals("edit")) {
                        result = controller.editReservation(ID, workspaces.get(checkedPosition).getName(), date, start_time, end_time);
                    } else{
                        result = controller.editReservation(ID, workspaces.get(checkedPosition).getName(), 0, 0, 0);
                    }
                    //Toast.makeText(EditReservationSeatActivity.this, checkedPosition + " " + selectedWorkspace.getName(), Toast.LENGTH_SHORT).show();
                    if (result == true) {
                        Toast.makeText(EditReservationSeatActivity.this, "Succesfully edited", Toast.LENGTH_SHORT).show();
                        if(origin.equals("edit")){
                            Intent intent = new Intent();
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }
                        finish();
                    }
                    else {
                        Toast.makeText(EditReservationSeatActivity.this, "Unknowns error", Toast.LENGTH_SHORT).show();
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

        return super.onOptionsItemSelected(item);
    }

}
