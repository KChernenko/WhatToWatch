package me.bitfrom.whattowatch.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.preference.DialogPreference;
import android.util.AttributeSet;

import me.bitfrom.whattowatch.R;

public class AboutDialog extends DialogPreference {

    public AboutDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.about_dialog);
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        if (Build.VERSION.SDK_INT >= 21) {
            builder.setTitle(R.string.about_dialog_freespace_fix);
        } else {
            builder.setTitle(R.string.about_dialog_title);
        }

        builder.setNegativeButton(null, null);
        super.onPrepareDialogBuilder(builder);
    }
}
