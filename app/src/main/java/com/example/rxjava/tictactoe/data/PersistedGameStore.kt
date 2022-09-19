package com.example.rxjava.tictactoe.data

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.*

class PersistedGameStore(private val sharedPreferences: SharedPreferences) {
    private val gson: Gson = Gson()
    private var savedGames: MutableList<SavedGame> = ArrayList<SavedGame>()
    private val savedGamesSubject: PublishSubject<List<SavedGame>> =
        PublishSubject.create()
    val savedGamesStream: Observable<List<SavedGame>>
        get() = savedGamesSubject.hide().startWith(savedGames)

    fun put(gameState: GameState): Observable<Void> {
        val timestamp = Date().time
        val savedGame = SavedGame(gameState, timestamp)
        savedGames.add(savedGame)
        persistGames()
        savedGamesSubject.onNext(savedGames)
        return Observable.empty()
    }

    private fun persistGames() {
        val jsonString: String = gson.toJson(savedGames)
        sharedPreferences.edit()
            .putString("saved_games", jsonString)
            .commit()
        Log.d(TAG, "Games saved")
    }

    companion object {
        private val TAG = PersistedGameStore::class.java.simpleName
    }

    init {
        val gamesJson = sharedPreferences.getString("saved_games", "[]")
        try {
            savedGames =
                gson.fromJson(gamesJson, object : TypeToken<List<SavedGame?>?>() {}.getType())
            Log.d(TAG, "Loaded " + savedGames.size + " games")
        } catch (e: JsonSyntaxException) {
            Log.d(TAG, "Failed to load games")
        }
    }
}