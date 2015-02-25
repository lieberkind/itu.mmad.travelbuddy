package it.lieberkind.travelbuddyv3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


public class TravelFragment extends Fragment {

    /**
     * Id for check-in station
     */
    public static final String CHECKIN_STATION = "checkin_station";

    /**
     * Id for check-out station
     */
    public static final String CHECKOUT_STATION = "checkout_station";

    /**
     * Id for whether the user is checked in or not
     */
    public static final String CHECKED_IN = "checked_in";

    /**
     * Latest start destination
     */
    private String lastStart = "";

    /**
     * Last end destination
     */
    private String lastDestination = "";

    /**
     * Listener for various events regarding the journey
     */
    private TravelListener travelListener;

    /**
     * Empty constructor
     */
    public TravelFragment() {
    }

    /**
     * Static method for easy creation of new TravelFragment instances
     *
     * @param checkinStation String
     * @param checkoutStation String
     * @param checkedIn boolean
     * @return A new TravelFragment instance
     */
    public static TravelFragment create(String checkinStation, String checkoutStation, boolean checkedIn)
    {
        TravelFragment fragment = new TravelFragment();

        Bundle args = new Bundle();
        args.putString(CHECKIN_STATION, checkinStation);
        args.putString(CHECKOUT_STATION, checkoutStation);
        args.putBoolean(CHECKED_IN, checkedIn);

        // Indicate that the fragment uses the Action Bar
        fragment.setHasOptionsMenu(true);

        fragment.setArguments(args);

        return fragment;
    }


    /**
     * Handler for when the fragment is attached to the activity
     *
     * @param activity Activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            travelListener = (TravelListener) activity;
        } catch (ClassCastException exception) {
            throw new ClassCastException(activity.toString() +
                    " must implement OnStationSelectorClickListener and TravelListener");
        }
    }

    /**
     * Handler for when the fragment is created
     *
     * @param savedInstanceState Bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Handler for when the fragment's view is created
     *
     * @param inflater LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     *
     * @return TravelFragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View travelFragmentView = inflater.inflate(R.layout.fragment_travel, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        if(getArguments() != null) {
            // Set the check-in station
            EditText checkinStationField = (EditText) travelFragmentView.findViewById(R.id.checkin_station);
            String checkinStation = getArguments().getString(CHECKIN_STATION);

            checkinStationField.setText(checkinStation);

            // Set the check-out station
            EditText checkoutStation = (EditText) travelFragmentView.findViewById(R.id.checkout_station);
            checkoutStation.setText(getArguments().getString(CHECKOUT_STATION));

            // If no check-in station is set, we should make sure that the check-in section is
            // enabled, and the check-out section is disabled
            boolean enableCheckinSection = !getArguments().getBoolean(CHECKED_IN);

            toggleEnabled(travelFragmentView.findViewById(R.id.checkin_station), enableCheckinSection);
            toggleEnabled(travelFragmentView.findViewById(R.id.select_checkin_station_button), enableCheckinSection);
            toggleEnabled(travelFragmentView.findViewById(R.id.checkin_button), enableCheckinSection);

            toggleEnabled(travelFragmentView.findViewById(R.id.checkout_station), !enableCheckinSection);
            toggleEnabled(travelFragmentView.findViewById(R.id.select_checkout_station_button), !enableCheckinSection);
            toggleEnabled(travelFragmentView.findViewById(R.id.checkout_button), !enableCheckinSection);
        }

        createButtonListeners(travelFragmentView);

        return travelFragmentView;
    }

    /**
     * Handler for when the options bar is created
     *
     * @param menu Menu
     * @param inflater MenuInflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item = menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, "Receipt");

        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        if(getActivity().getActionBar() != null) {
            getActivity().getActionBar().setTitle("TravelBuddy");
        }
    }

    /**
     * Handler for when an option item is selected
     *
     * @param item MenuItem
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == Menu.FIRST) {
            printReceipt();
        }

        return super.onOptionsItemSelected(item);
    }

    //<editor-fold desc="Button listeners">
    /**
     * Create the button listeners for the fragment's view
     *
     * @param fragmentView View
     */
    private void createButtonListeners(View fragmentView)
    {
        createSelectCheckinStationButtonListener(fragmentView);
        createSelectCheckoutStationButtonListener(fragmentView);
        createCheckinButtonListener(fragmentView);
        createCheckoutButtonListener(fragmentView);
        createLogoButtonListener(fragmentView);
    }

    /**
     * Create the "select check-in station" button listener
     *
     * @param fragmentView View
     */
    private void createSelectCheckinStationButtonListener(View fragmentView) {
        Button button = (Button) fragmentView.findViewById(R.id.select_checkin_station_button);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View button) {
                travelListener.onStationSelectorClick(button);
            }
        });
    }

    /**
     * Create the "select check-out station" button listener
     *
     * @param fragmentView View
     */
    private void createSelectCheckoutStationButtonListener(View fragmentView) {
        Button button = (Button) fragmentView.findViewById(R.id.select_checkout_station_button);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View button) {
                EditText checkinStationField = (EditText) getActivity().findViewById(R.id.checkin_station);

                String checkinStation = checkinStationField.getText().toString();

                travelListener.setCurrentCheckinStation(checkinStation);

                travelListener.onStationSelectorClick(button);
            }
        });
    }

    /**
     * Create the check-in button listener
     */
    private void createCheckinButtonListener(View view) {
        Button button = (Button) view.findViewById(R.id.checkin_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkin();
            }
        });
    }

    /**
     * Create the check-out button listener
     */
    private void createCheckoutButtonListener(View view) {
        Button button = (Button) view.findViewById(R.id.checkout_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkout();
            }
        });
    }

    /**
     * Create the on click listener for the logo
     *
     * @param view
     */
    private void createLogoButtonListener(View view) {

        Display display = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();

        // Only set the logo onClick listener if we're in "normal" rotation
        if(rotation == Surface.ROTATION_0) {
            ImageView logo = (ImageView) view.findViewById(R.id.logo);

            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    startActivity(intent);
                }
            });
        }

    }
    //</editor-fold>

    /**
     * Check in at a station
     */
    private void checkin() {
        EditText checkinField = (EditText) getActivity().findViewById(R.id.checkin_station);

        String station = checkinField.getText().toString();

        if(station.isEmpty()) {
            showMessage("Please enter a check-in station");
        } else {
            // Tell the travel listener that we have checked in
            travelListener.checkin(station);

            // Disable check-in section and disable check-out section
            toggleCheckinSection(false);
            toggleCheckoutSection(true);
        }
    }

    /**
     * Check out at a station
     */
    private void checkout()
    {
        EditText checkinField = (EditText) getActivity().findViewById(R.id.checkin_station);
        String checkinStation = checkinField.getText().toString();

        EditText checkoutField = (EditText) getActivity().findViewById(R.id.checkout_station);
        String checkoutStation = checkoutField.getText().toString();

        if(checkoutStation.isEmpty()) {
            showMessage("Please enter a check-out station");
        } else {
            // Save the stations
            lastStart = checkinStation;
            lastDestination = checkoutStation;

            // Clear the text fields
            checkinField.setText("");
            checkoutField.setText("");

            // Inform the user...
            showMessage("Journey ended!");

            // Tell the travel listener that we have checked out
            travelListener.checkout(checkoutStation);

            // Enable check-in section and disable check-out section
            toggleCheckinSection(true);
            toggleCheckoutSection(false);
        }
    }

    /**
     * Display a message to the user
     *
     * @param message
     */
    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Toggle the "enabled" state of a view
     *
     * @param viewId int
     * @param enabled boolean
     */
    private void toggleEnabled(int viewId, boolean enabled) {
        toggleEnabled(getActivity().findViewById(viewId), enabled);
    }

    /**
     * Toggle the "enabled" state of a view
     *
     * @param view View
     * @param enabled boolean
     */
    private void toggleEnabled(View view, boolean enabled) {
        view.setEnabled(enabled);
    }

    /**
     * Print a receipt of the user's last journey
     */
    private void printReceipt() {
        if(!lastStart.isEmpty() && !lastDestination.isEmpty()) {
            showMessage("You travelled from " + lastStart + " to " + lastDestination);
        } else {
            showMessage("Your journey has yet to begin");
        }
    }

    /**
     * Toggle the enabled state of the check-in related views
     *
     * @param show boolean
     */
    private void toggleCheckinSection(boolean show) {
        toggleEnabled(R.id.checkin_station, show);
        toggleEnabled(R.id.select_checkin_station_button, show);
        toggleEnabled(R.id.checkin_button, show);
    }

    /**
     * Toggle the enabled state of the check-out related views
     *
     * @param show boolean
     */
    private void toggleCheckoutSection(boolean show) {
        toggleEnabled(R.id.checkout_station, show);
        toggleEnabled(R.id.select_checkout_station_button, show);
        toggleEnabled(R.id.checkout_button, show);
    }

    /**
     * Listener interface for communication with containing activities
     */
    public interface TravelListener {
        public void onStationSelectorClick(View view);
        public void setCurrentCheckinStation(String station);
        public void checkin(String station);
        public void checkout(String station);
    }
}
