package quickreserve.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class EditReservationActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reservation);

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

        final TextView dateSelected = (TextView) findViewById(R.id.editReservationDateText);
        final TextView startTimeSelected = (TextView) findViewById(R.id.editReservationStartTimeText);
        final TextView endTimeSelected = (TextView) findViewById(R.id.editReservationEndTimeText);

        dateSelected.setText(TimeParser.parseDateFormat(date));
        startTimeSelected.setText(TimeParser.parseTime(start_time));
        endTimeSelected.setText(TimeParser.parseTime(end_time));

        Button submitButton = (Button) findViewById(R.id.editReservationSubmitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newDate = TimeParser.parseDate(dateSelected.getText().toString());
                int newStartTime = TimeParser.parseTime(startTimeSelected.getText().toString());
                int newEndTime = TimeParser.parseTime(endTimeSelected.getText().toString());
                if (newEndTime <= newStartTime){
                    Toast.makeText(EditReservationActivity.this, "End time must be later than start time " + newEndTime+ " " + newStartTime, Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent i = new Intent(EditReservationActivity.this, EditReservationSeatActivity.class);
                    i.putExtra("ID", ID);
                    i.putExtra("start_time", newStartTime);
                    i.putExtra("end_time", newEndTime);
                    i.putExtra("date", newDate);
                    i.putExtra("timeChange", true);
                    startActivityForResult(i, 1);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                finish();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_reservation, menu);
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
