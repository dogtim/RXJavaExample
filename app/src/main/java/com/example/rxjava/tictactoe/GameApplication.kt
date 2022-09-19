package com.example.rxjava.tictactoe

import android.app.Application
import com.example.rxjava.tictactoe.data.GameModel

class GameApplication : Application() {

    private lateinit var gameModel: GameModel
    override fun onCreate() {
        super.onCreate()
        gameModel = GameModel(this)
    }

    fun getGameModel(): GameModel {
        return gameModel
    }
}