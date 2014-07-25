package quickreserve.app;

/*
*   This class renders the Find a Friend page. This page allows you to find reservations that a
*   colleague/friend has made for the current day
* */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class CreepActivity extends Activity {

    TextView foundReservation;
    EditText attUIDSelected;
    List<Reservation> reservationList;
    Button searchButton;
    ListView reservationListView;
    MySQLiteHelper reservationManager;
    String att_uid;
    int date;
    private String[] mOptionsList;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private static final String ACTIVITY_DRAWER_REF = "Find a Friend";
    private MySQLiteHelper mySQLiteHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creep);

        //activity transition animation
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);

        att_uid = getIntent().getStringExtra("att_uid");
        foundReservation = (TextView) findViewById(R.id.creepTextView);
        reservationListView = (ListView) findViewById(R.id.creepReservationList);
        attUIDSelected = (EditText) findViewById(R.id.creepIDEditText);
        attUIDSelected.setImeOptions(EditorInfo.IME_ACTION_DONE);
        reservationList = new LinkedList<Reservation>();
        foundReservation.setText("");
        reservationManager = new MySQLiteHelper(this);
        mySQLiteHelper = new MySQLiteHelper(this);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        date = calendar.get(Calendar.DAY_OF_MONTH) + ((calendar.get(Calendar.MONTH) + 1) * 100) + (calendar.get(Calendar.YEAR) * 10000);

        searchButton = (Button) findViewById(R.id.creepSearchButton);

        /*
        * onClickListener for the search button. It displays the list of reservations made
        * by the employee whose id is typed in the search box
        * */
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(attUIDSelected.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplication(),"Please enter valid ATTUID",Toast.LENGTH_SHORT).show();

                }
                else if(mySQLiteHelper.getUser(attUIDSelected.getText().toString()) == null)
                {
                    AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(CreepActivity.this);

                    confirmationDialog.setTitle("User not found");
                    confirmationDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.cancel();

                        }
                    });
                    confirmationDialog.show();

                }
                else
                    updateList();
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
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ){

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle("Find a Friend");
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

    public void updateList(){
        final String ID = attUIDSelected.getText().toString();
        reservationList = reservationManager.getUserReservations(ID);
        ArrayList<Reservation> reservationListByToday = new ArrayList<Reservation>();

        Log.e("size test" , "size = " + reservationList.size());
        if (reservationList != null || reservationList.size() > 0)
        {
            for (Reservation r: reservationList){
                if(r.getDate() == date){
                    reservationListByToday.add(r);
                }
            }
            Log.e("size test" , "size by today = " + reservationListByToday.size());

            if(reservationListByToday.size()==0){
                foundReservation.setText("No reservations for today");
                reservationList = new LinkedList<Reservation>();
            }
            else{
                foundReservation.setText("Reservations for Today");
            }
            MyReservationAdapter adapter = new MyReservationAdapter(this, R.layout.my_reservation_row_layout, reservationListByToday);
            Log.e("test", reservationList.toString());
            reservationListView.setAdapter(adapter);
        }
        else
        {
            foundReservation.setText("No reservations for today");
            reservationList = new LinkedList<Reservation>();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.creep, menu);
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
}
