package com.brentpanther.newsapi.sample.ui.articles

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.brentpanther.newsapi.sample.R
import com.brentpanther.newsapi.sample.databinding.LoadStateFooterViewItemBinding

class LoadStateViewHolder(private val binding: LoadStateFooterViewItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(loadState: LoadState) {
        binding.progressBar.isVisible = loadState is LoadState.Loading
    }

    companion object {

        fun create(parent: ViewGroup) : LoadStateViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.load_state_footer_view_item, parent, false)
            val binding = LoadStateFooterViewItemBinding.bind(view)
            return LoadStateViewHolder(binding)
        }
    }
}
