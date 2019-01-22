package com.amazonaws.youruserpools.Utils;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.amazonaws.youruserpools.Activities.MainFragmentActivity;
import com.amazonaws.youruserpools.CognitoYourUserPoolsDemo.R;

/**
 * Created by Leandro on 3/15/2017.
 */

public class BackPressEditText extends AppCompatEditText {

    Context context;
    private MainFragmentActivity activity;

    public BackPressEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setActivity(MainFragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // User has pressed Back key. So hide the keyboard
            InputMethodManager mgr = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(this.getWindowToken(), 0);
            // TODO: Hide your view as you do it in your activity

            final RelativeLayout typeTextLayout = (RelativeLayout) activity.findViewById(R.id.type_text_layout);
            typeTextLayout.setVisibility(View.GONE);

            activity.pressedKeyboardBack = true;
        }
        return false;
    }

}
