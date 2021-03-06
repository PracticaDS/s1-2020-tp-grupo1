package ar.edu.unq.pdes.grupo1.myprivateblog.data

import androidx.lifecycle.LiveDataReactiveStreams
import io.reactivex.internal.operators.single.SingleJust
import io.reactivex.schedulers.Schedulers

class BlogEntriesRepository(val appDatabase: AppDatabase) {
    fun getAllBlogEntries() =
        LiveDataReactiveStreams.fromPublisher(appDatabase.blogEntriesDao().getAll())

    fun fetchLiveById(entryId: EntityID) =
        LiveDataReactiveStreams.fromPublisher(appDatabase.blogEntriesDao().loadById(entryId))

    fun fetchById(entryId: EntityID) = appDatabase.blogEntriesDao().loadById(entryId)

    fun createBlogEntry(blogEntry: BlogEntry) = appDatabase.blogEntriesDao()
        .insert(blogEntry)
        .compose {
            SingleJust(BlogEntry(
                uid = it.blockingGet().toInt(),
                title = blogEntry.title,
                bodyPath = blogEntry.bodyPath,
                cardColor = blogEntry.cardColor
            ))
        }
        .subscribeOn(Schedulers.io())

    fun updateBlogEntry(album: BlogEntry) =
        appDatabase.blogEntriesDao()
            .update(album)
            .subscribeOn(Schedulers.io())

    fun deleteBlogEntry(blogEntry: BlogEntry) =
        appDatabase.blogEntriesDao()
            .delete(blogEntry)
            .subscribeOn(Schedulers.io())

    fun insertOrUpdate(blogEntries: List<BlogEntry>) =
        appDatabase.blogEntriesDao()
            .insertOrUpdate(blogEntries)
            .subscribeOn(Schedulers.io())

}