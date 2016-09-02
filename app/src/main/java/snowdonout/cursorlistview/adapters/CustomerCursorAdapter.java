package snowdonout.cursorlistview.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import snowdonout.cursorlistview.R;

public class CustomerCursorAdapter extends CursorAdapter {

    public CustomerCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item, null);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView customerName = (TextView) view.findViewById(R.id.customer_name);
        String custName = cursor.getString(cursor.getColumnIndexOrThrow("customer_name"));
        customerName.setText(custName);

    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public long getItemId(int position) {
        if (getCursor().moveToPosition(position)) {
            return getCursor().getInt(getCursor().getColumnIndexOrThrow("_id"));
        }
        return -1;
    }
}
