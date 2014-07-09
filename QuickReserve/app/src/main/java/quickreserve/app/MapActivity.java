package quickreserve.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
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

    public static Toast getToast() {
        return toast;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        setContentView(R.layout.activity_map);
        getActionBar().setTitle(getString(R.string.select_a_section));
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
        getSupportFragmentManager().beginTransaction().add(R.id.container, firstFragment).commit();

        //toast = Toast.makeText(getApplicationContext(), "Please choose a section", Toast.LENGTH_SHORT);
        //toast.setGravity(Gravity.CENTER, 0,0);

        //toast.show();

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
