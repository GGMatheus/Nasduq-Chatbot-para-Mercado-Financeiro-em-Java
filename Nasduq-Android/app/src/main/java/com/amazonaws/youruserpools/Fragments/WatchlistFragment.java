package com.amazonaws.youruserpools.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.amazonaws.youruserpools.Adapters.WatchlistAdapter;
import com.amazonaws.youruserpools.CognitoYourUserPoolsDemo.R;
import com.amazonaws.youruserpools.Constants;
import com.amazonaws.youruserpools.Data.WatchlistItem;
import com.amazonaws.youruserpools.Webservices.WatchlistWebServiceTask;

import java.util.ArrayList;
import java.util.List;

public class WatchlistFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    public WatchlistAdapter watchlistAdapter;
    public List<WatchlistItem> watchlistItems;
    public Activity currentActivity;

    public WatchlistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WatchlistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WatchlistFragment newInstance(String param1, String param2) {
        WatchlistFragment fragment = new WatchlistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Inflate the layout for this fragment

        final View v = inflater.inflate(R.layout.fragment_watchlist, container, false);
        watchlistItems = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        final RecyclerView watchRecyclerView = (RecyclerView) v.findViewById(R.id.watchlist_recyclerview);

        currentActivity = this.getActivity();

        watchlistAdapter = new WatchlistAdapter(this, watchlistItems, currentActivity);
        watchRecyclerView.setLayoutManager(layoutManager);
        watchRecyclerView.setAdapter(watchlistAdapter);

        WatchlistWebServiceTask wlWebServiceTask = new WatchlistWebServiceTask(
                currentActivity,
                watchRecyclerView,
                watchlistAdapter,
                v);
        wlWebServiceTask.setPeriod(Constants.WATCHLIST_PERIOD_DAILY);
        wlWebServiceTask.execute();

        final Button btnDay = (Button) v.findViewById(R.id.button_day);
        final Button btnWeek = (Button) v.findViewById(R.id.button_week);
        final Button btnMonth = (Button)v.findViewById(R.id.button_month);
        final Button btnYear = (Button) v.findViewById(R.id.button_year);

        btnDay.setPressed(true);

        btnDay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                btnDay.setPressed(true);
                btnWeek.setPressed(false);
                btnMonth.setPressed(false);
                btnYear.setPressed(false);
                WatchlistWebServiceTask wlWebServiceTask = new WatchlistWebServiceTask(
                        currentActivity,
                        watchRecyclerView,
                        watchlistAdapter,
                        v);
                wlWebServiceTask.setPeriod(Constants.WATCHLIST_PERIOD_DAILY);
                wlWebServiceTask.execute();
                return true;
            }
        });

        btnWeek.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                btnWeek.setPressed(true);
                btnDay.setPressed(false);
                btnMonth.setPressed(false);
                btnYear.setPressed(false);
                WatchlistWebServiceTask wlWebServiceTask = new WatchlistWebServiceTask(
                        currentActivity,
                        watchRecyclerView,
                        watchlistAdapter,
                        v);
                wlWebServiceTask.setPeriod(Constants.WATCHLIST_PERIOD_WEEKLY);
                wlWebServiceTask.execute();
                return true;
            }
        });

        btnMonth.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                btnMonth.setPressed(true);
                btnDay.setPressed(false);
                btnWeek.setPressed(false);
                btnYear.setPressed(false);
                WatchlistWebServiceTask wlWebServiceTask = new WatchlistWebServiceTask(
                        currentActivity,
                        watchRecyclerView,
                        watchlistAdapter,
                        v);
                wlWebServiceTask.setPeriod(Constants.WATCHLIST_PERIOD_MONTHLY);
                wlWebServiceTask.execute();
                return true;
            }
        });

        btnYear.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                btnYear.setPressed(true);
                btnDay.setPressed(false);
                btnWeek.setPressed(false);
                btnMonth.setPressed(false);
                WatchlistWebServiceTask wlWebServiceTask = new WatchlistWebServiceTask(
                        currentActivity,
                        watchRecyclerView,
                        watchlistAdapter,
                        v);
                wlWebServiceTask.setPeriod(Constants.WATCHLIST_PERIOD_YEARLY);
                wlWebServiceTask.execute();
                return true;
            }
        });


        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
