package quickreserve.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class EditReservationActivity extends Activity {

    TextView dateSelected;
    private static TextView startTimeSelected;
    private static TextView endTimeSelected;
    CalendarView calendarView;
    View screenView;
    private static int timeButtonflag;
    ArrayList<Reservation> seatReservations;
    ArrayList<Reservation> reservationList;
    String seat;
    private static final String ACTIVITY_DRAWER_REF = "";
    private String att_uid;
    private static TextView reservationText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reservation);

        final Context context = this;
        Intent intent = getIntent();
        att_uid = intent.getStringExtra("att_uid");
        final int ID = intent.getIntExtra("ID", -1);
        if (ID == -1) {

            finish();
        }

        final MySQLiteHelper reservationManager = new MySQLiteHelper(this);
        Reservation reservation = reservationManager.getReservation(ID);

        int date = reservation.getDate();
        int start_time = reservation.getStartTime();
        int end_time = reservation.getEndTime();
        seat = reservation.getWorkspaceID();

        //remove when sqlhelper is updated
        List<Reservation> allReservations = reservationManager.getAllReservations();
        seatReservations = new ArrayList<Reservation>();
        for(Reservation r: allReservations){
            //ignores the reservation being edited
            if (r.getWorkspaceID().equals(seat) && r.getID()!=ID){
                seatReservations.add(r);
            }
        }

        dateSelected = (TextView) findViewById(R.id.editReservationDateText);
        startTimeSelected = (TextView) findViewById(R.id.editReservationStartTimeText);
        endTimeSelected = (TextView) findViewById(R.id.editReservationEndTimeText);
        calendarView = (CalendarView) findViewById(R.id.editReservationCalendarView);
        screenView = (View) findViewById(R.id.editReservationView);
        reservationText = (TextView) findViewById(R.id.editReservationTimeTextView);

        dateSelected.setText(TimeParser.parseDateFormat(date));
        startTimeSelected.setText(TimeParser.parseTime(start_time));
        endTimeSelected.setText(TimeParser.parseTime(end_time));

        updateList();

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, (date / 10000));
        c.set(Calendar.MONTH, ((date /100) %100)-1);
        c.set(Calendar.DAY_OF_MONTH, (date % 100));
        c.getTimeInMillis();
        calendarView.setDate(c.getTimeInMillis());
        //min date must be earlier than the current time
        calendarView.setMinDate(System.currentTimeMillis()-3000);
        //14 days of milliseconds
        calendarView.setMaxDate(System.currentTimeMillis() + 1209600000);


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
                updateList();
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
                final int newDate = TimeParser.parseDate(dateSelected.getText().toString());
                final int newStartTime = TimeParser.parseTime(startTimeSelected.getText().toString());
                final int newEndTime = TimeParser.parseTime(endTimeSelected.getText().toString());
                if (newEndTime <= newStartTime){
                    Toast.makeText(EditReservationActivity.this, "End time must be later than start time", Toast.LENGTH_SHORT).show();
                }
                else if (isTimeAvail()){
                    Intent i = new Intent(EditReservationActivity.this, EditReservationSeatActivity.class);
                    i.putExtra("ID", ID);
                    i.putExtra("start_time", newStartTime);
                    i.putExtra("end_time", newEndTime);
                    i.putExtra("date", newDate);
                    i.putExtra("seatName", seat);
                    i.putExtra("origin", "edit");
                    startActivityForResult(i, 1);
                }
                else{
                    AlertDialog.Builder seatChangeDialog = new AlertDialog.Builder(context);
                    seatChangeDialog.setTitle("Confirm seat change.");
                    seatChangeDialog.setMessage("Your current seat has another reservation at this time. Confirm seat change?");
                    seatChangeDialog.setCancelable(true);
                    seatChangeDialog.setPositiveButton("Change Seat", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent i = new Intent(EditReservationActivity.this, EditReservationSeatActivity.class);
                            i.putExtra("ID", ID);
                            i.putExtra("start_time", newStartTime);
                            i.putExtra("end_time", newEndTime);
                            i.putExtra("date", newDate);
                            i.putExtra("seatName", "");
                            i.putExtra("origin", "edit");
                            startActivityForResult(i, 1);
                        }

                    });
                    seatChangeDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }

                    });

                    AlertDialog finishedDialog = seatChangeDialog.create();
                    //DialogColor.dialogColor(finishedDialog);
                    //finishedDialog.getWindow().requestFeature(Window.FEATURE_CUSTOM_TITLE);
                    finishedDialog.show();
                }
            }
        });
    }
    public boolean isTimeAvail(){
        int newStartTime = TimeParser.parseTime(startTimeSelected.getText().toString());
        int newEndTime = TimeParser.parseTime(endTimeSelected.getText().toString());
        for(Reservation r: reservationList){
            if(r.getStartTime() >= newStartTime && r.getStartTime() < newEndTime){
                return false;
            }
            else if(r.getEndTime() > newStartTime && r.getEndTime() <= newEndTime){
                return false;
            }
            else if(r.getStartTime() <= newStartTime && r.getEndTime() >= newEndTime){
                return false;
            }
        }
        return true;
    }

    public void updateList(){
        final int date = TimeParser.parseDate(dateSelected.getText().toString());
        ListView reservationListView = (ListView) findViewById(R.id.editReservationTimeList);
        reservationList = new ArrayList<Reservation>();
        for (Reservation r: seatReservations){
            if(r.getDate() == date){
                reservationList.add(r);
            }
        }
        MyReservationAdapter adapter = new MyReservationAdapter(this, R.layout.my_reservation_row_layout, reservationList);
        Log.e("test", reservationList.toString());
        reservationListView.setAdapter(adapter);
        if (reservationList.size()==0){
            reservationText.setText("");
        }
        else{
            reservationText.setText("Reservations for " + seat + " on selected day");
        }
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

        return super.onOptionsItemSelected(item);
    }

    public static int getTimeButtonFlag() {
        return timeButtonflag;
    }

    //change set end time if start time overlaps
    private static void updateEndTime(){
        int start = TimeParser.parseTime(startTimeSelected.getText().toString());
        int end = TimeParser.parseTime(endTimeSelected.getText().toString());

        if(start >= end){
            end = (start + 100) % 2400;
            endTimeSelected.setText(TimeParser.parseCalendarTime(end /100, end % 100));
        }
    }

    //change set start time if end time overlaps
    private static void updateStartTime(){
        int start = TimeParser.parseTime(startTimeSelected.getText().toString());
        int end = TimeParser.parseTime(endTimeSelected.getText().toString());

        if (end <= start){
            start = end - 100;
            startTimeSelected.setText(TimeParser.parseCalendarTime(start / 100, start % 100));
        }
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
            return new CustomTimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            String time = TimeParser.parseCalendarTime(hourOfDay, minute);

            if(getTimeButtonFlag() == 1)
            {
                startTimeSelected.setText(time);
                updateEndTime();
            }
            else if(getTimeButtonFlag() == 2)
            {
                endTimeSelected.setText(time);
                updateStartTime();
            }
        }
    }
}
