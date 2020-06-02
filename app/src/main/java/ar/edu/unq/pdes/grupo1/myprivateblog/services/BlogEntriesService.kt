package ar.edu.unq.pdes.grupo1.myprivateblog.services

import android.content.Context
import androidx.lifecycle.LiveData
import ar.edu.unq.pdes.grupo1.myprivateblog.data.BlogEntriesRepository
import ar.edu.unq.pdes.grupo1.myprivateblog.data.BlogEntry
import ar.edu.unq.pdes.grupo1.myprivateblog.data.EntityID
import ar.edu.unq.pdes.grupo1.myprivateblog.rx.RxSchedulers
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.internal.operators.single.SingleJust
import timber.log.Timber
import java.io.File
import java.io.OutputStreamWriter
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

class BlogEntriesService @Inject constructor(
    val blogEntriesRepository: BlogEntriesRepository,
    val context: Context
) {
    fun createBlogEntry(title: String, body: String, cardColor: Int): Flowable<EntityID> {
        return Flowable.fromCallable {
            createFileWithContent(body)
        }.flatMapSingle {
            saveBlogEntry(title, it, cardColor)
        }.flatMapSingle {
            saveBlogEntryToFirebase(it.uid.toInt(), it.title, body, it.bodyPath, it.cardColor)
            SingleJust(it.uid)
        }.compose(RxSchedulers.flowableAsync())
    }

    fun updateBlogEntry(
        id: EntityID,
        bodyPath: String,
        title: String,
        body: String,
        cardColor: Int
    ): Flowable<String>? {
        return Flowable.fromCallable {
            updateFileWithBody(bodyPath, body)
        }.flatMapSingle {
            updateBlogEntryB(id, title, it, cardColor)
        }.flatMapSingle {
            saveBlogEntryToFirebase(id, title, body, bodyPath, cardColor)
            SingleJust(it)
        }.compose(RxSchedulers.flowableAsync())
    }

    private fun updateBlogEntryB(
        id: EntityID,
        title: String,
        bodyPath: String,
        cardColor: Int
    ): Single<String> {

        return blogEntriesRepository.updateBlogEntry(
            BlogEntry(
                uid = id,
                title = title,
                bodyPath = bodyPath,
                cardColor = cardColor
            )
        ).toSingle { bodyPath }
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
    ): Single<BlogEntry> {
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

    fun fetchBlogEntry(id: EntityID): Flowable<BlogEntry> {
        return blogEntriesRepository.fetchById(id)
            .compose(RxSchedulers.flowableAsync())
    }

    fun deleteBlogEntry(blogEntry: BlogEntry): Completable {
        deleteBlogEntryFromFirebase(blogEntry)
        return blogEntriesRepository.deleteBlogEntry(blogEntry)
            .compose(RxSchedulers.completableAsync())
    }

    private fun deleteBlogEntryFromFirebase(blogEntry: BlogEntry) {
        val database = Firebase.database
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val postId = blogEntry.uid
        database.getReference("blogEntries/$uid/$postId").removeValue()

    }

    fun getAllBlogEntries(): LiveData<List<BlogEntry>> {
        syncBlogEntriesFromFirebase()
        val blogEntries = blogEntriesRepository.getAllBlogEntries()
        return blogEntries
    }

    fun getBody(blogEntry: BlogEntry): String {
        return File(context.filesDir, blogEntry.bodyPath).readText()
    }

    private fun saveBlogEntryToFirebase(
        postId: Int,
        title: String,
        body: String,
        bodyPath: String,
        color: Int
    ): Unit {
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        // Write a message to the database
        val database = Firebase.database

        database.getReference("blogEntries/$uid/$postId").setValue(
            mapOf(
                "postId" to postId,
                "title" to title,
                "body" to body,
                "bodyPath" to bodyPath,
                "color" to color
            )
        )
    }

    private fun onBlogEntriesReceivedFromFirebase(blogEntries: List<HashMap<String, Any>>): Unit {
        blogEntries.map {
            val postId = it["postId"] as Long
            val title = it["title"] as String
            val body = it["body"] as String
            val bodyPath = it["bodyPath"] as String
            val cardColor = it["color"] as Long

            Pair(
                BlogEntry(
                    uid = postId.toInt(),
                    title = title,
                    bodyPath = bodyPath,
                    cardColor = cardColor.toInt()
                ), body
            )
        }.let {
            updateOrCreate(it)
        }
    }

    private fun updateOrCreate(blogEntries: List<Pair<BlogEntry, String>>) {
        blogEntries.forEach {
            updateFileWithBody(it.first.bodyPath, it.second)
        }
        blogEntries.map {
            it.first
        }.let {
            blogEntriesRepository.insertOrUpdate(it)
        }.subscribe()
    }

    private fun syncBlogEntriesFromFirebase() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val database = Firebase.database

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.getValue<Any>()?.let {
                    when (it) { //List<HashMap<String, Any>>
                        is Map<*, *> -> onBlogEntriesReceivedFromFirebase(it.values.toList() as List<HashMap<String, Any>>)
                        is List<*> -> onBlogEntriesReceivedFromFirebase((it.subList(1,it.size)) as List<HashMap<String, Any>>)
                        is Any -> return
                    }


                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Timber.w("loadPost:onCancelled")
            }
        }

        database.getReference("blogEntries/$uid").addListenerForSingleValueEvent(postListener)
    }
}