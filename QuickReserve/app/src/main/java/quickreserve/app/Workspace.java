package quickreserve.app;

/* Workspace.java
 * Created by Charlie Albright (ATT_UID: ca8681)
 * 06/09/2014
 * App: QuickReserve
*/

public class Workspace
{
    //private attributes, match columns in Database Table
    private int id;
    private String name;
    private int sector;
    private String phone_number;
    private String printer_number;
    private int is_booked;
    private int type;
    private String photo_path;

    public Workspace() {}

    public Workspace(String name, int sector, String phone_number, String printer_number, int is_booked, int type, String photo_path)
    {
        super();
        this.name = name;
        this.sector = sector;
        this.phone_number = phone_number;
        this.printer_number = printer_number;
        this.is_booked = is_booked;
        this.type = type;
        this.photo_path = photo_path;
    }

    //toString() override class - prints workspace data in view-friendly format
    @Override
    public String toString()
    {
        return "Workspace:\n\tID: " + id + "\n\tName: " + name + "\n\tSector: " + sector +
                "\n\tPhone Number: " + phone_number + "\n\tPrinter Number: " + printer_number +
                "\n\tBooked?: " + is_booked + "\n\tType: " + type + "\n\tPhoto Path: " + photo_path
                + "\n\n";
    }

    //GETTERS AND SETTERS

    //ID
    public int getID() {return id;}
    public void setID(int id) {this.id = id;}

    //Location
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    //sector
    public int getSector() {return sector;}
    public void setSector(int sector) {this.sector = sector;}

    //Phone Number
    public String getPhoneNumber() {return phone_number;}
    public void setPhoneNumber(String phone_number) {this.phone_number = phone_number;}

    //Printer Number
    public String getPrinterNumber() {return printer_number;}
    public void setPrinterNumber(String printer_number) {this.printer_number = printer_number;}

    //Is Booked
    public int getIsBooked() {return is_booked;}
    public void setIsBooked(int is_booked) {this.is_booked = is_booked;}

    //Type
    public int getType() {return type;}
    public void setType(int type) {this.type = type;}

    //Photo Path
    public String getPhotoPath() {return photo_path;}
    public void setPhotoPath(String photo_path) {this.photo_path = photo_path;}
}