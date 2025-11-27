package org.kotlang.freelancerfinance.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.kotlang.freelancerfinance.data.local.entity.ClientEntity

@Dao
interface ClientDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertClient(clientEntity: ClientEntity)

    @Update
    suspend fun updateClient(clientEntity: ClientEntity)

    @Delete
    suspend fun deleteClient(clientEntity: ClientEntity)

    @Query("SELECT * FROM clients ORDER BY name ASC")
    fun getAllClients(): Flow<List<ClientEntity>>

    @Query("SELECT * FROM clients WHERE id = :id")
    suspend fun getClientById(id: Long): ClientEntity?
}