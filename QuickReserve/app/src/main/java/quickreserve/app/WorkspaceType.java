package quickreserve.app;

/* WorkspaceType.java
 * Created by Charlie Albright (ATT_UID: ca8681)
 * 06/09/2014
 * App: QuickReserve
*/

public class WorkspaceType
{
    //private attributes, match columns in Database Table
    private int id;
    private String name;
    private int max_reservation_length;
    private int capacity;

    //basic Constructor
    public WorkspaceType() {}

    //Constructor
    public WorkspaceType(String name, int max_reservation_length, int capacity)
    {
        super();
        this.name = name;
        this.max_reservation_length = max_reservation_length;
        this.capacity = capacity;
    }

    //toString() override class - prints user data in view-friendly format
    @Override
    public String toString()
    {
        return "WorkspaceType:\n\tID: " + id + "\n\tName: " + name +
                "\n\tMax Reservation: " + max_reservation_length +
                " Hours\n\tCapacity: " + capacity + "\n\n";
    }

    //GETTERS AND SETTERS

    //ID
    public int getID() {return id;}
    public void setID(int id) {this.id = id;}

    //Name
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    //Max Reservation Length
    public int getMaxReservationLength() {return max_reservation_length;}
    public void setMaxReservationLength(int max_reservation_length) {this.max_reservation_length = max_reservation_length;}

    //Capacity
    public int getCapacity() {return capacity;}
    public void setCapacity(int capacity) {this.capacity = capacity;}

}
