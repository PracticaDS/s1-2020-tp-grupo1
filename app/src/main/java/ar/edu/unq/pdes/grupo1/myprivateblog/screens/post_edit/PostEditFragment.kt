package ar.edu.unq.pdes.grupo1.myprivateblog.screens.post_edit

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ar.edu.unq.pdes.grupo1.myprivateblog.R
import ar.edu.unq.pdes.grupo1.myprivateblog.screens.post_create.PostCreateFragment
import ar.edu.unq.pdes.grupo1.myprivateblog.screens.post_edit.PostEditFragmentArgs
import com.google.android.material.snackbar.Snackbar


class PostEditFragment : PostCreateFragment() {

    override val viewModel by viewModels<PostEditViewModel> { viewModelFactory }

    private val args: PostEditFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchBlogEntry(args.postId)
    }

    override fun onSave() {
        viewModel.savePost()
    }

    override fun onSuccess(view: View) {
        val snackbar: Snackbar = Snackbar.make(
            view,
            getString(R.string.PostEditSuccess),
            Snackbar.LENGTH_LONG
        )
        val snackbarView: View = snackbar.view
        snackbarView.setBackgroundColor(Color.parseColor("#33cc33"))
        snackbar.show()

        findNavController().navigateUp()
    }

}
