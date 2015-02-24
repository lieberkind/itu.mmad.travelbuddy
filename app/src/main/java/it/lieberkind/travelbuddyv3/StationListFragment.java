package it.lieberkind.travelbuddyv3;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link it.lieberkind.travelbuddyv3.StationListFragment.OnStationSelectedListener}
 * interface.
 */
public class StationListFragment extends Fragment implements AbsListView.OnItemClickListener {

    /**
     * List of stations
     */
    private ArrayList<String> mStations;

    /**
     * The listener for when an item in the list is clicked
     */
    private OnStationSelectedListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    /**
     * Static method for easy creation of new StationListFragment instances
     *
     * @return StationListFragment
     */
    public static StationListFragment create() {
        StationListFragment fragment = new StationListFragment();

        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StationListFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnStationSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnStationSelectedListener");
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

        // Indicate that this fragment uses the options bar
        setHasOptionsMenu(true);

        // Initialize the stations
        mStations = new ArrayList<String>();
        mStations.add("Copenhagen");
        mStations.add("CPH Airport");
        mStations.add("Berlin");

        mAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, mStations);
    }

    /**
     * Handler for when the view is created
     *
     * @param inflater LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stationlist, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    /**
     * Handler for when the action bar is created
     *
     * @param menu Menu
     * @param inflater MenuInflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // Remove all items from the menu bar
        menu.clear();

        if(getActivity().getActionBar() != null) {
            getActivity().getActionBar().setTitle("Pick a station");
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Handler for when the fragment is detached from the view
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Handler for when an item in the list is clicked
     *
     * @param parent AdapterView<?>
     * @param view View
     * @param position int
     * @param id long
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mListener.onStationSelected(mStations.get(position));
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnStationSelectedListener {
        public void onStationSelected(String station);
    }

}
