package com.fortie40.movieapp.helperclasses

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlin.math.abs

class OnlyVerticalSwipeRefreshLayout(context: Context, attrs: AttributeSet): SwipeRefreshLayout(
    context,
    attrs) {
    private val touchSlop: Int = ViewConfiguration.get(context).scaledTouchSlop
    private var prevX = 0f
    private var declined = false
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        when(ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                prevX = ev.x
                declined = false
            }
            MotionEvent.ACTION_MOVE -> {
                val eventX = ev.x
                val xDiff = abs(eventX - prevX)
                if (declined || xDiff > touchSlop) {
                    declined = true
                    return false
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }
}