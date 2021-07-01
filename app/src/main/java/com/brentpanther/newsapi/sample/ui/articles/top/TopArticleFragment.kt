package com.brentpanther.newsapi.sample.ui.articles.top

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.brentpanther.newsapi.sample.ui.articles.BaseArticleFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TopArticleFragment : BaseArticleFragment() {

    private val viewModel: TopArticleListViewModel by viewModels()
    private val args: TopArticleFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        load()
    }

    private fun load() {
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel.getArticles(args.view).collectLatest {
                adapter.submitData(it)
            }
        }
    }
}