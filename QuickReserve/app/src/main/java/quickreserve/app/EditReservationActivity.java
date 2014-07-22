package quickreserve.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.qozix.animation.easing.Linear;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class EditReservationActivity extends Activity implements Animation.AnimationListener{

    EditText dateSelected;
    private static EditText startTimeSelected;
    private static EditText endTimeSelected;
    private static EditText seatSelected;
    CalendarView calendarView;
    View screenView;
    private static int timeButtonflag;
    ArrayList<Reservation> seatReservations;
    ArrayList<Reservation> reservationList;
    String seat;
    private static final String ACTIVITY_DRAWER_REF = "";
    private String att_uid;
    private static TextView reservationText;
    List<Reservation> allReservations;
    int ID;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] mOptionsList;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private LinearLayout otherReservationsLayout;
    private TextView noReservationTextView;
    private Button editDateOverlayButton;
    private Button editStartOverlayButton;
    private Button editEndOverlayButton;
    private Button editSeatOverlayButton;
    private Animation fadeIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reservation);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        otherReservationsLayout = (LinearLayout)findViewById(R.id.otherReservationsLayout);
        noReservationTextView = (TextView) findViewById(R.id.noReservationTextView);
        final Context context = this;
        Intent intent = getIntent();
        att_uid = intent.getStringExtra("att_uid");
        ID = intent.getIntExtra("ID", -1);
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
        allReservations = reservationManager.getAllReservations();
        seatReservations = new ArrayList<Reservation>();
        for(Reservation r: allReservations){
            //ignores the reservation being edited
            if (r.getWorkspaceID().equals(seat) && r.getID()!=ID){
                seatReservations.add(r);
            }
        }

        dateSelected = (EditText) findViewById(R.id.editReservationDateText);
        startTimeSelected = (EditText) findViewById(R.id.editReservationStartTimeText);
        endTimeSelected = (EditText) findViewById(R.id.editReservationEndTimeText);
        calendarView = (CalendarView) findViewById(R.id.editReservationCalendarView);
        reservationText = (TextView) findViewById(R.id.editReservationTimeTextView);
        seatSelected = (EditText) findViewById(R.id.editReservationChangeSeatText);

        dateSelected.setText(TimeParser.parseDateFormat(date));
        startTimeSelected.setText(TimeParser.parseTime(start_time));
        endTimeSelected.setText(TimeParser.parseTime(end_time));
        seatSelected.setText(seat);

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

        editDateOverlayButton = (Button) findViewById(R.id.editDateOverlayButton);
        editStartOverlayButton = (Button) findViewById(R.id.editStartTimeOverlayButton);
        editEndOverlayButton = (Button) findViewById(R.id.editEndTimeOverlayButton);
        editSeatOverlayButton = (Button) findViewById(R.id.editSeatOverlayButton);
        fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_fade_in);
        fadeIn.setAnimationListener(this);


        Button calendarButton = (Button) findViewById(R.id.editReservationDateButton);
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(calendarView.getVisibility() != View.VISIBLE)
                {
                    fadeIn.setDuration(600);

                    //mCalendarView.setVisibility(View.VISIBLE);
                    //mChoiceLayout.setVisibility(View.GONE);
                    calendarView.setAnimation(fadeIn);
                    calendarView.startAnimation(fadeIn);
                }
                else
                    calendarView.setVisibility(View.GONE);

            }

        });

        View.OnClickListener dateChangeListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarView.setVisibility(View.GONE);
               // screenView.setVisibility(View.VISIBLE);
            }
        };

        calendarView.setOnClickListener(dateChangeListener);

        editDateOverlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editDateOverlayButton.setVisibility(View.VISIBLE);
                //fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_fade_in);

                Log.e("test", "overlayclicked");
                if(calendarView.getVisibility() != View.VISIBLE)
                {
                    fadeIn.setDuration(600);
                    calendarView.setAnimation(fadeIn);
                    calendarView.setVisibility(View.INVISIBLE);
                    calendarView.startAnimation(fadeIn);
                    Log.e("test", "should start showing ");
                }
                else {
                    calendarView.setVisibility(View.GONE);
                    Log.e("test", "should be gone");
                }
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
                    //screenView.setVisibility(View.VISIBLE);
                }
                dateSelected.setText(TimeParser.parseDate(year, month + 1, dayOfMonth));
                updateList();
            }
        });

        Button startTimeButton = (Button) findViewById(R.id.editReservationStartButton);
        Button endTimeButton = (Button) findViewById(R.id.editReservationEndButton);
        Button changeSeatButton = (Button) findViewById(R.id.editReservationChangeSeatButton);


        View.OnClickListener startTimeListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timeButtonflag = 1;
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(), "timePicker");

            }
        };

        startTimeButton.setOnClickListener(startTimeListener);
        editStartOverlayButton.setOnClickListener(startTimeListener);



        View.OnClickListener endTimeListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timeButtonflag = 2;
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(), "timePicker");

            }
        };

        endTimeButton.setOnClickListener(endTimeListener);
        editEndOverlayButton.setOnClickListener(endTimeListener);

        View.OnClickListener changeSeatListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, EditReservationSeatActivity.class);
                i.putExtra("att_uid", att_uid);
                i.putExtra("seatName", seat);
                startActivityForResult(i, 1);
            }
        };
        changeSeatButton.setOnClickListener(changeSeatListener);
        editSeatOverlayButton.setOnClickListener(changeSeatListener);

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
                    ReservationController controller = new ReservationController(context);
                    boolean result = controller.editReservation(ID, seat, newDate, newStartTime, newEndTime);

                    if (result == true) {
                        Toast.makeText(context, "Reservation succesfully edited", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        Toast.makeText(context, "Unknown error", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    AlertDialog.Builder seatChangeDialog = new AlertDialog.Builder(context);
                    seatChangeDialog.setTitle("Reservation conflict!");
                    seatChangeDialog.setMessage("Seat is reserved by a collegue  for the selected time and date. Change the seat or time.");
                    seatChangeDialog.setCancelable(true);
                    seatChangeDialog.setPositiveButton("Change Seat", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent i = new Intent(context, EditReservationSeatActivity.class);
                            i.putExtra("att_uid", att_uid);
                            i.putExtra("seatName", seat);
                            startActivityForResult(i, 1);
                        }

                    });
                    seatChangeDialog.setNegativeButton("Change Time", new DialogInterface.OnClickListener() {
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

        mOptionsList = getResources().getStringArray(R.array.options_list);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        ArrayList<String> drawerOptions = new ArrayList<String>(Arrays.asList(mOptionsList));


        // Set the adapter for the list view
        mDrawerList.setAdapter(new MyDrawerRowAdapter(this,
                R.layout.my_drawer_row_layout, drawerOptions));

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener(att_uid, ACTIVITY_DRAWER_REF
                ,getApplicationContext(),this,mDrawerLayout,mOptionsList));

        mDrawerToggle = new ActionBarDrawerToggle(
                EditReservationActivity.this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ){

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(getString(R.string.title_activity_edit_reservation));
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(getString(R.string.options));
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

    }


    @Override
    public void onAnimationStart(Animation animation) {

        Log.e("test", "animation should start");
        if(calendarView.getVisibility() == View.VISIBLE)
            calendarView.setVisibility(View.GONE);
        else
            calendarView.setVisibility(View.VISIBLE);

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        //mChoiceLayout.setVisibility(View.GONE);
        Log.e("test", "should be done showing ");
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

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

        seatReservations = new ArrayList<Reservation>();
        for(Reservation r: allReservations){
            //ignores the reservation being edited
            if (r.getWorkspaceID().equals(seat) && r.getID()!=ID){
                seatReservations.add(r);
            }
        }

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
            otherReservationsLayout.setVisibility(View.GONE);
            noReservationTextView.setVisibility(View.VISIBLE);
            noReservationTextView.setText("No other reservations for seat " + seat);
        }
        else{
            otherReservationsLayout.setVisibility(View.VISIBLE);
            noReservationTextView.setVisibility(View.GONE);
            reservationText.setText("Reservations for " + seat + " by collegues");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String seatName = data.getStringExtra("selectedSeat");
                if (!(seatName.equals(""))) {
                    seat = seatName;
                    seatSelected.setText(seatName);
                    updateList();
                }
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
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
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

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
