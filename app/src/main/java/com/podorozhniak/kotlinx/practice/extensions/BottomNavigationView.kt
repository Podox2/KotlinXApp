package com.podorozhniak.kotlinx.practice.extensions

import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import com.google.android.material.bottomnavigation.BottomNavigationView

fun BottomNavigationView.disableTooltip() {
    val content: View = getChildAt(0)
    if (content is ViewGroup) {
        content.forEach {
            it.setOnLongClickListener {
                return@setOnLongClickListener true
            }
            // disable vibration also
            it.isHapticFeedbackEnabled = false
        }
    }
}

/*default values for bottom nav view
<dimen name="design_bottom_navigation_active_item_max_width">168dp</dimen>
<dimen name="design_bottom_navigation_active_item_min_width">96dp</dimen>
<dimen name="design_bottom_navigation_active_text_size">14sp</dimen>
<dimen name="design_bottom_navigation_elevation">8dp</dimen>
<dimen name="design_bottom_navigation_height">56dp</dimen>
<dimen name="design_bottom_navigation_icon_size">24dp</dimen>
<dimen name="design_bottom_navigation_item_max_width">96dp</dimen>
<dimen name="design_bottom_navigation_item_min_width">56dp</dimen>
<dimen name="design_bottom_navigation_margin">8dp</dimen>
<dimen name="design_bottom_navigation_shadow_height">1dp</dimen>
<dimen name="design_bottom_navigation_text_size">12sp</dimen>
<color name="design_bottom_navigation_shadow_color">#14000000</color>*/