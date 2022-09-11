package com.example.rxjava.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rxjava.databinding.FragmentFillFormBinding
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject

/**
 *
 * This class shows the techniques of ViewModel combine RxJava
 *
 */
class TicTacToeFragment : Fragment() {

    private lateinit var binding: FragmentFillFormBinding

    private val titlePublisher: PublishSubject<String> = PublishSubject.create()
    private val messagePublisher: PublishSubject<String> = PublishSubject.create()
    private val clickPublish: PublishSubject<Any> = PublishSubject.create()

    private val TAG = this.javaClass.name

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFillFormBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.titleEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                titlePublisher.onNext(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding.messageEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                messagePublisher.onNext(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding.actionButton.setOnClickListener {
            clickPublish.onNext(Any())
        }

        // Create an ad-hoc structure that contains all information needed for the dialog. This
        // *could* be a custom data structure, but we can get away with a Pair in this simple case.
        val dialogInformationObservable: Observable<Pair<String, String>> =
            Observable.combineLatest(titlePublisher, messagePublisher) { a, b -> Pair(a, b) }

        // Convert the Void clicks into the necessary information to show the dialog
        val showDialogEventObservable: Observable<Pair<String, String>> =
            clickPublish.withLatestFrom(
                dialogInformationObservable
            ) { _, dialogInformation -> dialogInformation }

        // Subscribe to the event Observable that sends us the necessary information to show the
        // dialog at the time when it should be shown.
        val disposable = showDialogEventObservable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { (first, second) ->
                AlertDialog.Builder(this.context)
                    .setTitle(first)
                    .setMessage(second)
                    .show()
            }
        // debug()
    }

    private fun debug() {
        val disposable = titlePublisher.distinctUntilChanged()
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribe {
                Log.d(TAG, "query: $it")
            }
    }
}
