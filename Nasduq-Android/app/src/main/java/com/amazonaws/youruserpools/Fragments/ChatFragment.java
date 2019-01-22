package com.amazonaws.youruserpools.Fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amazonaws.youruserpools.Adapters.ChatMessageAdapter;
import com.amazonaws.youruserpools.Adapters.ChatOptionsAdapter;
import com.amazonaws.youruserpools.CognitoYourUserPoolsDemo.R;
import com.amazonaws.youruserpools.Data.ChatMessage;
import com.amazonaws.youruserpools.Data.ChatOption;
import com.amazonaws.youruserpools.Webservices.ChatMessageWebServiceTask;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ChatFragment extends Fragment {

    public RecyclerView chatRecyclerView;
    public ChatOptionsAdapter chatOptionsAdapter;
    public ChatMessageAdapter chatMessageAdapter;
    public List<ChatMessage> chatMessages;
    public ArrayList<ChatOption> chatOptions = new ArrayList();


    private OnFragmentInteractionListener mListener;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // inflating fragment's layout
        View v =  inflater.inflate(R.layout.fragment_chat, container, false);
        chatMessages = new ArrayList<>();

        // configuring and initializing chatMessageAdapter first
        chatMessageAdapter = new ChatMessageAdapter(this, chatMessages);
        LinearLayoutManager lm = new CustomLayoutManager(getActivity());
        chatRecyclerView = (RecyclerView) v.findViewById(R.id.list_msg);
        chatRecyclerView.setLayoutManager(lm);
        chatRecyclerView.setAdapter(chatMessageAdapter);

        // configuring and initializing chatOptionsAdapter
        chatOptionsAdapter = new ChatOptionsAdapter(chatOptions, getActivity(), chatRecyclerView);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true);
//        RecyclerView optionsRecyclerView = (RecyclerView) v.findViewById(R.id.user_chat_recyclerview);
//        optionsRecyclerView.setLayoutManager(layoutManager);
//        optionsRecyclerView.setAdapter(chatOptionsAdapter);
//        optionsRecyclerView.addItemDecoration(new SpacesItemDecoration(10));

        // lastly, sending init message to server
        ChatMessageWebServiceTask initMessageTask = new ChatMessageWebServiceTask(
                getActivity(),
                "init",
                new ChatOption(null, null, null, null, null),
                chatRecyclerView,
                chatOptionsAdapter
        );
        initMessageTask.execute();

        return v;
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

    public class CustomLayoutManager extends LinearLayoutManager {
        private static final float MILLISECONDS_PER_INCH = 100f;
        private Context mContext;

        public CustomLayoutManager(Context context) {
            super(context);
            mContext = context;
        }

        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView,
                                           RecyclerView.State state, final int position) {

            LinearSmoothScroller smoothScroller =
                    new LinearSmoothScroller(mContext) {

                        //This controls the direction in which smoothScroll looks
                        //for your view
                        @Override
                        public PointF computeScrollVectorForPosition
                        (int targetPosition) {
                            return CustomLayoutManager.this
                                    .computeScrollVectorForPosition(targetPosition);
                        }

                        //This returns the milliseconds it takes to
                        //scroll one pixel.
                        @Override
                        protected float calculateSpeedPerPixel
                        (DisplayMetrics displayMetrics) {
                            return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
                        }
                    };

            smoothScroller.setTargetPosition(position);
            startSmoothScroll(smoothScroller);
        }
    }
}
