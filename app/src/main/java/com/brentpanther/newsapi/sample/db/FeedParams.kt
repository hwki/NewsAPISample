package com.brentpanther.newsapi.sample.db

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FeedParams(
    val sources: List<String>?, val category: String?, val language: String?, val countryCode: String?,
    val query: String?
) : Parcelable