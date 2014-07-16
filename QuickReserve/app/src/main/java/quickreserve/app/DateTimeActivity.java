package quickreserve.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class DateTimeActivity extends Activity implements Animation.AnimationListener {

    private Button mDateButton;
    private Button mStartTimeButton;
    private Button mEndTimeButton;
    private Button mQuickReserveButton;
    private Button mPickSeatButton;
    private EditText mSelectedDate;
    private static EditText mSelectedStartTime;
    private static EditText mSelectedEndTime;
    private RelativeLayout mChoiceLayout;
    private CalendarView mCalendarView;
    private String tempDate;
    private Calendar calendar;
    private static int timeButtonflag;
    private static int hour_start;
    private static int min_start;
    private static int hour_end;
    private static int min_end;
    private static int day_selected;
    private static int month_selected;
    private static int year_selected;
    private String att_uid;
    private String[] mOptionsList;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private Animation fadeIn;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        setContentView(R.layout.activity_date_time);
        getActionBar().setTitle(getString(R.string.selectSeat));
        att_uid = getIntent().getStringExtra("att_uid");
        Toast.makeText(this, att_uid + " ", Toast.LENGTH_SHORT).show();
        mDateButton = (Button) findViewById(R.id.dateButton);
        mStartTimeButton = (Button) findViewById(R.id.startTimeButton);
        mEndTimeButton = (Button) findViewById(R.id.endTimeButton);
        mQuickReserveButton = (Button) findViewById(R.id.quickReserveButton);
        mPickSeatButton = (Button) findViewById(R.id.pickSeatButton);
        mSelectedDate = (EditText) findViewById(R.id.selectedDate);
        mSelectedStartTime = (EditText) findViewById(R.id.selectedStartTime);
        mSelectedEndTime = (EditText) findViewById(R.id.selectedEndTime);
        mChoiceLayout = (RelativeLayout) findViewById(R.id.choiceLayout);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        hour_start = calendar.get(Calendar.HOUR_OF_DAY);
        min_start = roundDown(calendar.get(Calendar.MINUTE));
        hour_end = calendar.get(Calendar.HOUR_OF_DAY) + 1;
        min_end = roundDown(calendar.get(Calendar.MINUTE));
        //min date must be earlier than the current time
        mCalendarView.setMinDate(calendar.getTimeInMillis()-3000);
        //14 days of milliseconds
        mCalendarView.setMaxDate(calendar.getTimeInMillis() + 1209600000);
        mSelectedStartTime.setText(TimeParser.parseCalendarTime(hour_start, min_start));
        mSelectedEndTime.setText(TimeParser.parseCalendarTime(hour_end, min_end));
        fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_fade_in);
        fadeIn.setAnimationListener(this);



        mSelectedDate.setText(TimeParser.parseDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)));
        timeButtonflag = -1;

        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fadeIn.setDuration(600);

                //mCalendarView.setVisibility(View.VISIBLE);
                //mChoiceLayout.setVisibility(View.GONE);
                mCalendarView.setAnimation(fadeIn);
                mCalendarView.startAnimation(fadeIn);
            }
        });

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int dayOfMonth) {

                int date = (year * 10000) + ((month+1)*100) + dayOfMonth;
                tempDate = TimeParser.parseDateFormat(date);

                Log.e("test", "onDateChanged");
                Log.e("test","button: " + mSelectedDate.getText());
                Log.e("test","temp: " + tempDate);

                if( (!tempDate.trim().equals(mSelectedDate.getText().toString().trim())
                                    && !mSelectedDate.getText().toString().equals("")))

                {
                    mCalendarView.clearAnimation();
                    mCalendarView.setVisibility(View.GONE);
                    mChoiceLayout.setVisibility(View.VISIBLE);
                }
                mSelectedDate.setText(TimeParser.parseDate(year, month + 1, dayOfMonth));
                day_selected = dayOfMonth;
                month_selected = month;
                year_selected = year;
            }
        });

        mStartTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timeButtonflag = 1;
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(), "timePicker");

            }
        });

        mEndTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeButtonflag = 2;
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(), "timePicker");

            }
        });

        mPickSeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int start_time = hour_start*100 + min_start;
                int end_time = hour_end * 100 + min_end;
                if(start_time >= end_time){
                    Toast.makeText(DateTimeActivity.this, "End time must be after start time", Toast.LENGTH_SHORT).show();
                }
                else if (!isUserTimeAvailable(TimeParser.parseDate(mSelectedDate.getText().toString()), start_time, end_time)){
                    Toast.makeText(DateTimeActivity.this, "You already have a reservation for this time.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                    intent.putExtra("start_time", start_time);
                    intent.putExtra("end_time", end_time);
                    Log.e("test", att_uid);
                    intent.putExtra("att_uid", att_uid);
                    intent.putExtra("date_selected", TimeParser.parseDate(mSelectedDate.getText().toString()));
                    startActivity(intent);
                }


            }
        });

        mQuickReserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int start_time = hour_start*100 + min_start;
                int end_time = hour_end * 100 + min_end;
                if(start_time >= end_time){
                    Toast.makeText(DateTimeActivity.this, "End time must be after start time", Toast.LENGTH_SHORT).show();
                }
                else if (!(isUserTimeAvailable(TimeParser.parseDate(mSelectedDate.getText().toString()), start_time, end_time))){
                    Toast.makeText(DateTimeActivity.this, "You already have a reservation for this time.", Toast.LENGTH_SHORT).show();
                }
                else{
                    quickReserve(TimeParser.parseDate(mSelectedDate.getText().toString()), start_time, end_time);
                }

            }
        });

        mOptionsList = getResources().getStringArray(R.array.options_list);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mOptionsList));

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ){

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(getString(R.string.title_activity_selectDateTime));
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(getString(R.string.options));
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }


    //Checks if user already has a reservation overlapping this time
    public boolean isUserTimeAvailable(int date, int start_time, int end_time){
        MySQLiteHelper manager = new MySQLiteHelper(DateTimeActivity.this);
        List<Reservation> currentReservations = manager.getUserReservations(att_uid);
        if(currentReservations==null || currentReservations.size()==0){
            return true;
        }
        else{
            for (Reservation r: currentReservations){
                if(r.getDate() == date){
                    if(r.getStartTime() >= start_time && r.getStartTime() < end_time){
                        return false;
                    }
                    else if(r.getEndTime() > start_time && r.getEndTime() <= end_time){
                        return false;
                    }
                    else if(r.getStartTime() <= start_time && r.getEndTime() >= end_time){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    //rounds down minute to match 15 minute interval
    public int roundDown(int min){
        if (min < 15){
            return 0;
        }
        else if (min < 30){
            return 15;
        }
        else if (min < 45){
            return 30;
        }
        else{
            return 45;
        }
    }

    //If possible reserves a seat at given time, prioritizes favorite seats,
    //but picks the first open seat if none of the favorites are available
    public void quickReserve(int date, int start_time, int end_time){
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(DateTimeActivity.this);
        String seat1= p.getString("seat1", "");
        String seat2= p.getString("seat2", "");
        String seat3= p.getString("seat3", "");
        boolean seat1F = false;
        boolean seat2F = false;
        boolean seat3F = false;
        MySQLiteHelper manager = new MySQLiteHelper(context);
        Toast.makeText(DateTimeActivity.this, seat1 + " " + seat2 + " " + seat3, Toast.LENGTH_SHORT).show();

        List<Workspace> openWorkspaces = manager.getOpenWorkspaces(date, start_time, end_time);
        if(openWorkspaces.size()==0){
            Toast.makeText(DateTimeActivity.this, "No seats are available for this time", Toast.LENGTH_SHORT).show();
            return;
        }
        for(Workspace w: openWorkspaces){
            if(w.getName().equals(seat1)){
                seat1F = true;
            }
            if(w.getName().equals(seat2)){
                seat2F = true;
            }
            if(w.getName().equals(seat3)){
                seat3F = true;
            }
        }
        String seatName = "";
        if(seat1F){
            seatName = seat1;
        }
        else if(seat2F){
            seatName = seat2;
        }
        else if(seat3F) {
            seatName = seat3;
        }
        else{
            seatName = openWorkspaces.get(0).getName();
        }
        ReservationController controller = new ReservationController(context);

        int result = controller.createReservation(seatName, att_uid, start_time, end_time, date);
        if(result == 0 || result == 1){
            Toast.makeText(DateTimeActivity.this, "Unknown error", Toast.LENGTH_SHORT).show();
        }
        if(result == 2){
            Toast.makeText(DateTimeActivity.this, "Seat " + seatName + " has been reserved", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(DateTimeActivity.this, MyReservationActivity.class);
            i.putExtra("att_uid", att_uid);
            startActivity(i);
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {
        mCalendarView.setVisibility(View.VISIBLE);
        mChoiceLayout.setVisibility(View.GONE);
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        //mChoiceLayout.setVisibility(View.GONE);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    private class DrawerItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            MapActivity.getToast().makeText(getApplicationContext(), mOptionsList[position].toString()
                    , Toast.LENGTH_SHORT).show();
            final int pos = position;
            mDrawerLayout.closeDrawers();
            mDrawerLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String option = mOptionsList[pos].toString();

                    if(option.equals("Add Reservation"))
                    {
                        Intent i = new Intent(getApplicationContext(), DateTimeActivity.class);
                        i.putExtra("att_uid", att_uid);
                        startActivity(i);
                    }
                    else if(option.equals("My Reservations"))
                    {
                        Intent i = new Intent(getApplicationContext(), MyReservationActivity.class);
                        i.putExtra("att_uid", att_uid);
                        startActivity(i);

                    }
                    else if(option.equals("Logout"))
                    {

                        AlertDialog.Builder logoutDialog = new AlertDialog.Builder(DateTimeActivity.this);
                        logoutDialog.setTitle("Do you want to log out?");
                        logoutDialog.setCancelable(false);
                        logoutDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                DateTimeActivity.this.finish();

                            }

                        });
                        logoutDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }

                        });

                        AlertDialog finishedDialog = logoutDialog.create();
                        finishedDialog.show();

                    }
                    else if(option.equals("About"))
                    {
                        Intent i = new Intent(getApplicationContext(), FavoriteSeatActivity.class);
                        startActivity(i);
                    }
                    else if(option.equals("Help"))
                    {

                    }

                }
            }, 300);

        }
    }

    @Override
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

    public static int getTimeButtonFlag() {
        return timeButtonflag;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.date_time, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        if (id == R.id.QRIcon) {
            Intent intent = new Intent(this, QRScannerActivity.class);
            intent.putExtra("att_uid", att_uid);
            startActivity(intent);
            return true;
        }
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int time = 0;
            if(getTimeButtonFlag() == 1){
                time = TimeParser.parseTime(mSelectedStartTime.getText().toString());
            }
            else{
                time = TimeParser.parseTime(mSelectedEndTime.getText().toString());
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
                mSelectedStartTime.setText(time);
                hour_start = hourOfDay;
                min_start = minute;
            }
            else if(getTimeButtonFlag() == 2)
            {
                mSelectedEndTime.setText(time);
                hour_end = hourOfDay;
                min_end = minute;
            }
        }
    }

    @Override
    public void onBackPressed() {

        if(mCalendarView.getVisibility() == View.VISIBLE)
        {
            mCalendarView.setVisibility(View.GONE);
            mChoiceLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            AlertDialog.Builder logoutDialog = new AlertDialog.Builder(DateTimeActivity.this);
            logoutDialog.setTitle("Do you want to log out?");
            logoutDialog.setCancelable(false);
            logoutDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    DateTimeActivity.this.finish();

                }

            });
            logoutDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }

            });

            AlertDialog finishedDialog = logoutDialog.create();
            finishedDialog.show();
            //super.onBackPressed();

        }




    }
}

