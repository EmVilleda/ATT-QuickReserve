package quickreserve.app;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class MapActivity extends FragmentActivity
{
    private String[] mOptionsList;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private static Toast toast;
    private String att_uid;
    private int date_selected;
    private ActionBarDrawerToggle mDrawerToggle;
    private String tempTitle;


    public static Toast getToast() {
        return toast;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        setContentView(R.layout.activity_map);
        tempTitle = getString(R.string.select_a_section);
        getActionBar().setTitle(tempTitle);
        att_uid = getIntent().getStringExtra("att_uid");
        date_selected = getIntent().getIntExtra("date_selected", -1);
        //Toast.makeText(this, att_uid + " " + date_selected, Toast.LENGTH_SHORT).show();

        mOptionsList = getResources().getStringArray(R.array.options_list);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mOptionsList));

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        InitFragment firstFragment = new InitFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container, firstFragment, "init_fragment").commit();

        //toast = Toast.makeText(getApplicationContext(), "Please choose a section", Toast.LENGTH_SHORT);
        //toast.setGravity(Gravity.CENTER, 0,0);

        //toast.show();
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
                getActionBar().setTitle(tempTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                tempTitle = getActionBar().getTitle().toString();
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


    private class DrawerItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            toast = new Toast(getApplicationContext());
            toast.makeText(getApplicationContext(), mOptionsList[position].toString()
                    , Toast.LENGTH_SHORT).show();
            mDrawerLayout.closeDrawers();
            mDrawerLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(MapActivity.this, MyReservationActivity.class);
                    i.putExtra("att_uid", att_uid);
                    startActivity(i);
                }
            }, 300);

        }
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




    @Override
    protected void onPause() {
        super.onPause();
        if(toast != null)
        {
            toast.cancel();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(toast != null)
        {
            toast.cancel();
        }
    }
}
