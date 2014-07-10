package quickreserve.app;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MyReservationActivity extends ActionBarActivity {

    List<Reservation> reservationList;
    MySQLiteHelper reservationManager = new MySQLiteHelper(this);
    //att_uid will be passed in the intent to this activity and should replace this one
    private String att_uid;
    MyReservationAdapter adapter;
    private TextView noReservations;
    private ListView reservationListView;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] mOptionsList;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;


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

    private class DrawerItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            MapActivity.getToast().makeText(getApplicationContext(), mOptionsList[position].toString()
                    , Toast.LENGTH_SHORT).show();
            mDrawerLayout.closeDrawers();
            mDrawerLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(getApplicationContext(), MyReservationActivity.class);
                    i.putExtra("att_uid", att_uid);
                    startActivity(i);
                }
            }, 300);

        }
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
            adapter = new MyReservationAdapter(this, R.layout.my_reservation_row_layout, reservationList);
            Log.e("test" , reservationList.toString());
            reservationView.setAdapter(adapter);
            reservationView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    int ID = reservationList.get(position).getID();
                    Intent intent = new Intent(MyReservationActivity.this, ViewReservationActivity.class);
                    intent.putExtra("ID", ID);
                    startActivity(intent);
                }
            });
        }
        else {
            noReservations.setVisibility(View.VISIBLE);
            reservationListView.setVisibility(View.GONE);
            Toast.makeText(this, "No Reservations.", Toast.LENGTH_SHORT).show();
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

}
