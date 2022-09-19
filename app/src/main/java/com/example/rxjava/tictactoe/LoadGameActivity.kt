package com.example.rxjava.tictactoe

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.rxjava.R
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
        setTitle("Load Previous Game")

        // Get the shared GameModel
        gameModel = (getApplication() as GameApplication).getGameModel()
        val listAdapter = SavedGamesListAdapter(this, android.R.layout.simple_list_item_1)
        val listView = findViewById(R.id.saved_games_list) as ListView
        listView.adapter = listAdapter
        listView.onItemClickListener =
            OnItemClickListener { adapterView: AdapterView<*>?, view: View, i: Int, l: Long ->
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