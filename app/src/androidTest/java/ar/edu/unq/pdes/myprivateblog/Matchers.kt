package ar.edu.unq.pdes.grupo1.myprivateblog

import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.CoreMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher


fun withTintColor(expectedColor: Int): Matcher<View?>? {
    return object : BoundedMatcher<View?, View>(View::class.java) {

        override fun describeTo(description: Description) {
            description.appendText("Checking the matcher on received view: ")
            description.appendText("with expectedStatus=$expectedColor")
        }

        override fun matchesSafely(view: View): Boolean {
            return view.backgroundTintList?.defaultColor == expectedColor
        }
    }
}

fun withBackgroundColor(expectedColor: Int): Matcher<View> {
    return object : BoundedMatcher<View, View>(View::class.java) {

        override fun describeTo(description: Description) {
            description.appendText("Checking the matcher on received view: ")
            description.appendText("with expectedStatus=$expectedColor")
        }

        override fun matchesSafely(view: View): Boolean {
            return (view.background as ColorDrawable).color == expectedColor
        }
    }
}

class RecyclerViewItemCountAssertion constructor(private val expectedCount: Int) : ViewAssertion {

    override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }

        val recyclerView = view as RecyclerView
        val actualItemCount = recyclerView.adapter?.itemCount
        ViewMatchers.assertThat<Int>(actualItemCount, CoreMatchers.`is`<Int>(expectedCount))
    }

    companion object {

        fun hasItemCount(expectedCount: Int): RecyclerViewItemCountAssertion {
            return RecyclerViewItemCountAssertion(expectedCount)
        }
    }

}
