package com.fortie40.movieapp.helperclasses

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.StaggeredGridLayoutManager

@Suppress("unused")
class MovieListStaggeredGridLayoutManager : StaggeredGridLayoutManager {
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int):
            super(context, attrs, defStyleAttr, defStyleRes)

    constructor(spanCount: Int, orientation: Int): super(spanCount, orientation)

    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }
}