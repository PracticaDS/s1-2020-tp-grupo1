package ar.edu.unq.pdes.myprivateblog.screens.post_create

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import ar.edu.unq.pdes.myprivateblog.BaseFragment
import ar.edu.unq.pdes.myprivateblog.ColorUtils
import ar.edu.unq.pdes.myprivateblog.R
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.fragment_post_edit.*
import org.wordpress.aztec.Aztec
import org.wordpress.aztec.ITextFormat
import org.wordpress.aztec.glideloader.GlideImageLoader
import org.wordpress.aztec.glideloader.GlideVideoThumbnailLoader
import org.wordpress.aztec.toolbar.IAztecToolbarClickListener
import timber.log.Timber

open class PostCreateFragment : BaseFragment() {
    override val layoutId = R.layout.fragment_post_edit

    protected open val viewModel by viewModels<PostCreateViewModel> { viewModelFactory }
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = FirebaseAnalytics.getInstance(context!!);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner, Observer {
            when (it) {

                PostCreateViewModel.State.ERROR -> {
                    onError(view)
                }

                PostCreateViewModel.State.SUCCESS -> {
                    onSuccess(view)
                }

                PostCreateViewModel.State.EDITING -> {

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
                body.fromHtml(it)
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
            onSave()
        }

        btn_close.setOnClickListener {
            closeAndGoBack()
        }

        color_picker.onColorSelectionListener = {
            viewModel.cardColor.postValue(it)
        }

        context?.apply {
            Aztec.with(body, source, formatting_toolbar, object : IAztecToolbarClickListener {
                override fun onToolbarCollapseButtonClicked() {
                }

                override fun onToolbarExpandButtonClicked() {
                }

                override fun onToolbarFormatButtonClicked(
                    format: ITextFormat,
                    isKeyboardShortcut: Boolean
                ) {
                }

                override fun onToolbarHeadingButtonClicked() {
                }

                override fun onToolbarHtmlButtonClicked() {
                }

                override fun onToolbarListButtonClicked() {
                }

                override fun onToolbarMediaButtonClicked(): Boolean = false

            })
                .setImageGetter(GlideImageLoader(this))
                .setVideoThumbnailGetter(GlideVideoThumbnailLoader(this))
        }


    }

    protected open fun onSave() {
        trackEvent()
        viewModel.createPost()
    }

    protected open fun onSuccess(view:View) {
        findNavController().navigate(
            PostCreateFragmentDirections.navActionSaveNewPost(
                viewModel.postId
            )
        )
    }

    protected open fun onError(view:View){
        // TODO: manage error states
    }

    private fun closeAndGoBack() {
        findNavController().navigateUp()
    }

    private fun trackEvent(){
        val params = Bundle()
        firebaseAnalytics.logEvent("post_created", params)
    }
}