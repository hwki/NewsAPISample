package com.brentpanther.newsapi.sample.ui.articles

import android.text.format.DateUtils
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import coil.load
import com.brentpanther.newsapi.sample.databinding.FragmentArticleBinding
import com.brentpanther.newsapi.sample.Article

class ArticleRecyclerViewAdapter(private val clickListener: (Article) -> Unit) :
        PagingDataAdapter<Article, ArticleRecyclerViewAdapter.ArticleViewHolder>(ArticleDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = FragmentArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    private class ArticleDiff : DiffUtil.ItemCallback<Article>() {

        override fun areItemsTheSame(old: Article, new: Article) = old.url == new.url

        override fun areContentsTheSame(old: Article, new: Article) = old.url == new.url

    }

    class ArticleViewHolder(private val binding: FragmentArticleBinding, private val clickListener: (Article) -> Unit) : RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article) = with(binding) {
            binding.root.setOnClickListener {
                clickListener.invoke(article)
            }
            binding.title.text = article.title
            binding.snippet.text = article.description
            binding.time.text = DateUtils.getRelativeTimeSpanString(article.publishedAt.time)
            val source = article.articleSource
            binding.source.text = if (source == null) "" else " - $source"
            binding.image.load(article.urlToImage) {
                crossfade(true)
            }
        }

    }
}
