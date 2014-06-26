package quickreserve.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class ViewReservationActivity extends ActionBarActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_reservation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume(){
        super.onResume();
        updateInfo();
    }

    protected void updateInfo(){
        Intent intent = getIntent();
        final int ID = intent.getIntExtra("ID", -1);
        if (ID == -1) {

            finish();
        }

        //finds reservation
        final MySQLiteHelper reservationManager = new MySQLiteHelper(this);
        Reservation reservation = reservationManager.getReservation(ID);
        if(reservation== null){
            Toast.makeText(this, "Reservation null.", Toast.LENGTH_SHORT).show();
        }

        Workspace workspace = reservationManager.getWorkspace(reservation.getWorkspaceID());
        if (workspace == null){
            Toast.makeText(this, "Workspace null.", Toast.LENGTH_SHORT).show();
        }



        TextView seatView = (TextView) findViewById(R.id.viewReservationSeat);
        TextView dateView = (TextView) findViewById(R.id.viewReservationDate);
        TextView timeView = (TextView) findViewById(R.id.viewReservationTime);
        TextView phoneView = (TextView) findViewById(R.id.viewReservationPhone);
        TextView printerView = (TextView) findViewById(R.id.viewReservationPrinter);

        seatView.setText("Seat Number: " + workspace.getName() + " " + workspace.getSector());
        dateView.setText("Reservation Date: " + TimeParser.parseDate(reservation.getDate()));
        timeView.setText("Reservation Time: " + TimeParser.parseTime(reservation.getStartTime(), reservation.getEndTime()));
        phoneView.setText("Phone Number: " + workspace.getPhoneNumber());
        printerView.setText("Printer View: " + workspace.getPrinterNumber());
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reservation);

        Intent intent = getIntent();
        final int ID = intent.getIntExtra("ID", -1);
        if (ID == -1) {

            finish();
        }

        //finds reservation
        final MySQLiteHelper reservationManager = new MySQLiteHelper(this);
        updateInfo();

        final Button editButton = (Button) findViewById(R.id.viewReservationEditButton);
        final Button deleteButton = (Button) findViewById(R.id.viewReservationDeleteButton);
        final Context context = this;

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder deleteDialog = new AlertDialog.Builder(context);
                deleteDialog.setTitle("Delete this reservation?");
                deleteDialog.setCancelable(true);
                deleteDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(reservationManager.deleteReservation(ID)) {
                            Toast.makeText(context, "Reservation successfully deleted.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else{
                            Toast.makeText(context, "Could not delete reservation.", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    }

                });
                deleteDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }

                });

                AlertDialog finishedDialog = deleteDialog.create();
                //DialogColor.dialogColor(finishedDialog);
                //finishedDialog.getWindow().requestFeature(Window.FEATURE_CUSTOM_TITLE);
                finishedDialog.show();


            }


        });


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder editDialog = new AlertDialog.Builder(context);
                editDialog.setTitle("Select which to edit.");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, context.getResources().getStringArray(R.array.edit_options_array));
                editDialog.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i==0){
                            Intent newIntent = new Intent(ViewReservationActivity.this, EditReservationTimeActivity.class);
                            newIntent.putExtra("ID", ID);
                            startActivity(newIntent);
                        }
                        else if(i==1){
                            Intent newIntent = new Intent(ViewReservationActivity.this, EditReservationSeatActivity.class);
                            newIntent.putExtra("ID", ID);
                            startActivity(newIntent);
                        }
                        else{
                            Intent newIntent = new Intent(ViewReservationActivity.this, EditReservationActivity.class);
                            newIntent.putExtra("ID", ID);
                            startActivity(newIntent);
                        }
                    }
                });
                editDialog.setCancelable(true);
                editDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }

                });
                AlertDialog finishedDialog = editDialog.create();
                //DialogColor.dialogColor(finishedDialog);
                finishedDialog.show();


            }


        });


    }
}