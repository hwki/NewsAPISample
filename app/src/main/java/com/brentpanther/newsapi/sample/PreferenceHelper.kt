package com.brentpanther.newsapi.sample

import android.content.SharedPreferences
import android.preference.PreferenceManager
import java.util.*
import kotlin.reflect.KProperty


class PreferenceHelper {

    var country: String by Delegate()
    var countryCode: String by Delegate()
    var city: String by Delegate()
    var state: String by Delegate()

    class Delegate {

        private val defaults = mapOf("countryCode" to Locale.getDefault().country,
                "country" to Locale.getDefault().displayCountry,
                "city" to "Iowa City",
                "state" to "Iowa")

        operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
            return sharedPreferences.getString(property.name, defaults[property.name])
        }

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
            return sharedPreferences.edit().putString(property.name, value).apply()
        }

        private val sharedPreferences: SharedPreferences by lazy {
            PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance())
        }

    }

}