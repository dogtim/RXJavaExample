package com.example.rxjava.tictactoe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.rxjava.R
import com.example.rxjava.fragment.CanvasListFragment

class TicTacToeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createFragment()
    }

    private fun createFragment() {
        setContentView(R.layout.activity_usecase)
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<CanvasListFragment>(R.id.canvas_fragment_container)
        }
    }

}