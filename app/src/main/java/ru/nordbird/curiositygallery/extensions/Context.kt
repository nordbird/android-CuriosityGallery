package ru.nordbird.curiositygallery.extensions

import android.content.Context

fun Context.dpToPx(dp: Int): Float {
    return dp.toFloat() * resources.displayMetrics.density
}

fun Context.pxToDp(px: Float): Int {
    return (px / resources.displayMetrics.density).toInt()
}