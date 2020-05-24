package ar.edu.unq.pdes.grupo1.myprivateblog.screens.post_create

import android.content.Context
import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.unq.pdes.grupo1.myprivateblog.services.BlogEntriesService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
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

        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        // Write a message to the database
        val database = Firebase.database

        val disposable = blogEntriesService.createBlogEntry(titleText.value.toString(), bodyText.value.toString(), cardColor.value!!)
            .subscribe({

                val blog = Blog(titleText.value.toString(), bodyText.value.toString(), cardColor.value!!)
                database.getReference("messages/$uid").push().setValue(blog)
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

data class Blog(var title: String?, var body: String?, var cardColor: Int)