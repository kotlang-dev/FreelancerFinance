package org.kotlang.freelancerfinance.domain.repository

import kotlinx.coroutines.flow.Flow
import org.kotlang.freelancerfinance.domain.model.Client

interface ClientRepository {
    fun getAllClients(): Flow<List<Client>>
    suspend fun insertClient(client: Client)
    suspend fun deleteClient(client: Client)
    suspend fun getClientById(id: Long): Client?
}