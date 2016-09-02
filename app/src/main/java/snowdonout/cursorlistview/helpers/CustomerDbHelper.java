package snowdonout.cursorlistview.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CustomerDbHelper extends SQLiteOpenHelper {

    private static String LOG_TAG = CustomerDbHelper.class.getName();

    private static String DB_NAME = "customer_db";
    private static int DB_VERSION = 1;
    public String CUSTOMER_TABLE_NAME = "customer_details";
    private static CustomerDbHelper helper;
    private String CUSTOMER_NAME = "customer_name";

    private String CREATE_CUSTOMER_TABLE_QUERY = "CREATE TABLE customer_details (customer_name TEXT)";

    public CustomerDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static synchronized CustomerDbHelper getInstance(Context context) {
        if (helper == null) {
            helper = new CustomerDbHelper(context);
        }
        return helper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CUSTOMER_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + CUSTOMER_TABLE_NAME);
        onCreate(db);
    }

    public void addCustomer(SQLiteDatabase database, String name) {
        ContentValues values = new ContentValues();
        values.put(CUSTOMER_NAME, name);
        database.insert(helper.CUSTOMER_TABLE_NAME, null, values);
        // database.close();

    }

    public List<String> getAllNames(SQLiteDatabase database) {
        List<String> names = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + helper.CUSTOMER_TABLE_NAME, null);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            names.add(cursor.getString(0));
        }
        return names;
    }

    public synchronized Cursor readCustomerName(SQLiteDatabase database) {
        Cursor cursor = database.rawQuery("SELECT rowid _id, customer_name FROM " + helper.CUSTOMER_TABLE_NAME + " WHERE customer_name IS NOT NULL", null);
        cursor.moveToFirst();
        Log.d(LOG_TAG, "cursor.getCount() " + cursor.getCount());
        return cursor;
    }

    //remove row from column
    public synchronized void removeCustomerName(SQLiteDatabase database, int position) {
        ContentValues values = new ContentValues();
        values.put(CUSTOMER_NAME, (String) null);
        database.delete(CUSTOMER_TABLE_NAME, "rowid=?", new String[]{String.valueOf(position)});
    }
}
