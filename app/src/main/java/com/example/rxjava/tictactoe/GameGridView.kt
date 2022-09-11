package com.example.rxjava.tictactoe

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.example.rxjava.R
import com.example.rxjava.tictactoe.data.FullGameState
import com.example.rxjava.tictactoe.data.GameGrid
import com.example.rxjava.tictactoe.data.GameSymbol
import com.example.rxjava.tictactoe.data.GridPosition

open class GameGridView : View {

    constructor(context: Context): this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        linePaint.setColor(Color.BLACK)
        linePaint.setStrokeWidth(8f)
        winnerLinePaint.setColor(Color.BLACK)
        winnerLinePaint.setStrokeWidth(30f)
        bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        bitmapSrcRect = Rect(0, 0, blackPlayerBitmap.getWidth(), blackPlayerBitmap.getHeight())
    }

    val TAG = GameGridView::class.java.simpleName
    var data: FullGameState? = null
        set(value) {
            field = value
            invalidate()
        }
    var _width = 0
    var _height = 0
    var linePaint: Paint = Paint()
    var winnerLinePaint: Paint = Paint()
    var bitmapPaint: Paint = Paint()
    var blackPlayerBitmap: Bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.symbol_black_circle)
    var redPlayerBitmap: Bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.symbol_red_circle)
    var bitmapSrcRect: Rect? = null

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        _width = right - left
        _height = bottom - top
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)
        Log.d(TAG, "width: $_width, height: $_height")
        val gridWidth: Float = getGridWidth().toFloat()
        val gridHeight: Float = getGridHeight().toFloat()
        val tileWidth = _width / gridWidth
        val tileHeight = _height / gridHeight
        drawSymbols(canvas, gridWidth, gridHeight, tileWidth, tileHeight)
        drawGridLines(canvas, gridWidth, gridHeight, tileWidth, tileHeight)

        data?.let {
            if (it.gameStatus.isEnded) {
                drawWinner(
                    canvas, tileWidth, tileHeight,
                    it.gameStatus.winningPositionStart!!,
                    it.gameStatus.winningPositionEnd!!
                )
            }
        }
    }

    open fun drawSymbols(
        canvas: Canvas,
        gridWidth: Float, gridHeight: Float,
        tileWidth: Float, tileHeight: Float
    ) {
        if (data == null) {
            return
        }
        val gameGrid: GameGrid = data!!.gameState.gameGrid
        var i = 0
        while (i < gridWidth) {
            var n = 0
            while (n < gridHeight) {

                val symbol: GameSymbol = gameGrid.getSymbolAt(i, n)!!
                val dst = RectF(
                    i * tileWidth, n * tileHeight,
                    (i + 1) * tileWidth, (n + 1) * tileHeight
                )
                if (symbol === GameSymbol.BLACK) {
                    canvas.drawBitmap(
                        blackPlayerBitmap!!,
                        bitmapSrcRect, dst,
                        bitmapPaint
                    )
                } else if (symbol === GameSymbol.RED) {
                    canvas.drawBitmap(
                        redPlayerBitmap!!,
                        bitmapSrcRect, dst,
                        bitmapPaint
                    )
                }
                n++
            }
            i++
        }
    }

    open fun drawGridLines(
        canvas: Canvas,
        gridWidth: Float, gridHeight: Float,
        tileWidth: Float, tileHeight: Float
    ) {
        var i = 0
        while (i <= gridWidth) {
            Log.d(TAG, "line $i")
            canvas.drawLine(i * tileWidth, 0f, i * tileWidth, _height.toFloat(), linePaint!!)
            i++
        }
        var n = 0
        while (n <= gridHeight) {
            canvas.drawLine(0f, n * tileHeight, _width.toFloat(), n * tileHeight, linePaint!!)
            n++
        }
    }

    open fun drawWinner(
        canvas: Canvas,
        tileWidth: Float, tileHeight: Float,
        start: GridPosition, end: GridPosition
    ) {
        canvas.drawLine(
            start.x * tileWidth + tileWidth / 2,
            start.y * tileHeight + tileHeight / 2,
            end.x * tileWidth + tileWidth / 2,
            end.y * tileHeight + tileHeight / 2,
            winnerLinePaint
        )
    }

    fun getGridWidth(): Int {
        return data?.gameState?.gameGrid?.width ?: 0
    }

    fun getGridHeight(): Int {
        return data?.gameState?.gameGrid?.height ?: 0
    }
}