package quickreserve.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class EditReservationSeatActivity extends Activity {

    protected int start_time;
    protected int end_time;
    protected int date;
    protected String origin;
    protected int currentSector;
    protected String seatName;
    TouchImageView sectorImage;
    private static final String ACTIVITY_DRAWER_REF = "";
    protected String att_uid;
    ArrayList<String> workspaceNames;
    private String[] mOptionsList;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reservation_seat);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        final Context context = this;

        Intent intent = getIntent();
        att_uid = intent.getStringExtra("att_uid");

        final MySQLiteHelper reservationManager = new MySQLiteHelper(this);

        seatName = intent.getStringExtra("seatName");

        List<Workspace> availableWorkspaces = reservationManager.getAllWorkspaces();
        //should possibly make a workspace list adapter instead
        workspaceNames = new ArrayList<String>();

        for (Workspace w: availableWorkspaces){
            workspaceNames.add(w.getName());
        }

        final ListView seatList = (ListView) findViewById(R.id.editReservationSeatList);
        sectorImage = (TouchImageView) findViewById(R.id.editReservationSeatImage);
        Button submitButton = (Button) findViewById(R.id.editReservationSeatButton);
        currentSector = 1;
        sectorImage.setImageResource(R.drawable.section_a);

        //to use in listener
        final List<Workspace> workspaces = availableWorkspaces;
        ListAdapter seatListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, workspaceNames);
        seatList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        seatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                int sector = workspaces.get(position).getSector();
                seatList.setItemChecked(position, true);
                setImage(sector);
            }
        });
        seatList.setAdapter(seatListAdapter);

        for (int index = 0; index < availableWorkspaces.size(); index++){
            if (availableWorkspaces.get(index).getName().equals(seatName)){
                int sector = availableWorkspaces.get(index).getSector();
                setImage(sector);
                seatList.setItemChecked(index, true);
                seatList.setSelection(index);
            }
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String seatName = workspaceNames.get(seatList.getCheckedItemPosition());
                    Intent intent = new Intent();
                    setResult(Activity.RESULT_OK, intent);
                    intent.putExtra("selectedSeat", seatName);
                    finish();
                }
                catch (Exception E) {
                    Toast.makeText(EditReservationSeatActivity.this, "Please select a seat", Toast.LENGTH_SHORT).show();

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
                EditReservationSeatActivity.this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ){

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle("Change time");
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

    public void setImage(int sector){
        if (sector == 1 && currentSector!=1) {
            sectorImage.setImageResource(R.drawable.section_a);
            currentSector = 1;
            sectorImage.resetZoom();
        } else if (sector == 2 && currentSector!=2) {
            sectorImage.setImageResource(R.drawable.section_b);
            currentSector = 2;
            sectorImage.resetZoom();
        } else if(sector == 3 && currentSector!=3){
            sectorImage.setImageResource(R.drawable.section_c);
            currentSector = 3;
            sectorImage.resetZoom();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_reservation_seat, menu);
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
