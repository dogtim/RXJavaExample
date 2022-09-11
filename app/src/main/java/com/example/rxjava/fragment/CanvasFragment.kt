package com.example.rxjava.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class CanvasFragment : Fragment() {

    companion object {
        const val argumentOfViewType = "selectId"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return arguments?.let {
            val type = it.getInt(argumentOfViewType)
            return when(UseCase.values()[type]) {
                UseCase.FILL_FORM -> View(context)
                UseCase.TIC_TAC_TOE -> View(context)
                UseCase.TODO_2 -> View(context)
                UseCase.TODO_3 -> View(context)
            }
        }
    }
}