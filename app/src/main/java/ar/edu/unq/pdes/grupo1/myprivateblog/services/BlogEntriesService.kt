package ar.edu.unq.pdes.grupo1.myprivateblog.services

import android.content.Context
import androidx.lifecycle.LiveData
import ar.edu.unq.pdes.grupo1.myprivateblog.data.BlogEntriesRepository
import ar.edu.unq.pdes.grupo1.myprivateblog.data.BlogEntry
import ar.edu.unq.pdes.grupo1.myprivateblog.data.EntityID
import ar.edu.unq.pdes.grupo1.myprivateblog.rx.RxSchedulers
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.internal.operators.single.SingleJust
import java.io.File
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
        }.flatMapSingle {
            saveBlogEntryToFirebase(it.toInt(), title, body, cardColor)
            SingleJust(it)
        }.compose(RxSchedulers.flowableAsync())
    }

    fun updateBlogEntry(id: EntityID, bodyPath : String, title: String, body: String, cardColor: Int): Flowable<String>? {
        return Flowable.fromCallable {
            updateFileWithBody(bodyPath, body)
        }.flatMapSingle {
            updateBlogEntryB(id, title, it, cardColor)
        }.flatMapSingle {
            saveBlogEntryToFirebase(id, title, body, cardColor)
            SingleJust(it)
        }.compose(RxSchedulers.flowableAsync())
    }

    private fun updateBlogEntryB(id: EntityID, title: String, bodyPath: String, cardColor: Int): Single<String> {

        return blogEntriesRepository.updateBlogEntry(
            BlogEntry(
                uid = id,
                title = title,
                bodyPath = bodyPath,
                cardColor = cardColor
            )
        ).toSingle{bodyPath}
    }

    private fun updateFileWithBody(bodyPath: String, body: String): String {
        val outputStreamWriter =
            OutputStreamWriter(context.openFileOutput(bodyPath, Context.MODE_PRIVATE))

        outputStreamWriter.use { it.flush(); it.write(body) }

        return bodyPath
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

    fun getAllBlogEntries(): LiveData<List<BlogEntry>> {
        return blogEntriesRepository.getAllBlogEntries()
    }

    fun getBody(blogEntry: BlogEntry): String {
        return File(context.filesDir, blogEntry.bodyPath).readText()
    }

    fun saveBlogEntryToFirebase(postId: Int, title: String, body: String, color: Int): Unit {
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        // Write a message to the database
        val database = Firebase.database

        database.getReference("blogEntries/$uid/$postId").setValue(mapOf(
            "postId" to postId,
            "title" to title,
            "body" to body,
            "color" to color
        ))
    }
}