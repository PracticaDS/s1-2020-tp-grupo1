package ar.edu.unq.pdes.myprivateblog

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import java.util.regex.Matcher



class PostCreationTest {


    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)


    @Test
    fun whenSavingNewPost_postQuantityShouldBeOneMore() {

        val postsQuantityBeforeCreating = 0 //TODO Hacer un setup, solo anda cuando no hay ningun post mas.

        Espresso.onView(ViewMatchers.withId(R.id.create_new_post))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.btn_save))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.btn_back))
            .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.posts_list_recyclerview))
            .check(RecyclerViewItemCountAssertion(postsQuantityBeforeCreating+1));


        val postsQuantityAfterCreating = 0


    }



}

class RecyclerViewItemCountAssertion constructor(private val expectedCount: Int) : ViewAssertion {

    override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }

        val recyclerView = view as RecyclerView
        val actualItemCount = recyclerView.adapter?.itemCount
        assertThat<Int>(actualItemCount, CoreMatchers.`is`<Int>(expectedCount))
    }

    companion object {

        fun hasItemCount(expectedCount: Int): RecyclerViewItemCountAssertion {
            return RecyclerViewItemCountAssertion(expectedCount)
        }
    }

}