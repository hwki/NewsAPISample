package com.brentpanther.newsapi.sample

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import androidx.view.get
import com.brentpanther.newsapi.sample.article.ArticleFragment
import com.brentpanther.newsapi.sample.article.LocationAwareArticleFragment
import com.brentpanther.newsapi.sample.location.CurrentLocation
import com.brentpanther.newsapi.sample.location.CurrentLocationViewModel
import com.brentpanther.newsapi.sample.location.LocationActivityHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity(), LocationActivityHelper {

    private lateinit var currentLocationViewModel: CurrentLocationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setActionBar(toolbar)
        toolbar.title = title
        currentLocationViewModel = ViewModelProviders.of(this).get(CurrentLocationViewModel::class.java)
        currentLocationViewModel.initialize()

        initializePager()

        navigation.setOnNavigationItemSelectedListener {
            val index = hashMapOf(R.id.menu_top to 0, R.id.menu_country to 1, R.id.menu_local to 2)[it.itemId]
            index?.let { pager.setCurrentItem(index, false) }
            true
        }
    }

    override fun onStart() {
        super.onStart()
        requestLocation(this, currentLocationViewModel, {
            updateLocation(it.country, it.city)
            updatePreferences(it)
        })
    }

    private fun initializePager() {
        val preferenceHelper = PreferenceHelper()
        with(pager) {
            adapter = object : FragmentPagerAdapter(supportFragmentManager) {
                override fun getItem(position: Int): Fragment {
                    return when (position) {
                        0 -> ArticleFragment.newInstance("top", sources="reuters,google-news")
                        1 -> LocationAwareArticleFragment.newInstance("country", country=preferenceHelper.countryCode)
                        2 -> LocationAwareArticleFragment.newInstance("local", city=preferenceHelper.city,
                                state=preferenceHelper.state)
                        else -> throw IllegalArgumentException("invalid pager position")
                    }
                }

                override fun getCount() = 3
            }
            offscreenPageLimit = 3
            addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
                override fun onPageSelected(position: Int) {
                    navigation.selectedItemId = navigation.menu.getItem(position).itemId
                }
            })
            updateLocation(preferenceHelper.country, preferenceHelper.city)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (locationPermissionGranted(this, requestCode, permissions, grantResults)) {
            requestLocation(this, currentLocationViewModel, {
                updatePreferences(it)
                updateLocation(it.country, it.city)
            })
        }
    }

    private fun updateLocation(country: String, city: String) {
        navigation.menu[1].title = country
        navigation.menu[2].title = city
    }

    private fun updatePreferences(location: CurrentLocation) {
        val preferenceHelper = PreferenceHelper()
        with(preferenceHelper) {
            city = location.city
            state = location.state
            country = location.country
            countryCode = location.countryCode
        }
    }

}
