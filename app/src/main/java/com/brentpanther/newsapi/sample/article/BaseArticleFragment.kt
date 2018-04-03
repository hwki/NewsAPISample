package com.brentpanther.newsapi.sample.article

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.net.toUri
import com.brentpanther.newsapi.sample.R

/**
 * Base class for common list of articles functionality
 */
open class BaseArticleFragment : Fragment() {

    protected var viewModel: ArticleListViewModel? = null
    private lateinit var adapter: ArticleRecyclerViewAdapter

    protected fun observeArticles() {
        viewModel?.articles?.observe(this, Observer {
            if (it == null) return@Observer
            adapter.articles = it.articles
            adapter.notifyDataSetChanged()
        } )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_article_list, container, false) as RecyclerView
        adapter = ArticleRecyclerViewAdapter(emptyList(), {
            val intent = CustomTabsIntent.Builder()
                    .setToolbarColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
                    .setStartAnimations(context!!, R.anim.slide_in_right, R.anim.hold)
                    .setExitAnimations(context!!, R.anim.hold, R.anim.slide_out_right)
                    .build()
            intent.launchUrl(context, it.url.toUri())
        })
        view.layoutManager = LinearLayoutManager(view.context)
        view.adapter = adapter
        observeArticles()

        return view
    }

}
