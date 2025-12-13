package org.kotlang.freelancerfinance.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.kotlang.freelancerfinance.data.local.dao.ClientDao
import org.kotlang.freelancerfinance.data.mapper.toDomain
import org.kotlang.freelancerfinance.data.mapper.toDomainList
import org.kotlang.freelancerfinance.data.mapper.toEntity
import org.kotlang.freelancerfinance.domain.model.Client
import org.kotlang.freelancerfinance.domain.repository.ClientRepository

class ClientRepositoryImpl(
    private val dao: ClientDao
) : ClientRepository {

    override fun getAllClients(): Flow<List<Client>> {
        return dao.getAllClients().map { entities ->
            entities.toDomainList()
        }
    }

    override suspend fun upsertClient(client: Client) {
        dao.upsertClient(client.toEntity())
    }

    override suspend fun deleteClient(id: Long) {
        dao.deleteClientById(id)
    }

    override suspend fun getClientById(id: Long): Client? {
        return dao.getClientById(id)?.toDomain()
    }
}