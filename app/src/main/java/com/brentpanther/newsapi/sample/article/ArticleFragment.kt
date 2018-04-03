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
        arguments?.apply {
            viewModel?.initializeTop(getString("section", ""), sources = getString("sources"))
        }
    }

    companion object {
        fun newInstance(section: String, sources: String? = null) : ArticleFragment {
            val articleFragment = ArticleFragment()
            with (Bundle()) {
                putString("sources", sources)
                putString("section", section)
                articleFragment.arguments = this
            }
            return articleFragment
        }
    }

}
