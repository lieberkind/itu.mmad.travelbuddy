package it.lieberkind.travelbuddyv3;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class TravelFragment extends Fragment {

    /**
     * Id's for stuff that need to go in bundles
     */
    public static final String CHECKIN_STATION = "checkin_station";
    public static final String CHECKOUT_STATION = "checkout_station";

    /**
     * Latest start and destination
     */
    private String lastStart;
    private String lastDestination;

    /**
     * The listener for when one of the "select station" buttons are pressed
     */
    OnStationSelectorClickListener stationSelectorClickListener;

    /**
     * Static method for easy creation of new TravelFragment instances
     *
     * @param checkinStation String
     * @param checkoutStation String
     * @return A new TravelFragment instance
     */
    public static TravelFragment create(String checkinStation, String checkoutStation)
    {
        TravelFragment fragment = new TravelFragment();

//        Bundle args = new Bundle();
//        args.putString(CHECKIN_STATION, checkinStation);
//        args.putString(CHECKOUT_STATION, checkoutStation);
//
//        fragment.setArguments(args);

        return fragment;
    }

    /**
     * Empty constructor
     */
    public TravelFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString("lol", "hello");

        super.onSaveInstanceState(outState);
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
            stationSelectorClickListener = (OnStationSelectorClickListener) activity;
        } catch (ClassCastException exception) {
            throw new ClassCastException(activity.toString() +
                    " must implement OnStationSelectorClickListener");
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

        // Indicate that this fragment uses the Action Bar
        setHasOptionsMenu(true);
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

//        if(getArguments() != null) {
//            // Set the check-in station
//            EditText checkinStation = (EditText) travelFragmentView.findViewById(R.id.checkin_station);
//            checkinStation.setText(getArguments().getString(CHECKIN_STATION));
//
//            // Set the check-out station
//            EditText checkoutStation = (EditText) travelFragmentView.findViewById(R.id.checkout_station);
//            checkoutStation.setText(getArguments().getString(CHECKOUT_STATION));
//        }

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
        MenuItem item = menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, "Receipt");

        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        if(getActivity().getActionBar() != null) {
            getActivity().getActionBar().setTitle("TravelBuddy");
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Handler for when an option item is selected
     *
     * @param item MenuItem
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case Menu.FIRST:
                printReceipt();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Create the button listeners for the fragment's view
     *
     * @param view View
     */
    private void createButtonListeners(View fragmentView)
    {
        createSelectCheckinStationButtonListener(fragmentView);
        createSelectCheckoutStationButtonListener(fragmentView);
        createCheckinButtonListener(fragmentView);
        createCheckoutButtonListener(fragmentView);
    }

    /**
     * Create the select check-in station button listener
     *
     * @param view View
     */
    private void createSelectCheckinStationButtonListener(View fragmentView) {
        Button button = (Button) fragmentView.findViewById(R.id.select_checkin_station_button);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                stationSelectorClickListener.onStationSelectorClick(view);
            }
        });
    }

    /**
     * Create the select check-out station button listener
     *
     * @param view View
     */
    private void createSelectCheckoutStationButtonListener(View fragmentView) {
        Button button = (Button) fragmentView.findViewById(R.id.select_checkout_station_button);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                stationSelectorClickListener.onStationSelectorClick(view);
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
     * Check in at a station
     */
    private void checkin() {
        EditText checkinField = (EditText) getActivity().findViewById(R.id.checkin_station);

        String station = checkinField.getText().toString();

        if(station.isEmpty()) {
            showMessage("Please enter a check-in station");
        } else {
            // Disable check-in button and text field
            toggleEnabled(R.id.checkin_button, false);
            toggleEnabled(R.id.checkin_station, false);
            toggleEnabled(R.id.select_checkin_station_button, false);

            // Enable check-out button and text field
            toggleEnabled(R.id.checkout_button, true);
            toggleEnabled(R.id.checkout_station, true);
            toggleEnabled(R.id.select_checkout_station_button, true);
        }
    }

    /**
     * Create the check-out button listener
     */
    private void createCheckoutButtonListener(View view) {
        Button button = (Button) view.findViewById(R.id.checkout_button);
    }

    public interface OnStationSelectorClickListener {
        public void onStationSelectorClick(View view);
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
        if(lastStart != null && lastDestination != null) {
            showMessage("You travelled from " + lastStart + " to " + lastDestination);
        } else {
            showMessage("Your journey has yet to begin");
        }
    }
}
