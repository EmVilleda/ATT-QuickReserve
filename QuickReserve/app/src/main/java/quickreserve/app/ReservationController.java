/*
 * ReservationController.java
 * 
 */

package quickreserve.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

public class ReservationController{

    private MySQLiteHelper reservationManager;
    private Context context;

    //Links the SQLHelper to the controller
    public ReservationController(Context context){
        reservationManager =  new MySQLiteHelper(context);
        this.context = context;
    }



    /*
     *Sends data for manager to create a Reservation
     *the start_time and end_time sent should be arbitrary and ignored in the manager class for first iteration
     *later iterations the manager should be checking time availability before allowing creation, we will only focus on date for now
     *expected int responses:(GUI should respond differently to each)
     *0 - unknown error (most likely with creating reservation in database)
     *1 - time is unavailable
     *2 - Reservation created successfully
     */
    public int createReservation(String workspace_name, String att_uid, int start_time, int end_time, int date){
 			
            /*
 			boolean isAvailable = isReservationAvailable(workspace_name, start_time, end_time, date);
 				if (isAvailable == false){
 					return 1;
 				}
            */
        Reservation reservation = new Reservation(workspace_name, att_uid, start_time, end_time, date);

        int hasBeenCreated = reservationManager.addReservation(reservation);

        return hasBeenCreated;
    }

    //opens a coonfirmation dialog when reservation is made
    public AlertDialog.Builder getDialog(String workspace_name, int start_time, int end_time, int date){
        final AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = layoutInflater.inflate(R.layout.my_reservation_dialog_layout, null);
        //finds workspace information, sets it to view
        Workspace workspace = reservationManager.getWorkspace(workspace_name);

        TextView seatView = (TextView) dialogView.findViewById(R.id.viewReservationSeat);
        TextView dateView = (TextView) dialogView.findViewById(R.id.viewReservationDate);
        TextView timeView = (TextView) dialogView.findViewById(R.id.viewReservationTime);
        TextView phoneView = (TextView) dialogView.findViewById(R.id.viewReservationPhone);
        TextView printerView = (TextView) dialogView.findViewById(R.id.viewReservationPrinter);

        seatView.setText("Seat " + workspace.getName());
        Log.e("date", "date = " + date);
        dateView.setText(TimeParser.getDay(date) + ", " + TimeParser.parseDate(date));
        timeView.setText(TimeParser.parseTime(start_time, end_time));
        phoneView.setText(workspace.getPhoneNumber());
        printerView.setText(workspace.getPrinterNumber());
        confirmationDialog.setView(dialogView);
        return confirmationDialog;
    }
    public AlertDialog.Builder getDialog(Reservation r){
        return getDialog(r.getWorkspaceID(), r.getStartTime(), r.getEndTime(), r.getDate());
    }

 		/*
 		 *Sends id of the reservation to be deleted
 		 *Expected responses:
 		 *false - unknown error
 		 *true - Reservation deleted successfully
 		 */


    public boolean deleteReservation(int id){
        boolean hasBeenDeleted = reservationManager.deleteReservation(id);
        return hasBeenDeleted;
    }


    /*
     *Edits a reservation
     *false - unknown error adding (most likely with creating reservation in database)
     *true -  Reservation created successfully
     */
    public boolean editReservation(int id, String workspace_name,String seat, int date, int start_time, int end_time){
        return reservationManager.editReservation(id, workspace_name,seat, date, start_time, end_time);
    }



    //returns true or false depending on if the workspace is available at the passed in time
    public boolean isReservationAvailable(String workspace_name, int start_time, int end_time, int date){
        return true;
    }
}