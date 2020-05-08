package io.github.mattpvaughn.pixelly.data.local

import android.content.SharedPreferences
import io.github.mattpvaughn.pixelly.data.local.PrefsRepo.Companion.KEY_SOME_BOOLEAN
import io.github.mattpvaughn.pixelly.data.local.PrefsRepo.Companion.KEY_SOME_FLOAT
import io.github.mattpvaughn.pixelly.data.local.PrefsRepo.Companion.KEY_AUTH_TOKEN
import io.github.mattpvaughn.pixelly.data.local.PrefsRepo.Companion.KEY_SOME_STRING
import javax.inject.Inject

/**
 * An interface for getting/setting persistent preferences for Chronicle
 */
interface PrefsRepo {
    var someString: String
    var authToken: String
    var someFloat: Float
    var someBoolean: Boolean

    /**
     * Get a saved preference value corresponding to [key], providing [defaultValue] if no value
     * is already set. Return false in the case of no value already set if [defaultValue] is not
     * provided
     */
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean

    /** Save a preference with key == [key] and value == [value] to the preferences repo */
    fun setBoolean(key: String, value: Boolean)

    /** Clear all saved preferences */
    fun clearAll()

    fun registerPrefsListener(listener: SharedPreferences.OnSharedPreferenceChangeListener)

    fun unRegisterPrefsListener(listener: SharedPreferences.OnSharedPreferenceChangeListener)

    companion object {
        const val KEY_SOME_FLOAT = "float"
        const val KEY_SOME_STRING = "float"
        const val KEY_SOME_BOOLEAN = "bool"
        const val KEY_AUTH_TOKEN = "auth token"
    }
}

/** An implementation of [PrefsRepo] wrapping [SharedPreferences] */
class SharedPreferencesPrefsRepo @Inject constructor(private val sharedPreferences: SharedPreferences) :
    PrefsRepo {

    private val defaultAuthToken = ""
    override var authToken: String
        get() = getString(KEY_AUTH_TOKEN, defaultAuthToken)
        set(value) = sharedPreferences.edit().putString(KEY_AUTH_TOKEN, value).apply()

    private val defaultSomeString = ""
    override var someString: String
        get() = getString(KEY_SOME_STRING, defaultSomeString)
        set(value) = sharedPreferences.edit().putString(KEY_SOME_STRING, value).apply()

    private val defaultBoolean = false
    override var someBoolean: Boolean
        get() = sharedPreferences.getBoolean(KEY_SOME_BOOLEAN, defaultBoolean)
        set(value) = sharedPreferences.edit().putBoolean(KEY_SOME_BOOLEAN, value).apply()

    private val defaultFloat = 1.0f
    override var someFloat: Float
        get() = sharedPreferences.getFloat(KEY_SOME_FLOAT, defaultFloat)
        set(value) = sharedPreferences.edit().putFloat(KEY_SOME_FLOAT, value).apply()

    override fun setBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    private fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    override fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }

    override fun registerPrefsListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun unRegisterPrefsListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }
}
