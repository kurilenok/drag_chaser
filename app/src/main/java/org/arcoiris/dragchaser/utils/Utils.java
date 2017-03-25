package org.arcoiris.dragchaser.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by kukolka on 3/25/2017.
 */

public class Utils {

    public static void showSnackbar(View view, String text) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
    }
}
