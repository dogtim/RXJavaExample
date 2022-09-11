package com.example.rxjava.tictactoe

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.example.rxjava.tictactoe.data.GridPosition
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function

class InteractiveGameGridView: GameGridView {
    constructor(context: Context): this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr)
    companion object {
        private fun getGridPosition(
            touchX: Float, touchY: Float,
            viewWidthPixels: Int, viewHeightPixels: Int,
            gridWidth: Int, gridHeight: Int
        ): GridPosition {
            val rx = touchX / (viewWidthPixels + 1).toFloat()
            val i = (rx * gridWidth).toInt()
            val ry = touchY / (viewHeightPixels + 1).toFloat()
            val n = (ry * gridHeight).toInt()
            return GridPosition(i, n)
        }
    }
}