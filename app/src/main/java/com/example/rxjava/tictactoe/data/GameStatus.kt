package com.example.rxjava.tictactoe.data

class GameStatus protected constructor(
    val winner: GameSymbol?,
    val winningPositionStart: GridPosition?,
    val winningPositionEnd: GridPosition?
) {
    val isEnded: Boolean
        get() = winner != null

    companion object {
        fun ended(
            winner: GameSymbol?,
            winningPositionStart: GridPosition?,
            winningPositionEnd: GridPosition?
        ): GameStatus {
            return GameStatus(winner, winningPositionStart, winningPositionEnd)
        }

        fun ongoing(): GameStatus {
            return GameStatus(null, null, null)
        }
    }
}