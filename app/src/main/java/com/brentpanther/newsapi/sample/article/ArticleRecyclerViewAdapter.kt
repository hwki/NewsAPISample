package com.brentpanther.newsapi.sample.article

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.brentpanther.newsapi.sample.network.Article
import com.brentpanther.newsapi.sample.R
import com.squareup.picasso.Picasso
import java.util.*

class ArticleRecyclerViewAdapter(var articles: List<Article>,
                                 private val listener: (Article) -> Unit) :
        RecyclerView.Adapter<ArticleRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_article, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articles[position]
        holder.title.text = article.title
        holder.snippet.text = article.description
        holder.time.text = calculateTime(article.publishedAt)
        holder.source.text = " - ${article.publisher}"

        Picasso.get().load(article.urlToImage).resizeDimen(R.dimen.thumbnail_size, R.dimen.thumbnail_size)
                .centerCrop()
         .into(holder.image)

        holder.mView.setOnClickListener {
            listener.invoke(articles[position])
        }
    }

    private fun calculateTime(publishedDate: Date): String {
        val milliseconds = Calendar.getInstance(TimeZone.getTimeZone("UTC")).time.time - publishedDate.time
        val minutes = milliseconds / (1000 * 60)
        if (minutes < 60) return "${minutes}m ago"
        val hours = minutes / 60
        if (hours < 24) return "${hours}h ago"
        return "${hours / 24}d ago"
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

        val title = mView.findViewById(R.id.title) as TextView
        val snippet = mView.findViewById(R.id.snippet) as TextView
        val image = mView.findViewById(R.id.image) as ImageView
        val time = mView.findViewById(R.id.time) as TextView
        val source = mView.findViewById(R.id.source) as TextView

    }
}
