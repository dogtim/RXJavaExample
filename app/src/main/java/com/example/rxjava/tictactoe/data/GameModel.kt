package com.example.rxjava.tictactoe.data

import android.content.Context
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class GameModel(context: Context) {
    private val activeGameState = BehaviorSubject.createDefault(EMPTY_GAME)
    private val persistedGameStore: PersistedGameStore
    fun newGame() {
        activeGameState.onNext(EMPTY_GAME)
    }

    fun putActiveGameState(value: GameState) {
        activeGameState.onNext(value)
    }

    fun getActiveGameState(): Observable<GameState> {
        return activeGameState.hide()
    }

    val savedGamesStream: Observable<List<SavedGame>>
        get() = persistedGameStore.savedGamesStream

    fun saveActiveGame(): Observable<Void> {
        return persistedGameStore.put(activeGameState.value!!)
    }

    companion object {
        private const val SAVED_GAMES_FILE_NAME = "saved_games"
        private const val GRID_WIDTH = 7
        private const val GRID_HEIGHT = 7
        private val EMPTY_GRID = GameGrid(GRID_WIDTH, GRID_HEIGHT)
        private val EMPTY_GAME = GameState(EMPTY_GRID, GameSymbol.EMPTY)
    }

    init {
        val savedGames = context.getSharedPreferences(SAVED_GAMES_FILE_NAME, 0)
        persistedGameStore = PersistedGameStore(savedGames)
    }
}