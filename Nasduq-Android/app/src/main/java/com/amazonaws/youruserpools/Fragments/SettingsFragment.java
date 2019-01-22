package com.amazonaws.youruserpools.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.UpdateAttributesHandler;
import com.amazonaws.youruserpools.Activities.ChangePasswordActivity;
import com.amazonaws.youruserpools.Activities.SignUpActivity;
import com.amazonaws.youruserpools.AppHelper;
import com.amazonaws.youruserpools.CognitoYourUserPoolsDemo.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private final String TAG="SettingsFragment";
    private String mParam1;
    private String mParam2;
    private String username;
    private CognitoUser user;
    private ProgressDialog waitDialog;
    private AlertDialog userDialog;
    private CognitoUserSession session;
    private CognitoUserDetails details;

    private OnFragmentInteractionListener mListener;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
    public void onResume() {
        super.onResume();
        getDetails();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Inflate the layout for this fragment

        final View v = inflater.inflate(R.layout.fragment_settings, container, false);

        init();

       final Button button = (Button) v.findViewById(R.id.buttonSignOut);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        Button btnName = (Button) v.findViewById(R.id.button_edit_name);
        Button btnEmail = (Button) v.findViewById(R.id.button_edit_email);
        Button btnChangePassword = (Button) v.findViewById(R.id.button_change_password);
        Button btnVerifyEmail = (Button) v.findViewById(R.id.button_verify_email);
        Button btnTalkToAHuman = (Button) v.findViewById(R.id.talk_human_button);

        final Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);

        btnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                final EditText editText = new EditText(getContext());
                alert.setMessage("Set a new profile name");
                alert.setTitle("Edit profile name");

                editText.setText(details.getAttributes().getAttributes().get("given_name"));

                alert.setView(editText);

                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        updateAttribute("given_name", editText.getText().toString());
                        getDetails();
                        dialogInterface.dismiss();
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alert.show();
            }
        });

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder aware = new AlertDialog.Builder(getActivity());
                final EditText edittext = new EditText(getContext());
                aware.setMessage("set a new e-mail address");
                aware.setTitle("Edit e-mail address");

                edittext.setText(details.getAttributes().getAttributes().get("email"));

                aware.setView(edittext);

                aware.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        updateAttribute("email", edittext.getText().toString());
                        getDetails();
                        dialogInterface.dismiss();
                    }
                });

                aware.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                aware.show();
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });

        btnVerifyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                final EditText editText = new EditText(getContext());
                alert.setMessage("Please enter the verification code sent to your email.");
                alert.setTitle("Verify email address");

                alert.setView(editText);

                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AppHelper.getPool().getUser(AppHelper.getCurrUser()).verifyAttributeInBackground("email", editText.getText().toString(), verHandler);
                        getDetails();
                        dialogInterface.dismiss();
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alert.show();
            }
        });

        btnTalkToAHuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FragmentManager fragmentManager = getFragmentManager();
//                ChatFragment chatFragment = (ChatFragment) fragmentManager.findFragmentByTag(ChatFragment.TAG);
            }
        });

        return v;
    }

    GenericHandler verHandler = new GenericHandler() {
        @Override
        public void onSuccess() {
            // Refresh the screen
            getDetails();
        }

        @Override
        public void onFailure(Exception exception) {
            // Show error
            closeWaitDialog();
            showDialogMessage("Verification failed", AppHelper.formatException(exception), false);
        }
    };


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

    private void signOut() {
        user.signOut();
        exit();
    }

    private void init() {
        // Get the user name
        Bundle extras = getActivity().getIntent().getExtras();
        username = AppHelper.getCurrUser();
        user = AppHelper.getPool().getUser(username);
        getDetails();
    }

    GetDetailsHandler detailsHandler = new GetDetailsHandler() {
        @Override
        public void onSuccess(CognitoUserDetails cognitoUserDetails) {
            closeWaitDialog();
            // Store details in the AppHandler
            AppHelper.setUserDetails(cognitoUserDetails);
            details = cognitoUserDetails;

            TextView name_label = (TextView) getActivity().findViewById(R.id.settings_name_placeholder);
            TextView email_label = (TextView) getActivity().findViewById(R.id.settings_email_placeholder);
            Button verify_email = (Button) getActivity().findViewById(R.id.button_verify_email);
            TextView verify_message = (TextView) getActivity().findViewById(R.id.verify_message);

            String email_data = cognitoUserDetails.getAttributes().getAttributes().get("email");
            String name_data =  cognitoUserDetails.getAttributes().getAttributes().get("given_name");

            boolean email_verified = Boolean.parseBoolean(cognitoUserDetails.getAttributes().getAttributes().get("email_verified"));

            try {
                if (name_data != null) {
                    name_label.setText(name_data);
                }

                if (email_data != null) {
                    email_label.setText(email_data);
                }

                if (!email_verified){
                    email_label.setBackgroundColor(Color.argb(255, 255, 150, 150));
                    verify_email.setVisibility(View.VISIBLE);
                    verify_message.setVisibility(View.VISIBLE);
                }
                else {
                    email_label.setBackgroundColor(Color.argb(255, 0xEE, 0xEE, 0xEE));
                    verify_email.setVisibility(View.GONE);
                    verify_message.setVisibility(View.GONE);
                }
            } catch (NullPointerException e) {
                Log.w("[NullPointerException]", "caught NullPointerException, probably due to view not existing any more. No big deal.");
                e.printStackTrace();
            }
            // Trusted devices?
        }

        @Override
        public void onFailure(Exception exception) {
            closeWaitDialog();
            showDialogMessage("Could not fetch user details!", AppHelper.formatException(exception), true);
        }
    };

    private void getDetails() {
        AppHelper.getPool().getUser(username).getDetailsInBackground(detailsHandler);
    }

    private void exit () {
        Intent intent = new Intent();
        if(username == null)
            username = "";
        intent.putExtra("name",username);
        getActivity().setResult(SignUpActivity.RESULT_SIGNED_OUT, intent);
        getActivity().finish();
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

    UpdateAttributesHandler updateHandler = new UpdateAttributesHandler() {
        @Override
        public void onSuccess(List<CognitoUserCodeDeliveryDetails> attributesVerificationList) {
            // Update successful
            if(attributesVerificationList.size() > 0) {
                showDialogMessage("Updated", "The updated attribute has to be verified",  false);
            }
            getDetails();
        }

        @Override
        public void onFailure(Exception exception) {
            // Update failed
            closeWaitDialog();
            showDialogMessage("Update failed", AppHelper.formatException(exception), false);
        }
    };



    private void updateAttribute(String attributeType, String attributeValue) {

        if(attributeType == null || attributeType.length() < 1) {
            return;
        }
        CognitoUserAttributes updatedUserAttributes = new CognitoUserAttributes();
        updatedUserAttributes.addAttribute(attributeType, attributeValue);
        Toast.makeText(getActivity().getApplicationContext(), attributeType + ": " + attributeValue, Toast.LENGTH_LONG);
        AppHelper.getPool().getUser(AppHelper.getCurrUser()).updateAttributesInBackground(updatedUserAttributes, updateHandler);
    }

    private void showDialogMessage(String title, String body, final boolean exit) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title).setMessage(body).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    userDialog.dismiss();
                    if(exit) {
                        exit();
                    }
                } catch (Exception e) {
                    // Log failure
                    Log.e(TAG,"Dialog dismiss failed");
                    if(exit) {
                        exit();
                    }
                }
            }
        });
        userDialog = builder.create();
        userDialog.show();
    }

    private void closeWaitDialog() {
        try {
            waitDialog.dismiss();
        }
        catch (Exception e) {
            //
        }
    }
}
