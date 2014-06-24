package quickreserve.app;

/* Reservation.java
 * Created by Charlie Albright (ATT_UID: ca8681)
 * 06/09/2014
 * App: QuickReserve
*/

public class Reservation
{
    //private attributes, match columns in Database Table
    private int id;
    private String workspace_name;
    private String att_uid;
    private int start_time;
    private int end_time;
    private int date;

    //basic Constructor
    public Reservation() {}

    //Constructor
    public Reservation(String workspace_name, String att_uid, int start_time, int end_time, int date)
    {
        super();
        this.workspace_name = workspace_name;
        this.att_uid = att_uid;
        this.start_time = start_time;
        this.end_time = end_time;
        this.date = date;
    }

    //toString() override class - prints user data in view-friendly format
    @Override
    public String toString()
    {
        return "Reservation:\n\tID: " + id + "\n\tWorkspace ID: " + workspace_name
                + "\n\tATT_UID: " + att_uid + "\n\tStart Time: " + start_time
                + "\n\tEnd Time: " + end_time + "\n\tDate : " + date + "\n\n";
    }

    //GETTERS AND SETTERS

    //ID
    public int getID() {return id;}
    public void setID(int id) {this.id = id;}

    //Workspace ID
    public String getWorkspaceID() {return workspace_name;}
    public void setWorkspaceID(String workspace_name) {this.workspace_name = workspace_name;}

    //ATT_UID
    public String getAttUid() {return att_uid;}
    public void setAttUid(String att_uid) {this.att_uid = att_uid;}

    //Start Time
    public int getStartTime() {return start_time;}
    public void setStartTime(int start_time) {this.start_time = start_time;}

    //End Time
    public int getEndTime() {return end_time;}
    public void setEndTime(int end_time) {this.end_time = end_time;}

    //Date
    public int getDate() {return date;}
    public void setDate(int date) {this.date = date;}
    public void setDate(int year, int month, int day)
    {
        int newDate = (year*10000) + ((month+1)*100) + day;
        this.date = newDate;
    }
}