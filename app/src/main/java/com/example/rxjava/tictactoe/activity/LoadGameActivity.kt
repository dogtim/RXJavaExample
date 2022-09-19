package com.example.rxjava.tictactoe.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.rxjava.R
import com.example.rxjava.tictactoe.GameApplication
import com.example.rxjava.tictactoe.SavedGamesListAdapter
import com.example.rxjava.tictactoe.data.GameModel
import com.example.rxjava.tictactoe.data.SavedGame
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class LoadGameActivity : AppCompatActivity() {
    private lateinit var gameModel: GameModel
    private val subscriptions = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_game)
        title = "Load Previous Game"
        // Get the shared GameModel
        gameModel = (application as GameApplication).getGameModel()
        val listAdapter = SavedGamesListAdapter(this, android.R.layout.simple_list_item_1)
        val listView: ListView = findViewById(R.id.saved_games_list)
        listView.adapter = listAdapter
        listView.onItemClickListener =
            OnItemClickListener { _: AdapterView<*>?, view: View, _: Int, _: Long ->
                val savedGame: SavedGame = view.tag as SavedGame
                gameModel.putActiveGameState(savedGame.gameState)
                finish()
            }
        subscriptions.add(
            gameModel.savedGamesStream
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { savedGames ->
                    listAdapter.clear()
                    listAdapter.addAll(savedGames)
                }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.clear()
    }
}