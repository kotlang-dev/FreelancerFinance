package org.kotlang.freelancerfinance.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.kotlang.freelancerfinance.data.local.entity.ServiceItemEntity

@Dao
interface ServiceItemDao {

    @Upsert
    suspend fun upsertServiceItem(item: ServiceItemEntity)

    @Query("DELETE FROM service_items WHERE id = :id")
    suspend fun deleteServiceItemById(id: Long)

    @Query("SELECT * FROM service_items ORDER BY name ASC")
    fun getAllServiceItems(): Flow<List<ServiceItemEntity>>

    @Query("SELECT * FROM service_items WHERE id = :id")
    suspend fun getServiceItemById(id: Long): ServiceItemEntity?
}