package com.example.rxjava.tictactoe.data

class GameState(val gameGrid: GameGrid, val lastPlayedSymbol: GameSymbol) {

    fun setSymbolAt(gridPosition: GridPosition, symbol: GameSymbol): GameState {
        return GameState(
            gameGrid.setSymbolAt(gridPosition, symbol), symbol
        )
    }

    fun isEmpty(gridPosition: GridPosition): Boolean {
        return gameGrid.getSymbolAt(gridPosition) === GameSymbol.EMPTY
    }

    override fun equals(obj: Any?): Boolean {
        return obj is GameState &&
                obj.gameGrid.equals(gameGrid) &&
                obj.lastPlayedSymbol.equals(lastPlayedSymbol)
    }

}