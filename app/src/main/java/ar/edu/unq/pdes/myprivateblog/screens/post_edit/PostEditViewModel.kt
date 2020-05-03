package ar.edu.unq.pdes.myprivateblog.screens.post_edit

import android.content.Context
import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.unq.pdes.myprivateblog.data.BlogEntry
import ar.edu.unq.pdes.myprivateblog.data.EntityID
import ar.edu.unq.pdes.myprivateblog.services.BlogEntriesService
import javax.inject.Inject


class PostEditViewModel @Inject constructor(
    private val blogEntriesService: BlogEntriesService,
    val context: Context
) : ViewModel() {

    enum class State {
        EDITING, SUCCESS, ERROR
    }

    val state = MutableLiveData(State.EDITING)
    var post = MutableLiveData<BlogEntry?>()
    val titleText = MutableLiveData("")
    val bodyText = MutableLiveData("")
    val cardColor = MutableLiveData(Color.LTGRAY)

    fun fetchBlogEntry(id: EntityID) {
        val disposable = blogEntriesService
            .fetchBlogEntry(id)
            .subscribe {
                post.value = it
                titleText.value = it.title
                cardColor.value = it.cardColor
                bodyText.value = getBody(it)
            }
    }

    fun savePost() {
        val disposable = blogEntriesService.updateBlogEntry(post.value!!.uid, post.value!!.bodyPath!!, titleText.value.toString(), bodyText.value!!, cardColor.value!!)
            ?.subscribe {
                state.value = State.SUCCESS
            }
    }

    fun getBody(blogEntry: BlogEntry): String {
        return blogEntriesService.getBody(blogEntry)
    }

}
