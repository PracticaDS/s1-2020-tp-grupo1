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
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_post_edit.*


class PostEditFragment : BaseFragment() {
    override val layoutId = R.layout.fragment_post_edit

    private val viewModel by viewModels<PostEditViewModel> { viewModelFactory }

    private val args: PostEditFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchBlogEntry(args.postId)

        viewModel.state.observe(viewLifecycleOwner, Observer {
            when(it) {
                PostEditViewModel.State.SUCCESS -> {

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
                PostEditViewModel.State.ERROR -> {

                    // TODO: { Mostrar un mensaje de error y arreglar el estado. }
                }
            }
        })

        viewModel.titleText.observe(viewLifecycleOwner, Observer {
            if(!it.equals(title.text.toString())) {
                title.setText(it)
            }
        })

        viewModel.bodyText.observe(viewLifecycleOwner, Observer {
            if(!it.equals(body.toFormattedHtml())) {
                body.setText(it)
            }
        })

        viewModel.cardColor.observe(viewLifecycleOwner, Observer {
            header_background.setBackgroundColor(it)
            val itemsColor = ColorUtils.findTextColorGivenBackgroundColor(it)
            title.setTextColor(itemsColor)
            title.setHintTextColor(itemsColor)
            btn_save.setColorFilter(itemsColor)
            btn_close.setColorFilter(itemsColor)

            applyStatusBarStyle(it)
        })

        title.doOnTextChanged { text, start, count, after ->
            viewModel.titleText.postValue(text.toString())
        }

        body.doOnTextChanged { text, start, count, after ->
            viewModel.bodyText.value = body.toFormattedHtml()
        }

        btn_save.setOnClickListener {
            viewModel.savePost()
        }

        color_picker.onColorSelectionListener = {
            viewModel.cardColor.postValue(it)
        }

        btn_close.setOnClickListener {
            closeAndGoBack()
        }

    }

    private fun closeAndGoBack() {
        findNavController().navigateUp()
    }
}
