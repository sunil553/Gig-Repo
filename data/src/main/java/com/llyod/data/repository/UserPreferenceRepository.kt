package com.llyod.data.repository

import android.content.SharedPreferences
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : UserPreferencesRepo {

   override fun saveString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }
    override fun saveBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    override fun clearAllData() {
        sharedPreferences.edit().clear().apply()
    }


}

interface UserPreferencesRepo {

    fun saveString(key: String, value: String)

    fun getString(key: String, defaultValue: String) : String

    fun saveBoolean(key: String, value: Boolean)

    fun getBoolean(key: String, defaultValue: Boolean) : Boolean

    fun clearAllData()
}
