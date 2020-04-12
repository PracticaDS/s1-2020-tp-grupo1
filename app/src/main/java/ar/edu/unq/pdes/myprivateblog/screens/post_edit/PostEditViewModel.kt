package ar.edu.unq.pdes.myprivateblog.screens.post_edit

import android.content.Context
import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.unq.pdes.myprivateblog.data.BlogEntriesRepository
import ar.edu.unq.pdes.myprivateblog.data.BlogEntry
import ar.edu.unq.pdes.myprivateblog.data.EntityID
import ar.edu.unq.pdes.myprivateblog.rx.RxSchedulers
import ar.edu.unq.pdes.myprivateblog.screens.post_create.PostCreateViewModel
import io.reactivex.Flowable
import java.io.OutputStreamWriter
import java.util.*
import javax.inject.Inject


class PostEditViewModel @Inject constructor(
    val blogEntriesRepository: BlogEntriesRepository,
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
        val disposable = blogEntriesRepository
            .fetchById(id)
            .compose(RxSchedulers.flowableAsync())
            .subscribe {
                post.value = it
            }
    }

    fun savePost() {
        val disposable = Flowable.fromCallable {
            val fileName = post.value?.bodyPath

            val outputStreamWriter =
                OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE))

            outputStreamWriter.use { it.flush(); it.write(bodyText.value) }

            fileName

        }.flatMapSingle {
            blogEntriesRepository.updateBlogEntry(
                BlogEntry(
                    uid = post.value!!.uid,
                    title = titleText.value.toString(),
                    bodyPath = it,
                    cardColor = cardColor.value!!
                )
            ).toSingle { it }
        }.compose(RxSchedulers.flowableAsync()).subscribe {
            state.value = State.SUCCESS
        }
    }

    fun onPostChange(_title: String, _cardColor: Int, _body: String) {
        titleText.value = _title
        cardColor.value = _cardColor
        bodyText.value = _body
    }

}
