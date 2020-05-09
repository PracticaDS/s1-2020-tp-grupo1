package ar.edu.unq.pdes.myprivateblog.screens.post_edit

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ar.edu.unq.pdes.myprivateblog.BaseFragment
import ar.edu.unq.pdes.myprivateblog.ColorUtils
import ar.edu.unq.pdes.myprivateblog.R
import ar.edu.unq.pdes.myprivateblog.screens.post_create.PostCreateFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_post_edit.*


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
