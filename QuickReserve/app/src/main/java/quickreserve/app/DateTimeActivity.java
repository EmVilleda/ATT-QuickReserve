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
    private static int day_selected;
    private static int month_selected;
    private static int year_selected;
    private String att_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time);

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
        min_start = calendar.get(Calendar.MINUTE);
        hour_end = calendar.get(Calendar.HOUR_OF_DAY) + 1;
        min_end = calendar.get(Calendar.MINUTE);
        mSelectedStartTime.setText(TimeParser.parseCalendarTime(hour_start, min_start));
        mSelectedEndTime.setText(TimeParser.parseCalendarTime(hour_end, min_end));

        mSelectedDate.setText(TimeParser.parseDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)));
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

                int date = (year * 10000) + ((month+1)*100) + dayOfMonth;
                tempDate = TimeParser.parseDateFormat(date);
                Toast.makeText(DateTimeActivity.this, "" + tempDate, Toast.LENGTH_SHORT).show();

                Log.e("test", "onDateChanged");
                Log.e("test","button: " + mSelectedDate.getText());
                Log.e("test","temp: " + tempDate);

                if( (!tempDate.trim().equals(mSelectedDate.getText().toString().trim())
                                    && !mSelectedDate.getText().toString().equals("")))

                {
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
            return new TimePickerDialog(getActivity(), this, hour, minute,
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
}

