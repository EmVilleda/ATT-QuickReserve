package quickreserve.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class QRResultActivity extends Activity {

    private int[] weekdays;
    private int[] days;
    private int[] months;
    private int[] years;
    private int index;

    private String seat;
    private String codeFormat;
    private String att_uid;

    private TextView seatTextView;
    private TextView dateTextView;
    private TextView isBookedTextView;
    private Button previousDateButton;
    private Button nextDateButton;
    private Button reserveButton;

    private MySQLiteHelper mySQLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrresult);

        mySQLiteHelper = new MySQLiteHelper(this);

        weekdays = new int[14];
        days = new int[14];
        months = new int[14];
        years = new int[14];
        index = 0;

        seatTextView = (TextView)findViewById(R.id.seatNameTextView);
        dateTextView = (TextView)findViewById(R.id.dateTextView);
        isBookedTextView = (TextView)findViewById(R.id.isBookedTextView);
        previousDateButton = (Button)findViewById(R.id.previousDateButton);
        nextDateButton = (Button)findViewById(R.id.nextDateButton);
        reserveButton = (Button)findViewById(R.id.reserveFromQRButton);

        previousDateButton.setClickable(false);
        previousDateButton.setBackgroundColor(getResources().getColor(R.color.gray));

        Intent intent = getIntent();
        seat = intent.getStringExtra("value");
        codeFormat = intent.getStringExtra("format");
        att_uid = intent.getStringExtra("att_uid");

        boolean validSeat = checkValidSeat();
        if(validSeat) {
            seatTextView.setText(seat);
        } else {
            Toast.makeText(this, "Seat code not recognized.", Toast.LENGTH_SHORT).show();
            finish();
        }

        setDates();
        checkSeatStatus();
        populateDateView();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.qrresult, menu);
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

    private void setDates()
    {
        Calendar c = Calendar.getInstance();

        for(int i = 0; i < 14; i++)
        {
            weekdays[i] = c.get(Calendar.DAY_OF_WEEK);
            days[i] = c.get(Calendar.DAY_OF_MONTH);
            months[i] = c.get(Calendar.MONTH)+1;
            years[i] = c.get(Calendar.YEAR);
            c.add(Calendar.DAY_OF_YEAR, 1);
        }

    }

    private void populateDateView()
    {
        String stringWeekday = "";
        switch (weekdays[index])
        {
            case 1:
                stringWeekday = "Sunday";
                break;
            case 2:
                stringWeekday = "Monday";
                break;
            case 3:
                stringWeekday = "Tuesday";
                break;
            case 4:
                stringWeekday = "Wednesday";
                break;
            case 5:
                stringWeekday = "Thursday";
                break;
            case 6:
                stringWeekday = "Friday";
                break;
            case 7:
                stringWeekday = "Saturday";
                break;
            default:
                break;
        }

        dateTextView.setText(stringWeekday + ", " + months[index] + "/" + days[index]);
    }

    private boolean checkValidSeat()
    {
        Workspace workspace = mySQLiteHelper.getWorkspace(seat);
        if(workspace == null)
            return false;
        else
            return true;
    }

    private void checkSeatStatus()
    {
        boolean booked = mySQLiteHelper.isWorkspaceReserved(seat, days[index], months[index], years[index]);
        if(booked)
        {
            isBookedTextView.setText("BOOKED");
            isBookedTextView.setTextColor(getResources().getColor(R.color.orange));
            reserveButton.setClickable(false);

        }
        else
        {
            isBookedTextView.setText("AVAILABLE");
            isBookedTextView.setTextColor(getResources().getColor(R.color.blue));
            reserveButton.setClickable(true);
        }
    }

    public void onNextDateButtonClicked(View view)
    {
        previousDateButton.setClickable(true);
        previousDateButton.setBackgroundColor(getResources().getColor(R.color.blue));
        index++;
        if(index == 13) {
            nextDateButton.setClickable(false);
            nextDateButton.setBackgroundColor(getResources().getColor(R.color.gray));
        }

        checkSeatStatus();
        populateDateView();
    }

    public void onPreviousDateButtonClicked(View view)
    {
        nextDateButton.setClickable(true);
        nextDateButton.setBackgroundColor(getResources().getColor(R.color.blue));
        index--;
        if(index == 0) {
            previousDateButton.setClickable(false);
            previousDateButton.setBackgroundColor(getResources().getColor(R.color.gray));
        }

        checkSeatStatus();
        populateDateView();
    }

    public void onScanNewSeatButtonClicked(View view)
    {
        Intent intent = new Intent(this, QRScannerActivity.class);
        intent.putExtra("att_uid", att_uid);
        startActivity(intent);
        finish();
    }

    public void onReserveClicked(View view)
    {
        Reservation reservation = new Reservation();
        reservation.setWorkspaceID(seat);
        reservation.setAttUid(att_uid);
        reservation.setDate(years[index], months[index]-1, days[index]);
        reservation.setStartTime(800);
        reservation.setEndTime(1700);
        mySQLiteHelper.addReservation(reservation);

        Intent intent = new Intent(this, MyReservationActivity.class);
        intent.putExtra("att_uid", att_uid);
        startActivity(intent);
        finish();
    }
}
