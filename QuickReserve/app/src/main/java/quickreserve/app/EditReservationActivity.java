package quickreserve.app;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;


public class EditReservationActivity extends Activity {

    TextView dateSelected;
    private static TextView startTimeSelected;
    private static TextView endTimeSelected;
    CalendarView calendarView;
    View screenView;
    private static int timeButtonflag;


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

        dateSelected = (TextView) findViewById(R.id.editReservationDateText);
        startTimeSelected = (TextView) findViewById(R.id.editReservationStartTimeText);
        endTimeSelected = (TextView) findViewById(R.id.editReservationEndTimeText);
        calendarView = (CalendarView) findViewById(R.id.editReservationCalendarView);
        screenView = (View) findViewById(R.id.editReservationView);

        dateSelected.setText(TimeParser.parseDateFormat(date));
        startTimeSelected.setText(TimeParser.parseTime(start_time));
        endTimeSelected.setText(TimeParser.parseTime(end_time));


        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, (date / 10000));
        c.set(Calendar.MONTH, ((date /100) %100)-1);
        c.set(Calendar.DAY_OF_MONTH, (date % 100));
        c.getTimeInMillis();
        calendarView.setDate(c.getTimeInMillis());


        Button calendarButton = (Button) findViewById(R.id.editReservationDateButton);
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarView.setVisibility(View.VISIBLE);
                screenView.setVisibility(View.GONE);
            }
        });

        calendarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarView.setVisibility(View.GONE);
                screenView.setVisibility(View.VISIBLE);
            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int dayOfMonth) {

                int date = (year * 10000) + ((month+1)*100) + dayOfMonth;
                String tempDate = TimeParser.parseDateFormat(date);



                if( (!tempDate.trim().equals(dateSelected.getText().toString().trim())
                        && !dateSelected.getText().toString().equals("")))

                {
                    calendarView.setVisibility(View.GONE);
                    screenView.setVisibility(View.VISIBLE);
                }
                dateSelected.setText(TimeParser.parseDate(year, month + 1, dayOfMonth));
            }
        });

        Button startTimeButton = (Button) findViewById(R.id.editReservationStartButton);
        Button endTimeButton = (Button) findViewById(R.id.editReservationEndButton);

        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timeButtonflag = 1;
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(), "timePicker");

            }
        });
        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timeButtonflag = 2;
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(), "timePicker");

            }
        });


        Button submitButton = (Button) findViewById(R.id.editReservationSubmitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newDate = TimeParser.parseDate(dateSelected.getText().toString());
                int newStartTime = TimeParser.parseTime(startTimeSelected.getText().toString());
                int newEndTime = TimeParser.parseTime(endTimeSelected.getText().toString());
                if (newEndTime <= newStartTime){
                    Toast.makeText(EditReservationActivity.this, "End time must be later than start time", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(EditReservationActivity.this, "" + newDate, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(EditReservationActivity.this, EditReservationSeatActivity.class);
                    i.putExtra("ID", ID);
                    i.putExtra("start_time", newStartTime);
                    i.putExtra("end_time", newEndTime);
                    i.putExtra("date", newDate);
                    i.putExtra("origin", "edit");
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

    public static int getTimeButtonFlag() {
        return timeButtonflag;
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int time = 0;
            if(getTimeButtonFlag() == 1){
                time = TimeParser.parseTime(startTimeSelected.getText().toString());
            }
            else{
                time = TimeParser.parseTime(endTimeSelected.getText().toString());
            }
            int hour = time / 100;
            int minute = time % 100;



            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            String time = TimeParser.parseCalendarTime(hourOfDay, minute);

            if(getTimeButtonFlag() == 1)
            {
                startTimeSelected.setText(time);
            }
            else if(getTimeButtonFlag() == 2)
            {
                endTimeSelected.setText(time);
            }
        }
    }

}
