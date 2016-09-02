package snowdonout.cursorlistview.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.List;

public class DbHandler {

    private CustomerDbHelper helper;
    private SQLiteDatabase database;

    public DbHandler(Context context) {
        //dont user Activity context here. it might leak
        helper = CustomerDbHelper.getInstance(context.getApplicationContext());
    }

    public void addCustomer(String name) {
        database = helper.getWritableDatabase();
        helper.addCustomer(database, name);
        // database.close();
    }

    public List<String> getAllNames() {
        database = helper.getWritableDatabase();
        return helper.getAllNames(database);
    }

    public Cursor readCustomerNames() {
        database = helper.getWritableDatabase();
        return helper.readCustomerName(database);
    }

    public void removeCustomerDetail(int position) {
        database = helper.getWritableDatabase();
        helper.removeCustomerName(database, position);
    }
}
