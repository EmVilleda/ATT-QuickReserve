package quickreserve.app;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class DateTimeActivity extends Activity {

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
    private int tempID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time);

        tempID = getIntent().getIntExtra("tempID", -1);
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
        mSelectedDate.setText((calendar.get(Calendar.MONTH) + 1)+ "/" + calendar.get(Calendar.DAY_OF_MONTH));
        timeButtonflag = -1;

        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCalendarView.setVisibility(View.VISIBLE);
                mChoiceLayout.setVisibility(View.GONE);
            }
        });

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int dayOfMonth) {

                tempDate = (month + 1)  + "/" + dayOfMonth;

                Log.e("test", "onDateChanged");
                Log.e("test","button: " + mSelectedDate.getText());
                Log.e("test","temp: " + tempDate);

                if( (!tempDate.trim().equals(mSelectedDate.getText().toString().trim())
                                    && !mSelectedDate.getText().toString().equals("")))

                {
                    mCalendarView.setVisibility(View.GONE);
                    mChoiceLayout.setVisibility(View.VISIBLE);
                }
                mSelectedDate.setText(tempDate);
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
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                intent.putExtra("hour_start", hour_start);
                intent.putExtra("min_start", min_start);
                intent.putExtra("hour_end", hour_end);
                intent.putExtra("min_end", min_end);
                intent.putExtra("tempID", tempID);
                startActivity(intent);

            }
        });
    }

    public static int getTimeButtonflag() {
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
        if (id == R.id.action_settings) {
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
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);



            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            StringBuilder sb = new StringBuilder();
            if(hourOfDay%12 < 10)
            {
                if(hourOfDay == 12)
                {
                    sb.append(hourOfDay).append(":");
                }
                else
                    sb.append("0").append(hourOfDay%12).append(":");

            }
            else
                sb.append(hourOfDay%12).append(":");

            if(minute < 10)
            {
                sb.append("0").append(minute + " ");
            }
            else
                sb.append(minute + " ");

            if (hourOfDay%24 >= 12 )
            {
                sb.append("PM");

            }
            else
                sb.append("AM");


            if(getTimeButtonflag() == 1)
            {
                mSelectedStartTime.setText(sb.toString());
                hour_start = hourOfDay;
                min_start = minute;
            }
            else if(getTimeButtonflag() == 2)
            {
                mSelectedEndTime.setText(sb.toString());
                hour_end = hourOfDay;
                min_end = minute;
            }
        }
    }
}

