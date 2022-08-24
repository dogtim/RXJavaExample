package com.example.rxjava.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.rxjava.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        testMedi()
    }

    private fun testMedi() {
        val observer1 = Observer<Int> { newName ->
            // Update the UI, in this case, a TextView.
            Log.i("dogtim","observer1 $newName")
        }
        val liveData1 = MutableLiveData<Int>()
        val observer2 = Observer<Int> { newName ->
            // Update the UI, in this case, a TextView.
            Log.i("dogtim","observer2 $newName")
        }
        val liveData2 = MutableLiveData<Int>()
        val liveDataMerger = MediatorLiveData<Int>()

        liveData1.observe(viewLifecycleOwner, observer1)
        liveData2.observe(viewLifecycleOwner, observer2)
        // Create the observer which updates the UI.
        val mergerObserver = Observer<Int> { newName ->
            // Update the UI, in this case, a TextView.
            Log.i("dogtim","mergerObserver $newName")
        }
        liveDataMerger.addSource(liveData1, mergerObserver)
        liveDataMerger.addSource(liveData2, mergerObserver)
        liveDataMerger.observe(viewLifecycleOwner, mergerObserver)

        liveData1.value = 9
        liveData2.value = 10
    }
}