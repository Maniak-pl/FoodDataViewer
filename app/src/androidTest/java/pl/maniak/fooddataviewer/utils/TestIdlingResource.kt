package pl.maniak.fooddataviewer.utils

import androidx.test.espresso.idling.CountingIdlingResource

class TestIdlingResource: IdlingResource {

    var countingIdlingResource = CountingIdlingResource("IdlingResource")

    override fun increment() {
        countingIdlingResource.increment()
    }

    override fun decrement() {
        countingIdlingResource.decrement()
    }
}