package com.example.rxjava.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rxjava.R
import com.example.rxjava.databinding.FragmentRecycleBinding

class CanvasListFragment : Fragment() {

    private lateinit var adapter: UseCaseAdapter
    private lateinit var binding: FragmentRecycleBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecycleBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun setupAdapter() {
        adapter = UseCaseAdapter(UseCase.values().toList(), activity as AppCompatActivity)
        binding.tutorsRecyclerView.layoutManager = LinearLayoutManager(binding.root.context)
        binding.tutorsRecyclerView.adapter = adapter
    }

}

enum class UseCase { FILL_FORM, TODO_1, TODO_2, TODO_3 }

private class UseCaseAdapter(private val entries: List<UseCase>, private val activity: AppCompatActivity) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_usecase, parent, false)
        return DesignViewHolder(view)
    }

    override fun onBindViewHolder(@Nullable holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DesignViewHolder) {
            val entry = entries[position]
            holder.textView.text = entry.name
            holder.itemView.setOnClickListener {

                when(entry) {
                    UseCase.FILL_FORM -> {
                        activity.supportFragmentManager.commit {
                            replace<FillFormFragment>(R.id.canvas_fragment_container)
                            addToBackStack(null)
                        }
                    }
                    else -> {
                        val bundle = bundleOf(CanvasFragment.argumentOfViewType to entry.ordinal)
                        activity.supportFragmentManager.commit {
                            replace<CanvasFragment>(R.id.canvas_fragment_container, args = bundle)
                            addToBackStack(null)
                        }
                    }
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return entries.size
    }

    class DesignViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
    }
}