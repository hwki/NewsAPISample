package com.brentpanther.newsapi.sample.ui.articles.location

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.brentpanther.newsapi.sample.R
import com.brentpanther.newsapi.sample.ui.articles.BaseArticleFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationAwareArticleFragment : BaseArticleFragment() {

    private val viewModel: LocationArticleListViewModel by viewModels()
    private val args: LocationAwareArticleFragmentArgs by navArgs()

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
               load()
            } else {
                _binding?.let {
                    it.list.isVisible = false
                    it.viewLoading.isVisible = false
                    it.viewEmpty.text = getString(R.string.location_not_found)
                    it.viewEmpty.isVisible = true
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            load()
        }
        else {
            requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

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
