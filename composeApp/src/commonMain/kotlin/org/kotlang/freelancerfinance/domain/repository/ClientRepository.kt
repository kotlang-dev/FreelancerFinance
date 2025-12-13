package org.kotlang.freelancerfinance.domain.repository

import kotlinx.coroutines.flow.Flow
import org.kotlang.freelancerfinance.domain.model.Client

interface ClientRepository {
    fun getAllClients(): Flow<List<Client>>
    suspend fun upsertClient(client: Client)
    suspend fun deleteClient(id: Long)
    suspend fun getClientById(id: Long): Client?
}