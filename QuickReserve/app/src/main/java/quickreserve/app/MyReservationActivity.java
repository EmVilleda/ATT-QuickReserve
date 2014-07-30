package quickreserve.app;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyReservationActivity extends ActionBarActivity {

    List<Reservation> reservationList;
    MySQLiteHelper reservationManager = new MySQLiteHelper(this);
    //att_uid will be passed in the intent to this activity and should replace this one
    private String att_uid;
    MyEditReservationAdapter adapter;
    private TextView noReservations;
    private ListView reservationListView;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] mOptionsList;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private static final String ACTIVITY_DRAWER_REF = "My Reservations";
    private Button mAddReservationButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        setContentView(R.layout.activity_my_reservation);

        reservationListView = (ListView) findViewById(R.id.myReservationListView);
        noReservations = (TextView) findViewById(R.id.noReservations);
        att_uid = getIntent().getStringExtra("att_uid");
        Log.e("test", att_uid);

        updateList();
        Log.e("test", "onCreate");

        mOptionsList = getResources().getStringArray(R.array.options_list);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        ArrayList<String> drawerOptions = new ArrayList<String>(Arrays.asList(mOptionsList));


        // Set the adapter for the list view
        mDrawerList.setAdapter(new MyDrawerRowAdapter(this,
                R.layout.my_drawer_row_layout, drawerOptions));

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener(att_uid, ACTIVITY_DRAWER_REF
                ,getBaseContext(),MyReservationActivity.this,mDrawerLayout,mOptionsList));

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
                getActionBar().setTitle(getString(R.string.title_activity_my_reservation));
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

        mAddReservationButton = (Button) findViewById(R.id.addReservationButton);

        mAddReservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), DateTimeActivity.class);
                i.putExtra("att_uid", att_uid);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //activity.finish();
                startActivity(i);
            }
        });

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
    protected void onResume(){
        super.onResume();
        updateList();
        Log.e("test", "onResume");

    }

    @Override
    protected void onPause() {
        super.onPause();
        updateList();
        Log.e("test", "onPause");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateList();
        Log.e("test", "onDestroy");

    }

    /*
         *Gets the most current reservations and assigns them to the array adapter
         */
    public void updateList(){
        noReservations.setVisibility(View.GONE);
        reservationListView.setVisibility(View.VISIBLE);
        Log.e("test", att_uid);

        reservationList = reservationManager.getUserReservations(att_uid);
        ListView reservationView = (ListView) findViewById(R.id.myReservationListView);
        if(reservationList!= null){
            adapter = new MyEditReservationAdapter(this, R.layout.my_edit_reservation_row_layout, reservationList);
            Log.e("test" , reservationList.toString());
            reservationView.setAdapter(adapter);
            reservationView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    /*int ID = reservationList.get(position).getID();
                    Intent intent = new Intent(MyReservationActivity.this, ViewReservationActivity.class);
                    intent.putExtra("ID", ID);
                    intent.putExtra("att_uid", att_uid);
                    startActivity(intent);*/
                }
            });
        }
        else {
            noReservations.setVisibility(View.VISIBLE);
            reservationListView.setVisibility(View.GONE);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_reservation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {

        ActivityManager mngr = (ActivityManager) getSystemService( ACTIVITY_SERVICE );

        List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);

        Log.e("test", "" + taskList.get(0).numActivities);
        Log.e("test", "" + taskList.get(0).baseActivity);
        Log.e("test", "" + taskList.get(0).topActivity);

        if(taskList.get(0).numActivities == 2)
        {
            AlertDialog.Builder logoutDialog = new AlertDialog.Builder(MyReservationActivity.this);
            logoutDialog.setTitle("Do you want to log out?");
            logoutDialog.setCancelable(false);
            logoutDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    i.putExtra("att_uid", att_uid);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();

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

}
