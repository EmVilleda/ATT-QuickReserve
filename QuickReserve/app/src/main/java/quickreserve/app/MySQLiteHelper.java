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

    public void firstRun()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        populateUsers(db);
        populateWorkspaces(db);
        populateWorkspaceTypes(db);
    }

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
                values.put("photo_path", columns[5].trim());
                db.insert("workspaces", null, values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

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

    public int addReservation(Reservation reservation)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String workspaceName = reservation.getWorkspaceID();
        String date = String.valueOf(reservation.getDate());
        String startTime = String.valueOf(reservation.getStartTime());
        String endTime = String.valueOf(reservation.getEndTime());

        try {
            Cursor cursor = db.query("reservations",
                    RESERVATION_COLUMNS,
                    "workspace_id = ? AND date = ? AND start_time < ? AND end_time > ?",
                    new String[] { workspaceName, date, endTime, startTime},
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

    public List<Reservation> getAllReservations()
    {
        List<Reservation> reservations = new LinkedList<Reservation>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM reservations";
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

    public List<Reservation> getUserReservations(User user)
    {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int date = (year*10000)+(month*100)+day;
        String dateStr = String.valueOf(date);
        List<Reservation> userReservations = new LinkedList<Reservation>();
        String attUid = user.getAttUid();
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor cursor =
                    db.query("reservations", // a. table
                            RESERVATION_COLUMNS, // b. column names
                            " att_uid = ? AND date >= ?", // c. selections
                            new String[]{attUid, dateStr}, // d. selections args
                            null, // e. group by
                            null, // f. having
                            "date ASC", // g. order by
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
