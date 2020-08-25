package com.fortie40.movieapp.helperclasses

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fortie40.movieapp.R

class ItemDecorationMovieListColumns(private val context: Context, private val spanCount: Int)
    : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val lp =
            view.layoutParams as StaggeredGridLayoutManager.LayoutParams
        val spanIndex = lp.spanIndex

        val densityPixels6 = context.resources.getDimensionPixelOffset(R.dimen.densityPixels6)
        val densityPixels12 = context.resources.getDimensionPixelOffset(R.dimen.densityPixels12)
        val densityPixels16 = context.resources.getDimensionPixelOffset(R.dimen.densityPixels16)

        if (position < spanCount)
            outRect.top = densityPixels16
        else
            outRect.top = densityPixels12

        when (spanIndex) {
            0 -> {
                outRect.left = densityPixels16
                outRect.right = densityPixels6
            }
            spanCount - 1 -> {
                outRect.right = densityPixels16
                outRect.left = densityPixels6
            }
            else -> {
                outRect.right = densityPixels6
                outRect.left = densityPixels6
            }
        }
    }
}