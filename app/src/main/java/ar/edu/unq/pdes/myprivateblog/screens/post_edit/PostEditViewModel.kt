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
    val blogEntriesService: BlogEntriesService,
    val context: Context
) : ViewModel() {

    enum class State {
        EDITING, SUCCESS, ERROR
    }

    val state = MutableLiveData(State.EDITING)
    var post = MutableLiveData<BlogEntry?>()
    val titleText = MutableLiveData("")
    val bodyText = MutableLiveData("")
    val cardColor = MutableLiveData<Int>(Color.LTGRAY)




    fun fetchBlogEntry(id: EntityID) {
        val disposable = blogEntriesService
            .fetchBlogEntry(id)
            .subscribe {
                post.value = it
            }
    }

    fun savePost() {
        val disposable = blogEntriesService.updateBlogEntry(post.value!!.uid, post.value!!.bodyPath!!, titleText.value.toString(), bodyText.value!!, cardColor.value!!)
            ?.subscribe {
                state.value = State.SUCCESS
            }
    }

    fun onPostChange(_title: String, _cardColor: Int, _body: String) {
        titleText.value = _title
        cardColor.value = _cardColor
        bodyText.value = _body
    }

}
