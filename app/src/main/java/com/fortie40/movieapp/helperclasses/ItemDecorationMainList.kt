package com.fortie40.movieapp.helperclasses

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.fortie40.movieapp.R

class ItemDecorationMainList(private val context: Context) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val densityPixels6 = context.resources.getDimensionPixelOffset(R.dimen.densityPixels6)

        if (position != 0) {
            outRect.top = densityPixels6
            outRect.bottom = densityPixels6
        }
    }
}