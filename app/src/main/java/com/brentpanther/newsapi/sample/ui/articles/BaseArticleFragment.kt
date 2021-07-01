package com.brentpanther.newsapi.sample.ui.articles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.brentpanther.newsapi.sample.R
import com.brentpanther.newsapi.sample.databinding.FragmentArticleListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job

/**
 * Base class for common list of articles functionality
 */
@AndroidEntryPoint
open class BaseArticleFragment : Fragment() {

    protected var job: Job? = null
    protected var _binding: FragmentArticleListBinding? = null
    protected lateinit var adapter: ArticleRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentArticleListBinding.inflate(inflater, container, false).also {
            _binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding?.let { binding ->
            adapter = ArticleRecyclerViewAdapter {
                val intent = CustomTabsIntent.Builder()
                    .setStartAnimations(requireContext(), R.anim.slide_in_right, R.anim.hold)
                    .setExitAnimations(requireContext(), R.anim.hold, R.anim.slide_out_right)
                    .build()
                intent.launchUrl(requireContext(), it.url.toUri())
            }
            binding.list.adapter = adapter.withLoadStateFooter(FooterLoadStateAdapter())
            binding.list.layoutManager = LinearLayoutManager(view.context)
            adapter.addLoadStateListener {
                binding.viewLoading.isVisible = it.mediator?.refresh is LoadState.Loading
                if (listOf(it.append, it.mediator?.append, it.refresh, it.mediator?.refresh)
                        .all { s -> s is LoadState.NotLoading }) {
                    _binding?.refresh?.isRefreshing = false
                }
            }
            binding.refresh.setOnRefreshListener {
                adapter.refresh()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
