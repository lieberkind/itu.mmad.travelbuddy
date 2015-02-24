package it.lieberkind.travelbuddyv3;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class TravelActivity extends Activity
        implements TravelFragment.StationSelectorListener,
                    TravelFragment.TravelListener,
                    StationListFragment.OnStationSelectedListener {

    /**
     * The last journey's starting point
     */
    private String currentStart;

    /**
     * Bla bla. Change this description
     */
    private String currentDestination;

    /**
     * Indicate whether or not the user is currently checked in
     */
    private boolean checkedIn = false;

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

        fragmentTransaction.add(R.id.frame, TravelFragment.create(currentStart, currentDestination, checkedIn));
        fragmentTransaction.commit();
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
            currentStart = station;
        } else {
            currentDestination = station;
        }

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.frame, TravelFragment.create(currentStart, currentDestination, checkedIn));

        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void setCheckinStation(String station) {
        currentStart = station;
    }

    @Override
    public void checkin(String station) {
        checkedIn = true;
    }

    @Override
    public void checkout(String station) {
        checkedIn = false;
        currentStart = "";
        currentDestination = "";
    }
}
