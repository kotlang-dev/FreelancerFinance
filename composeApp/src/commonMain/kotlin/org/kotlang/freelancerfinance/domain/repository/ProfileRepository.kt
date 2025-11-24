package org.kotlang.freelancerfinance.domain.repository

import kotlinx.coroutines.flow.Flow
import org.kotlang.freelancerfinance.domain.model.BusinessProfile

interface ProfileRepository {
    suspend fun saveProfile(profile: BusinessProfile)
    fun getProfile(): Flow<BusinessProfile?>
}