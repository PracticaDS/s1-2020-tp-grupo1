package ar.edu.unq.pdes.myprivateblog.screens.post_detail

import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ar.edu.unq.pdes.myprivateblog.BaseFragment
import ar.edu.unq.pdes.myprivateblog.ColorUtils
import ar.edu.unq.pdes.myprivateblog.R
import ar.edu.unq.pdes.myprivateblog.data.BlogEntry
import kotlinx.android.synthetic.main.fragment_post_detail.*
import java.io.File

class PostDetailFragment : BaseFragment() {
    override val layoutId = R.layout.fragment_post_detail

    private val viewModel by viewModels<PostDetailViewModel> { viewModelFactory }

    private val args: PostDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchBlogEntry(args.postId)

        viewModel.post.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                renderBlogEntry(it)
            }
        })
        viewModel.body.observe(viewLifecycleOwner, Observer {
            renderBody(it)
        })

        btn_back.setOnClickListener {
            findNavController().navigateUp()
        }

        btn_edit.setOnClickListener {
            findNavController().navigate(PostDetailFragmentDirections.navActionEditPost(args.postId))
        }

        btn_delete_post.setOnClickListener {
            viewModel.deletePost()
            findNavController().navigateUp()
        }

    }

    private fun renderBody(bodyText: String) {
        body.settings.javaScriptEnabled = true
        body.settings.setAppCacheEnabled(true)
        body.settings.cacheMode = WebSettings.LOAD_DEFAULT
        body.webViewClient = WebViewClient()
        body.loadData(bodyText, "text/html", "UTF-8")
    }

    fun renderBlogEntry(post: BlogEntry) {
        title.text = post.title
        header_background.setBackgroundColor(post.cardColor)
        applyStatusBarStyle(post.cardColor)
        title.setTextColor(ColorUtils.findTextColorGivenBackgroundColor(post.cardColor))
    }
}