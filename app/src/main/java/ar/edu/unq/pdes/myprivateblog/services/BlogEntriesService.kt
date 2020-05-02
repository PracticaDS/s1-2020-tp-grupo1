package ar.edu.unq.pdes.myprivateblog.services

import android.content.Context
import ar.edu.unq.pdes.myprivateblog.BaseApplication
import ar.edu.unq.pdes.myprivateblog.data.BlogEntriesRepository
import ar.edu.unq.pdes.myprivateblog.data.BlogEntry
import ar.edu.unq.pdes.myprivateblog.data.EntityID
import ar.edu.unq.pdes.myprivateblog.rx.RxSchedulers
import ar.edu.unq.pdes.myprivateblog.screens.post_create.PostCreateViewModel
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.io.OutputStreamWriter
import java.util.*
import javax.inject.Inject

class BlogEntriesService @Inject constructor(
    val blogEntriesRepository: BlogEntriesRepository,
    val context: Context){
    fun createBlogEntry(title: String, body: String, cardColor: Int): Flowable<Long> {
        return Flowable.fromCallable {

            createFileWithContent(body)

        }.flatMapSingle {

            saveBlogEntry(title, it, cardColor)

        }.compose(RxSchedulers.flowableAsync())

    }

    private fun saveBlogEntry(
        title: String,
        it: String,
        cardColor: Int
    ): Single<Long> {
        return blogEntriesRepository.createBlogEntry(
            BlogEntry(
                title = title,
                bodyPath = it,
                cardColor = cardColor
            )
        )
    }

    private fun createFileWithContent(body: String): String {
        val fileName = UUID.randomUUID().toString() + ".body"
        val outputStreamWriter =
            OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE))
        outputStreamWriter.use { it.write(body) }
        return fileName
    }

    fun fetchBlogEntry(id: EntityID):Flowable<BlogEntry>{
        return blogEntriesRepository.fetchById(id)
            .compose(RxSchedulers.flowableAsync())
    }

    fun deleteBlogEntry(blogEntry: BlogEntry): Completable {
        return blogEntriesRepository.deleteBlogEntry(blogEntry)
            .compose(RxSchedulers.completableAsync())
    }


}