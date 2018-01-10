package net.vrgsoft.timelineviewkotlin

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.util.DisplayMetrics
import android.view.View

object Utils {
    fun getColumnWidth(context: Context?): Int {
        when (context?.resources?.displayMetrics?.densityDpi) {
            DisplayMetrics.DENSITY_LOW -> return 250
            DisplayMetrics.DENSITY_MEDIUM -> return 250
            DisplayMetrics.DENSITY_HIGH -> return 250
            DisplayMetrics.DENSITY_XHIGH -> return 250
            DisplayMetrics.DENSITY_XXHIGH -> return 350
            DisplayMetrics.DENSITY_XXXHIGH -> return 520
            else -> return 350
        }
    }

    fun getXInWindow(v: View): Int {
        val coords = IntArray(2)
        v.getLocationInWindow(coords)
        return coords[0]
    }

    fun getXInWindow(activity: Activity): Int {
        val display = activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.x

    }
}
