package org.kotlang.freelancerfinance.data.preferences

import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {
    suspend fun <T> savePreference(key: PrefsKey<T>, value: T)
    fun <T> getPreference(key: PrefsKey<T>, defaultValue: T): Flow<T>
}