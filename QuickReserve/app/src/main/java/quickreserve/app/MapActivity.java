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
import android.widget.TextView;
import android.widget.Toast;


public class MapActivity extends FragmentActivity
{
    private String[] mOptionsList;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private static Toast toast;
    private String ID;

    public static Toast getToast() {
        return toast;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        ID = getIntent().getStringExtra("ID");

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

        toast = Toast.makeText(getApplicationContext(), "Please choose a section", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0,0);

        toast.show();

    }


    private class DrawerItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {


            toast = new Toast(getApplicationContext());
            toast.makeText(getApplicationContext(), mOptionsList[position].toString()
                    , Toast.LENGTH_SHORT).show();
            mDrawerLayout.closeDrawer(mDrawerList);
            Intent i = new Intent(MapActivity.this, MyReservationActivity.class);
            i.putExtra("ID", ID);
            startActivity(i);


        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        toast.cancel();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        toast.cancel();
    }
}
