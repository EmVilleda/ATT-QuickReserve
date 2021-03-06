package quickreserve.app;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/* MySQLiteHelper.java
 * Created by Charlie Albright (ATT_UID: ca8681)
 * 06/12/2014
 * App: QuickReserve


    A WILD X-WING APPEARED
               __
    .-.__      \ .-.  ___  __|_|     
'--.-.-(   \/\;;\_\.-._______.-.___---( )
    (-)___     \ \ .-\ \;;\(   \       \ \
      \   '---._\_((Q)) \;;\\ .-\     __(-)
       \         __'-' / .--.((Q))---'    \
        \  ___.-:    \|  |   \'-'_         \
        .0'      \ .-.\   \   \ \ '--.__    \
        |_\__.----((Q))\   \__|--\_      \   0
        (-)        '-'  \_  :  \-' '--.___\   \
          \               \  \  \       \(-)
           \               \  \  \         \
            \               \  \  \         \
             0               \  \  \         \
              \               \  \__|         0
                               \_:.  \         \
                                 \ \  \
                                  \ \  \
                                   \_\_|

*/
public class MySQLiteHelper extends SQLiteOpenHelper {


    //static values for database
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "QuickReserveDB";

    //static values for table "users"
    private static final String CREATE_USER_TABLE = "CREATE TABLE users (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "att_uid TEXT, " +
            "first_name TEXT, " + "last_name TEXT, " + "UNIQUE (att_uid))";
    private static final String[] USER_COLUMNS = {"id", "att_uid", "first_name", "last_name"};

    //static values for table "reservations"
    private static final String CREATE_RESERVATION_TABLE = "CREATE TABLE reservations (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "workspace_id TEXT, " + "att_uid TEXT, " +
            "start_time INTEGER, " + "end_time INTEGER, " + "date INTEGER)";
    private static final String[] RESERVATION_COLUMNS = {"id", "workspace_id", "att_uid",
            "start_time", "end_time", "date"};

    //static values for table "workspaces"
    private static final String CREATE_WORKSPACE_TABLE = "CREATE TABLE workspaces (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "name TEXT, " + "sector INTEGER, " +
            "phone_number TEXT, " + "printer_number TEXT, " + "is_booked INTEGER, " +
            "type INTEGER, " + "photo_path TEXT)";
    private static final String[] WORKSPACE_COLUMNS = {"id", "name", "sector", "phone_number",
            "printer_number", "is_booked", "type", "photo_path"};

    //static values for table "workspaceTypes"
    private static final String CREATE_WORKSPACE_TYPE_TABLE = "CREATE TABLE workspaceTypes (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "name TEXT, " + "max_reservation_length INTEGER, "
            + "capacity INTEGER)";
    private static final String[] WORKSPACE_TYPE_COLUMNS = {"id", "name", "max_reservation_length",
            "capacity"};


    private Context context;

    //Constructor
    public MySQLiteHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_RESERVATION_TABLE);
        db.execSQL(CREATE_WORKSPACE_TABLE);
        db.execSQL(CREATE_WORKSPACE_TYPE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS reservations");
        db.execSQL("DROP TABLE IF EXISTS workspaces");
        db.execSQL("DROP TABLE IF EXISTS workspaceTypes");
        this.onCreate(db);
    }

    //Populates the DB with pre-assigned data on the app's first run
    public void firstRun()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        populateUsers(db);
        populateWorkspaces(db);
        populateWorkspaceTypes(db);
    }

    //Deletes all data from the database
    public boolean clearDatabase()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("DROP TABLE IF EXISTS users");
            db.execSQL("DROP TABLE IF EXISTS reservations");
            db.execSQL("DROP TABLE IF EXISTS workspaces");
            db.execSQL("DROP TABLE IF EXISTS workspaceTypes");
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //clears and repopulates the data in the DB
    public void repopulateData(SQLiteDatabase db)
    {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS reservations");
        db.execSQL("DROP TABLE IF EXISTS workspaces");
        db.execSQL("DROP TABLE IF EXISTS workspaceTypes");
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_RESERVATION_TABLE);
        db.execSQL(CREATE_WORKSPACE_TABLE);
        db.execSQL(CREATE_WORKSPACE_TYPE_TABLE);
        populateUsers(db);
        populateWorkspaces(db);
        populateWorkspaceTypes(db);
    }

    //Populates the User table with users from file
    private void populateUsers(SQLiteDatabase db)
    {
        String userCSV = "UserData.csv";
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open(userCSV);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        db.beginTransaction();
        try {
            while((line = buffer.readLine()) != null)
            {
                String[] columns = line.split(",");
                if(columns.length != 3) {
                    Log.d("CSVParsing", "Skipping bad data row: " + line + " columns: " + columns.length);
                    continue;
                }
                ContentValues values = new ContentValues();
                values.put("att_uid", columns[0].trim());
                values.put("first_name", columns[1].trim());
                values.put("last_name", columns[2].trim());
                db.insert("users", null, values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        db.setTransactionSuccessful();
        db.endTransaction();

    }

    //Populates the Workspace table with workspaces from file
    private void populateWorkspaces(SQLiteDatabase db)
    {
        String workspaceCSV = "WorkspaceData.csv";
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open(workspaceCSV);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        db.beginTransaction();
        try {
            while((line = buffer.readLine()) != null)
            {
                String[] columns = line.split(",");
                if(columns.length != 6) {
                    Log.d("CSVParsing", "Skipping bad data row: " + line + " columns: " + columns.length);
                    continue;
                }
                ContentValues values = new ContentValues();
                values.put("name", columns[0].trim());
                values.put("sector", Integer.parseInt(columns[1].trim()));
                values.put("phone_number", columns[2].trim());
                values.put("printer_number", columns[3].trim());
                values.put("type", Integer.parseInt(columns[4].trim()));
                values.put("is_booked", 0);
                values.put("photo_path", columns[5].trim());
                db.insert("workspaces", null, values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    //Populates the WorkspaceType table with workspace types from file
    private void populateWorkspaceTypes(SQLiteDatabase db)
    {
        String workspaceTypeCSV = "WorkspaceTypeData.csv";
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open(workspaceTypeCSV);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        db.beginTransaction();
        try {
            while((line = buffer.readLine()) != null)
            {
                String[] columns = line.split(",");
                if(columns.length != 3) {
                    Log.d("CSVParsing", "Skipping bad data row: " + line + " columns: " + columns.length);
                    continue;
                }
                ContentValues values = new ContentValues();
                values.put("name", columns[0]);
                values.put("max_reservation_length", Integer.parseInt(columns[1].trim()));
                values.put("capacity", Integer.parseInt(columns[2].trim()));
                db.insert("workspaceTypes", null, values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    //User functions

    //inserts a new user into the database
    public boolean addUser(User user)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("att_uid", user.getAttUid());
        values.put("first_name", user.getFirstName());
        values.put("last_name", user.getLastName());

        try
        {
            db.insert("users", null, values);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //gets a user from the database based off of their att_uid, or returns null if not found
    public User getUser(String att_uid)
    {
        User user = null;
        SQLiteDatabase db = this.getReadableDatabase();
        try
        {
            Cursor cursor =
                    db.query("users", // a. table
                            USER_COLUMNS, // b. column names
                            " att_uid = ?", // c. selections
                            new String[]{att_uid}, // d. selections args
                            null, // e. group by
                            null, // f. having
                            null, // g. order by
                            null); // h. limit
            if (cursor != null && cursor.getCount()>0)
            {
                cursor.moveToFirst();
                user = new User();
                user.setID(Integer.parseInt(cursor.getString(0)));
                user.setAttUid(cursor.getString(1));
                user.setFirstName(cursor.getString(2));
                user.setLastName(cursor.getString(3));
            }
            else
                return null;
        } catch (SQLiteException e) {
            e.printStackTrace();
            return null;
        }
        return user;
    }

    //returns all the users that exist in the DB
    public List<User> getAllUsers()
    {
        List<User> users = new LinkedList<User>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM users";

        try {
            Cursor cursor = db.rawQuery(query, null);
            User user = null;
            if (cursor.moveToFirst()) {
                do {
                    user = new User();
                    user.setID(Integer.parseInt(cursor.getString(0)));
                    user.setAttUid(cursor.getString(1));
                    user.setFirstName(cursor.getString(2));
                    user.setLastName(cursor.getString(3));
                    users.add(user);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            return null;
        }
        return users;
    }

    //deletes the user passed into the function from the database
    public boolean deleteUser(User user)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete("users", "att_uid = ?", new String[]{user.getAttUid()});
        } catch (SQLiteException e) {
            e.printStackTrace();
            db.close();
            return false;
        }
        db.close();
        return true;
    }

    //Reservation functions

    //adds a new reservation into the DB
    public int addReservation(Reservation reservation)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String att_uid = reservation.getAttUid();
        String date = String.valueOf(reservation.getDate());
        String startTime = String.valueOf(reservation.getStartTime());
        String endTime = String.valueOf(reservation.getEndTime());

        try {
            Cursor cursor = db.query("reservations",
                    RESERVATION_COLUMNS,
                    "att_uid = ? AND date = ? AND start_time < ? AND end_time > ?",
                    new String[] { att_uid, date, endTime, startTime},
                    null,
                    null,
                    null);

            if(cursor != null && cursor.getCount() > 0)
                return 1;

        } catch (SQLiteException e) {
            e.printStackTrace();
            return 0;
        }

        try {
            ContentValues values = new ContentValues();
            values.put("workspace_id", reservation.getWorkspaceID());
            values.put("att_uid", reservation.getAttUid());
            values.put("start_time", reservation.getStartTime());
            values.put("end_time", reservation.getEndTime());
            values.put("date", reservation.getDate());

            db.insert("reservations", null, values);
        } catch (SQLiteException e) {
            e.printStackTrace();
            db.close();
            return 0;
        }
        db.close();
        return 2;
    }

    //gets a reservation from the DB by its unique ID
    public Reservation getReservation(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String idStr = String.valueOf(id);
        Reservation reservation = null;
        try
        {
            Cursor cursor =
                    db.query("reservations", // a. table
                            RESERVATION_COLUMNS, // b. column names
                            "id = ?", // c. selections
                            new String[]{idStr}, // d. selections args
                            null, // e. group by
                            null, // f. having
                            null, // g. order by
                            null); // h. limit
            if (cursor != null && cursor.getCount()>0)
            {
                cursor.moveToFirst();
                reservation = new Reservation();
                reservation.setID(Integer.parseInt(cursor.getString(0)));
                reservation.setWorkspaceID(cursor.getString(1));
                reservation.setAttUid(cursor.getString(2));
                reservation.setStartTime(Integer.parseInt(cursor.getString(3)));
                reservation.setEndTime(Integer.parseInt(cursor.getString(4)));
                reservation.setDate(Integer.parseInt(cursor.getString(5)));
            } else {
                return null;
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            return null;
        }
        return reservation;
    }

    //gets all reservations that exist in the DB
    public List<Reservation> getAllReservations()
    {
        List<Reservation> reservations = new LinkedList<Reservation>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM reservations ORDER BY date, start_time ASC";
        Reservation reservation = null;
        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    reservation = new Reservation();
                    reservation.setID(Integer.parseInt(cursor.getString(0)));
                    reservation.setWorkspaceID(cursor.getString(1));
                    reservation.setAttUid(cursor.getString(2));
                    reservation.setStartTime(Integer.parseInt(cursor.getString(3)));
                    reservation.setEndTime(Integer.parseInt(cursor.getString(4)));
                    reservation.setDate(Integer.parseInt(cursor.getString(5)));
                    reservations.add(reservation);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            return null;
        }
        return reservations;
    }

    //gets a specific user's reservations, not including reservations where the date and time has already passed
    public List<Reservation> getUserReservations(String att_uid)
    {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int date = (year*10000)+(month*100)+day;
        int time = hour*100 + minute;
        String dateStr = String.valueOf(date);
        String timeStr = String.valueOf(time);
        List<Reservation> userReservations = new LinkedList<Reservation>();
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor =
                    db.query("reservations", // a. table
                            RESERVATION_COLUMNS, // b. column names
                            " att_uid = ? AND date >= ?", // c. selections
                            new String[]{att_uid, dateStr}, // d. selections args
                            null, // e. group by
                            null, // f. having
                            "date, start_time ASC", // g. order by
                            null); // h. limit

            Reservation reservation = null;
            if (cursor.getCount() > 0)
            {
                cursor.moveToFirst();
                do {
                    reservation = new Reservation();
                    reservation.setID(Integer.parseInt(cursor.getString(0)));
                    reservation.setWorkspaceID(cursor.getString(1));
                    reservation.setAttUid(cursor.getString(2));
                    reservation.setStartTime(Integer.parseInt(cursor.getString(3)));
                    reservation.setEndTime(Integer.parseInt(cursor.getString(4)));
                    reservation.setDate(Integer.parseInt(cursor.getString(5)));
                    if(reservation.getEndTime() > time || reservation.getDate() != date)
                        userReservations.add(reservation);
                } while (cursor.moveToNext());
            }
            else
                return null;
        } catch (SQLiteException e) {
            e.printStackTrace();
            return null;
        }
        return userReservations;
    }

    //checks if a workspace is reserved or not during a specified day
    public boolean isWorkspaceReserved(String workspaceName, int day, int month, int year)
    {
        int date = (year*10000)+(month*100)+day;
        String dateStr = String.valueOf(date);
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor = db.query("reservations",
                    RESERVATION_COLUMNS, // b. column names
                    "workspace_id = ? AND date = ?", // c. selections
                    new String[]{workspaceName, dateStr}, // d. selections args
                    null, // e. group by
                    null, // f. having
                    null, // g. order by
                    null); // h. limit

            if(cursor.getCount() > 0)
                return true;
            else
                return false;
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
    }

    //edits an existing reservation with the provided new information, and also check to make sure that the edit does not conflict with an existing reservation
    public boolean editReservation(int rowID, String att_uid, String workspaceName, int date, int startTime, int endTime)
    {
        String idStr = String.valueOf(rowID);
        String dateStr = String.valueOf(date);
        String startTimeStr = String.valueOf(startTime);
        String endTimeStr = String.valueOf(endTime);
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        if(workspaceName != null)
            values.put("workspace_id", workspaceName);
        if(date != 0)
            values.put("date", date);
        if(startTime != 0)
            values.put("start_time", startTime);
        if(endTime != 0)
            values.put("end_time", endTime);

        try {
            Cursor cursor = db.query("reservations",
                    RESERVATION_COLUMNS,
                    "att_uid = ? AND date = ? AND start_time < ? and end_time > ?",
                    new String[] {att_uid, dateStr, endTimeStr, startTimeStr},
                    null,
                    null,
                    null,
                    null);

            if(cursor.getCount() > 0)
            {
                cursor.moveToFirst();
                do {
                    if(Integer.parseInt(cursor.getString(0)) != rowID)
                        return false;
                } while(cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }

        try {
            db.update("reservations", values, "id = ?", new String[] {idStr});
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //deletes a reservation from the DB
    public boolean deleteReservation(int id)
    {
        String idStr = String.valueOf(id);
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete("reservations", "id = ?", new String[]{idStr});
        } catch (SQLiteException e) {
            e.printStackTrace();
            db.close();
            return false;
        }
        db.close();
        return true;
    }


    //Workspace functions

    //adds a new workspace to the DB
    public boolean addWorkspace(Workspace workspace)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("name", workspace.getName());
            values.put("sector", workspace.getSector());
            values.put("phone_number", workspace.getPhoneNumber());
            values.put("printer_number", workspace.getPrinterNumber());
            values.put("is_booked", workspace.getIsBooked());
            values.put("type", workspace.getType());
            values.put("photo_path", workspace.getPhotoPath());
            db.insert("workspaces", null, values);
        } catch (SQLiteException e) {
            e.printStackTrace();
            db.close();
            return false;
        }
        return true;
    }

    //gets a workspace from the DB by its unique workspace name
    public Workspace getWorkspace(String workspaceName)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Workspace workspace = null;
        try {
            Cursor cursor =
                    db.query("workspaces",
                            WORKSPACE_COLUMNS,
                            " name = ?",
                            new String[]{workspaceName},
                            null,
                            null,
                            null,
                            null);


            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                workspace = new Workspace();
                workspace.setID(Integer.parseInt(cursor.getString(0)));
                workspace.setName(cursor.getString(1));
                workspace.setSector(Integer.parseInt(cursor.getString(2)));
                workspace.setPhoneNumber(cursor.getString(3));
                workspace.setPrinterNumber(cursor.getString(4));
                workspace.setIsBooked(Integer.parseInt(cursor.getString(5)));
                workspace.setType(Integer.parseInt(cursor.getString(6)));
                workspace.setPhotoPath(cursor.getString(7));
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            return null;
        }
        return workspace;
    }

    //gets all workspaces that exist in the DB
    public List<Workspace> getAllWorkspaces()
    {
        List<Workspace> workspaces = new LinkedList<Workspace>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM workspaces";
        Workspace workspace = null;

        try {
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    workspace = new Workspace();
                    workspace.setID(Integer.parseInt(cursor.getString(0)));
                    workspace.setName(cursor.getString(1));
                    workspace.setSector(Integer.parseInt(cursor.getString(2)));
                    workspace.setPhoneNumber(cursor.getString(3));
                    workspace.setPrinterNumber(cursor.getString(4));
                    workspace.setType(Integer.parseInt(cursor.getString(6)));
                    workspace.setPhotoPath(cursor.getString(7));
                    workspaces.add(workspace);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            return null;
        }
        return workspaces;
    }

    //gets all workspaces in the DB that are a part of a specific floor sector
    public List<Workspace> getAllWorkspaces(int sector)
    {
        List<Workspace> workspaces = new LinkedList<Workspace>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sectorStr = String.valueOf(sector);
        Workspace workspace = null;

        try {
            Cursor cursor =
                    db.query("workspaces",
                            WORKSPACE_COLUMNS,
                            "sector = ?",
                            new String[] {sectorStr},
                            null,
                            null,
                            null);
            if (cursor.moveToFirst() && cursor.getCount() > 0) {
                do {
                    workspace = new Workspace();
                    workspace.setID(Integer.parseInt(cursor.getString(0)));
                    workspace.setName(cursor.getString(1));
                    workspace.setSector(Integer.parseInt(cursor.getString(2)));
                    workspace.setPhoneNumber(cursor.getString(3));
                    workspace.setPrinterNumber(cursor.getString(4));
                    workspace.setType(Integer.parseInt(cursor.getString(6)));
                    workspace.setPhotoPath(cursor.getString(7));
                    workspaces.add(workspace);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            return null;
        }
        return workspaces;
    }

    //gets a list of workspaces that are NOT booked during the given date and time
    public List<Workspace> getOpenWorkspaces(int date, int startTime, int endTime)
    {
        List<Workspace> workspaces = new LinkedList<Workspace>();
        List<Workspace> allWorkspaces = getAllWorkspaces();
        List<String> takenWorkspaces = new LinkedList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String dateStr = String.valueOf(date);
        String startTimeStr = String.valueOf(startTime);
        String endTimeStr = String.valueOf(endTime);
        String workspace = null;

        try {
            Cursor cursor = db.query("reservations",
                    new String[]{"workspace_id"},
                    "date = ? AND start_time < ? AND end_time > ?",
                    new String[]{dateStr, endTimeStr, startTimeStr},
                    null,
                    null,
                    null);
            if (cursor.moveToFirst()) {
                do {
                    workspace = cursor.getString(0);
                    takenWorkspaces.add(workspace);
                } while (cursor.moveToNext());
            }
            int size = allWorkspaces.size();
            for (int i = 0; i < size; i++) {
                if (!takenWorkspaces.contains(allWorkspaces.get(i).getName()))
                    workspaces.add(allWorkspaces.get(i));
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            return null;
        }
        return workspaces;
    }

    //same as getOpenWorkspaces, but includes logic for an extra workspace to be added
    public List<Workspace> getOpenWorkspacesIncludeExtra(int date, int startTime, int endTime, String workspaceName)
    {
        List<Workspace> workspaces = new LinkedList<Workspace>();
        List<Workspace> allWorkspaces = getAllWorkspaces();
        List<String> takenWorkspaces = new LinkedList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String dateStr = String.valueOf(date);
        String startTimeStr = String.valueOf(startTime);
        String endTimeStr = String.valueOf(endTime);
        String workspace = null;

        try {
            Cursor cursor = db.query("reservations",
                    new String[]{"workspace_id"},
                    "date = ? AND start_time < ? AND end_time > ?",
                    new String[]{dateStr, endTimeStr, startTimeStr},
                    null,
                    null,
                    null);
            if (cursor.moveToFirst()) {
                do {
                    workspace = cursor.getString(0);
                    takenWorkspaces.add(workspace);
                } while (cursor.moveToNext());
            }
            int size = allWorkspaces.size();
            for (int i = 0; i < size; i++) {
                if (!takenWorkspaces.contains(allWorkspaces.get(i).getName()) || allWorkspaces.get(i).getName().equals(workspaceName))
                    workspaces.add(allWorkspaces.get(i));
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            return null;
        }
        return workspaces;
    }

    //gets a list of workspaces for a sector, indicating whether or not they are reserved with a boolean
    public List<Workspace> getMasterWorkspacesList(int date, int startTime, int endTime, int sector)
    {
        List<Workspace> allWorkspaces = getAllWorkspaces(sector);
        List<String> takenWorkspaces = new LinkedList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String dateStr = String.valueOf(date);
        String startTimeStr = String.valueOf(startTime);
        String endTimeStr = String.valueOf(endTime);
        String workspace;

        try {
            Cursor cursor = db.query("reservations",
                    new String[]{"workspace_id"},
                    "date = ? AND start_time < ? AND end_time > ?",
                    new String[]{dateStr, endTimeStr, startTimeStr},
                    null,
                    null,
                    null);
            if (cursor.moveToFirst()) {
                do {
                    workspace = cursor.getString(0);
                    takenWorkspaces.add(workspace);
                } while (cursor.moveToNext());
            }
            int size = allWorkspaces.size();
            for (int i = 0; i < size; i++) {
                if (takenWorkspaces.contains(allWorkspaces.get(i).getName()))
                    allWorkspaces.get(i).setIsBooked(1);
                else
                    allWorkspaces.get(i).setIsBooked(0);
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            return null;
        }
        return allWorkspaces;
    }

    //gets a list of workspaces, indicating whether or not they are reserved with a boolean
    public List<Workspace> getMasterWorkspacesList(int date, int startTime, int endTime)
    {
        List<Workspace> allWorkspaces = getAllWorkspaces();
        List<String> takenWorkspaces = new LinkedList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String dateStr = String.valueOf(date);
        String startTimeStr = String.valueOf(startTime);
        String endTimeStr = String.valueOf(endTime);
        String workspace;

        try {
            Cursor cursor = db.query("reservations",
                    new String[]{"workspace_id"},
                    "date = ? AND start_time < ? AND end_time > ?",
                    new String[]{dateStr, endTimeStr, startTimeStr},
                    null,
                    null,
                    null);
            if (cursor.moveToFirst()) {
                do {
                    workspace = cursor.getString(0);
                    takenWorkspaces.add(workspace);
                } while (cursor.moveToNext());
            }
            int size = allWorkspaces.size();
            for (int i = 0; i < size; i++) {
                if (takenWorkspaces.contains(allWorkspaces.get(i).getName()))
                    allWorkspaces.get(i).setIsBooked(1);
                else
                    allWorkspaces.get(i).setIsBooked(0);
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            return null;
        }
        return allWorkspaces;
    }
}
