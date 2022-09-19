package com.example.rxjava.tictactoe.data

import java.util.*

class GameGrid() {
    var width: Int = 0
    var height: Int = 0
    private var grid: Array<Array<GameSymbol?>>? = null

    constructor(width: Int, height: Int) : this() {
        this.width = width
        this.height = height
        grid = Array<Array<GameSymbol?>>(width) { arrayOfNulls<GameSymbol?>(height) }
        for (i in 0 until width) {
            for (n in 0 until height) {
                grid!![i][n] = GameSymbol.EMPTY
            }
        }
    }

    constructor(width: Int, height: Int, grid: Array<Array<GameSymbol?>>) : this() {
        this.width = width
        this.height = height
        this.grid = grid
    }

    private fun copy(): GameGrid {
        val grid: Array<Array<GameSymbol?>> = Array<Array<GameSymbol?>>(width) {
            arrayOfNulls<GameSymbol>(
                height
            )
        }
        for (i in 0 until width) {
            System.arraycopy(this.grid!![i], 0, grid[i], 0, height)
        }
        return GameGrid(width, height, grid)
    }

    fun getSymbolAt(gridPosition: GridPosition): GameSymbol? {
        return getSymbolAt(gridPosition.x, gridPosition.y)
    }

    fun getSymbolAt(x: Int, y: Int): GameSymbol? {
        return grid?.get(x)?.get(y)
    }

    fun setSymbolAt(position: GridPosition, symbol: GameSymbol?): GameGrid {
        val copy = copy()
        copy.grid!!.get(position.x)[position.y] = symbol
        return copy
    }

    override fun equals(obj: Any?): Boolean {
        return obj is GameGrid && obj.width == width && obj.height == height &&
                contentEquals(obj.grid!!)
    }

    private fun contentEquals(grid: Array<Array<GameSymbol?>>): Boolean {
        if (this.grid!!.size != grid.size) {
            return false
        }
        for (i in grid.indices) {
            if (!Arrays.equals(this.grid!![i], grid[i])) {
                return false
            }
        }
        return true
    }
}