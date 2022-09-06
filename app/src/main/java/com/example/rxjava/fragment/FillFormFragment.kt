package com.example.rxjava.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rxjava.databinding.FragmentFillFormBinding

class FillFormFragment : Fragment() {

    private lateinit var binding: FragmentFillFormBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFillFormBinding.inflate(layoutInflater)
        return binding.root
    }
}