package org.kotlang.freelancerfinance.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.kotlang.freelancerfinance.data.local.dao.ServiceItemDao
import org.kotlang.freelancerfinance.data.mapper.toDomain
import org.kotlang.freelancerfinance.data.mapper.toEntity
import org.kotlang.freelancerfinance.domain.model.ServiceItem
import org.kotlang.freelancerfinance.domain.repository.ServiceItemRepository

class ServiceItemRepositoryImpl(
    private val dao: ServiceItemDao
) : ServiceItemRepository {

    override fun getAllServiceItems(): Flow<List<ServiceItem>> {
        return dao.getAllServiceItems().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getServiceItemById(id: Long): ServiceItem? {
        return dao.getServiceItemById(id)?.toDomain()
    }

    override suspend fun upsertServiceItem(item: ServiceItem) {
        dao.upsertServiceItem(item.toEntity())
    }

    override suspend fun deleteServiceItem(id: Long) {
        dao.deleteServiceItemById(id)
    }
}