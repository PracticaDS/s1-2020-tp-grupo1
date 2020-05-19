package ar.edu.unq.pdes.grupo1.myprivateblog

import android.graphics.Color
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.web.assertion.WebViewAssertions.webMatches
import androidx.test.espresso.web.sugar.Web.onWebView
import androidx.test.espresso.web.webdriver.DriverAtoms.findElement
import androidx.test.espresso.web.webdriver.DriverAtoms.getText
import androidx.test.espresso.web.webdriver.Locator
import androidx.test.ext.junit.rules.ActivityScenarioRule
import ar.edu.unq.pdes.grupo1.myprivateblog.MainActivity
import it.xabaras.android.espresso.recyclerviewchildactions.RecyclerViewChildActions.Companion.childOfViewAtPositionWithMatcher
import org.hamcrest.CoreMatchers.containsString
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

        onWebView()
            .withElement(findElement(Locator.XPATH, "/*"))
            .check(webMatches(getText(), containsString(body)))

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
