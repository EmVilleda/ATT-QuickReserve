package quickreserve.app;

/* User.java
 * Created by Charlie Albright (ATT_UID: ca8681)
 * 06/09/2014
 * App: QuickReserve
*/

public class User
{
    //private attributes, match columns in Database Table
    private int id;
    private String att_uid;
    private String first_name;
    private String last_name;

    //basic Constructor
    public User() {}

    //Constructor
    public User(String att_uid, String first_name, String last_name)
    {
        super();
        this.att_uid = att_uid;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    //toString() override class - prints user data in view-friendly format
    @Override
    public String toString()
    {
        return "User:\n\tID: " + id + "\n\tATT_UID: " + att_uid
                + "\n\tFirst Name: " + first_name + "\n\tLast Name: "
                + last_name + "\n\n";
    }

    //GETTERS AND SETTERS

    //ID
    public int getID() {return id;}
    public void setID(int id) {this.id = id;}

    //ATT_UID
    public String getAttUid() {return att_uid;}
    public void setAttUid(String att_uid) {this.att_uid = att_uid;}

    //First Name
    public String getFirstName() {return first_name;}
    public void setFirstName(String first_name) {this.first_name = first_name;}

    //Last Name
    public String getLastName() {return last_name;}
    public void setLastName(String last_name) {this.last_name = last_name;}
}
