package quickreserve.app;

/*
* Lets you select favourite seat from activity
* */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import quickreserve.app.R;

public class FavoriteSeatSelectActivity extends Activity {
    //To be removed from selected seats
    protected String tbr1;
    protected String tbr2;
    protected String selectedSeat;
    protected int selectedSeatFlag;
    protected List<Workspace> workspaces;
    protected ArrayList<String> workspaceNames;
    protected ListView seatList;
    protected Button submitButton;
    protected int currentSector;
    protected int selectedFavorite;
    protected TouchImageView sectorImage;
    private static final String ACTIVITY_DRAWER_REF = "";
    private String[] mOptionsList;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String att_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_seat_select);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);

        final Context context = this;
        Intent intent = getIntent();
        att_uid = getIntent().getStringExtra("att_uid");
        selectedFavorite = intent.getIntExtra("selectedFavorite", -1);
        //Sets removable seats depending on which seat we're changing
        selectedSeat = intent.getStringExtra("selectedSeat");
        getActionBar().setTitle(getString(R.string.title_activity_favorite_seat_select));

        if(selectedFavorite == 1){
            tbr1 = p.getString("seat2", "");
            tbr2 = p.getString("seat3", "");
        }
        else if(selectedFavorite == 2){
            tbr1 = p.getString("seat1", "");
            tbr2 = p.getString("seat3", "");
        }
        else if(selectedFavorite == 3){
            tbr1 = p.getString("seat1", "");
            tbr2 = p.getString("seat2", "");
        }
        else{
            finish();
        }
        selectedSeatFlag = 0;

        workspaces = new MySQLiteHelper(this).getAllWorkspaces();
        //Removes the other 2 favorite workspaces from the list if they exist, keeps a flag of the current seat selected
        workspaceNames = new ArrayList<String>();
        workspaceNames.add("None");
        for(int index = 0; index < workspaces.size(); index++){
            String wName = workspaces.get(index).getName();
            if(wName.equals(tbr1) || wName.equals(tbr2)){
                workspaces.remove(index);
                index--;
            }
            else{
                workspaceNames.add(wName);
            }
            if(wName.equals(selectedSeat)){
                selectedSeatFlag=index+1;
            }
        }
        sectorImage = (TouchImageView) findViewById(R.id.favoriteSeatSelectImage);
        if(selectedSeatFlag==0){
            sectorImage.setImageResource(R.drawable.section_a);
            currentSector = 1;
        }
        else{
            int sector = workspaces.get(selectedSeatFlag-1).getSector();
            setImage(sector);
        }

        seatList = (ListView) findViewById(R.id.favoriteSeatSelectList);
        seatList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        seatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(position!=0) {
                    int sector = workspaces.get(position - 1).getSector();
                    seatList.setItemChecked(position, true);
                    setImage(sector);
                }
            }
        });
        ListAdapter seatListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, workspaceNames);
        seatList.setAdapter(seatListAdapter);
        seatList.setItemChecked(selectedSeatFlag, true);
        seatList.setSelection(selectedSeatFlag);

        submitButton = (Button) findViewById(R.id.favoriteSeatSelectSubmitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String seatName = workspaceNames.get(seatList.getCheckedItemPosition());
                if(seatName.equals("None")){
                    seatName = "";
                }
                Intent returnIntent = new Intent();
                returnIntent.putExtra("selectedFavorite", selectedFavorite);
                returnIntent.putExtra("selectedSeat", seatName);
                setResult(RESULT_OK,returnIntent);
                finish();

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
                getActionBar().setTitle(getString(R.string.title_activity_favorite_seat));
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
        getMenuInflater().inflate(R.menu.favorite_seat_select, menu);
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
