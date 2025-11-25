package org.kotlang.freelancerfinance.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
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