package org.kotlang.freelancerfinance.domain.repository

import kotlinx.coroutines.flow.Flow
import org.kotlang.freelancerfinance.domain.model.ServiceItem

interface ServiceItemRepository {

    fun getAllServiceItems(): Flow<List<ServiceItem>>
    
    suspend fun getServiceItemById(id: Long): ServiceItem?
    
    suspend fun upsertServiceItem(item: ServiceItem)
    
    suspend fun deleteServiceItem(id: Long)
}