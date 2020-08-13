package com.fortie40.movieapp.layoutmanagers

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fortie40.movieapp.NUMBER_OF_COLUMNS
import com.fortie40.movieapp.R

class ItemDecorationMovieColumn(private val context: Context): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val lp =
            view.layoutParams as StaggeredGridLayoutManager.LayoutParams
        val spanIndex = lp.spanIndex

        val densityPixels12 = context.resources.getDimensionPixelOffset(R.dimen.densityPixels12)
        val densityPixels16 = context.resources.getDimensionPixelOffset(R.dimen.densityPixels16)

        if (position < NUMBER_OF_COLUMNS)
            outRect.top = densityPixels16
        else
            outRect.top = densityPixels12

        when (spanIndex) {
            0 -> {
                outRect.left = densityPixels16
                outRect.right = densityPixels12
            }
            NUMBER_OF_COLUMNS - 1 -> outRect.right = densityPixels16
        }
    }
}