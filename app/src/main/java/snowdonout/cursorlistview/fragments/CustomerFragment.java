package snowdonout.cursorlistview.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import snowdonout.cursorlistview.R;
import snowdonout.cursorlistview.adapters.CustomerCursorAdapter;
import snowdonout.cursorlistview.helpers.DbHandler;


public class CustomerFragment extends Fragment {
    public static String TAG = CustomerFragment.class.getName();

    private ListView rows;
    private CustomerCursorAdapter adapter;
    private DbHandler controller;

    private AlertDialog addCustomerDialog;

    public CustomerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = new DbHandler(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_view, container, false);
        rows = (ListView) view.findViewById(R.id.customer_list);
        Button addCustomer = (Button) view.findViewById(R.id.add_customer);
        addCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCustomer();
            }
        });
        rows.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                controller.removeCustomerDetail(Math.round(adapter.getItemId(position)));
                adapter.swapCursor(controller.readCustomerNames());
                return true;
            }
        });
        adapter = new CustomerCursorAdapter(getActivity(), controller.readCustomerNames());
        rows.setAdapter(adapter);
        return view;

    }

    private void showAddCustomer() {
        if (addCustomerDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setView(R.layout.add_customer_dialog);
            addCustomerDialog = builder.create();
        }
        if (!addCustomerDialog.isShowing() && !getActivity().isFinishing())
            addCustomerDialog.show();

        final EditText name = (EditText) addCustomerDialog.findViewById(R.id.name);
        name.getText().clear();
        name.requestFocus();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager manager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.showSoftInput(name, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 20);

        Button submit = (Button) addCustomerDialog.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(name.getText()) ||
                        (name.getText() != null && TextUtils.isEmpty(name.getText().toString().trim()))) {
                    Toast.makeText(getActivity(), CustomerFragment.this.getText(R.string.name_empty), Toast.LENGTH_SHORT).show();
                } else {
                    String val = TextUtils.isEmpty(name.getText()) ? "" : name.getText().toString();
                    controller.addCustomer(val);
                    addCustomerDialog.dismiss();
                    adapter.swapCursor(controller.readCustomerNames());
                    if (controller.readCustomerNames().getCount() == 1) {
                        Snackbar bar = Snackbar.make(rows, CustomerFragment.this.getText(R.string.remove_msg), Snackbar.LENGTH_LONG);
                        View view = bar.getView();
                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                        params.gravity = Gravity.CENTER;
                        params.width = FrameLayout.LayoutParams.WRAP_CONTENT;
                        view.setLayoutParams(params);
                        bar.show();
                    }
                }
            }
        });
    }
}
