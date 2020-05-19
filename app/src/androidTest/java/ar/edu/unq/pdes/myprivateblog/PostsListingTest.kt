package ar.edu.unq.pdes.grupo1.myprivateblog

import android.graphics.Color
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.web.assertion.WebViewAssertions.webMatches
import androidx.test.espresso.web.sugar.Web.onWebView
import androidx.test.espresso.web.webdriver.DriverAtoms.findElement
import androidx.test.espresso.web.webdriver.DriverAtoms.getText
import androidx.test.espresso.web.webdriver.Locator
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import ar.edu.unq.pdes.grupo1.myprivateblog.MainActivity
import org.hamcrest.CoreMatchers.containsString
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class PostsListingTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun whenTappingOnNewPostFab_postCreationScreenShouldOpen() {

        onView(withId(R.id.create_new_post))
            .perform(click())

        onView(withId(R.id.fragment_post_edit_root)).check(matches(isDisplayed()))

    }

    @Test
    fun whenCreatingPost_shouldNavigateToPostDetail() {
        onView(withId(R.id.create_new_post))
            .perform(click())

        val postTitle = "post1"

        onView(withId(R.id.title))
            .perform(typeText(postTitle))

        val bodyText = "This is the body"
        onView(withId(R.id.body))
            .perform(typeText(bodyText))

        val pickedColor = Color.parseColor("#b39ddb")

        onView(withTintColor(pickedColor))
            .perform(click())

        onView(withId(R.id.btn_save))
            .check(matches(isDisplayed()))

        onView(withId(R.id.btn_save))
            .perform(click())


        onView(withId(R.id.title))
            .check(matches(withText(postTitle)))

        onWebView()
            .withElement(findElement(Locator.XPATH, "/*"))
            .check(webMatches(getText(), containsString(bodyText)))
    }

}


/*@Test
fun whenTappingOnTheFirstPost_DetailScreenOfThatPostShouldOpen(){
    val postTitle = "Post1"
    createPostWithTitle(postTitle)

    onView(withId(R.id.btn_back))
        .perform(click())

    tapFirstPost()

    onView(withId(R.id.title))
        .check(ViewAssertions.matches(ViewMatchers.withText(postTitle)))

}

//}

@Test
fun whenTappingOnEditIconOnPostDetail_editionScreenShouldOpen() {

    val postTitle = "Post1"
    createPostWithTitle(postTitle)


    onView(withId(R.id.btn_edit))
        .perform(click())

    //check layout?
    onView(withId(R.id.title))
        .check(ViewAssertions.matches(ViewMatchers.withText(postTitle)))

}
*/