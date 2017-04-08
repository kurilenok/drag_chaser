package org.arcoiris.dragchaser.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import java.util.Map;

/**
 * Created by kukolka on 3/25/2017.
 */

public class Utils {

    public static void snack(View view, String text) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
    }

    public static void toast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    public static String getKeyByValue(String value, Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (value.equalsIgnoreCase(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}
