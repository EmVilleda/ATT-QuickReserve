package quickreserve.app;

/*
*   Class for favorite seat activity. Lets you choose favrite seats
* */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class FavoriteSeatActivity extends Activity {

    protected TextView seat1Text;
    protected TextView seat2Text;
    protected TextView seat3Text;
    protected ImageButton seat1Button;
    protected ImageButton seat2Button;
    protected ImageButton seat3Button;
    protected Button submitButton;
    protected Button seat1OverlayButton;
    protected Button seat2OverlayButton;
    protected Button seat3OverlayButton;
    private static final String ACTIVITY_DRAWER_REF = "Favorite Seats";
    private String[] mOptionsList;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String att_uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_seat);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        final Context context = this;
        getActionBar().setTitle(getString(R.string.title_activity_favorite_seat));
        att_uid = getIntent().getStringExtra("att_uid");
        seat1Text = (TextView) findViewById(R.id.favoriteSeatText1);
        seat2Text = (TextView) findViewById(R.id.favoriteSeatText2);
        seat3Text = (TextView) findViewById(R.id.favoriteSeatText3);


        seat1Button = (ImageButton) findViewById(R.id.favoriteSeatButton1);
        seat1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, FavoriteSeatSelectActivity.class);
                //selected = # of favorite
                i.putExtra("selectedFavorite", 1);
                i.putExtra("att_uid", att_uid);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                String seat = seat1Text.getText().toString();
                if (seat.equals("Select a seat")){
                    seat = "";
                }
                i.putExtra("selectedSeat", seat);
                startActivityForResult(i, 1);
            }
        });

        seat1OverlayButton = (Button)findViewById(R.id.seat1OverlayButton);
        seat1OverlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, FavoriteSeatSelectActivity.class);
                //selected = # of favorite
                i.putExtra("selectedFavorite", 1);
                i.putExtra("att_uid", att_uid);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                String seat = seat1Text.getText().toString();
                if (seat.equals("Select a seat")){
                    seat = "";
                }
                i.putExtra("selectedSeat", seat);
                startActivityForResult(i, 1);
            }
        });

        seat2Button = (ImageButton) findViewById(R.id.favoriteSeatButton2);
        seat2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, FavoriteSeatSelectActivity.class);
                //selected = # of favorite
                i.putExtra("selectedFavorite", 2);
                i.putExtra("att_uid", att_uid);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                String seat = seat2Text.getText().toString();
                if (seat.equals("Select a seat")){
                    seat = "";
                }
                i.putExtra("selectedSeat", seat);
                startActivityForResult(i, 1);
            }
        });

        seat2OverlayButton = (Button)findViewById(R.id.seat2OverlayButton);
        seat2OverlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, FavoriteSeatSelectActivity.class);
                //selected = # of favorite
                i.putExtra("selectedFavorite", 2);
                i.putExtra("att_uid", att_uid);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                String seat = seat2Text.getText().toString();
                if (seat.equals("Select a seat")){
                    seat = "";
                }
                i.putExtra("selectedSeat", seat);
                startActivityForResult(i, 1);
            }
        });

        seat3Button = (ImageButton) findViewById(R.id.favoriteSeatButton3);
        seat3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, FavoriteSeatSelectActivity.class);
                //selected = # of favorite
                i.putExtra("selectedFavorite", 3);
                i.putExtra("att_uid", att_uid);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                String seat = seat3Text.getText().toString();
                if (seat.equals("Select a seat")){
                    seat = "";
                }
                i.putExtra("selectedSeat", seat);
                startActivityForResult(i, 1);
            }
        });

        seat3OverlayButton = (Button)findViewById(R.id.seat3OverlayButton);
        seat3OverlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, FavoriteSeatSelectActivity.class);
                //selected = # of favorite
                i.putExtra("selectedFavorite", 3);
                i.putExtra("att_uid", att_uid);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                String seat = seat3Text.getText().toString();
                if (seat.equals("Select a seat")){
                    seat = "";
                }
                i.putExtra("selectedSeat", seat);
                startActivityForResult(i, 1);
            }
        });

        submitButton = (Button) findViewById(R.id.favoriteSeatSubmitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = p.edit();
                editor.putString("seat1", seat1Text.getText().toString());
                editor.putString("seat2", seat2Text.getText().toString());
                editor.putString("seat3", seat3Text.getText().toString());
                editor.commit();
                AlertDialog.Builder saveDialog = new AlertDialog.Builder(context);
                saveDialog.setTitle("Favorite seats saved.");
                saveDialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        finish();
                    }
                });
                saveDialog.show();
            }
        });

        initialSeats();

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

    //sets seats to initial preffered values
    public void initialSeats(){
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
        updateSeat(seat1Text, p.getString("seat1", ""));
        updateSeat(seat2Text, p.getString("seat2", ""));
        updateSeat(seat3Text, p.getString("seat3", ""));
    }

    public void updateSeat(TextView view, String seat){
        if(seat.equals("")){
            view.setText("Select a seat");
        }
        else{
            view.setText(seat);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                //selected = # of favorite
                int selectedSeat = data.getIntExtra("selectedFavorite", -1);
                String seatName = data.getStringExtra("selectedSeat");
                if(seatName == null || seatName.equals("")){
                    seatName = "Select a seat";
                }

                if(selectedSeat==1){
                    updateSeat(seat1Text, seatName);
                }
                else if(selectedSeat==2){
                    updateSeat(seat2Text, seatName);
                }
                else if(selectedSeat==3){
                    updateSeat(seat3Text, seatName);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.favorite_seat, menu);
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
