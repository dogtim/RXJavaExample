package com.example.rxjava.tictactoe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rxjava.R
import com.example.rxjava.tictactoe.data.GameModel

class TicTacToeActivity : AppCompatActivity() {
    private var gameModel: GameModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameModel = GameModel(this)
        setContentView(R.layout.activity_tictactoe)
    }
}