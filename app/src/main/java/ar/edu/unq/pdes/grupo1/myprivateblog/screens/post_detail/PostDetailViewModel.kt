package ar.edu.unq.pdes.grupo1.myprivateblog.screens.post_detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.unq.pdes.grupo1.myprivateblog.data.BlogEntry
import ar.edu.unq.pdes.grupo1.myprivateblog.data.EntityID
import ar.edu.unq.pdes.grupo1.myprivateblog.services.BlogEntriesService
import timber.log.Timber
import javax.inject.Inject

class PostDetailViewModel @Inject constructor(
    private val blogEntriesService: BlogEntriesService
) : ViewModel() {

    var post = MutableLiveData<BlogEntry?>()
    var body = MutableLiveData("")

    fun fetchBlogEntry(id: EntityID) {
        val disposable = blogEntriesService.fetchBlogEntry(id).subscribe {
                post.value = it
                body.value = getBody(it)
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