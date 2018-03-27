package com.brentpanther.newsapi.sample.article

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle

/**
 * Fragment to show a list of articles from a list of sources
 */
class ArticleFragment : BaseArticleFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ArticleListViewModel::class.java)
        arguments?.let {
            viewModel?.initialize(sources = it.getString("sources"))
        }
    }

    companion object {
        fun newInstance(sources: String? = null) : ArticleFragment {
            val articleFragment = ArticleFragment()
            val args = Bundle()
            args.putString("sources", sources)
            articleFragment.arguments = args
            return articleFragment
        }
    }

}
