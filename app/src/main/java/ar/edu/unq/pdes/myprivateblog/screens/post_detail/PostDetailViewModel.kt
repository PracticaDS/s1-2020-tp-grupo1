package ar.edu.unq.pdes.myprivateblog.screens.post_detail

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.unq.pdes.myprivateblog.data.BlogEntriesRepository
import ar.edu.unq.pdes.myprivateblog.data.BlogEntry
import ar.edu.unq.pdes.myprivateblog.data.EntityID
import ar.edu.unq.pdes.myprivateblog.rx.RxSchedulers
import ar.edu.unq.pdes.myprivateblog.services.BlogEntriesService
import timber.log.Timber
import javax.inject.Inject

class PostDetailViewModel @Inject constructor(
    val blogEntriesService: BlogEntriesService,
    val context: Context
) : ViewModel() {

    var post = MutableLiveData<BlogEntry?>()



    fun fetchBlogEntry(id: EntityID) {
        val disposable= blogEntriesService.fetchBlogEntry(id).subscribe {
                post.value = it
            }
    }

    fun deletePost() {
        val disposable = blogEntriesService.deleteBlogEntry(post.value!!)
            .subscribe {
                Timber.i("Sarasa")
            }

    }

    fun getBody(post: BlogEntry): String {
        return blogEntriesService.getBody(post)
    }
}