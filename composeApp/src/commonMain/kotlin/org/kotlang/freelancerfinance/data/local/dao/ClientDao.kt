package org.kotlang.freelancerfinance.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.kotlang.freelancerfinance.data.local.entity.ClientEntity

@Dao
interface ClientDao {

    @Upsert
    suspend fun upsertClient(clientEntity: ClientEntity)

    @Query("DELETE FROM clients WHERE id = :id")
    suspend fun deleteClientById(id: Long)

    @Query("SELECT * FROM clients ORDER BY name ASC")
    fun getAllClients(): Flow<List<ClientEntity>>

    @Query("SELECT * FROM clients WHERE id = :id")
    suspend fun getClientById(id: Long): ClientEntity?
}