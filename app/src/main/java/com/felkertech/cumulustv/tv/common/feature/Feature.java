package com.felkertech.cumulustv.tv.common.feature;

import android.content.Context;

/**
 * A feature is elements of code that are turned off for most users until a feature is fully
 * launched.
 *
 * <p>Expected usage is:
 * <pre>{@code
 *     if (MY_FEATURE.isEnabled(context) {
 *         showNewCoolUi();
 *     } else{
 *         showOldBoringUi();
 *     }
 * }</pre>
 */
public interface Feature {
    boolean isEnabled(Context context);


}
