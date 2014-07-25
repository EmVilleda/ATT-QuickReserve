package quickreserve.app;

import android.app.Activity;
import android.app.ActivityManager;
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
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class DateTimeActivity extends Activity implements Animation.AnimationListener {

    private ImageButton mDateButton;
    private ImageButton mStartTimeButton;
    private ImageButton mEndTimeButton;
    private Button mQuickReserveButton;
    private Button mPickSeatButton;
    private Button mDateOverlayButton;
    private Button mStartTimeOverlayButton;
    private Button mEndTimeOverlayButton;
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

    private static int timePickerFlag;
    private String att_uid;
    private String[] mOptionsList;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private Animation fadeIn;
    private Context context;
    private static final String ACTIVITY_DRAWER_REF = "Add Reservation";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        setContentView(R.layout.activity_date_time);
        getActionBar().setTitle("Select Date and Time");
        att_uid = getIntent().getStringExtra("att_uid");
        Log.e("test", "testing uid - " + att_uid);
        mDateButton = (ImageButton) findViewById(R.id.dateButton);
        mDateOverlayButton = (Button) findViewById(R.id.dateOverlayButton);
        mStartTimeOverlayButton = (Button) findViewById(R.id.startTimeOverlayButton);
        mEndTimeOverlayButton = (Button) findViewById(R.id.endTimeOverlayButton);
        mStartTimeButton = (ImageButton) findViewById(R.id.startTimeButton);
        mEndTimeButton = (ImageButton) findViewById(R.id.endTimeButton);
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
        day_selected = calendar.get(Calendar.DAY_OF_MONTH);
        month_selected = calendar.get(Calendar.MONTH);
        year_selected = calendar.get(Calendar.YEAR);
        //min date must be earlier than the current time
        mCalendarView.setMinDate(calendar.getTimeInMillis() - 3000);
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

                mDateOverlayButton.setVisibility(View.VISIBLE);
                if (mCalendarView.getVisibility() != View.VISIBLE) {
                    fadeIn.setDuration(600);

                    //mCalendarView.setVisibility(View.VISIBLE);
                    //mChoiceLayout.setVisibility(View.GONE);
                    mCalendarView.setVisibility(View.INVISIBLE);
                    mCalendarView.setAnimation(fadeIn);
                    mCalendarView.startAnimation(fadeIn);
                } else
                    mCalendarView.setVisibility(View.GONE);

            }
        });

        mDateOverlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDateOverlayButton.setVisibility(View.VISIBLE);
                //fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_fade_in);

                Log.e("test", "overlayclicked");
                if (mCalendarView.getVisibility() != View.VISIBLE) {
                    fadeIn.setDuration(600);
                    mCalendarView.setAnimation(fadeIn);
                    mCalendarView.setVisibility(View.INVISIBLE);
                    mCalendarView.startAnimation(fadeIn);
                    Log.e("test", "should start showing ");
                } else {
                    mCalendarView.setVisibility(View.GONE);
                    Log.e("test", "should be gone");
                }
            }
        });


        mCalendarView.setShownWeekCount(3);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int dayOfMonth) {

                int date = (year * 10000) + ((month + 1) * 100) + dayOfMonth;
                tempDate = TimeParser.parseDateFormat(date);

                Log.e("test", "onDateChanged");
                Log.e("test", "button: " + mSelectedDate.getText());
                Log.e("test", "temp: " + tempDate);

                if ((!tempDate.trim().equals(mSelectedDate.getText().toString().trim())
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
                timePickerFlag = 1;
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(), "timePicker");


            }
        });

        mStartTimeOverlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timeButtonflag = 1;
                timePickerFlag = 1;
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(), "timePicker");

            }
        });


        mEndTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeButtonflag = 2;
                timePickerFlag = 2;
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(), "timePicker");

            }
        });

        mEndTimeOverlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeButtonflag = 2;
                timePickerFlag = 2;
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(), "timePicker");

            }
        });

        mPickSeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar c = Calendar.getInstance();
                int current_time = (c.get(Calendar.HOUR_OF_DAY) * 100  + c.get(Calendar.MINUTE));
                int start_time = hour_start * 100 + min_start;
                int end_time = hour_end * 100 + min_end;
                if (start_time >= end_time) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Reservation error");
                    alertDialog.setMessage("End time must be after start time");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    alertDialog.show();
                }
                else if (!isUserTimeAvailable(TimeParser.parseDate(mSelectedDate.getText().toString()), start_time, end_time)) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Reservation error");
                    alertDialog.setMessage("You already have a reservation for this time.");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    alertDialog.show();

                }
                else if(day_selected == c.get(Calendar.DAY_OF_MONTH) && end_time <= current_time)
                {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Reservation error");
                    alertDialog.setMessage("Time requested has already passed");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    alertDialog.show();

                }

                else {
                    Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                    intent.putExtra("start_time", start_time);
                    intent.putExtra("end_time", end_time);
                    Log.e("test", att_uid);
                    intent.putExtra("att_uid", att_uid);
                    intent.putExtra("date_selected", TimeParser.parseDate(mSelectedDate.getText().toString()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });

        mQuickReserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int current_time = (c.get(Calendar.HOUR_OF_DAY) * 100  + c.get(Calendar.MINUTE));
                int start_time = hour_start * 100 + min_start;
                int end_time = hour_end * 100 + min_end;

                Log.e("final test", "current hour = " + c.get(Calendar.HOUR_OF_DAY));
                Log.e("final test", "current minute = " + c.get(Calendar.MINUTE));
                Log.e("final test", "current time = " + (c.get(Calendar.HOUR_OF_DAY) * 100  + c.get(Calendar.MINUTE)));
                Log.e("final test", "start time = " + start_time);
                Log.e("final test", "end time = " + end_time);
                Log.e("final test", "day selected = " + day_selected + " month selected = " + month_selected
                                    + "year selected = " + year_selected);
                Log.e("final test", "cal day = " + c.get(Calendar.DAY_OF_MONTH) + " cal month = " + c.get(Calendar.MONTH)
                                    + "cal year = " + c.get(Calendar.YEAR));


                if (start_time >= end_time) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Reservation error");
                    alertDialog.setMessage("End time must be after start time");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    alertDialog.show();
                }
                else if (!(isUserTimeAvailable(TimeParser.parseDate(mSelectedDate.getText().toString()), start_time, end_time))) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Reservation error");
                    alertDialog.setMessage("You already have a reservation for this time.");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    alertDialog.show();

                }
                else if(day_selected == c.get(Calendar.DAY_OF_MONTH) && end_time <= current_time)
                {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Reservation error");
                    alertDialog.setMessage("Time requested has already passed");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    alertDialog.show();

                }
                else {
                    quickReserve(TimeParser.parseDate(mSelectedDate.getText().toString()), start_time, end_time);
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
                , getApplicationContext(), this, mDrawerLayout, mOptionsList));

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

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

    //change set end time if start time overlaps
    private static void updateEndTime() {
        int start = hour_start * 100 + min_start;
        int end = hour_end * 100 + min_end;

        if (start >= end) {
            hour_end = (hour_start + 1) % 24;
            min_end = min_start;
            mSelectedEndTime.setText(TimeParser.parseCalendarTime(hour_end, min_end));
        }
    }

    //change set start time if end time overlaps
    private static void updateStartTime() {
        int start = hour_start * 100 + min_start;
        int end = hour_end * 100 + min_end;

        if (end <= start) {
            hour_start = hour_end - 1;
            min_start = min_end;
            mSelectedStartTime.setText(TimeParser.parseCalendarTime(hour_start, min_start));
        }
    }

    //Checks if user already has a reservation overlapping this time
    public boolean isUserTimeAvailable(int date, int start_time, int end_time) {
        MySQLiteHelper manager = new MySQLiteHelper(DateTimeActivity.this);
        List<Reservation> currentReservations = manager.getUserReservations(att_uid);
        if (currentReservations == null || currentReservations.size() == 0) {
            return true;
        } else {
            for (Reservation r : currentReservations) {
                if (r.getDate() == date) {
                    if (r.getStartTime() >= start_time && r.getStartTime() < end_time) {
                        return false;
                    } else if (r.getEndTime() > start_time && r.getEndTime() <= end_time) {
                        return false;
                    } else if (r.getStartTime() <= start_time && r.getEndTime() >= end_time) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    //rounds down minute to match 15 minute interval
    public int roundDown(int min) {
        if (min < 15) {
            return 0;
        } else if (min < 30) {
            return 15;
        } else if (min < 45) {
            return 30;
        } else {
            return 45;
        }
    }

    //If possible reserves a seat at given time, prioritizes favorite seats,
    //but picks the first open seat if none of the favorites are available
    public void quickReserve(int date, int start_time, int end_time) {

        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(DateTimeActivity.this);
        String seat1 = p.getString("seat1", "");
        String seat2 = p.getString("seat2", "");
        String seat3 = p.getString("seat3", "");
        boolean seat1F = false;
        boolean seat2F = false;
        boolean seat3F = false;
        MySQLiteHelper manager = new MySQLiteHelper(context);

        List<Workspace> openWorkspaces = manager.getOpenWorkspaces(date, start_time, end_time);
        if (openWorkspaces.size() == 0) {
            Toast.makeText(DateTimeActivity.this, "No seats are available for this time", Toast.LENGTH_SHORT).show();
            return;
        }
        for (Workspace w : openWorkspaces) {
            if (w.getName().equals(seat1)) {
                seat1F = true;
            }
            if (w.getName().equals(seat2)) {
                seat2F = true;
            }
            if (w.getName().equals(seat3)) {
                seat3F = true;
            }
        }
        String seatName = "";
        if (seat1F) {
            seatName = seat1;
        } else if (seat2F) {
            seatName = seat2;
        } else if (seat3F) {
            seatName = seat3;
        } else {
            seatName = openWorkspaces.get(0).getName();
        }
        ReservationController controller = new ReservationController(context);

        int result = controller.createReservation(seatName, att_uid, start_time, end_time, date);
        if (result == 0 || result == 1) {
            Toast.makeText(DateTimeActivity.this, "Unknown error", Toast.LENGTH_SHORT).show();
        }
        if (result == 2) {
            AlertDialog.Builder confirmationDialog = controller.getDialog(seatName, start_time, end_time, date);
            confirmationDialog.setTitle("Reservation confirmed");
            confirmationDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(DateTimeActivity.this, MyReservationActivity.class);
                    intent.putExtra("att_uid", att_uid);
                    startActivity(intent);
                }
            });
            confirmationDialog.show();
        }
    }



//EditReservationActivity


    @Override
    public void onAnimationStart(Animation animation) {

        Log.e("test", "animation should start");
        if(mCalendarView.getVisibility() == View.VISIBLE)
            mCalendarView.setVisibility(View.GONE);
        else
            mCalendarView.setVisibility(View.VISIBLE);

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        //mChoiceLayout.setVisibility(View.GONE);
        Log.e("test", "should be done showing ");
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

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
                updateEndTime();
            }
            else if(getTimeButtonFlag() == 2)
            {
                mSelectedEndTime.setText(time);
                hour_end = hourOfDay;
                min_end = minute;
                updateStartTime();
            }
        }
    }

    @Override
    public void onBackPressed() {

        ActivityManager mngr = (ActivityManager) getSystemService( ACTIVITY_SERVICE );

        List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);

        Log.e("test", "" + taskList.get(0).numActivities);
        Log.e("test", "" + taskList.get(0).baseActivity);
        Log.e("test", "" + taskList.get(0).topActivity);


        if(taskList.get(0).numActivities == 2 &&
                taskList.get(0).topActivity.getClassName().equals(this.getClass().getName())) {
            Log.e("test", "2 activity in the stack");
        }

        if(mCalendarView.getVisibility() == View.VISIBLE)
        {
            mCalendarView.setVisibility(View.GONE);
            mChoiceLayout.setVisibility(View.VISIBLE);
        }
        else if(taskList.get(0).numActivities == 2)
        {
            AlertDialog.Builder logoutDialog = new AlertDialog.Builder(DateTimeActivity.this);
            logoutDialog.setTitle("Do you want to log out?");
            logoutDialog.setCancelable(false);
            logoutDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    Intent i = new Intent(context, LoginActivity.class);
                    i.putExtra("att_uid", att_uid);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
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
        else
            super.onBackPressed();

    }

    public static int getTimePickerFlag() {
        return timePickerFlag;
    }

}

