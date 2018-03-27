package com.brentpanther.newsapi.sample.article

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brentpanther.newsapi.sample.location.CurrentLocationViewModel


class LocationAwareArticleFragment : BaseArticleFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ArticleListViewModel::class.java)
        arguments?.let {
            val country = it.getString("country")
            val city = it.getString("city")
            val state = it.getString("state")
            if (country != null) {
                viewModel?.initialize(country)
            } else {
                viewModel?.initializeLocal(city, state)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        val currentLocationViewModel = ViewModelProviders.of(activity!!).get(CurrentLocationViewModel::class.java)
        currentLocationViewModel.location?.observe(this, Observer {
            if (it == null) return@Observer

            // if current location hasn't changed, don't reload articles
            val country = arguments?.getString("country")
            val city = arguments?.getString("city")
            val state = arguments?.getString("state")

            if (country != null) {
                if (country == it.countryCode) return@Observer
                // country has changed, update articles for country fragment
                viewModel?.clear(this)
                viewModel?.initialize(it.country)
            } else if (city != null && state != null) {
                if (city == it.city && state == it.state) return@Observer
                // city, state has changed, update articles for local fragment
                viewModel?.clear(this)
                viewModel?.initializeLocal(it.city, it.state)
            }

            observeArticles()
        })
        return view
    }

    companion object {
        fun newInstance(country: String? = null, city: String? = null, state: String? = null) : LocationAwareArticleFragment {
            val articleFragment = LocationAwareArticleFragment()
            with(Bundle()) {
                putString("country", country)
                putString("city", city)
                putString("state", state)
                articleFragment.arguments = this
            }
            return articleFragment
        }
    }
}
