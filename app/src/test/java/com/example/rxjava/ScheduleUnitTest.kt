package com.example.rxjava

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.hamcrest.CoreMatchers.hasItems
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern


/**
 * Practice the Schedule in RxJava
 *
 * See [Scheduler](http://reactivex.io/RxJava/3.x/javadoc/io/reactivex/rxjava3/core/Scheduler.html).
 */
class ScheduleUnitTest {
    /**
     * The switchMap operator is similar to flatMap, except that it retains the **result of only the latest observable**, discarding the previous ones.
     *
     * using the supplied function and then merges all the observables into a single observable.
     *
     */
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}