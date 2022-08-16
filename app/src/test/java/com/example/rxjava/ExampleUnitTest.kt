package com.example.rxjava

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.functions.Cancellable
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.regex.Pattern

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

    @Test
    fun map() {
        val expirationDatePattern: Pattern = Pattern.compile("^\\d\\d/\\d\\d$")
        val creditCardExpirationDate = Observable.create<String> {  emitter ->
            emitter.onNext("Kotlin")
            emitter.onNext("Java")
            emitter.onNext("Python")
            emitter.onNext("Javascript")
            emitter.onNext("Go")
            emitter.onNext("C")
            emitter.onNext("Rust")
            emitter.onComplete()

        }
        val isValidExpirationDate: Observable<Boolean> = creditCardExpirationDate
            .map { text -> expirationDatePattern.matcher(text).find() }

        val observer: Observer<Boolean> = object : Observer<Boolean> {
            override fun onSubscribe(d: Disposable) {
                println(d)
            }

            override fun onNext(s: Boolean) {
                println(s)
            }

            override fun onError(e: Throwable) {
                println(e.localizedMessage)
            }

            override fun onComplete() {
                println("complete")
            }
        }
        isValidExpirationDate.subscribe(observer)
    }
}