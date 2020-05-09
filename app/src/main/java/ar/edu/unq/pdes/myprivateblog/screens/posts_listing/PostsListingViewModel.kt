package ar.edu.unq.pdes.myprivateblog.screens.posts_listing

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ar.edu.unq.pdes.myprivateblog.data.BlogEntry
import ar.edu.unq.pdes.myprivateblog.services.BlogEntriesService
import javax.inject.Inject

class PostsListingViewModel @Inject constructor(
    val blogEntriesService: BlogEntriesService
) : ViewModel() {

    val posts: LiveData<List<BlogEntry>> by lazy {
        blogEntriesService.getAllBlogEntries()
    }


}
