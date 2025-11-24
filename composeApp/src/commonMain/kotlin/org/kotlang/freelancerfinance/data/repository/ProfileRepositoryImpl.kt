package org.kotlang.freelancerfinance.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import org.kotlang.freelancerfinance.data.preferences.PreferenceRepository
import org.kotlang.freelancerfinance.data.preferences.PrefsKey
import org.kotlang.freelancerfinance.domain.model.BusinessProfile
import org.kotlang.freelancerfinance.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val preferenceRepository: PreferenceRepository
) : ProfileRepository {

    override suspend fun saveProfile(profile: BusinessProfile) {
        val jsonString = Json.encodeToString(profile)
        preferenceRepository.savePreference(
            key = PrefsKey.UserProfile,
            value = jsonString
        )
    }

    override fun getProfile(): Flow<BusinessProfile?> {
        return preferenceRepository
            .getPreference(PrefsKey.UserProfile, "")
            .map { jsonString ->
                if (jsonString.isBlank()) {
                    null
                } else {
                    try {
                        Json.decodeFromString<BusinessProfile>(jsonString)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        null
                    }
                }
            }
    }
}