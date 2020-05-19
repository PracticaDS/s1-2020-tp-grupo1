package ar.edu.unq.pdes.grupo1.myprivateblog.screens.post_create

import android.content.Context
import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.unq.pdes.grupo1.myprivateblog.services.BlogEntriesService
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

        val disposable = blogEntriesService.createBlogEntry(titleText.value.toString(), bodyText.value.toString(), cardColor.value!!)
            .subscribe({
                postId = it.toInt()
                state.value =
                    State.SUCCESS
            }, {
                state.value=
                    State.ERROR
            })
    }
    fun errorHandled() {
        state.value =
            State.EDITING
    }

}
