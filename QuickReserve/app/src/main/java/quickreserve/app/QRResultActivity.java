package quickreserve.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
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
    private ImageButton previousDateButton;
    private ImageButton nextDateButton;
    private Button reserveButton;

    private String[] mOptionsList;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private MySQLiteHelper mySQLiteHelper;
    private static final String ACTIVITY_DRAWER_REF = "";

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
        previousDateButton = (ImageButton)findViewById(R.id.previousDateButton);
        nextDateButton = (ImageButton)findViewById(R.id.nextDateButton);
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
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ){

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle("Reserve Seat");
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
        if (mDrawerToggle.onOptionsItemSelected(item)) {
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
            isBookedTextView.setText("NOT AVAILABLE");
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
        previousDateButton.setBackground(getResources().getDrawable(R.drawable.icon_button));
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
        nextDateButton.setBackground(getResources().getDrawable(R.drawable.icon_button));
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
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
        int result = mySQLiteHelper.addReservation(reservation);

        if(result == 1){

            AlertDialog.Builder conflictDialog = new AlertDialog.Builder(QRResultActivity.this);
            conflictDialog.setTitle("Reservation conflict");
            conflictDialog.setMessage("You already have another reservation for the selected day");
            conflictDialog.setPositiveButton("Change day", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    //finish();
                }
            });
            conflictDialog.show();

        }
        else if (result == 2){
            ReservationController controller = new ReservationController(this);

            AlertDialog.Builder confirmationDialog = controller.getDialog(reservation.getWorkspaceID(), reservation.getStartTime(), reservation.getEndTime(), reservation.getDate());
            confirmationDialog.setTitle("Reservation confirmed");
            confirmationDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(getApplication(), MyReservationActivity.class);
                    intent.putExtra("att_uid", att_uid);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });
            confirmationDialog.show();


        }

    }
}
