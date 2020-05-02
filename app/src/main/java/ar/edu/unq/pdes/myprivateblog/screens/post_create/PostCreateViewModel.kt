package ar.edu.unq.pdes.myprivateblog.screens.post_create

import android.content.Context
import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.unq.pdes.myprivateblog.services.BlogEntriesService
import javax.inject.Inject


class PostCreateViewModel @Inject constructor(
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

    var post = 0

    fun createPost() {
        // TODO: extract this to some BlogEntryService or BlogEntryActions or some other super meaningful name...
        val disposable=blogEntriesService.createBlogEntry(titleText.value.toString(), bodyText.value.toString(), cardColor.value!!)
            .subscribe {
                post = it.toInt()
                state.value = PostCreateViewModel.State.SUCCESS
            }
    }

}
