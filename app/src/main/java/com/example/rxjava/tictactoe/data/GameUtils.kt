package com.example.rxjava.tictactoe.data

import io.reactivex.Observable

object GameUtils {
    fun calculateGameStatus(gameGrid: GameGrid): GameStatus {
        val WIDTH: Int = gameGrid.width
        val HEIGHT: Int = gameGrid.height
        for (r in 0 until WIDTH) {
            for (c in 0 until HEIGHT) {
                val player = gameGrid.getSymbolAt(r, c)
                if (player === GameSymbol.EMPTY) continue
                if (c + 3 < WIDTH && player === gameGrid.getSymbolAt(
                        r,
                        c + 1
                    ) && player === gameGrid.getSymbolAt(
                        r,
                        c + 2
                    ) && player === gameGrid.getSymbolAt(r, c + 3)
                ) return GameStatus.ended(
                    player,
                    GridPosition(r, c),
                    GridPosition(r, c + 3)
                )
                if (r + 3 < HEIGHT) {
                    if (player === gameGrid.getSymbolAt(
                            r + 1,
                            c
                        ) && player === gameGrid.getSymbolAt(
                            r + 2,
                            c
                        ) && player === gameGrid.getSymbolAt(r + 3, c)
                    ) return GameStatus.ended(
                        player,
                        GridPosition(r, c),
                        GridPosition(r + 3, c)
                    )
                    if (c + 3 < WIDTH && player === gameGrid.getSymbolAt(
                            r + 1,
                            c + 1
                        ) && player === gameGrid.getSymbolAt(
                            r + 2,
                            c + 2
                        ) && player === gameGrid.getSymbolAt(r + 3, c + 3)
                    ) return GameStatus.ended(
                        player,
                        GridPosition(r, c),
                        GridPosition(r + 3, c + 3)
                    )
                    if (c - 3 >= 0 && player === gameGrid.getSymbolAt(
                            r + 1,
                            c - 1
                        ) && player === gameGrid.getSymbolAt(
                            r + 2,
                            c - 2
                        ) && player === gameGrid.getSymbolAt(r + 3, c - 3)
                    ) return GameStatus.ended(
                        player,
                        GridPosition(r, c),
                        GridPosition(r + 3, c - 3)
                    )
                }
            }
        }
        return GameStatus.ongoing()
    }

    fun processGamesMoves(
        gameStateObservable: Observable<GameState>,
        gameStatusObservable: Observable<GameStatus>,
        playerInTurnObservable: Observable<GameSymbol>,
        touchEventObservable: Observable<GridPosition>
    ): Observable<GameState> {
        val gameInfoObservable: Observable<Pair<GameState, GameSymbol>> =
            Observable.combineLatest(
                gameStateObservable,
                playerInTurnObservable) { a, b -> Pair(a, b) }
        val gameNotEndedTouches: Observable<GridPosition> = touchEventObservable
            .withLatestFrom(gameStatusObservable) { a, b -> Pair(a, b) }
            .filter { pair -> !pair.second.isEnded }
            .map { pair -> pair.first }
        val filteredTouches: Observable<GridPosition> = gameNotEndedTouches
            .withLatestFrom(gameStateObservable) { a, b -> Pair(a, b) }
            .map { pair -> dropMarker(pair.first, pair.second.gameGrid) }
            .filter { position -> position.y >= 0 }

        return filteredTouches
            .withLatestFrom(gameInfoObservable) { gridPosition, gameInfo ->
                    gameInfo.first.setSymbolAt(
                        gridPosition, gameInfo.second
                    )
                }
    }

    private fun dropMarker(gridPosition: GridPosition, gameGrid: GameGrid): GridPosition {
        var i: Int = gameGrid.height - 1
        while (i >= -1) {
            if (i == -1) {
                // Let -1 fall through
                break
            }
            val symbol = gameGrid.getSymbolAt(
                gridPosition.x, i
            )
            if (symbol === GameSymbol.EMPTY) {
                break
            }
            i--
        }
        return GridPosition(gridPosition.x, i)
    }
}