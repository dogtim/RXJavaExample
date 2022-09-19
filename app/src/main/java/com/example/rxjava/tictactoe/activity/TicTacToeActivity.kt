package com.example.rxjava.tictactoe.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.rxjava.MainActivity
import com.example.rxjava.R
import com.example.rxjava.tictactoe.GameApplication
import com.example.rxjava.tictactoe.InteractiveGameGridView
import com.example.rxjava.tictactoe.PlayerView
import com.example.rxjava.tictactoe.data.GameModel
import com.example.rxjava.tictactoe.data.GameStatus
import com.example.rxjava.tictactoe.data.GameViewModel
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class TicTacToeActivity : AppCompatActivity() {

    private val TAG: String = MainActivity::class.java.getSimpleName()

    private val viewSubscriptions = CompositeDisposable()
    private lateinit var gameModel: GameModel
    private lateinit var gameViewModel: GameViewModel

    private lateinit var gameGridView: InteractiveGameGridView
    private lateinit var playerInTurnImageView: PlayerView
    private var winnerView: View? = null
    private lateinit var winnerTextView: TextView
    private lateinit var newGameButton: Button
    private lateinit var saveGameButton: Button
    private lateinit var loadGameButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tictactoe)

        // Get the shared GameModel
        gameModel = (application as GameApplication).getGameModel()
        resolveViews()
        createViewModel()
        makeViewBinding()
    }

    private fun resolveViews() {
        gameGridView = findViewById(R.id.grid_view)
        playerInTurnImageView = findViewById(R.id.player_in_turn_image_view)
        winnerView = findViewById(R.id.winner_view)
        winnerTextView = findViewById(R.id.winner_text_view)
        newGameButton = findViewById(R.id.new_game_button)
        saveGameButton = findViewById(R.id.save_game_button)
        loadGameButton = findViewById(R.id.load_game_button)
    }

    private fun createViewModel() {
        gameViewModel = GameViewModel(
            gameModel.getActiveGameState(),
            gameModel::putActiveGameState,
            gameGridView.getTouchesOnGrid()
        )
        gameViewModel.subscribe()
    }

    private fun makeViewBinding() {
        // Handle new game, saving and loading games
        viewSubscriptions.add(
            RxView.clicks(newGameButton)
            .subscribe { gameModel.newGame() }
        )
        viewSubscriptions.add(RxView.clicks(saveGameButton)
            .subscribe { gameModel.saveActiveGame() }
        )
        viewSubscriptions.add(RxView.clicks(loadGameButton)
            .subscribe { showLoadGameActivity() }
        )

        // Bind the View Model
        viewSubscriptions.add(
            gameViewModel.getFullGameState().observeOn(AndroidSchedulers.mainThread()).subscribe{ state ->
                gameGridView.data = state
            }
        )
        viewSubscriptions.add(
            gameViewModel.getPlayerInTurn().observeOn(AndroidSchedulers.mainThread()).subscribe{ state ->
                playerInTurnImageView.setData(state)
            }
        )
        viewSubscriptions.add(
            gameViewModel.getGameStatus()
                .map(GameStatus::isEnded)
                .map { isEnded -> if (isEnded) View.VISIBLE else View.GONE }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { visibility: Int -> winnerView!!.visibility = visibility }
        )

        viewSubscriptions.add(
            gameViewModel.getGameStatus()
                .map { gameStatus -> if (gameStatus.isEnded) "Winner: " + gameStatus.winner else "" }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(winnerTextView::setText)
        )
    }

    private fun releaseViewBinding() {
        viewSubscriptions.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseViewBinding()
        gameViewModel.unsubscribe()
    }

    private fun showLoadGameActivity() {
        val intent = Intent(this, LoadGameActivity::class.java)
        startActivity(intent)
    }
}