package com.example.rxjava

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.schedulers.TestScheduler
import io.reactivex.rxjava3.subscribers.TestSubscriber
import org.hamcrest.CoreMatchers.hasItems
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*
import java.util.concurrent.TimeUnit
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
        val creditCardExpirationDate = Observable.create<String> { emitter ->
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

    fun makeFlatMap(source: Observable<String>, scheduler: TestScheduler): Observable<String> {
        var delayHook = true
        val result = source.flatMap { s ->
            val delayTime: Long = if (delayHook) 10 else 5
            delayHook = !delayHook
            Observable.just(
                "$s MockItem1",
                "$s MockItem2"
            ).delay(delayTime, TimeUnit.SECONDS, scheduler)
        }

        return result
    }

    fun makeSwitchMap(source: Observable<String>, scheduler: TestScheduler): Observable<String> {
        var delayHook = true
        val result = source.switchMap { s ->
            val delayTime: Long = if (delayHook) 10 else 5
            delayHook = !delayHook
            Observable.just(
                "$s MockItem1",
                "$s MockItem2"
            ).delay(delayTime, TimeUnit.SECONDS, scheduler)
        }

        return result
    }

    fun testMap(isFlatMap: Boolean): MutableList<String> {
        val scheduler = TestScheduler()
        val actualOutput: MutableList<String> = ArrayList()
        val keywordToSearch: List<String> = listOf("b", "bo", "boo", "book", "books")
        val observable = if (isFlatMap) {
            makeFlatMap(Observable.fromIterable(keywordToSearch), scheduler)
        } else {
            makeSwitchMap(Observable.fromIterable(keywordToSearch), scheduler)
        }
        observable
            .toList()
            .doOnSuccess {
                actualOutput.addAll(it)
            }
            .subscribe()
        scheduler.advanceTimeBy(1, TimeUnit.MINUTES)

        return actualOutput

    }


    /**
     * The flatMap operator converts each item returned from a source observable into an independent observable
     *
     * using the supplied function and then merges all the observables into a single observable.
     *
     */
    @Test
    fun flatMap() {
        assertThat(
            testMap(true), hasItems(
                "bo MockItem1", "bo MockItem2",
                "book MockItem1", "book MockItem2",
                "b MockItem1", "b MockItem2",
                "boo MockItem1", "boo MockItem2",
                "books MockItem1", "books MockItem2"
            )
        )
    }


    /**
     * The switchMap operator is similar to flatMap, except that it retains the **result of only the latest observable**, discarding the previous ones.
     *
     * using the supplied function and then merges all the observables into a single observable.
     *
     */
    @Test
    fun switchMap() {
        assertThat(
            testMap(false), hasItems(
                "books MockItem1", "books MockItem2"
            )
        )
    }

    /**
     * Code from [flatmap-switchmap-and-concatmap-differences](https://medium.com/appunite-edu-collection/rxjava-flatmap-switchmap-and-concatmap-differences-examples-6d1f3ff88ee0)
     * ConcatMap works almost the same as flatMap, but preserves the order of items.
     */
    @Test
    fun concateMap() {
        val items: List<String> = listOf("a", "b", "c", "d", "e", "f")
        val scheduler = TestScheduler()
        var result = ""
        Observable.fromIterable(items)
            .concatMap { s ->
                val delay = Random().nextInt(10)
                Observable.just(s.toString() + "x")
                    .delay(delay.toLong(), TimeUnit.SECONDS, scheduler)
            }
            .toList()
            .doOnSuccess {
                // If use the flatMap, you will get random order list rather than order list as concateMap
                result = it.joinToString(" ")
            }.subscribe()

        scheduler.advanceTimeBy(1, TimeUnit.MINUTES)
        assertEquals(result, "ax bx cx dx ex fx")
    }

    @Test
    fun theTestObserver() {
        val observable: Observable<String> = Observable.error(RuntimeException())
        val testSubscriber: TestObserver<String> = TestObserver.create()

        observable.subscribe(testSubscriber)

        testSubscriber.assertError(RuntimeException::class.java)
    }

    @Test
    fun theTestObserverAdavanced() {
        val items: List<String> = listOf("a", "b", "c", "d", "e", "f")
        val testSubscriber: TestObserver<List<String>> = TestObserver.create()
        val scheduler = TestScheduler()
        var result = ""
        Observable.fromIterable(items)
            .concatMap { s ->
                val delay = Random().nextInt(10)
                Observable.just(s.toString() + "x")
                    .delay(delay.toLong(), TimeUnit.SECONDS, scheduler)
            }
            .toList()
            .doOnSuccess {
                // If use the flatMap, you will get random order list rather than order list as concateMap
                result = it.joinToString(" ")
                assertEquals(result, "ax bx cx dx ex fx")
            }.subscribe()

        scheduler.advanceTimeBy(1, TimeUnit.MINUTES)
        testSubscriber.assertNoErrors()
    }
}