package ar.edu.unq.pdes.grupo1.myprivateblog.screens.posts_listing

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ar.edu.unq.pdes.grupo1.myprivateblog.data.BlogEntry
import ar.edu.unq.pdes.grupo1.myprivateblog.services.BlogEntriesService
import javax.inject.Inject

class PostsListingViewModel @Inject constructor(
    val blogEntriesService: BlogEntriesService
) : ViewModel() {

    val posts: LiveData<List<BlogEntry>> by lazy {
        blogEntriesService.getAllBlogEntries()
    }


}
