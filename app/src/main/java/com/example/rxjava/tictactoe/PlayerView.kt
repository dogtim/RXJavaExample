package com.example.rxjava.tictactoe

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.example.rxjava.R
import com.example.rxjava.tictactoe.data.GameSymbol

class PlayerView(context: Context, attrs: AttributeSet?) : AppCompatImageView(context, attrs){

    fun setData(gameSymbol: GameSymbol?) {
        when (gameSymbol) {
            GameSymbol.BLACK -> setImageResource(R.drawable.symbol_black_circle)
            GameSymbol.RED -> setImageResource(R.drawable.symbol_red_circle)
            else -> setImageResource(0)
        }
    }
}