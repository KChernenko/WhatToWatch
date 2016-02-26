package me.bitfrom.whattowatch.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

import me.bitfrom.whattowatch.R;

public class MessageHandlerUtility {

    public static void showMessage(View view, String message, int snackBarLength) {
        switch (snackBarLength) {
            case Snackbar.LENGTH_LONG: {
                final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
                snackbar.setAction(R.string.snackbar_dismiss, v -> {
                    snackbar.dismiss();
                });
                snackbar.show();
                break;
            }
            case Snackbar.LENGTH_SHORT: {
                final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
                snackbar.setAction(R.string.snackbar_dismiss, v -> {
                    snackbar.dismiss();
                });
                snackbar.show();
                break;
            }
            default:
                break;
        }
    }
}
