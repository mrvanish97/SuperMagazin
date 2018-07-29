package com.uonagent.supermagazin.utils

import android.annotation.SuppressLint
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView

@SuppressLint("RestrictedApi")
fun BottomNavigationView.disableShiftMode() {
    val menuView = this.getChildAt(0) as BottomNavigationMenuView
    try {
        val shiftingMode = menuView::class.java.getDeclaredField("mShiftingMode")
        shiftingMode.setAccessible(true)
        shiftingMode.setBoolean(menuView, false)
        shiftingMode.setAccessible(false)
        for (i in 0..(menuView.childCount - 1)) {
            val item = menuView.getChildAt(i) as BottomNavigationItemView
            item.setShiftingMode(false)
            // set once again checked value, so view will be updated
            item.setChecked(item.getItemData().isChecked())
        }
    } catch (e: NoSuchFieldException) {
    } catch (e: IllegalAccessException) {
    }
}