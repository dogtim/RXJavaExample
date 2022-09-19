package com.example.rxjava.tictactoe.data

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.BehaviorSubject

class GameViewModel(val activeGameStateObservable: Observable<GameState>,
                    val putActiveGameState: Consumer<GameState>?,
                    val touchEventObservable: Observable<GridPosition>?) {
    private val subscriptions = CompositeDisposable()

    private val gameStateSubject = BehaviorSubject.create<GameState>()

    private lateinit var playerInTurnObservable: Observable<GameSymbol>
    private lateinit var gameStatusObservable: Observable<GameStatus>

    init {
        playerInTurnObservable = activeGameStateObservable
            .map<Any>(GameState::lastPlayedSymbol)
            .map { symbol: Any ->
                if (symbol === GameSymbol.BLACK) {
                    return@map GameSymbol.RED
                } else {
                    return@map GameSymbol.BLACK
                }
            }
        gameStatusObservable = activeGameStateObservable
            .map(GameState::gameGrid)
            .map(GameUtils::calculateGameStatus)
    }

    fun getGameStatus(): Observable<GameStatus> {
        return gameStatusObservable
    }

    fun getFullGameState(): Observable<FullGameState?> {
        return Observable.combineLatest(gameStateSubject, gameStatusObservable, { a, b -> FullGameState(a, b) })
    }

    fun getPlayerInTurn(): Observable<GameSymbol> {
        return playerInTurnObservable
    }

    fun subscribe() {
        subscriptions.add(
            activeGameStateObservable
                .subscribe(Consumer { t: GameState ->
                    gameStateSubject.onNext(
                        t
                    )
                })
        )
        subscriptions.add(
            GameUtils.processGamesMoves(
                activeGameStateObservable,
                gameStatusObservable,
                playerInTurnObservable,
                touchEventObservable!!
            ).subscribe(putActiveGameState)
        )
    }

    fun unsubscribe() {
        subscriptions.clear()
    }
}