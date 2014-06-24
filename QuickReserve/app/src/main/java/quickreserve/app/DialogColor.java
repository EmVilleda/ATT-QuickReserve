package quickreserve.app;

import android.app.AlertDialog;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

/**
 * Created by at892q on 6/19/2014.
 */
public class DialogColor {
    public DialogColor(){}

    public static void dialogColor(AlertDialog dialog) {
        try {
            Resources resources = dialog.getContext().getResources();
            int color = resources.getColor(R.color.orange); // your color here


            int alertTitleId = resources.getIdentifier("alertTitle", "id", "android");
            TextView alertTitle = (TextView) dialog.getWindow().getDecorView().findViewById(alertTitleId);
            alertTitle.setTextColor(color); // change title text color

            int titleDividerId = resources.getIdentifier("titleDivider", "id", "android");
            View titleDivider = dialog.getWindow().getDecorView().findViewById(titleDividerId);
            titleDivider.setBackgroundColor(color); // change divider color
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
