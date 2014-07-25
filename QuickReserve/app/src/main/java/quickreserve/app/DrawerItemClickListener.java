package quickreserve.app;

/*
*   Class for the Drawer item click listener.
*
*  Signature move for awesome code
*
                                                        /0000\
                                                       /000000)
                                                      (000000/
                                                      |00000/
                                         _______      |00000|
                                       /000  o000000/ /000000\
                                      000000  0000 //00000000\
                                      \0000000  000||000000000|
                                /000000 \000000  00||000000000|
                               0000000o  0oooooo 00/0000000000|
                           /0000 \0000000o 0oooo) //0000000000|
               --ooo_____ |00000  \0000000) //00000--000000000|
              (0000000000\ \\\00000  0ooo/ //00000000000000000/
              \00000000000 \\\00ooo) /|||||/00000000000000000/
                    --ooooo_ \\---00000000000000000000000000/
                               \\00000000000000000000000000/
                                 \\0000000000000000000000/
                                     -ooooooooooooooooo-
*/


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

/**
 * Created by DM0497 on 7/16/2014.
 */
public class DrawerItemClickListener extends Activity implements android.widget.AdapterView.OnItemClickListener {

    Context context;
    private String[] mOptionsList;
    private String att_uid;
    private DrawerLayout mDrawerLayout;
    private String activityName;
    Activity activity;


    public DrawerItemClickListener(String att_uid, String activityName, Context context, Activity activity
            , DrawerLayout mDrawerLayout, String[] mOptionsList)
    {
        this.att_uid = att_uid;
        this.activityName = activityName;
        this.context = context;
        this.mDrawerLayout = mDrawerLayout;
        this.mOptionsList = mOptionsList;
        this.activity = activity;

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        //MapActivity.getToast().makeText(getApplicationContext(), mOptionsList[position].toString()
               // , Toast.LENGTH_SHORT).show();
        final int pos = position;
        mDrawerLayout.closeDrawers();

        /*
        * Great code for smooth transitions when drawer opens and closes
        * */
        mDrawerLayout.postDelayed(new Runnable() {
            @Override

            public void run() {
                String option = mOptionsList[pos].toString();

                if(option.equals(activityName))
                {
                    mDrawerLayout.closeDrawers();
                }
                else if(option.equals("Add Reservation"))
                {
                    Log.e("test", "testing uid - " + att_uid);
                    Intent i = new Intent(activity, DateTimeActivity.class);
                    i.putExtra("att_uid", att_uid);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //activity.finish();
                    activity.startActivity(i);

                }
                else if(option.equals("My Reservations"))
                {
                    Intent i = new Intent(activity, MyReservationActivity.class);
                    i.putExtra("att_uid", att_uid);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    activity.startActivity(i);
                    //activity.finish();

                }
                else if(option.equals("View Map"))
                {
                    Intent i = new Intent(context, ViewMapActivity.class);
                    i.putExtra("att_uid", att_uid);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    activity.startActivity(i);
                    //activity.finish();

                }
                else if(option.equals("Scan QR Code"))
                {
                    Intent i = new Intent(activity, QRScannerActivity.class);
                    i.putExtra("att_uid", att_uid);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    activity.startActivity(i);
                    //activity.finish();
                }
                else if(option.equals("Logout"))
                {

                    AlertDialog.Builder logoutDialog = new AlertDialog.Builder(activity);
                    logoutDialog.setTitle("Do you want to log out?");
                    logoutDialog.setCancelable(false);
                    logoutDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {


                        Intent i = new Intent(activity, LoginActivity.class);
                        i.putExtra("att_uid", att_uid);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(i);
                        activity.finish();

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
                else if(option.equals("Favorite Seats"))
                {
                    Intent i = new Intent(context, FavoriteSeatActivity.class);
                    i.putExtra("att_uid", att_uid);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    activity.startActivity(i);
                    //activity.finish();

                }
                else if(option.equals("Find a Friend")){
                    Intent i = new Intent(context, CreepActivity.class);
                    i.putExtra("att_uid", att_uid);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    activity.startActivity(i);
                }
                else if(option.equals("About"))
                {
                    Intent i = new Intent(context, AboutActivity.class);
                    i.putExtra("att_uid", att_uid);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    activity.startActivity(i);

                }
                else if(option.equals("Help"))
                {
                    Intent i = new Intent(context, HelpActivity.class);
                    i.putExtra("att_uid", att_uid);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    activity.startActivity(i);
                }

            }
        }, 300);

    }
}