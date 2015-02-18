package it.lieberkind.travelbuddyv3;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class TravelActivity extends Activity {

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
     * Define the behavior for when the activity is created
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

        createCheckinButtonListener();
        createCheckoutButtonListener();
    }

    /**
     * Create the action bar menu items
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, "Receipt");

        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return true;
    }

    /**
     * Define behavior for when a menu item is selected in the action bar
     *
     * @param featureId
     * @param item
     * @return
     */
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case Menu.FIRST:
                printReceipt();
                break;
            default:
                break;
        }

        return super.onMenuItemSelected(featureId, item);
    }

    /**
     * Define behavior for when the instance state is saved
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(LAST_START, lastStart);
        outState.putString(LAST_DESTINATION, lastDestination);

        super.onSaveInstanceState(outState);
    }

    /**
     * Define behavior for when the instance state is restored
     *
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        lastStart = savedInstanceState.getString(LAST_START);
        lastDestination = savedInstanceState.getString(LAST_DESTINATION);

        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * Print a receipt of the user's last journey
     */
    private void printReceipt() {
        if(lastStart != null && lastDestination != null) {
            showMessage("You travelled from " + lastStart + " to " + lastDestination);
        } else {
            showMessage("Your journey has yet to begin");
        }
    }

    /**
     * Create the check-in button listener
     */
    private void createCheckinButtonListener() {
        Button button = (Button) findViewById(R.id.checkin_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TravelActivity.this.checkin();
            }
        });
    }

    /**
     * Create the check-out button listener
     */
    private void createCheckoutButtonListener() {
        Button button = (Button) findViewById(R.id.checkout_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TravelActivity.this.checkout();
            }
        });
    }

    /**
     * Check in at a station
     */
    private void checkin() {
        EditText checkinField = (EditText) findViewById(R.id.checkin_station);

        String station = checkinField.getText().toString();

        if(station.isEmpty()) {
            showMessage("Please enter a check-in station");
        } else {
            // Disable check-in button and text field
            toggleEnabled(R.id.checkin_button, false);
            toggleEnabled(R.id.checkin_station, false);

            // Enable check-out button and text field
            toggleEnabled(R.id.checkout_button, true);
            toggleEnabled(R.id.checkout_station, true);
        }
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
}
