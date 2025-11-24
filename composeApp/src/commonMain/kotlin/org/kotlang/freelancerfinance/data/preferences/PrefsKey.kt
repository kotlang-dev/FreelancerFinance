package org.kotlang.freelancerfinance.data.preferences

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey

sealed class PrefsKey<T>(val key: Preferences.Key<T>) {
    data object UserProfile : PrefsKey<String>(stringPreferencesKey("user_profile_data"))
}