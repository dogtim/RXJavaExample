package com.example.rxjava

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun just() {
        val observables = Observable.just("123", "456")
        val observer: Observer<String> = object : Observer<String> {
            override fun onSubscribe(d: Disposable) {
                println(d)
            }

            override fun onNext(s: String) {
                println(s)
            }

            override fun onError(e: Throwable) {
                println(e.localizedMessage)
            }

            override fun onComplete() {
                println("complete")
            }
        }
        observables.subscribe(observer)
    }
}