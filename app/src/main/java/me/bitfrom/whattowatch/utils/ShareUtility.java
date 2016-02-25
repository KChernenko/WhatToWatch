package me.bitfrom.whattowatch.utils;

import android.app.Activity;
import android.content.Intent;

import com.cocosw.bottomsheet.BottomSheet;
import com.cocosw.bottomsheet.BottomSheetHelper;


public class ShareUtility {

    /**
     * Returns bottom sheet builder object, that provides lollipop's like share action via bottom sheet.
     * @param activity activity instance
     * @param text text you want to share
     * @return shareAction bottom sheet
     **/
    public static BottomSheet.Builder getShareActions(Activity activity, String text) {
        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);

        return BottomSheetHelper.shareAction(activity, shareIntent);
    }
}
