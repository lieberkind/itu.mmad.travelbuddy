package it.lieberkind.travelbuddyv3;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class TravelActivity extends Activity
        implements  TravelFragment.OnStationSelectorClickListener,
                    StationListFragment.OnStationSelectedListener {

    /**
     * Last start identifier
     */
    private static final String LAST_START = "last_start";

    /**
     * Last destination identifier
     */
    private static final String LAST_DESTINATION = "last_destination";

    /**
     * The last journey's starting point
     */
    private String lastStart;

    /**
     * The last journey's destination
     */
    private String lastDestination;

    /**
     * The id of the last pressed station selector button
     */
    private int lastSelectStationButtonId;

    /**
     * Define the behavior for when the activity is created
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_travel);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.frame, TravelFragment.create(lastStart, lastDestination));
        fragmentTransaction.commit();
    }

    /**
     * Check out at a station
     */
    private void checkout()
    {
        EditText checkinField = (EditText) findViewById(R.id.checkin_station);
        String checkinStation = checkinField.getText().toString();

        EditText checkoutField = (EditText) findViewById(R.id.checkout_station);
        String checkoutStation = checkoutField.getText().toString();

        if(checkoutStation.isEmpty()) {
            showMessage("Please enter a check-out station");
        } else {
            // Save the stations
            this.lastStart = checkinStation;
            this.lastDestination = checkoutStation;

            // Clear the text fields
            checkinField.setText("");
            checkoutField.setText("");

            // Inform the user...
            showMessage("Journey ended!");

            // Disable check-out button and text field
            toggleEnabled(checkoutField, false);
            toggleEnabled(R.id.checkout_button, false);

            // Enable the check-in button and text field
            toggleEnabled(R.id.checkin_button, true);
            toggleEnabled(R.id.checkin_station, true);
        }
    }

    /**
     * Display a message to the user
     *
     * @param message
     */
    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Toggle the "enabled" state of a view
     *
     * @param viewId
     * @param enabled
     */
    private void toggleEnabled(int viewId, boolean enabled) {
        toggleEnabled(findViewById(viewId), enabled);
    }

    /**
     * Toggle the "enabled" state of a view
     *
     * @param view
     * @param enabled
     */
    private void toggleEnabled(View view, boolean enabled) {
        view.setEnabled(enabled);
    }

    @Override
    public void onStationSelectorClick(View view) {
        lastSelectStationButtonId = view.getId();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.frame, StationListFragment.create());

        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onStationSelected(String station) {
        if(lastSelectStationButtonId == R.id.select_checkin_station_button) {
            lastStart = station;
        } else {
            lastDestination = station;
        }

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.frame, TravelFragment.create(lastStart, lastDestination));

        transaction.addToBackStack(null);

        transaction.commit();
    }
}
