package ar.edu.unq.pdes.grupo1.myprivateblog

import android.graphics.Color
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
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
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class PostEditionTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    private lateinit var postTitle: String

    @Before
    fun setUp(){
        postTitle = "Post1"
        val postBody = "Body1"
        val postColor = Color.parseColor("#e6ee9c")
        createPost(postTitle, postBody, postColor)
    }

    @Test
    fun whenChangingPostTitleAndTappingOnSave_detailScreenOfThePostShouldShowNewTitle(){

        clickEditButton()

        val editedPostTitle = "Post2"

        typeOnTitle(editedPostTitle)

        clickSaveButton()

        onView(withId(R.id.title))
            .check(matches(withText(editedPostTitle)))
    }


    @Test
    fun whenChangingPostBodyAndTappingSave_detailScreenShouldShowNewBody(){

        clickEditButton()

        val editedPostBody = "Body2"

        typeOnBody(editedPostBody)

        clickSaveButton()

        onWebView()
            .withElement(findElement(Locator.XPATH, "/*"))
            .check(webMatches(getText(), containsString(editedPostBody)))
    }

    @Test
    fun whenChangingPostColorAndTappingSave_detailScreenShouldShowThePostWithTheNewColor(){

        clickEditButton()

        val newPickedColor = Color.parseColor("#e6ee9c")

        clickOnColor(newPickedColor)

        clickSaveButton()

        onView(withId(R.id.header_background))
            .check(matches(withBackgroundColor(newPickedColor)))

    }

    @Test
    fun whenPostIsEditedAndCancelIsTapped_detailScreenOfThePostShouldOpenWithThePostUnchanged(){

        clickEditButton()

        val editedPostTitle = "Post2"

        typeOnTitle(editedPostTitle)

        onView(withId(R.id.btn_close))
            .perform(click())

        onView(withId(R.id.title))
            .check(matches(withText(postTitle)))
    }

}