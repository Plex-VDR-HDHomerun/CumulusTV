package com.felkertech.cumulustv.tv;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;

/**
 * A collection of utility methods, all static.
 */
public class Utils {

    /*
     * Making sure public utility methods remain static
     */
    private Utils() {
    }

    private static final SimpleDateFormat ISO_8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ",
            Locale.US);

    /**
     * Returns the screen/display size
     */
    public static Point getDisplaySize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static int convertDpToPixel(Context ctx, int dp) {
        float density = ctx.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    /**
     * Converts time in milliseconds to a ISO 8061 string.
     */
    public static String toIsoDateTimeString(long timeMillis) {
        return ISO_8601.format(new Date(timeMillis));
    }

    public static String normalizeGenre(String genreName) {
        if (genreName == null) {
            return "";
        }
        genreName = genreName.replaceAll("_", " / ");
        genreName = genreName.toLowerCase();
        genreName = genreName.replaceAll(",", ", ");
        for (int i = 0; i < genreName.length(); i++) {
            if (i == 0 || genreName.charAt(i - 1) == ' ') {
                genreName = genreName.substring(0, i) +
                        genreName.substring(i, i + 1).toUpperCase() +
                        genreName.substring(i + 1);
            }
        }
        return genreName;
    }
}
