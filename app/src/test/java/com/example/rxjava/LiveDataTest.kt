package com.example.rxjava

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class LiveDataTest {
    inline fun <reified T> lambdaMock(): T = mock(T::class.java)

    class Presenter {

        val titleLiveData = MutableLiveData<String>()
        fun showTitle(title: String) {
            titleLiveData.postValue(title)
        }
        fun observeTitleChanges(lifecycle: Lifecycle, observer: (String) -> Unit) {
            titleLiveData.observe({ lifecycle }) { title ->
                title?.let(observer)
            }
        }
    }
    /**
     * Add below because [link](https://androiderrors.com/method-getmainlooper-in-android-os-looper-not-mocked-still-occuring-even-after-adding-rximmediateschedulerrule/)
     *
     * Error message: java.lang.RuntimeException: Method getMainLooper in android.os.Looper not mocked. at android.os.Looper.getMainLooper(Looper.java)
     *
     */
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    /**
     * Test the function of MediatorLiveData
     *
     * See [liveDate Test](https://proandroiddev.com/how-to-unit-test-livedata-and-lifecycle-components-8a0af41c90d9).
     */
    @Test
    fun showTitleTest() {
        val presenter = Presenter()

        val observer = lambdaMock<(String) -> Unit>()
        val lifecycle = LifecycleRegistry(mock(LifecycleOwner::class.java))
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        presenter.observeTitleChanges(lifecycle, observer)

        presenter.showTitle("title")

        verify(observer).invoke("title")
    }
}