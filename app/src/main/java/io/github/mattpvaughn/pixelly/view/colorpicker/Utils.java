package io.github.mattpvaughn.pixelly.view.colorpicker;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.util.Pair;

/**
 * A simple utility class containing assorted static utility methods used by the
 * application
 */
public class Utils {
    // Regexes to match #RGB, #RRGGBB, and #AARRGGBB hex strings
    private static final String HEX_PATTERN_RGB = "#[A-Fa-f0-9]{3}]";
    private static final String HEX_PATTERN_RRGGBB = "#[A-Fa-f0-9]{6}";
    private static final String HEX_PATTERN_AARRGGBB = "#[A-Fa-f0-9]{8}";

    // Bit mask to ignore alpha in ColorInt colors
    private static final int IGNORE_ALPHA_BITMASK = 0xFFFFFF;

    // String format template for a 6-digit hex code
    private static final String COLOR_STRING_FORMAT = "#%06X";

    /**
     * Converts a dp (density pixels) value to pixel value for a given device
     *
     * @param inputDP   - the input size, in density pixels
     * @param resources - a {@link Resources} object needed to access system resource
     *                  utilities
     * @return a value in pixels corresponding to the input dp value
     * @throws IllegalArgumentException if inputDP < 0
     */
    public static int dpToPixels(int inputDP, @NonNull Resources resources) {
        if (inputDP < 0) {
            throw new IllegalArgumentException("DP cannot be negative");
        }
        return Math.round(
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, inputDP,
                        resources.getDisplayMetrics()));
    }


    /**
     * Removes leading numbers in a string
     *
     * @param input - the input string provided, may or may not being with leading
     *              numbers
     * @return an input string which does not begin with a number
     */
    @NonNull
    public static String removeLeadingNumbers(@NonNull String input) {
        while (input.substring(0, 1).matches("[0-9]")) {
            input = input.substring(1);
        }
        return input;
    }


    /**
     * Display a message to the user in the current context
     *
     * @param context - the context in which we will display the error
     * @param title   - the title of the message
     * @param message - the content of the message
     */
    public static void showUserMessage(@NonNull Context context,
                                       @NonNull String title,
                                       @NonNull String message) {
        Log.e("Iconstructor error", message);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        builder.show();
    }


    /**
     * Get an array of resource IDs from an R.array.______ resource
     *
     * @param resources   - needed to access application resources
     * @param intArrayRes - a handle pointing to an integer array from xml, where
     *                    each entry is an application resource id
     * @return an array of application resource ids
     */
    @NonNull
    public static int[] getResourcesFromIntegerArray(@NonNull Resources resources,
                                                     @ArrayRes int intArrayRes) {
        TypedArray ta = resources.obtainTypedArray(intArrayRes);
        int[] ints = new int[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            ints[i] = ta.getResourceId(i, -1);
        }
        ta.recycle();
        return ints;
    }

    /**
     * Transform a value uniformly from within one range to another, staying in the
     * same relative position (percentage-wise) within the new range
     *
     * @param fromBounds - a pair of integers, the first being the minimum value of
     *                   original range, the second being the maximum. both
     *                   inclusive
     * @param toBounds   - a pair of integers, the first being the minimum value of
     *                   the goal range, the second the maximum. both inclusive
     * @param value      - a value within the bounds of fromRange
     * @return the new transformed value
     * @throws IllegalArgumentException if either of the bounds are null, or the
     *                                  values they contain arenull
     * @throws IllegalArgumentException if fromBounds.first > fromBounds.second or if
     *                                  toBounds.first > toBounds.second
     */
    public static int convertUniformlyBetweenRanges(
            Pair<Integer, Integer> fromBounds, Pair<Integer, Integer> toBounds,
            int value) {
        if (fromBounds == null || toBounds == null || fromBounds.first == null ||
            fromBounds.second == null || toBounds.first == null ||
            toBounds.second == null) {
            throw new IllegalArgumentException("Illegal null bounds passed");
        }
        if (fromBounds.first > fromBounds.second ||
            toBounds.first > toBounds.second) {
            throw new IllegalArgumentException(
                    "Invalid bounds: fromRange or toRange");
        }
        if (value < fromBounds.first || value > fromBounds.second) {
            throw new IllegalArgumentException("Value not within original bounds");
        }

        int fromRange = fromBounds.second - fromBounds.first;
        int toRange = toBounds.second - toBounds.first;
        float percentOfRange = ((float) value - fromBounds.first) / fromRange;
        return Math.round(percentOfRange * toRange + toBounds.first);
    }

    // convert hexadecimal string to color
    // --> make the string the form #AAAAAA, pass to Color.parseColor
    public static int parseColor(@NonNull String s) {
        if (!s.startsWith("#")) {
            s = "#" + s;
        }
        // Convert type #ABC to #AABBCC
        if (s.matches(HEX_PATTERN_RGB)) {
            s = "#" + s.charAt(1) + s.charAt(1) + s.charAt(2) + s.charAt(
                    2) + s.charAt(
                    3) + s.charAt(3);
        }
        return Color.parseColor(s);
    }

    // Check whether a string is a valid hex code in the form
    // #FFFFFF or #FFFFFFFF. The # is optional
    public static boolean isValidColor(@NonNull String s) {
        if (!s.startsWith("#")) {
            s = "#" + s;
        }
        return s.matches(HEX_PATTERN_RRGGBB) || s.matches(HEX_PATTERN_AARRGGBB);
    }

    // convert color into to string
    @NonNull
    public static String parseColorToString(int color) {
        return String.format(COLOR_STRING_FORMAT, (IGNORE_ALPHA_BITMASK & color));
    }

}