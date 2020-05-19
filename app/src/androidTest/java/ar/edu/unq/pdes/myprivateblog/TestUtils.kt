package ar.edu.unq.pdes.grupo1.myprivateblog

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import ar.edu.unq.pdes.grupo1.myprivateblog.screens.posts_listing.BlogEntryViewHolder

fun clickEditButton() {
    Espresso.onView(ViewMatchers.withId(R.id.btn_edit))
        .perform(ViewActions.click())
}

fun clickSaveButton() {
    Espresso.onView(ViewMatchers.withId(R.id.btn_save))
        .perform(ViewActions.click())
}

fun createPost(title: String, body: String, color: Int) {
    clickCreatePostButton()

    typeOnTitle(title)

    typeOnBody(body)

    clickOnColor(color)

    clickSaveButton()
}

fun clickOnColor(color: Int) {
    Espresso.onView(withTintColor(color))
        .perform(ViewActions.click())
}

fun typeOnTitle(editedPostTitle: String) {
    Espresso.onView(ViewMatchers.withId(R.id.title))
        .perform(ViewActions.clearText())

    Espresso.onView(ViewMatchers.withId((R.id.title)))
        .perform(ViewActions.typeText(editedPostTitle))
}

fun typeOnBody(editedPostBody: String) {
    Espresso.onView(ViewMatchers.withId(R.id.body))
        .perform(ViewActions.clearText())

    Espresso.onView(ViewMatchers.withId((R.id.body)))
        .perform(ViewActions.typeText(editedPostBody))
}

fun tapFirstPost(): Unit {
    Espresso.onView(ViewMatchers.withId(R.id.posts_list_recyclerview))
        .perform(
            RecyclerViewActions.actionOnItemAtPosition<BlogEntryViewHolder>(0,
                ViewActions.click()
            ))
}

fun clickDeleteButton() {
    Espresso.onView(ViewMatchers.withId(R.id.btn_delete_post))
        .perform(ViewActions.click())
}

fun clickCreatePostButton(){
    Espresso.onView(ViewMatchers.withId(R.id.create_new_post))
        .perform(ViewActions.click())
}

fun clickBackButton(){
    Espresso.onView(ViewMatchers.withId(R.id.btn_back))
        .perform(ViewActions.click())
}
