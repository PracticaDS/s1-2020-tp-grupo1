package ar.edu.unq.pdes.myprivateblog

import android.graphics.Color
import androidx.test.espresso.Espresso
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import org.junit.Test


class PostCreationTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun whenSavingNewPost_postQuantityShouldBeOneMore() {

        val postsQuantityBeforeCreating = 0

        clickCreatePostButton()

        clickSaveButton()

        clickBackButton()

        Espresso.onView(ViewMatchers.withId(R.id.posts_list_recyclerview))
            .check(RecyclerViewItemCountAssertion(postsQuantityBeforeCreating+1));
    }



    @Test
    fun  whenDeletingAPost_ThereAreNoPosts(){

        val postsQuantityBeforeCreating = 0

        createPost("Post1", "Body1", Color.parseColor("#e6ee9c"))

        clickDeleteButton()

        Espresso.onView(ViewMatchers.withId(R.id.posts_list_recyclerview))
            .check(RecyclerViewItemCountAssertion(postsQuantityBeforeCreating));
    }

}

