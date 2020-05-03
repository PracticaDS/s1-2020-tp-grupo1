package ar.edu.unq.pdes.myprivateblog.screens.post_create

import android.content.Context
import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.unq.pdes.myprivateblog.data.BlogEntry
import ar.edu.unq.pdes.myprivateblog.services.BlogEntriesService
import javax.inject.Inject


open class PostCreateViewModel @Inject constructor(
    val blogEntriesService: BlogEntriesService,
    val context: Context
) : ViewModel() {

    enum class State {
        EDITING, SUCCESS, ERROR
    }

    val state = MutableLiveData(State.EDITING)
    val titleText = MutableLiveData("")
    val bodyText = MutableLiveData("")
    val cardColor = MutableLiveData<Int>(Color.LTGRAY)

    var postId: Int = 0

    fun createPost() {
        // TODO: extract this to some BlogEntryService or BlogEntryActions or some other super meaningful name...
        val disposable=blogEntriesService.createBlogEntry(titleText.value.toString(), bodyText.value.toString(), cardColor.value!!)
            .subscribe {
                postId = it.toInt()
                state.value = PostCreateViewModel.State.SUCCESS
            }
    }

}
