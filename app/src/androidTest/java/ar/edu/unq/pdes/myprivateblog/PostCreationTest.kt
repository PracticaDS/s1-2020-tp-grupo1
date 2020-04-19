package ar.edu.unq.pdes.myprivateblog

import android.graphics.Color
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import it.xabaras.android.espresso.recyclerviewchildactions.RecyclerViewChildActions.Companion.childOfViewAtPositionWithMatcher
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

        onView(withId(R.id.posts_list_recyclerview))
            .check(RecyclerViewItemCountAssertion(postsQuantityBeforeCreating + 1));
    }


    @Test
    fun whenDeletingAPost_ThereAreNoPosts() {

        val postsQuantityBeforeCreating = 0

        createPost("Post1", "Body1", Color.parseColor("#e6ee9c"))

        clickDeleteButton()

        onView(withId(R.id.posts_list_recyclerview))
            .check(RecyclerViewItemCountAssertion(postsQuantityBeforeCreating));
    }


    @Test
    fun whenCreatingAPostAndTappingOnCancel_ListingOfPostsScreenShouldOpen() {

        onView(withId(R.id.create_new_post))
            .perform(click())

        onView(withId(R.id.btn_close))
            .perform(click())

        onView(withId(R.id.fragment_posts_listing_root))
            .check(matches(isDisplayed()))
    }


    @Test
    fun whenCreatingAPostAndTappingOnCancel_postQuantityShouldBeZero() {

        val postsQuantityBeforeCreating = 0

        onView(withId(R.id.create_new_post))
            .perform(click())

        onView(withId(R.id.btn_close))
            .perform(click())

        onView(withId(R.id.posts_list_recyclerview))
            .check(RecyclerViewItemCountAssertion(postsQuantityBeforeCreating));

    }


    @Test
    fun whenCreatingAPostAndTappingSave_DetailScreenShouldOpenWithSameTitle() {

        val title = "Post1"

        val color = Color.parseColor("#f48fb1")

        clickCreatePostButton()

        typeOnTitle(title)

        clickSaveButton()

        onView(withId(R.id.title))
            .check(matches(withText(title)))

    }

    @Test
    fun whenCreatingAPostAndTappingSave_DetailScreenShouldOpenWithSameBody() {

        val body = "Body1"

        clickCreatePostButton()

        typeOnBody(body)

        clickSaveButton()

        onView(withId(R.id.body))
            .check(matches(withText(body)))

    }

    @Test
    fun whenCreatingAPostAndTappingSave_DetailScreenShouldOpenWithSameColor() {

        val color = Color.parseColor("#f48fb1")

        clickCreatePostButton()

        clickOnColor(color)

        clickSaveButton()

        onView(withId(R.id.header_background))
            .check(matches(withBackgroundColor(color)))
    }


    @Test
    fun whenCreatingAPostAndTappingSave_ListingScreenShouldHaveCardWithSameTitle() {

        val title = "Post1"
        val color = Color.parseColor("#f48fb1")

        clickCreatePostButton()
        typeOnTitle(title)
        clickOnColor(color)
        clickSaveButton()
        clickBackButton()

        onView(withId(R.id.posts_list_recyclerview))
            .check(matches(
                childOfViewAtPositionWithMatcher(
                    R.id.item_title,
                    0,
                    withText(title)
                )
            ))
    }

}
