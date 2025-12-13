package org.kotlang.freelancerfinance.data.mapper

import org.kotlang.freelancerfinance.data.local.entity.ClientEntity
import org.kotlang.freelancerfinance.domain.model.Client
import org.kotlang.freelancerfinance.domain.model.IndianState

// Entity -> Domain
fun ClientEntity.toDomain(): Client {
    return Client(
        id = id,
        name = name,
        gstin = gstin,
        address = address,
        state = IndianState.getByCode(stateCode)
    )
}

// Domain -> Entity
fun Client.toEntity(): ClientEntity {
    return ClientEntity(
        id = id,
        name = name,
        gstin = gstin,
        address = address,
        stateCode = state.code
    )
}

// Helper for Lists
fun List<ClientEntity>.toDomainList(): List<Client> {
    return map { it.toDomain() }
}