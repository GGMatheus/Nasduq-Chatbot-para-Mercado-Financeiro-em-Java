package com.amazonaws.youruserpools.Activities;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;

import com.amazonaws.youruserpools.Adapters.FragmentAdapter;
import com.amazonaws.youruserpools.CognitoYourUserPoolsDemo.R;
import com.amazonaws.youruserpools.Fragments.ChatFragment;
import com.amazonaws.youruserpools.Fragments.SettingsFragment;
import com.amazonaws.youruserpools.Fragments.WatchlistFragment;

/**
 * Created by Leandro on 3/9/2017.
 */

public class MainFragmentActivity
        extends FragmentActivity
        implements ChatFragment.OnFragmentInteractionListener,
                   SettingsFragment.OnFragmentInteractionListener,
                   WatchlistFragment.OnFragmentInteractionListener {
    static final int NUM_ITEMS = 3;

    private AlertDialog userDialog;

    FragmentAdapter mAdapter;
    ViewPager mPager;

    public boolean pressedKeyboardBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pager);

        mAdapter = new FragmentAdapter(getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPager.setCurrentItem(1, false);
    }

    private void showDialogMessage(String title, String body) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainFragmentActivity.this.setResult(RESULT_CANCELED);
                MainFragmentActivity.this.finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setTitle(title).setMessage(body);
        userDialog = builder.create();
        userDialog.show();
    }

    @Override
    public void onBackPressed() {
        if(!pressedKeyboardBack) {
            showDialogMessage("Are you sure?", "Do you want to leave Nasduq application?");
        }
        else {
            pressedKeyboardBack = false;
        }
    }

    public void onFragmentInteraction(Uri uri) {
    }
}
